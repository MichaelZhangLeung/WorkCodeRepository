package com.quantum.vpn.srv

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.LocalSocket
import android.net.LocalSocketAddress
import android.net.VpnService
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.os.RemoteException
import android.os.SystemClock

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

import com.quantum.vpn.IVpnService
import com.quantum.vpn.MainActivity
import com.quantum.vpn.R
import com.quantum.vpn.VpnStateWatcher
import com.quantum.vpn.ui.VpnRequestActivity
import com.quantum.vpn.util.Log

import org.json.JSONException
import org.json.JSONObject
import org.yaml.snakeyaml.Yaml

import java.io.BufferedReader
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.util.Arrays
import java.util.Locale
import java.util.Objects

import com.quantum.vpn.util.Utils.extract
import com.quantum.vpn.util.Utils.freePort

class QuantumVpnService : VpnService() {

    private var localDnsService: LocalDnsService? = null
    private var tun2socks: Process? = null
    private var wormhole: Process? = null
    private var tunfd: ParcelFileDescriptor? = null

    private var vpnStateWatcher: VpnStateWatcher? = null

    private val serviceImpl = object : IVpnService.Stub() {

        override fun checkEnable(): Boolean {
            return tunfd != null
        }

        override fun startVpn() {
            val intent = Intent(this@QuantumVpnService, QuantumVpnService::class.java)
            ContextCompat.startForegroundService(this@QuantumVpnService, intent)
        }

        override fun stopVpn() {
            updateNotification(R.string.vpn_prepare)
            killProcesses(null)
            stopSelf()
        }

        @Throws(RemoteException::class)
        override fun getRESTfulApi(): String {
            try {
                val jobject = JSONObject()
                jobject.put("hostname", RESTFUL_HOST)
                jobject.put("port", RESTFUL_PORT)
                jobject.put("secret", RESTFUL_SECRET)
                return jobject.toString()
            } catch (e: JSONException) {
                throw RemoteException(e.message)
            }

        }

        @Throws(RemoteException::class)
        override fun setVpnStateListener(watcher: VpnStateWatcher) {
            if (vpnStateWatcher != null && vpnStateWatcher !== watcher) {
                vpnStateWatcher!!.asBinder().unlinkToDeath(recipient, 0)
                watcher.asBinder().linkToDeath(recipient, 0)
            }
            vpnStateWatcher = watcher
        }
    }

    private val recipient = object : IBinder.DeathRecipient {
        override fun binderDied() {
            vpnStateWatcher!!.asBinder().unlinkToDeath(this, 0)
        }
    }

    override fun onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("QuantumVpn", getText(R.string.app_name), NotificationManager.IMPORTANCE_LOW)
            NotificationManagerCompat.from(this).createNotificationChannel(channel)
        }
        startForeground(1, NotificationCompat.Builder(this, "QuantumVpn")
                .setContentIntent(PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), 0))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getText(R.string.app_name))
                .setContentText(getText(R.string.vpn_prepare))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build())
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (VpnService.prepare(this) != null) {
            val requestIntent = Intent(this, VpnRequestActivity::class.java)
            val bundle = Bundle()
            bundle.putBinder("watcher", vpnStateWatcher!!.asBinder())
            requestIntent.putExtras(bundle)
            requestIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(requestIntent)
            stopSelf()
        } else {
            updateNotification(R.string.vpn_connecting)
            try {
                sendFd(startVpn())
                try {
                    if (Objects.requireNonNull<VpnStateWatcher>(vpnStateWatcher).asBinder().pingBinder()) {
                        val jsonObject = JSONObject()
                        jsonObject.put("code", 0)
                        vpnStateWatcher!!.onStateChanged(true, jsonObject.toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                updateNotification(R.string.vpn_connected)
            } catch (e: IOException) {
                Log.e("QuantumVpn", "start vpn failed!", e)
                updateNotification(R.string.vpn_prepare)
                killProcesses(e.message)
                stopSelf()
            }

        }
        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        val binder = super.onBind(intent)
        return binder ?: serviceImpl
    }

    private fun updateNotification(content: Int) {
        NotificationManagerCompat.from(this).notify(1, NotificationCompat.Builder(this, "QuantumVpn")
                .setContentIntent(PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), 0))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getText(R.string.app_name))
                .setContentText(getText(content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build())
    }

    @Throws(IOException::class)
    private fun sendFd(fd: FileDescriptor) {
        var tries = 0
        val path = File(noBackupFilesDir, "sock_path").absolutePath
        while (true) {
            try {
                SystemClock.sleep((50 shl tries).toLong())
                LocalSocket().use { localSocket ->
                    localSocket.connect(LocalSocketAddress(path, LocalSocketAddress.Namespace.FILESYSTEM))
                    localSocket.setFileDescriptorsForSend(arrayOf(fd))
                    localSocket.getOutputStream().write(42)
                }
                return
            } catch (e: IOException) {
                if (tries > 5) throw e
                tries++
            }

        }
    }

    @Throws(IOException::class)
    private fun startClash(): Int {
        extract(this, "Country.mmdb", File(noBackupFilesDir, "Country.mmdb"))
        val configFile = File(noBackupFilesDir, "config.yaml")
        extract(this, "config.yaml", configFile)
        val config: String
        val socksPort = freePort()
        val fin = FileInputStream(configFile)
        try {
            val yaml = Yaml()
            val map = yaml.load(fin) as Map<*, *>
            map.put("socks-port", socksPort)
            map.put("secret", RESTFUL_SECRET)
            RESTFUL_PORT = freePort()
            map.put("external-controller", String.format(Locale.ENGLISH, "%s:%d", RESTFUL_HOST, RESTFUL_PORT = freePort()))
            config = yaml.dump(map)
        } finally {
            fin.close()
        }
        val writer = FileWriter(configFile)
        try {
            writer.write(config)
            writer.flush()
        } finally {
            writer.close()
        }

        val cmd = Arrays.asList(File(applicationInfo.nativeLibraryDir, Executable.WORMHOLE).absolutePath,
                "-d", ".",
                "-f", "config.yaml")
        wormhole = ProcessBuilder(cmd).directory(noBackupFilesDir).start()

        val sb = StringBuilder()
        for (s in cmd) {
            sb.append(' ').append(s)
        }
        Log.d("QuantumVpn", sb.toString())
        SystemClock.sleep(2000L)
        Thread(Runnable {
            try {
                val reader = BufferedReader(InputStreamReader(wormhole!!.inputStream))
                var line: String
                while ((line = reader.readLine()) != null) {
                    Log.d("QuantumVpn", "clash:$line")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
        try {
            val exitValue = wormhole!!.exitValue()
            throw IllegalStateException(String.format("wormhole has been exited:%d", exitValue))
        } catch (ignore: IllegalThreadStateException) {
            //            ignore.printStackTrace();
        }

        return socksPort
    }

    @Throws(IOException::class)
    private fun startVpn(): FileDescriptor {
        Executable.killAll()
        val builder = VpnService.Builder()
                .setSession("takemehomecountryroad")
                .setMtu(VPN_MTU)
                .addAddress(PRIVATE_VLAN4_CLIENT, 30)
                .addDnsServer(PRIVATE_VLAN4_ROUTER)

        builder.addRoute("0.0.0.0", 0)

        try {
            builder.addDisallowedApplication(packageName)
        } catch (ignore: PackageManager.NameNotFoundException) {
            //            ignore.printStackTrace();//impossible
        }

        val tunfd = Objects.requireNonNull<ParcelFileDescriptor>(builder.establish())
        this.tunfd = tunfd

        val socksPort = startClash()
        localDnsService = LocalDnsService()
        val localDnsPort = freePort()
        localDnsService!!.start("127.0.0.1", localDnsPort, "8.8.8.8", 53)

        val cmd = Arrays.asList(File(applicationInfo.nativeLibraryDir, Executable.TUN2SOCKS).absolutePath,
                "--netif-ipaddr", PRIVATE_VLAN4_ROUTER,
                "--socks-server-addr", "127.0.0.1:$socksPort",
                "--tunmtu", VPN_MTU.toString(),
                "--sock-path", "sock_path",
                "--dnsgw", "127.0.0.1:$localDnsPort",
                "--loglevel", "info",
                "--logger", "stdout",
                "--enable-udprelay")

        val cmdline = StringBuilder()
        for (s in cmd) {
            cmdline.append(" ").append(s)
        }
        Log.d("QuantumVpn", cmdline.toString())

        tun2socks = ProcessBuilder(cmd).directory(noBackupFilesDir).start()
        return tunfd.fileDescriptor
    }

    private fun killProcesses(reason: String?) {
        Executable.killAll()
        if (tun2socks != null) {
            tun2socks!!.destroy()
        }
        if (wormhole != null) {
            wormhole!!.destroy()
        }
        if (localDnsService != null) {
            localDnsService!!.stop()
        }
        try {
            tunfd!!.close()
        } catch (ignore: IOException) {
            //            ignore.printStackTrace();
        } finally {
            tunfd = null
        }
        try {
            if (Objects.requireNonNull<VpnStateWatcher>(vpnStateWatcher).asBinder().pingBinder()) {
                val jsonObject = JSONObject()
                jsonObject.put("code", if (reason == null) 0 else 1)
                jsonObject.put("reason", reason)
                vpnStateWatcher!!.onStateChanged(false, jsonObject.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        tun2socks = null
        wormhole = null
        localDnsService = null
    }

    companion object {

        private val VPN_MTU = 1500
        private val PRIVATE_VLAN4_CLIENT = "172.19.0.1"
        private val PRIVATE_VLAN4_ROUTER = "172.19.0.2"

        private val RESTFUL_SECRET = "cXVhbXR1bXZwbg=="
        private val RESTFUL_HOST = "127.0.0.1"
        private var RESTFUL_PORT = 0
    }
}
