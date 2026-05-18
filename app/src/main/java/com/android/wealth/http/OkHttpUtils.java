package com.android.wealth.http;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {

    private Handler handler = new Handler(Looper.getMainLooper());

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectionPool(new ConnectionPool(500, 20, TimeUnit.MINUTES))
            .connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .build();

    /**
     * okHttp get方法的url拼接参数,并返回request对象
     *
     * @param url
     * @param paramMap
     * @return
     */
    private static Request getRequest(String url, Map<String, Object> paramMap, @Nullable Map<String, String> headers) {
        if (paramMap != null && paramMap.size() > 0) {
            StringBuilder urlBuilder = new StringBuilder(url);
            urlBuilder.append("?");
            for (String key : paramMap.keySet()) {
                urlBuilder
                        .append(key)
                        .append("=")
                        .append(paramMap.get(key))
                        .append("&");
            }
            url = urlBuilder.substring(0, urlBuilder.length() - 1);
        }
        Request.Builder urlBuilder = new Request.Builder().url(url);
        if (headers != null) {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                if (value != null) {
                    urlBuilder.addHeader(key, value);
                }
            }
        }
        return urlBuilder.get().build();
    }

    public static void get(Activity activity, String url, Map<String, Object> paramMap, HttpCallback callback) {
        get(activity, url, paramMap, null, callback);
    }


    /**
     * okHttp get异步请求
     *
     * @param url
     * @param paramMap
     * @param callback
     * @return
     */
    public static void get(Activity activity, String url, Map<String, Object> paramMap, @Nullable Map<String, String> headers, HttpCallback callback) {
        CLIENT.newCall(getRequest(url, paramMap, headers))
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        requestFailure(activity, callback, e, e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.code() == 200 && response.body() != null) {
                            requestSuccess(activity, callback, response.body().string());
                        } else {
                            requestSuccess(activity, callback, "{}");
                        }
                    }
                });
    }

    /**
     * okHttp post返回request对象
     *
     * @param url
     * @param body
     * @return
     */
    private static Request getRequest(String url, RequestBody body, @Nullable Map<String, String> headers) {

        Request.Builder urlBuilder = new Request.Builder().url(url);
        if (headers != null) {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                if (value != null) {
                    urlBuilder.addHeader(key, value);
                }
            }
        }
        return urlBuilder
                .post(body)
                .build();
    }

    public static void post(Activity activity, String url, RequestBody body, HttpCallback callback) {
        post(activity, url, body, null, callback);
    }

    /**
     * okHttp post异步请求(json方式提交)
     *
     * @param url
     * @param bodyMap
     * @param callback
     */
    public static void post(Activity activity, String url, RequestBody body, @Nullable Map<String, String> headers, HttpCallback callback) {
        CLIENT.newCall(getRequest(url, body,headers))
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        requestFailure(activity, callback, e, e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.code() == 200 && response.body() != null) {
                            requestSuccess(activity, callback, response.body().string());
                        } else {
                            requestSuccess(activity, callback, "{}");
                        }
                    }
                });
    }

    private static void requestSuccess(Activity activity, HttpCallback callback, String result) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        activity.runOnUiThread(() -> {
            if (callback != null) {
                callback.onSuccess(result);
            }
        });
    }

    private static void requestFailure(Activity activity, HttpCallback callback, Exception e, String result) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        activity.runOnUiThread(() -> {
            if (callback != null) {
                callback.onFailure(e, result);
            }
        });
    }


}
