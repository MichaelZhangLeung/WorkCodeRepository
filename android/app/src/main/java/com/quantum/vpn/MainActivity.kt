package com.quantum.vpn

import android.os.Bundle
import com.quantum.vpn.util.Utils
import io.flutter.app.FlutterActivity
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Utils.log("MainActivity.onCreate")
        super.onCreate(savedInstanceState)
        GeneratedPluginRegistrant.registerWith(this)
        QuantumBridge.init(this)
    }

    override fun onResume() {
        Utils.log("MainActivity.onResume")
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    override fun onDestroy() {
        Utils.log("MainActivity.onDestroy")
        super.onDestroy()
        QuantumBridge.destroy()
    }
}
