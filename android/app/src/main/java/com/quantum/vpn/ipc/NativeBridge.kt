package com.quantum.vpn.ipc

import android.os.RemoteException
import android.webkit.JavascriptInterface

import com.quantum.vpn.IVpnService
import com.quantum.vpn.util.Log

class NativeBridge {

    private var vpnService: IVpnService? = null

    val resTfulApi: String
        @JavascriptInterface
        get() {
            Log.d("QuantumVpn", "getRESTfulApi")
            if (vpnService == null) {
                Log.e("QuantumVpn", "VpnService not prepare!")
                return "{}"
            }
            try {
                return vpnService!!.resTfulApi
            } catch (e: RemoteException) {
                return "{}"
            }

        }

    fun setVpnService(vpnService: IVpnService) {
        this.vpnService = vpnService
    }

    @JavascriptInterface
    fun checkEnable(): Boolean {
        Log.d("QuantumVpn", "checkEnable")
        if (vpnService == null) {
            Log.e("QuantumVpn", "VpnService not prepare!")
            return false
        }
        try {
            return vpnService!!.checkEnable()
        } catch (e: RemoteException) {
            return false
        }

    }

    @JavascriptInterface
    fun startVpn() {
        Log.d("QuantumVpn", "startVpn")
        if (vpnService == null) {
            Log.e("QuantumVpn", "VpnService not prepare!")
            return
        }
        try {
            vpnService!!.startVpn()
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }

    @JavascriptInterface
    fun stopVpn() {
        Log.d("QuantumVpn", "stopVpn")
        if (vpnService == null) {
            Log.e("QuantumVpn", "VpnService not prepare!")
            return
        }
        try {
            vpnService!!.stopVpn()
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }
}
