package com.baidu.ar.pro.Information;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.ar.pro.ChatRoom.ChatRoomActivity;
import com.baidu.ar.pro.R;
import com.baidu.ar.pro.settingActivity;

public class InformationActivity extends Activity {

    private ImageButton friendButton;

    private ImageButton rankingButton;

    private ImageButton settingButton;

    private TextView nickNameText;

    private TextView EmailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_layout);

        friendButton = findViewById(R.id.friend_button);
        rankingButton = findViewById(R.id.ranking_button);
        settingButton = findViewById(R.id.setting_button);
        nickNameText = findViewById(R.id.nick_name_text);
        EmailText = findViewById(R.id.email_text);

        //获取用户信息


        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationActivity.this, ChatRoomActivity.class));
            }
        });
        rankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationActivity.this, ChatRoomActivity.class));
            }
        });
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationActivity.this, settingActivity.class));
            }
        });
    }
}
