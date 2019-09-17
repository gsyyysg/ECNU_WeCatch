package com.baidu.ar.pro;

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}