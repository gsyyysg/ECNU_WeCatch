package com.baidu.ar.pro.ChatRoom;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.ar.pro.R;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomActivity extends Activity {

    private List<Msg> msgList = new ArrayList<Msg>();

    private List<Friend> friendList = new ArrayList<Friend>();

    private TextView chatroomName;

    private EditText inputText;

    private EditText inputFriendId;

    private Button sendButton;

    private Button addFriendButton;

    private RecyclerView msgRecyclerView;

    private MsgAdapter msgAdapter;

    private RecyclerView friendRecyclerView;

    private FriendAdapter friendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_layout);
        initMsgs(); // 初始化消息数据
        initFriends();// 初始化好友数据
        inputText = findViewById(R.id.input_text);
        inputFriendId = findViewById(R.id.input_friend_id);
        sendButton = findViewById(R.id.send_Button);
        addFriendButton = findViewById(R.id.add_friend_button);
        chatroomName = findViewById(R.id.chatroom_name);

        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(msgAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    msgAdapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    inputText.setText(""); // 清空输入框中的内容
                }
            }
        });
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputFriendId.getText().toString();
                Friend friendAdd = new Friend(content);
                friendList.add(friendAdd);
                inputFriendId.setText("");
            }
        });

        friendRecyclerView = findViewById(R.id.friend_recycler_view);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        friendRecyclerView.setLayoutManager(layoutManager1);
        friendAdapter = new FriendAdapter(friendList, msgAdapter ,chatroomName);
        friendRecyclerView.setAdapter(friendAdapter);
    }

    private void initMsgs() {
        Msg msg1 = new Msg("欢迎来到聊天室", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("这里现在还什么功能都没有", Msg.TYPE_RECEIVED);
        msgList.add(msg2);
        Msg msg3 = new Msg("但是你可以自言自语", Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }

    private void initFriends() {
        Friend friend1 = new Friend("friend1");
        friendList.add(friend1);
        Friend friend2 = new Friend("friend2");
        friendList.add(friend2);
        Friend friend3 = new Friend("friend3");
        friendList.add(friend3);
        Friend friend4 = new Friend("friend4");
        friendList.add(friend4);
        Friend friend5 = new Friend("friend5");
        friendList.add(friend5);
    }

}
