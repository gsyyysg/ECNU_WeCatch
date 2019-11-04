package com.baidu.ar.pro;

public interface Listener {

    void onNewMessage();

    void onNewTask();

    void onFailed();

    void onPaused();

    void onCanceled();

}
