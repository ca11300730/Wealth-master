package com.android.wealth.utils;

import com.google.gson.Gson;


/**
 * GSON 单例
 */
public class GsonPoll {

    private static final class Instance {
        private static final Gson S_GSON = new  Gson();
    }

    public static Gson get(){
        return Instance.S_GSON;
    }
}
