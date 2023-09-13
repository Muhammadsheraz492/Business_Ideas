package com.dualspace;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.VpnService;
import android.os.Build;
import android.os.RemoteException;
import android.security.KeyChain;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ConfigParser;
import de.blinkt.openvpn.core.ProfileManager;
import de.blinkt.openvpn.core.VPNLaunchHelper;

public class OpenVPNModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext context;
    private String alias;
//    private  VpnProfile vp;
private static final int REQUEST_VPN_PERMISSION = 1;

    private VpnProfile vpnProfile;

    private String config = "",
            username = "",
            password = "",
            name = "",
            dns1 = VpnProfile.DEFAULT_DNS1,
            dns2 = VpnProfile.DEFAULT_DNS2;
    private ArrayList<String> bypassPackages;

    public OpenVPNModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
        // You should handle the KeyChain alias callback appropriately here.
    }

    @Override
    public String getName() {
        return "OpenVPNModule";
    }

    @ReactMethod
    public void requestVpnPermission(Promise promise) {
        
        if (getCurrentActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getCurrentActivity(), Manifest.permission.BIND_VPN_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                // Request the permission and resolve or reject the Promise based on the result.
                ActivityCompat.requestPermissions(getCurrentActivity(), new String[]{Manifest.permission.BIND_VPN_SERVICE}, REQUEST_VPN_PERMISSION);
                promise.resolve(true); // You can also resolve with some data if needed.
            } else {
                promise.resolve(false); // Permission already granted.
            }
        } else {
            promise.reject("NO_ACTIVITY", "No current activity found.");
        }
    }

    @ReactMethod
public  void  prepare(String Config,String CountryName,String username,String password,Promise promise){


        VpnService.prepare(context);
//        VPNClient.connect()
        //    Intent vpnpermissionIntent = VpnService.prepare(context);

        try{
//    ConfigParser configParser=new ConfigParser();
//                  configParser.parseConfig(new StringReader(Config));
//vpnProfile=configParser.convertProfile();
//            int needpw=vpnProfile.needUserPWInput(false);
//    if(vpnpermissionIntent!=null){
//        if (context.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted

            // You can proceed with using the camera
//        } else {
//            // Permission is not granted, request it
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
//        }

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        }else {
//        promise.resolve("Permission Error");
//        }
                if (getCurrentActivity() != null) {
                    if (ActivityCompat.checkSelfPermission(getCurrentActivity(), Manifest.permission.BIND_VPN_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                        // Request the permission and resolve or reject the Promise based on the result.
                        ActivityCompat.requestPermissions(getCurrentActivity(), new String[]{Manifest.permission.BIND_VPN_SERVICE}, REQUEST_VPN_PERMISSION);
//                        promise.resolve(true); // You can also resolve with some data if needed.
                        connect(Config,CountryName,username,password,promise);

                    } else {
                        promise.resolve("This is Permission Error"); // Permission already granted.
                    }
                } else {
                    promise.reject("NO_ACTIVITY", "No current activity found.");
                }



        }catch(Exception e) {
        promise.reject("Error connecting to VPN", e.getMessage());
    }
}

// Inside your native module class


//    @ReactMethod
    public void connect(String Config,String contryNam,String username,String password, final Promise promise) {
        ConfigParser cp = new ConfigParser();
        try {
//            ConfigParser configParser=new ConfigParser();
            cp.parseConfig(new StringReader(Config));
            vpnProfile=cp.convertProfile();
           vpnProfile.mName=contryNam;

           if(vpnProfile.checkProfile(context)!= de.blinkt.openvpn.R.string.no_error_found){
//               throw new RemoteException(context.getString(vpnProfile.checkProfile(context)));

           }
           vpnProfile.mProfileCreator=context.getPackageName();
           vpnProfile.mUsername=username;
           vpnProfile.mPassword=password;
            vpnProfile.mDNS1 = VpnProfile.DEFAULT_DNS1;
            vpnProfile.mDNS2 = VpnProfile.DEFAULT_DNS2;
//            vpnProfile.mAlias=credentials;
//            if (dns1 != null && dns2 != null) {
//                vpnProfile.mOverrideDNS = true;
//            }

//            if (bypassPackages != null && bypassPackages.size() > 0) {
//                vpnProfile.mAllowedAppsVpn.addAll(bypassPackages);
//                vpnProfile.mAllowAppVpnBypass = true;
//            }
//            promise.resolve(vpnProfile.);
            AddProfile(promise);


            // Resolve the promise with meaningful information about the connection status
        } catch (Exception e) {
            promise.reject("Error connecting to VPN", e.getMessage());
        }
    }
public     void AddProfile(Promise promise){
    try {
        ProfileManager.setTemporaryProfile(context, vpnProfile);
        VPNLaunchHelper.startOpenVpn(vpnProfile,context);
        promise.resolve("VPN connection initiated");

    } catch (Exception e) {
        e.printStackTrace();
        promise.reject("Error connecting to VPN", e.getMessage());

        // Handle the exception here or log it for debugging.
    }

}

    @ReactMethod
    public void checkVpnStatus(Promise promise) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            // Network is connected or connecting
            // You can add additional logic to check if your VPN is running here
            promise.resolve("Vpn is Connected"); // VPN is connected
        } else {
            promise.resolve("Vpn not Connected"); // VPN is not connected
        }
    }
}
