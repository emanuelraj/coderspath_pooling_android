package com.herosandzeros.pooling;

import android.content.Intent;

import com.orm.SugarApp;

import timber.log.Timber;

/**
 * Created by mathan on 29/8/15.
 */
public class MainApplication extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        startService(new Intent(getApplicationContext(), SocketService.class));
    }
}
