package com.dualspace;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.widget.Toast;

public class AppInfoModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    public static WritableArray applist;

    public AppInfoModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return "AppInfoModule"; // This is the name you will use in JavaScript
    }
       @ReactMethod
public void createCalendarEvent(String name, String location) {
   Log.d("CalendarModule", "Create event called with name: " + name
   + " and location: " + location);
}
//    @ReactMethod
//public void getInstalledApplications(Promise promise) {
//    try {
//        Log.d("Hello","Function Called");
////        system.err.println("exception: " + ex.getmessage());
//
//         PackageManager packageManager = getReactApplicationContext().getPackageManager();
//        Toast.makeText(reactContext, "Hello Function is Runing", Toast.LENGTH_SHORT).show();
//
//        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
//         List<String> appNames = new ArrayList<>();
//
//         for (ApplicationInfo appInfo : installedApplications) {
//             appNames.add(appInfo.loadLabel(packageManager).toString());
//         }
//
//        promise.resolve(packageManager); // Ensure you are resolving with the correct data type
//    } catch (Exception e) {
//        promise.reject("ERROR", e.getMessage());
//    }
//}
@ReactMethod
public void getInstalledApplications(Promise promise) {
    try {
        //get a list of installed apps.
        PackageManager pm = getReactApplicationContext().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(0);

        WritableArray app_list = new WritableNativeArray();

        // ...

        for (ApplicationInfo packageInfo : packages) {
            try {

                if (getReactApplicationContext().getPackageManager().getLaunchIntentForPackage(packageInfo.packageName) != null) {
                    WritableMap info = new WritableNativeMap();
                    info.putString("name", packageInfo.loadLabel(pm).toString());
                    info.putString("packagename", packageInfo.packageName);

                    // ...
//
                    app_list.pushMap(info);
                }

            } catch (Exception ex) {
                System.err.println("Exception: " + ex.getMessage());
            }
        }

        promise.resolve(app_list); // Ensure you are resolving with the correct data type
    } catch (Exception e) {
        promise.reject("ERROR", e.getMessage());
    }
}

}
