package com.crazyma.jsoncakesample;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by david on 2016/7/7.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
