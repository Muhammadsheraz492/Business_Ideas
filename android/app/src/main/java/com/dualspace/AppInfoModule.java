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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
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
                    Drawable icon = packageInfo.loadIcon(pm);
        if (icon != null) {
            Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String base64Icon = Base64.encodeToString(byteArray, Base64.DEFAULT);
            info.putString("icon", base64Icon);
        }


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
