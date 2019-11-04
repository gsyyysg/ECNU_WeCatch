package com.baidu.ar.pro;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class ListenerService extends Service {

    private ListenerTask listenerTask;

    private String listenUrl = "";

    private Listener listener = new Listener() {
        @Override
        public void onNewMessage() {
            getNotificationManager().notify(1, getNotification("你收到了一条新信息！"));
        }

        @Override
        public void onNewTask() {
            getNotificationManager().notify(1, getNotification("有新的任务发布！"));

        }

        @Override
        public void onFailed() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onCanceled() {

        }
    };

    private ListenerBinder mBinder = new ListenerBinder();

    public ListenerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class ListenerBinder extends Binder{

        public void startListen(String url){
            if(listenerTask == null){
                listenUrl = url;
                listenerTask = new ListenerTask(listener);
                listenerTask.execute(listenUrl);
                Toast.makeText(ListenerService.this, "Listening...", Toast.LENGTH_SHORT). show();
            }
        }

        public void pauseListen(){

        }

        public void cancelListen() {

        }

        public void newMessage(){
            listener.onNewMessage();
        }

        public void newTask(){
            listener.onNewTask();
        }

    }

    private NotificationManager getNotificationManager(){
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title){

        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("default", "channelname", NotificationManager.IMPORTANCE_HIGH);
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，通知才能正常弹出
            getNotificationManager().createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(this, LoginActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.drawable.icon);
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        return builder.build();

    }
}
