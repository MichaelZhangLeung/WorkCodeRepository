package com.quantum.vpn.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import com.quantum.vpn.R
import com.quantum.vpn.VpnStateWatcher
import com.quantum.vpn.srv.QuantumVpnService

import org.json.JSONObject

import java.util.Objects

class VpnRequestActivity : AppCompatActivity() {

    protected fun onCreate(@Nullable savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        val intent = VpnService.prepare(this)
        if (intent == null) {
            onActivityResult(REQUEST_CONNECT, RESULT_OK, null)
        } else {
            startActivityForResult(intent, REQUEST_CONNECT)
        }
    }

    protected fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val intent = Intent(this, QuantumVpnService::class.java)
            intent.putExtras(getIntent())
            ContextCompat.startForegroundService(this, intent)
        } else {
            try {
                val extras = Objects.requireNonNull(getIntent().getExtras())
                val binder = Objects.requireNonNull<IBinder>(extras.getBinder("watcher"))
                val watcher = VpnStateWatcher.Stub.asInterface(binder)
                val jsonObject = JSONObject()
                jsonObject.put("code", 1)
                jsonObject.put("reason", getText(R.string.vpn_permission_denied).toString())
                watcher.onStateChanged(false, jsonObject.toString())
            } catch (ignore: Exception) {
                //                ignore.printStackTrace();
            }

            Toast.makeText(this, R.string.vpn_permission_denied, Toast.LENGTH_LONG).show()
        }
        finish()
    }

    companion object {

        private val REQUEST_CONNECT = 1
    }

}
