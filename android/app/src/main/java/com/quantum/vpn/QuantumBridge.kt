package com.quantum.vpn

import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class QuantumBridge private constructor() : MethodChannel.MethodCallHandler {

    internal fun loadJs(jsMap: Map<String, String>) {
        if (uiChannel != null) {
            uiChannel!!.invokeMethod("loadJs", jsMap)
        }
    }

    override fun onMethodCall(methodCall: MethodCall, result: MethodChannel.Result) {
        when (methodCall.method) {
            "checkEnable" -> if (quantum != null) {
                val enable = quantum!!.checkEnable()
                result.success(enable)
            }
            "startVpn" -> if (quantum != null) {
                quantum!!.startVpn()
            }
            "stopVpn" -> if (quantum != null) {
                quantum!!.stopVpn()
            }
            "getRESTfulApi" -> if (quantum != null) {
                val resTfulApi = quantum!!.resTfulApi
                result.success(resTfulApi)
            }
        }
    }

    companion object {
        private val CHANNEL_ONE_NAME = "quantum_vpn/channel_1"
        private val CHANNEL_TWO_NAME = "quantum_vpn/channel_2"
        private var uiChannel: MethodChannel? = null
        private val plugin = QuantumBridge()
        private var quantum: QuantumWrapper? = null
        private var initialization: Boolean = false

        internal fun init(activity: FlutterActivity) {
            if (initialization) {
                return
            }
            if (quantum == null) {
                quantum = QuantumWrapper(activity)
            }
            quantum!!.wrap(plugin)
            registerWith(activity)
            initialization = true
        }

        internal fun destroy() {
            if (quantum != null) {
                quantum!!.destroy()
                quantum = null
            }
            initialization = false
        }

        private fun registerWith(flutterActivity: FlutterActivity) {
            val channel = MethodChannel(flutterActivity.flutterView, CHANNEL_ONE_NAME)
            channel.setMethodCallHandler(plugin)
            uiChannel = MethodChannel(flutterActivity.flutterView, CHANNEL_TWO_NAME)
        }
    }
}
