package com.android.wealth.http;


public interface HttpCallback {
    // 成功回调
    void onSuccess(String response);
    // 失败回调
    void onFailure(Exception e, String errorMessage);
}
