package com.serveme.gazaunderattack;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.webkit.CookieSyncManager;
import java.io.File;
import java.security.MessageDigest;

public class GazaUnderAttackApplication extends Application {
    private static Context applicationContext;

    public static Context getContext() {
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.serveme.gazaunderattack",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", "*******************************");
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.d("KeyHash:", "*******************************");
            }
        } catch (Exception e) {

        }

        CookieSyncManager.createInstance(this);

        try {
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            File httpCacheDir = new File(getCacheDir(), "http");
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (Exception httpResponseCacheNotAvailable) {
        }

        applicationContext = this.getApplicationContext();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
