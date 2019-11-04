package com.baidu.ar.pro;


import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public static void sendRequest(final String address, final Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendPostRequest(final String address, String message ,final Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, JSON);
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendPostRequest(final String address, String message, String[] header ,final Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, JSON);
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .addHeader(header[0], header[1])
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendGetRequest(final String address, final Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendGetRequest(final String address, String[] header, final Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .get()
                .addHeader(header[0], header[1])
                .build();
        client.newCall(request).enqueue(callback);
    }


}
