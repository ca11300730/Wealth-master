package com.android.wealth;

import android.app.Application;
import android.content.Context;


public class App extends Application {

    private static  Application APPLICATION;
    @Override
    public void onCreate() {
        super.onCreate();
        APPLICATION = this;

    }

    public static Context getContext() {
        return APPLICATION.getApplicationContext();
    }
}
