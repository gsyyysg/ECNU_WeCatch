package com.baidu.ar.pro;

import android.os.AsyncTask;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class ListenerTask extends AsyncTask {

    private String url = "";

    private Listener listener;

    public ListenerTask (Listener listener){
        this.listener = listener;

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        /*
        final JSONObject JSONmessage = new JSONObject();
        try {

        } catch (Exception e) {
            e.fillInStackTrace();
            Log.d("test", e.toString());
        }

        //验证登录信息
        HttpUtil.sendPostRequest(url, JSONmessage.toString(), new okhttp3.Callback(){

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                Log.d("test", JSONmessage.toString());
                String responseData = response.body().string();

                //如果有新消息，则通知

            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                Log.d("test", e.toString());
                e.fillInStackTrace();
            }

        });

         */

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

}
