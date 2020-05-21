package com.quantum.vpn.util

import android.content.Context

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.ServerSocket

object Utils {

    fun freePort(): Int {
        try {
            val socket = ServerSocket(0)
            try {
                return socket.localPort
            } finally {
                socket.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return 0
        }

    }

    @Throws(IOException::class)
    fun extract(context: Context, assetsPath: String, destFile: File) {
        extract(context, assetsPath, destFile.absolutePath)
    }

    @Throws(IOException::class)
    fun extract(context: Context, assetsPath: String, destPath: String) {
        val `in` = context.assets.open(assetsPath)
        val out = FileOutputStream(destPath)
        try {
            val buffer = ByteArray(4096)
            var len: Int
            len = `in`.read(buffer)
            while (len > 0) {
                out.write(buffer, 0, len)
            }
            out.flush()
        } finally {
            `in`.close()
            out.close()
        }
    }

    fun log(msg: String) {
        Log.d("QuantumVpn", msg)
    }

}
