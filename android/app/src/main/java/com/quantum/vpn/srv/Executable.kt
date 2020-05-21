package com.quantum.vpn.srv

import android.system.Os
import android.system.OsConstants
import android.text.TextUtils

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FilenameFilter
import java.util.Objects

object Executable {

    val TUN2SOCKS = "libtun2socks.so"
    val WORMHOLE = "libwormhole.so"

    fun killAll() {
        val processes = Objects.requireNonNull(File("/proc").listFiles { dir, name -> TextUtils.isDigitsOnly(name) })
        for (process in processes) {
            try {
                val reader = BufferedReader(FileReader(File(process, "cmdline")))
                val cmdline = Objects.requireNonNull(reader.readLine())
                if (cmdline.contains(TUN2SOCKS) || cmdline.contains(WORMHOLE)) {
                    Os.kill(Integer.parseInt(process.name), OsConstants.SIGKILL)
                }
            } catch (ignore: Exception) {
                //                ignore.printStackTrace();
            }

        }
    }
}
