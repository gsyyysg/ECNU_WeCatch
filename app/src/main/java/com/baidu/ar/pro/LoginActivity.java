package com.baidu.ar.pro;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ar.pro.AR.ARModel;
import com.baidu.ar.pro.ChatRoom.Message;
import com.baidu.ar.pro.Collection.Collection;
import com.baidu.ar.pro.Map.MapActivity;
import com.baidu.ar.pro.Task.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class LoginActivity extends Activity {

    private Button loginButton;

    private Button touristButton;

    private Button registerButton;

    private EditText emailText;

    private EditText passwordText;

    private TextView appName;

    private User user;

    private String url = "http://47.100.58.47:5000/auth/login";

    private String user_url = "http://47.100.58.47:5000/info/user";

    private ListenerService.ListenerBinder listenerBinder;

    String listenUrl = null;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            listenerBinder = (ListenerService.ListenerBinder)service;
            listenerBinder.startListen(listenUrl);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        touristButton = findViewById(R.id.tourist_button);
        registerButton = findViewById(R.id.register_button);
        loginButton = findViewById(R.id.sign_in_button);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        appName = findViewById(R.id.app_name);

        appName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/小单纯体.ttf"));


        final List<User> tempList = LitePal.where("owner = ?", "1").find(User.class);
        //从数据库获得owner为1的User类

        if (tempList.size() == 1) {
            //如果存在owner为1的
            user = tempList.get(0);

            emailText.setText(user.getEmail());
            passwordText.setText(user.getPassword());
            //将owner的email和密码添加到输入栏
        } else{
            user = null;
        }

        touristButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //传递用户信息到MapActivity
                LitePal.deleteAll(User.class);
                LitePal.deleteAll(Task.class);
                LitePal.deleteAll(Collection.class);
                LitePal.deleteAll(Message.class);
                LitePal.deleteAll(ARModel.class);

                initdb();

                user = new User();
                user.setOwner(true);
                user.setCookie("visitor");
                user.save();


                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String password, email;
                password = passwordText.getText().toString();
                email = emailText.getText().toString();

                final JSONObject JSONmessage = new JSONObject();
                try {
                    JSONmessage.put("password", password)
                            .put("email", email);
                } catch (Exception e) {
                    e.fillInStackTrace();
                    Log.d("test", e.toString());
                }

                //验证登录信息
                HttpUtil.sendPostRequest(url, JSONmessage.toString(), new okhttp3.Callback() {

                    @Override
                    public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                        Log.d("test", JSONmessage.toString());
                        String responseData = response.body().string();
                        String loginResult = responseData.substring(0, 4);

                        Log.d("test", responseData);

                        Log.d("test", loginResult);

                        if (responseData.substring(0, 4).equals("登陆成功")) {
                            //登录成功

                            if (user == null || !emailText.getText().toString().equals(user.getEmail())) {

                                final JSONObject JSONmessage = new JSONObject();
                                try {
                                    JSONmessage.put("email", email);
                                } catch (Exception e) {
                                    e.fillInStackTrace();
                                    Log.d("test", e.toString());
                                }

                                HttpUtil.sendPostRequest(user_url, JSONmessage.toString(), new okhttp3.Callback() {

                                    @Override
                                    public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                                        String responseData = response.body().string();
                                        Log.d("test", responseData);

                                        LitePal.deleteAll(User.class);
                                        LitePal.deleteAll(Task.class);
                                        LitePal.deleteAll(Collection.class);
                                        LitePal.deleteAll(Message.class);
                                        LitePal.deleteAll(ARModel.class);

                                        initdb();

                                        try{
                                            JSONObject jsonObject = new JSONObject(responseData);
                                            int id = jsonObject.getInt("id");
                                            int gold = jsonObject.getInt("currency");
                                            String nickname = jsonObject.getString("name");
                                            String email = jsonObject.getString("email");
                                            String about_me = jsonObject.getString("about_me");
                                            /*List<Integer> task_id = (List<Integer>)jsonObject.get("tasks");

                                            for(int i : task_id){
                                                Task task = new Task(i, 1);
                                                task.save();
                                            }*/

                                            Log.d("test", email +"\n"+ nickname +"\n"+ about_me +"\n"+ id +"\n"+ gold);
                                            user = new User(email,nickname, gold,id,true,password,about_me);
                                            user.setCookie(responseData.substring(4));

                                        }catch (Exception e){
                                            Log.d("test", e.toString());
                                            e.printStackTrace();
                                        }
                                        user.setTrackingCollectionID(1);

                                        user.save();

                                        Intent intent1 = new Intent(LoginActivity.this, ListenerService.class);
                                        startService(intent1);
                                        bindService(intent1, connection, BIND_AUTO_CREATE);

                                        Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                                        startActivity(intent);

                                    }

                                    @Override
                                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                                        Looper.prepare();
                                        Toast.makeText(getApplication(), "用户名或密码错误", Toast.LENGTH_LONG).show();
                                        Looper.loop();
                                        Log.d("test", e.toString());
                                        e.fillInStackTrace();
                                    }

                                });

                            }
                            else{
                                user.setCookie(responseData.substring(4));
                                user.setTrackingCollectionID(1);
                                user.save();

                                Intent intent1 = new Intent(LoginActivity.this, ListenerService.class);
                                startService(intent1);
                                bindService(intent1, connection, BIND_AUTO_CREATE);

                                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            //登录失败
                            Log.d("test", "登陆失败");
                        }

                    }

                    @Override
                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                        //Toast.makeText(getApplication(), "用户名或密码错误", Toast.LENGTH_LONG).show();
                        Log.d("test", e.toString());
                        e.fillInStackTrace();
                    }

                });
            }
        });
    }


    private void initdb() {

        Bitmap bitmap;
        InputStream stream = getResources().openRawResource(+R.drawable.task1);
        bitmap = BitmapFactory.decodeStream(stream);


        Task one = new Task("校徽寻找之旅", 1, 3, 10, "校徽寻找之旅.png");
        one.setTask_background("我是华东师范大学最伟大的校徽，我是校徽，我是校徽，我是校徽，重要的事情说三遍！！！！快来领取我吧！");
        one.setTask_ad_image_ID(R.drawable.task_ad_1);
        one.setTask_status(3); //1--已完成，2--进行中，3--未领取
        saveBitmapToFile("Image", bitmap, "校徽寻找之旅.png");


        stream = getResources().openRawResource(+R.drawable.task2);
        bitmap = BitmapFactory.decodeStream(stream);
        Task two = new Task("美食总动员", 2, 3, 8, "美食总动员.png");
        two.setTask_background("坐标上海，美食馆名：华东吃饭大学--玉米炒提子，原谅绿鸡腿，只有你想不到，没有华东师大的厨师大大做不到！");
        two.setTask_ad_image_ID(R.drawable.task_ad_2);
        two.setTask_status(3);
        saveBitmapToFile("Image", bitmap, "美食总动员.png");
        //比如这个进行中就应该插入collectedid列表


        Task three = new Task("华师女子图鉴", 3, 3, 5, "华师女子图鉴.png");
        three.setTask_ad_image_ID(R.drawable.task_ad_3);
        three.setTask_status(3);
        stream = getResources().openRawResource(+R.drawable.task3);
        bitmap = BitmapFactory.decodeStream(stream);
        Log.d("ImagePath", three.getTask_imagePath());
        saveBitmapToFile("Image", bitmap, "华师女子图鉴.png");


        Task four = new Task("捉喵记", 4, 3, 15, "捉喵记.png");
        four.setTask_background("这种生物，危险系数极高，" +
                "普通人一旦与它进行眼神接触，" +
                "便会沦陷在它形态各异却都温柔闪亮的大眼睛里；\n" +
                "可能产生的症状有：管不住上扬的嘴，迈不开僵硬的腿，" +
                "情不自禁心情大好，难以自拔伸手撸猫；\n" +
                "华师捉猫记正式开始！把它们都关进手机屏幕，" +
                "就可以拯救地球（自己独享）了！");
        four.setTask_ad_image_ID(R.drawable.task4);
        four.setTask_status(3);
        stream = getResources().openRawResource(+R.drawable.task4);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "捉喵记.png");


        List<Integer> collectionlist = new ArrayList<>();
        Collection aiji = new Collection(1, "猫王.png");
        stream = getResources().openRawResource(+R.drawable.collection1);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "猫王.png");
        aiji.setCollection_name("猫王");
        aiji.setCollection_story(" ");
        aiji.setCollection_hint("你爱过大海，我爱过你~\n" +
                "我不是天下唯一一个，为n只母猫动心的帅猫吧。\n" +
                "我很卑微，我配不上小花，之前没觉得，但慢慢发现我们真的不合适\n" +
                "其实，我也没有小红想想的那么好...\n" +
                "欸？抱我起来干什么？不行！不可以割！欸！？");
        aiji.setLatitude(121.4096);
        aiji.setLongitude(31.2356);
        aiji.setAR_ID(1);

        collectionlist.add(1);

        Collection amercancurcat = new Collection(2, "猫猫2.png");
        amercancurcat.setCollection_name("猫猫2");
        stream = getResources().openRawResource(+R.drawable.collection2);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "猫猫2.png");
        amercancurcat.setAR_ID(2);
        collectionlist.add(2);

        Collection bolila = new Collection(3, "猫猫3.png");
        bolila.setCollection_name("猫猫3");
        stream = getResources().openRawResource(+R.drawable.collection3);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "猫猫3.png");
        bolila.setAR_ID(3);
        collectionlist.add(3);

        Collection bosicat = new Collection(4, "猫猫4.png");
        bosicat.setCollection_name("猫猫4");
        stream = getResources().openRawResource(+R.drawable.collection4);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "猫猫4.png");
        bosicat.setAR_ID(4);
        collectionlist.add(4);

        Collection britishcat = new Collection(5, "猫猫5.png");
        britishcat.setCollection_name("猫猫5");
        stream = getResources().openRawResource(+R.drawable.collection5);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "猫猫5.png");
        britishcat.setAR_ID(5);
        collectionlist.add(5);

        Collection dollcat = new Collection(6, "猫猫6.png");
        dollcat.setCollection_name("猫猫6");
        stream = getResources().openRawResource(+R.drawable.collection6);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "猫猫6.png");
        dollcat.setAR_ID(6);
        collectionlist.add(6);

        Collection yiguo = new Collection(7, "猫猫7.png");
        yiguo.setCollection_name("猫猫7");
        stream = getResources().openRawResource(+R.drawable.collection7);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "猫猫7.png");
        yiguo.setAR_ID(7);
        collectionlist.add(7);

        Collection norwaycat = new Collection(8, "猫猫8.png");
        norwaycat.setCollection_name("猫猫8");
        stream = getResources().openRawResource(+R.drawable.collection8);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "猫猫8.png");
        norwaycat.setAR_ID(8);
        collectionlist.add(8);

        Collection sugelancat = new Collection(9, "猫猫9.png");
        sugelancat.setCollection_name("猫猫9");
        stream = getResources().openRawResource(+R.drawable.collection9);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "猫猫9.png");
        sugelancat.setAR_ID(9);
        collectionlist.add(9);

        Collection xinjiapocat = new Collection(10, "猫猫10.png");
        xinjiapocat.setCollection_name("猫猫10");
        stream = getResources().openRawResource(+R.drawable.collection10);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "猫猫10.png");
        xinjiapocat.setAR_ID(10);
        collectionlist.add(10);


        four.setTask_collected_ID(collectionlist);

        // finished_collection_id.add(1);finished_collection_id.add(2);finished_collection_id.add(3);finished_collection_id.add(4);finished_collection_id.add(5);finished_collection_id.add(6);finished_collection_id.add(7);
        // four.Set_Task_CollectedId(finished_collection_id);

        Task five = new Task("Hi Buddy", 5, 3, 3, "Hi Buddy.png");
        five.setTask_ad_image_ID(R.drawable.task_ad_5);
        five.setTask_status(3);
        stream = getResources().openRawResource(+R.drawable.task5);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "Hi Buddy.png");


       //Initialize AR Model
        ARModel arone = new ARModel("ar1.png",1,"10313537");
        stream = getResources().openRawResource(+R.drawable.collection1);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "ar1.png");

        ARModel artwo = new ARModel("ar2.png",2,"10313575");
        stream = getResources().openRawResource(+R.drawable.collection2);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "ar2.png");

        ARModel arthree = new ARModel("ar3.png", 3,"10313536");
        stream = getResources().openRawResource(+R.drawable.collection3);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "ar3.png");

        ARModel arfour = new ARModel("ar4.png",4,"10313538");
        stream = getResources().openRawResource(+R.drawable.collection4);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "ar4.png");

        ARModel arfive = new ARModel("ar5.png",5,"10313574");
        stream = getResources().openRawResource(+R.drawable.collection5);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "ar5.png");

        ARModel arsix = new ARModel("ar6.png",6,"10313573");
        stream = getResources().openRawResource(+R.drawable.collection6);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "ar6.png");

        ARModel arseven = new ARModel("ar7.png",7,"10313576");
        stream = getResources().openRawResource(+R.drawable.collection7);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "ar7.png");

        ARModel areight = new ARModel("ar8.png",8,"10313577");
        stream = getResources().openRawResource(+R.drawable.collection8);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "ar8.png");

        ARModel arnine = new ARModel("ar9.png",9,"10313579");
        stream = getResources().openRawResource(+R.drawable.collection9);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "ar9.png");

        ARModel arten = new ARModel("ar10.png",10,"10313578");
        stream = getResources().openRawResource(+R.drawable.collection10);
        bitmap = BitmapFactory.decodeStream(stream);
        saveBitmapToFile("Image", bitmap, "ar10.png");



        one.save();
        two.save();
        three.save();
        four.save();
        five.save();

        aiji.save();
        amercancurcat.save();
        bolila.save();
        bosicat.save();
        britishcat.save();
        dollcat.save();
        yiguo.save();
        norwaycat.save();
        sugelancat.save();
        xinjiapocat.save();

        arone.save();
        artwo.save();
        arthree.save();
        arfour.save();
        arfive.save();
        arsix.save();
        arseven.save();
        areight.save();
        arnine.save();
        arten.save();


    }

    private void saveBitmapToFile(String path, Bitmap bm, String picName)
    {
        File Folder = new File(getFilesDir()+"/"+path);
        File f = new File(Folder.getAbsolutePath()+"/"+picName);
        FileOutputStream out = null;

        try{
            if(!Folder.exists())
            {
                Folder.mkdir();
                Log.d("ImageSave","Create directory succeed!");
            }
            if(!f.exists())
            {
                f.createNewFile();
                Log.d("ImageSave","Create File succeed!");
            }
            out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            Log.d("ImageSave","Has Succeed!");
            Log.d("ImageSave",Folder.getAbsolutePath());
        }
        catch(Exception e){
            Log.d("ImageSave",e.toString());
            e.printStackTrace();
        }
        finally {
            try{
                out.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}