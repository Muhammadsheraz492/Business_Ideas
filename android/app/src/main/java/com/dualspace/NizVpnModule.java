package com.dualspace;

import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class NizVpnModule extends ReactContextBaseJavaModule {

    private static final String EVENT_VPN_STATUS = "onVpnStatus";

    public NizVpnModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "NizVpnModule";
    }

    @ReactMethod
    public void startVpn(Callback callback) {
        // Trigger VPN connection from here (you may communicate with MyVpnService)
        // Handle VPN-related logic and send status updates to React Native using events
        // Example:
//        if (vpnConnectionSuccessful) {
//            callback.invoke("VPN started successfully");
//            sendVpnStatus("connected");
//        } else {
//            callback.invoke("Failed to start VPN");
//            sendVpnStatus("disconnected");
//        }
    }

    private void sendVpnStatus(String status) {
        // Send VPN status updates to React Native
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(EVENT_VPN_STATUS, status);
    }
}
