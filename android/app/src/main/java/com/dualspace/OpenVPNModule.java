package com.dualspace;


import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import java.io.FileWriter;
import java.io.StringReader;

import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ConfigParser;
import de.blinkt.openvpn.core.VpnStatus;
import de.blinkt.openvpn.core.ProfileManager;
import de.blinkt.openvpn.core.VPNLaunchHelper;

public class OpenVPNModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext context;
    public OpenVPNModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context=reactContext;
    }

    @Override
    public String getName() {
        return "OpenVPNModule";
    }

    @ReactMethod
    public void connect(String configFilePath, final Promise promise) {
        ConfigParser cp=new ConfigParser();


        try {
            cp.parseConfig(new StringReader(configFilePath));
            VpnProfile vp=cp.convertProfile();
            if(vp.checkProfile(context)!= de.blinkt.openvpn.R.string.no_error_found){
                promise.reject("Error connecting to VPN", context.getString(vp.checkProfile(context)));

            }
            vp.mProfileCreator=context.getPackageName();

            vp.mName="VPN";
            vp.mUsername="openvpn";
            vp.mPassword="Hacker@1";
            ProfileManager.setTemporaryProfile(context,vp);
           VPNLaunchHelper.startOpenVpn(vp,context);
            promise.resolve("Connected Vpn");

        } catch (Exception e) {
            promise.reject("Error connecting to VPN", e.getMessage());
        }
    }
}
