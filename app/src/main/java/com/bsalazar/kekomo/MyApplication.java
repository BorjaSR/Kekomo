package com.bsalazar.kekomo;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by bsalazar on 26/06/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        builder.detectFileUriExposure();
        StrictMode.setVmPolicy(builder.build());
    }
}
