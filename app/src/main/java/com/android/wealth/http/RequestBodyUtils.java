package com.android.wealth.http;

import android.util.ArrayMap;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestBodyUtils {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MULTIPART = MediaType.parse("multipart/form-data");
    public static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8");
    public static final MediaType TEXT = MediaType.parse("text/plain");
    public static final MediaType FILE = MediaType.parse("application/otcet-stream");

    public static RequestBody createForm(Map<String, Object> params) {
        String body = mapToQuery(params);
        return createForm(body);
    }

    public static RequestBody createForm(String body) {
        return RequestBody.create(FORM, body);
    }

    public static RequestBody createJson(Map<String, Object> params) {
        String json = "{}";
        if (params != null) {
            ArrayMap<String, Object> root = new ArrayMap<>();
            for (String key : params.keySet()) {
                if (params.get(key) != null) {
                    root.put(key, params.get(key) + "");
                }
            }
            json = new JSONObject(root).toString();
        }
        return createJson(json);
    }

    public static RequestBody createJson(String json) {
        return RequestBody.create(JSON, json);
    }


    private static String mapToQuery(Map<String, Object> parms) {
        StringBuilder result = new StringBuilder();
        if (parms != null) {
            for (String str : parms.keySet()) {
                String value = parms.get(str) + "";
                result.append(str).append("=").append(value).append("&");
            }
            if (result.length() > 2) {
                result = new StringBuilder(result.substring(0, result.length() - 1));
            }
        }
        return result.toString();
    }

    public static MultipartBody getMultipartBody(List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < files.size(); i++) {
            RequestBody requestBody = RequestBody.create(MULTIPART, files.get(i));
            builder.addFormDataPart("pictures" + i, files.get(i).getName(), requestBody);
        }
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

    public static MultipartBody getVideoMultipartBody(File files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody requestBody = RequestBody.create(MULTIPART, files);
        builder.addFormDataPart("file", files.getName(), requestBody);
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

}
