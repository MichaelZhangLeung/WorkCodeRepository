// IVpnService.aidl
package com.quantum.vpn;

import com.quantum.vpn.VpnStateWatcher;

interface IVpnService {

    boolean checkEnable();

    void startVpn();

    void stopVpn();

    String getRESTfulApi();

    void setVpnStateListener(in VpnStateWatcher watcher);
}
