package com.dualspace;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class AppInfoModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

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
    @ReactMethod
public void getInstalledApplications(Promise promise) {
    try {
        PackageManager packageManager = reactContext.getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        List<String> appNames = new ArrayList<>();

        for (ApplicationInfo appInfo : installedApplications) {
            appNames.add(appInfo.loadLabel(packageManager).toString());
        }

        promise.resolve(appNames); // Ensure you are resolving with the correct data type
    } catch (Exception e) {
        promise.reject("ERROR", e.getMessage());
    }
}
}
