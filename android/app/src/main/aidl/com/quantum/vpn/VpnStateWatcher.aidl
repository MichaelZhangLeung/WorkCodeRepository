// StateWatcher.aidl
package com.quantum.vpn;

interface VpnStateWatcher {
    void onStateChanged(boolean enable, String json);
}
