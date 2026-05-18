package com.android.wealth.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.wealth.App;

public class PreUtils {

    private static PreUtils instance;

    public static PreUtils getInstance() {
        if (instance==null){
            instance = new PreUtils();
        }
        return instance;
    }

    public static final String CONFIG_FILE_NAME = "config";
    
    private Context getContext(){
        return App.getContext();
    }

    public  void putBoolean(String key, boolean value){
        SharedPreferences sp = getContext().getSharedPreferences(CONFIG_FILE_NAME,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).apply();
    }

    public  boolean getBoolean(String key, boolean defValue){
        SharedPreferences sp = getContext().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }

    public  void putString(String key, String value){
        SharedPreferences sp = getContext().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key,value).apply();
    }

    public  String getString(String key, String defValue){
        SharedPreferences sp = getContext().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key,defValue);
    }

    public  void putInt(String key, int value){
        SharedPreferences sp = getContext().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).apply();
    }

    public  int getInt(String key, int defValue){
        SharedPreferences sp = getContext().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key,defValue);
    }

    public  void putLong(String key, long value){
        SharedPreferences sp = getContext().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putLong(key,value).apply();
    }

    public  long getLong(String key, long defValue){
        SharedPreferences sp = getContext().getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key,defValue);
    }

}
