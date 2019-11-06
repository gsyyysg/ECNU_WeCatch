package com.baidu.ar.pro.ChatRoom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

    private EditText inputFriendId;

    private Button sendButton;

    private Button addFriendButton;

    private ImageButton backButton;

    private RecyclerView msgRecyclerView;

    private MsgAdapter msgAdapter;

    private RecyclerView friendRecyclerView;

    private FriendAdapter friendAdapter;

    private List<User> friendList;

    private User user;

    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_layout);
        inputText = findViewById(R.id.input_text);
        inputFriendId = findViewById(R.id.input_friend_id);
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

        friendList = LitePal.where("owner = ?", "0").find(User.class).subList(0, 1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(msgAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Message msg = new Message(content, user.getUser_ID(), friendAdapter.getFriend_id());
                    msgList = msgAdapter.mMsgList;
                    msgList.add(msg);
                    msg.save();
                    msgAdapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    inputText.setText(""); // 清空输入框中的内容


                    final JSONObject JSONmessage = new JSONObject();
                    try {
                        //将message的信息加入

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

                            //处理，并加入数据库

                        }

                        @Override
                        public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                            Log.d("test", e.toString());
                            e.fillInStackTrace();
                        }

                    });
                }
            }
        });
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                String content = inputText.getText().toString();

                final JSONObject JSONmessage = new JSONObject();
                try {
                    //将所查找的朋友id加入
                } catch (Exception e) {
                    e.fillInStackTrace();
                    Log.d("test", e.toString());
                }

                HttpUtil.sendPostRequest(url, JSONmessage.toString(), new okhttp3.Callback(){

                    @Override
                    public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                        Log.d("test", JSONmessage.toString());
                        String responseData = response.body().string();

                        if(true){
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ChatRoomActivity.this);
                            dialog.setTitle("您搜索的用户是：");
                            dialog.setMessage(content);
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("确认添加", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //添加
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
                        }
                        else{
                            //不存在
                            Toast.makeText(getApplication(), "您搜索的用户不存在", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                        Log.d("test", e.toString());
                        e.fillInStackTrace();
                    }

                });
                */
                User friend = LitePal.where("user_ID = ?", inputFriendId.getText().toString()).find(User.class).get(0);
                friendList.add(friend);
                friendAdapter.notifyItemInserted(friendList.size() - 1); // 当有新消息时，刷新ListView中的显示
                inputFriendId.setText(""); // 清空输入框中的内容
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

}
