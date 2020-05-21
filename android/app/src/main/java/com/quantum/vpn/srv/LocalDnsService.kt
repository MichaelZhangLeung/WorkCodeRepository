package com.quantum.vpn.srv

import com.quantum.vpn.util.Log

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress

class LocalDnsService {

    @Volatile
    private var running = false
    private var datagramSocket: DatagramSocket? = null

    fun start(host: String, port: Int, remoteHost: String, remotePort: Int) {
        running = true
        Thread(Runnable {
            try {
                startServer(host, port, remoteHost, remotePort)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }

    fun stop() {
        if (running && datagramSocket != null) {
            running = false
            datagramSocket!!.close()
            datagramSocket = null
        }
    }

    @Throws(IOException::class)
    private fun startServer(host: String, port: Int, remoteHost: String, remotePort: Int) {
        datagramSocket = DatagramSocket(InetSocketAddress(host, port))
        val buffer = ByteArray(512)
        while (running) {
            val packet = DatagramPacket(buffer, buffer.size)
            Log.d("QuantumVpn", "receive dns packet")
            datagramSocket!!.receive(packet)
            Log.d("QuantumVpn", "forward dns packet ")
            val replyPacket = forward(remoteHost, remotePort, packet)
            replyPacket.socketAddress = packet.socketAddress
            try {
                Log.d("QuantumVpn", "reply dns packet ")
                datagramSocket!!.send(replyPacket)
            } catch (ignore: IOException) {
                Log.e("QuantumVpn", "reply dns packet", ignore)
                //                ignore.printStackTrace();
            }

        }
    }

    @Throws(IOException::class)
    private fun forward(host: String, port: Int, rawPacket: DatagramPacket): DatagramPacket {
        val socket = DatagramSocket()
        socket.send(DatagramPacket(rawPacket.data, rawPacket.offset, rawPacket.length, InetSocketAddress(host, port)))
        val buffer = ByteArray(512)
        val replyPacket = DatagramPacket(buffer, buffer.size)
        socket.receive(replyPacket)
        return replyPacket
    }

}
