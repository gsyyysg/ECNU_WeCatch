package com.baidu.ar.pro;

import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

public class ListenerTask extends AsyncTask {

    private String task_url = "http://47.100.58.47:5000/info/messages";

    private String collection_url = "http://47.100.58.47:5000/info/collection";

    private String message_url = "http://47.100.58.47:5000/info/messages";

    private User user;

    private Listener listener;

    public ListenerTask (Listener listener){
        this.listener = listener;
    }

    @Override
    protected Object doInBackground(Object[] objects) {


        while(true) {
            Log.d("test", "doinbackground");
            try {
                Thread.currentThread();
                Thread.sleep(10000);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (user == null) {
                //延迟

                List<User> tempList = LitePal.where("owner = ?", "1").find(User.class);

                if(tempList.size() >= 1)
                    user = tempList.get(0);
                else
                    user = null;


                continue;
            }

            String header[] = new String[2];
            header[0] = "Authorization";
            header[1] = user.getCookie();

            final JSONObject JSONmessage = new JSONObject();
            try {
                JSONmessage.put("id", user.getUser_ID());
            } catch (Exception e) {
                e.fillInStackTrace();
                Log.d("test", e.toString());
            }

            HttpUtil.sendPostRequest(message_url, JSONmessage.toString(),header, new okhttp3.Callback() {

                @Override
                public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d("test", responseData);

                    if(responseData.equals("[{}]") || responseData.startsWith("<!DOCTYPE"))return ;
/*
                    responseData = responseData.substring(1, responseData.length() - 1).replace("\"", "");
                    String[] list = responseData.split(",");


                    JSONObject jsonArray[] = new JSONObject[list.length];


 */

                    /*
                    for(int i=0; i<list.length; i++){

                        try {
                            jsonArray[i] = new JSONObject(list[i]);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    */

                    //处理JSON，生成message并保存
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);

                        List<Message> temp = LitePal.findAll(Message.class);
                        if(temp.size() < jsonArray.length())
                            listener.onNewMessage();
                        for (int i = temp.size(); i < jsonArray.length(); ++i) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String content = jsonObject.getString("content");
                            int receiver_id = jsonObject.getInt("receiver_id");
                            int sender_id = jsonObject.getInt("sender_id");

                            com.baidu.ar.pro.ChatRoom.Message msg = new com.baidu.ar.pro.ChatRoom.Message(id,content, sender_id,receiver_id);
                            msg.save();
                        }
                        listener.onNewTask();
                    }catch(Exception e){
                        Log.d("test", e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                    //Toast.makeText(getApplication(), "用户名或密码错误", Toast.LENGTH_LONG).show();
                    Log.d("test", e.toString());
                    e.fillInStackTrace();
                }

            });

/*
            HttpUtil.sendGetRequest(task_url, new okhttp3.Callback() {

                @Override
                public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {

                    String responseData = response.body().string();
                    Log.d("test", responseData);

                    //处理JSON 生成Task并保存
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        List<Task> temp = LitePal.findAll(Task.class);
                        if(temp.size() < jsonArray.length())
                            listener.onNewTask();
                        for (int i = temp.size(); i < jsonArray.length(); ++i) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String name = jsonObject.getString("name");
                            int difficulty = jsonObject.getInt("difficulty");
                            String description = jsonObject.getString("description");
                            int img = jsonObject.getInt("img");
                            List<Integer> collections = (List<Integer>) jsonObject.get("collections");

                            Task task;
                            List<Task> list = LitePal.where("task_id = ?", Integer.toString(id)).find(Task.class);
                            if(list.size() != 0)
                                task = list.get(0);
                            else {
                                task = new Task();
                                task.setTask_status(0);
                            }
                            final List<com.baidu.ar.pro.Collection.Collection> collectionList = new List<Collection>;
                            for(int j : collections){
                                final JSONObject JSONmessage = new JSONObject();
                                try {
                                    JSONmessage.put("id", i);
                                } catch (Exception e) {
                                    e.fillInStackTrace();
                                    Log.d("test", e.toString());
                                }

                                HttpUtil.sendPostRequest(collection_url, JSONmessage.toString(), new okhttp3.Callback() {

                                    @Override
                                    public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                                        String responseData = response.body().string();
                                        Log.d("test", responseData);

                                        try {
                                            JSONObject jsonObject2 = new JSONObject(responseData);
                                            int id = jsonObject2.getInt("id");
                                            String name = jsonObject2.getString("name");
                                            int difficulty = jsonObject2.getInt("difficulty");
                                            String description = jsonObject2.getString("description");
                                            int img = jsonObject2.getInt("img");
                                            String hint = jsonObject2.getString("hint");
                                            double latitude = jsonObject2.getDouble("latitude");
                                            double longitude = jsonObject2.getDouble("longitude");
                                            int reward = jsonObject2.getInt("reward");

                                            com.baidu.ar.pro.Collection.Collection c = new com.baidu.ar.pro.Collection.Collection(id, name, img, description, hint, reward, 0);
                                            collectionList.add(c);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }


                                    }

                                    @Override
                                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                                        Log.d("test", e.toString());
                                        e.fillInStackTrace();
                                    }

                                });
                            }
                            task.setTask_ID(id);
                            task.setTask_name(name);
                            task.setTask_difficulty(difficulty);
                            task.setTask_background(description);
                            task.setTask_ad_image_ID(img);
                            task.save();

                        }

                    }catch(Exception e){
                        Log.d("test", e.toString());
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                    //Toast.makeText(getApplication(), "用户名或密码错误", Toast.LENGTH_LONG).show();
                    Log.d("test", e.toString());
                    e.fillInStackTrace();
                }

            });

            HttpUtil.sendPostRequest(collection_url,  ,new okhttp3.Callback() {

                @Override
                public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {

                    String responseData = response.body().string();
                    Log.d("test", responseData);

                    //处理JSON 生成Task并保存
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        List<Task> temp = LitePal.findAll(Task.class);
                        if(temp.size() < jsonArray.length())
                            listener.onNewTask();
                        for (int i = temp.size(); i < jsonArray.length(); ++i) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String name = jsonObject.getString("name");
                            int difficulty = jsonObject.getInt("difficulty");
                            String description = jsonObject.getString("description");
                            int img = jsonObject.getInt("img");
                            List<Integer> collections = (List<Integer>) jsonObject.get("collections");

                            Task task;
                            List<Task> list = LitePal.where("task_id = ?", Integer.toString(id)).find(Task.class);
                            if(list.size() != 0)
                                task = list.get(0);
                            else {
                                task = new Task();
                                task.setTask_status(0);
                            }
                            task.setTask_ID(id);
                            task.setTask_name(name);
                            task.setTask_difficulty(difficulty);
                            task.setTask_background(description);
                            task.setTask_ad_image_ID(img);
                            task.save();
                        }

                    }catch(Exception e){
                        Log.d("test", e.toString());
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                    //Toast.makeText(getApplication(), "用户名或密码错误", Toast.LENGTH_LONG).show();
                    Log.d("test", e.toString());
                    e.fillInStackTrace();
                }

            });

            try {
                Thread.currentThread();
                Thread.sleep(5000);
            }catch (Exception e){
                e.printStackTrace();
            }

 */
        }


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
