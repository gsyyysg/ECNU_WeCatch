package com.baidu.ar.pro.ChatRoom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ar.pro.HttpUtil;
import com.baidu.ar.pro.LoginActivity;
import com.baidu.ar.pro.R;
import com.baidu.ar.pro.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;


public class ChatRoomActivity extends Activity {

    private List<Message> msgList = new ArrayList<Message>();

    private TextView chatroomName;

    private EditText inputText;

    private EditText inputFriendEmail;

    private Button sendButton;

    private Button addFriendButton;

    private ImageButton backButton;

    private RecyclerView msgRecyclerView;

    private MsgAdapter msgAdapter;

    private RecyclerView friendRecyclerView;

    private FriendAdapter friendAdapter;

    private List<User> friendList;

    private User user;

    private String send_url = "http://47.100.58.47:5000/test/send";

    private String add_url = "http://47.100.58.47:5000/info/user";

    private Handler handler = new Handler();

    private User friend = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_layout);
        inputText = findViewById(R.id.input_text);
        inputFriendEmail = findViewById(R.id.input_friend_id);
        sendButton = findViewById(R.id.send_Button);
        addFriendButton = findViewById(R.id.add_friend_button);
        chatroomName = findViewById(R.id.chatroomNameText);
        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        backButton = findViewById(R.id.chat_back_button);

        //获取用户信息
        List<User> tempList = LitePal.where("owner = ?", "1").find(User.class);

        if(tempList.size() == 1)
            user = tempList.get(0);
        else if(tempList.size() == 0){
            //没有owner，重新登录
            Intent intent = new Intent(ChatRoomActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else{
            //owner人数大于1，数据库数据出问题
        }
        friendList = LitePal.where("owner = ?", "0").find(User.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(msgAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    final Message msg = new Message(content, user.getUser_ID(), friendAdapter.getFriend_id());

                    Log.d("test", friendAdapter.getFriend_id()+"");

                    final JSONObject JSONmessage = new JSONObject();
                    try {
                        //将message的信息加入
                        JSONmessage.put("text", msg.getContent())
                                .put("id", msg.getReceiver_id());

                    } catch (Exception e) {
                        e.fillInStackTrace();
                        Log.d("test", e.toString());
                    }


                    String header[] = new String[2];
                    header[0] = "Authorization";
                    header[1] = user.getCookie();

                    HttpUtil.sendPostRequest(send_url, JSONmessage.toString(), header, new okhttp3.Callback(){

                        @Override
                        public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                            String responseData = response.body().string();
                            Log.d("test", responseData);

                            //处理，并加入数据库
                            if(responseData.equals("Message sent")){

                            }
                            else{

                            }

                        }

                        @Override
                        public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                            Log.d("test", e.toString());
                            e.fillInStackTrace();
                        }

                    });

                    try {
                        Thread.currentThread();
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    msgList = msgAdapter.mMsgList;
                    msgList.add(msg);
                    msg.save();
                    msgAdapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    inputText.setText(""); // 清空输入框中的内容
                }
            }
        });
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String content = inputFriendEmail.getText().toString();

                final JSONObject JSONmessage = new JSONObject();
                try {
                    //将所查找的朋友email加入
                    JSONmessage.put("email", content);
                } catch (Exception e) {
                    e.fillInStackTrace();
                    Log.d("test", e.toString());
                }

                HttpUtil.sendPostRequest(add_url, JSONmessage.toString(), new okhttp3.Callback(){

                    @Override
                    public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                        final String responseData = response.body().string();
                        Log.d("test", responseData);

                        if(!responseData.equals("找不到")){


                            try {
                                JSONObject jsonObject = new JSONObject(responseData);

                                int id = jsonObject.getInt("id");
                                String nickname = jsonObject.getString("name");
                                String email = jsonObject.getString("email");

                                friend = new User(email,nickname,id);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ChatRoomActivity.this);
                            dialog.setTitle("您搜索的用户是：");
                            dialog.setMessage("ID："+friend.getUser_ID() + "\n邮箱：" + friend.getEmail());
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("确认添加", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //添加

                                    new Thread(){
                                        public void run(){

                                            handler.post(runnable);
                                        }
                                    }.start();
                                    friend.save();


                                    Toast.makeText(getApplication(), "添加成功",Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //添加
                                    Toast.makeText(getApplication(), "取消添加",Toast.LENGTH_SHORT).show();
                                }
                            });
                            Looper.prepare();
                            dialog.show();
                            Looper.loop();

                        }
                        else{
                            //不存在
                            Looper.prepare();
                            Toast.makeText(getApplication(), "您搜索的用户不存在", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }


                    }

                    @Override
                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                        Log.d("test", e.toString());
                        e.fillInStackTrace();
                    }

                });
                inputFriendEmail.setText(""); // 清空输入框中的内容
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        friendRecyclerView = findViewById(R.id.friend_recycler_view);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        friendRecyclerView.setLayoutManager(layoutManager1);
        friendAdapter = new FriendAdapter(friendList, msgAdapter ,chatroomName);
        friendRecyclerView.setAdapter(friendAdapter);
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            friendList.add(friend);
            friendAdapter.notifyItemInserted(friendList.size() - 1); // 当有新消息时，刷新ListView中的显示
        }
    };

}
