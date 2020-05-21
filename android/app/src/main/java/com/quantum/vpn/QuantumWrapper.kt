package com.quantum.vpn

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.RemoteException
import android.webkit.WebView
import com.quantum.vpn.ipc.NativeBridge
import com.quantum.vpn.srv.QuantumVpnService
import com.quantum.vpn.util.Log
import com.quantum.vpn.util.Utils

import java.util.Collections
import java.util.HashMap

import io.flutter.BuildConfig
import io.flutter.app.FlutterActivity

class QuantumWrapper internal constructor(private val context: FlutterActivity) {

    private var plugin: QuantumBridge? = null
    private var nativeBridge: NativeBridge? = null
    private var vpnService: IVpnService? = null

    private val watcher = object : VpnStateWatcher.Stub() {
        override fun onStateChanged(enable: Boolean, json: String) {
            Log.d("QuantumVpn", "vpn状态切换:$enable $json")
            Handler(Looper.getMainLooper()).post {
                val script = String.format("javascript:native.onVpnStateChanged(%b,'%s')", enable, json)
                plugin!!.loadJs(Collections.singletonMap("js", script))
            }
        }
    }

    private val conn = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Utils.log("conn in")
            val vpnService = IVpnService.Stub.asInterface(service)
            try {
                vpnService.setVpnStateListener(watcher)
            } catch (e: RemoteException) {
                Utils.log("conn exception:$e")
                e.printStackTrace()
            }

            nativeBridge!!.setVpnService(vpnService)
            this@QuantumWrapper.vpnService = vpnService
            Utils.log("conn out")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            nativeBridge!!.setVpnService(null)
            Utils.log("disconn call")
        }
    }

    internal val resTfulApi: String
        get() = if (nativeBridge != null) {
            nativeBridge!!.resTfulApi
        } else ""

    fun wrap(plugin: QuantumBridge) {
        this.plugin = plugin
        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        context.bindService(Intent(context, QuantumVpnService::class.java), conn, Context.BIND_AUTO_CREATE)
        nativeBridge = NativeBridge()
    }

    fun checkEnable(): Boolean {
        return if (nativeBridge != null) {
            nativeBridge!!.checkEnable()
        } else false
    }

    fun startVpn() {
        if (nativeBridge != null) {
            nativeBridge!!.startVpn()
        }
    }

    fun stopVpn() {
        if (nativeBridge != null) {
            nativeBridge!!.stopVpn()
        }
    }

    internal fun destroy() {
        try {
            vpnService!!.setVpnStateListener(null)
        } catch (e: RemoteException) {
            e.printStackTrace()
        } finally {
            context?.unbindService(conn)
        }
    }
}
