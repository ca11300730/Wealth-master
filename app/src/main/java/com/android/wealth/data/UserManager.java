package com.android.wealth.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.wealth.App;

public class UserManager {

    // 单例
    private static final UserManager USER_MANAGER = new UserManager();

    private UserManager() {
    }

    public static UserManager getUserManager() {
        return USER_MANAGER;
    }

    // 是否登录
    public boolean isLogin() {
        return getPreferences().getBoolean("login", false);
    }

    // 登录
    public void login(String userName, String userId) {
        getPreferences().edit().putBoolean("login", true)
                .putString("userName", userName)
                .putString("userId", userId).apply();
    }

    // 返回登录名
    public String getUserName() {
        return getPreferences().getString("userName", "");
    }

    public String getUserId() {
        return getPreferences().getString("userId", "0");
    }

    // 退出登录
    public void logout() {
        getPreferences().edit().clear().apply();
    }

    /**
     * 登录状态缓存
     */
    private SharedPreferences getPreferences() {
        return App.getContext().getSharedPreferences(this.getClass().getName(), Context.MODE_MULTI_PROCESS);
    }
}
