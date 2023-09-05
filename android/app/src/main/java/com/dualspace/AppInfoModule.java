package com.dualspace;
import android.content.Context;
import android.content.Intent;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

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
                    try {
                        String apkPath = pm.getApplicationInfo(packageInfo.packageName, 0).sourceDir;
                        // info.putString("apkpath", apkPath);
                    } catch (PackageManager.NameNotFoundException e) {
                        // Handle exception if the package name is not found
                        System.err.println("Exception: " + e.getMessage());

                    }
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
@ReactMethod
public void copyAndOpenAPKFile(String packageName, Promise promise) {
    try {
        PackageManager pm = getReactApplicationContext().getPackageManager();
        ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);

        String sourceApkPath = appInfo.sourceDir;
        String destinationFileName = new File(sourceApkPath).getName(); // Get the original file name

        // Build the destination path in the current directory
        String destinationPath = getReactApplicationContext().getFilesDir().getAbsolutePath() + "/" + destinationFileName;

        // Copy the APK file
        copyAPKFile(sourceApkPath, destinationPath);

        // Open the copied APK file
        // openAPKFile(destinationPath);
         try {
            File file = new File(destinationPath);
            if (file.exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(getReactApplicationContext(), getReactApplicationContext().getPackageName() + ".fileprovider", file);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getReactApplicationContext().startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        promise.resolve("APK file copied and opened successfully");
    } catch (PackageManager.NameNotFoundException | IOException e) {
        promise.reject("ERROR", e.getMessage());
    }
}
    private void copyAPKFile(String sourceFilePath, String destinationFilePath) throws IOException {
        File sourceFile = new File(sourceFilePath);
        File destinationFile = new File(destinationFilePath);

        if (sourceFile.exists()) {
            FileInputStream inputStream = new FileInputStream(sourceFile);
            FileOutputStream outputStream = new FileOutputStream(destinationFile);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
        }
    }
    @ReactMethod
    public void openAPKFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = FileProvider.getUriForFile(getReactApplicationContext(), getReactApplicationContext().getPackageName() + ".fileprovider", file);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getReactApplicationContext().startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
