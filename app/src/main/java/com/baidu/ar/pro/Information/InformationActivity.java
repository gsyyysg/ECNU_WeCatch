package com.baidu.ar.pro.Information;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.ar.pro.ChatRoom.ChatRoomActivity;
import com.baidu.ar.pro.R;
import com.baidu.ar.pro.settingActivity;

public class InformationActivity extends Activity {

    private ConstraintLayout rankingLayout;

    private ConstraintLayout settingLayout;

    private ConstraintLayout friendLayout;

    private TextView nickNameText;

    private TextView EmailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_layout);

        friendLayout = findViewById(R.id.friend_Layout);
        rankingLayout = findViewById(R.id.ranking_Layout);
        settingLayout = findViewById(R.id.settingLayout);
        nickNameText = findViewById(R.id.nick_name_text);
        EmailText = findViewById(R.id.email_text);

        //获取用户信息


        friendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationActivity.this, ChatRoomActivity.class));
            }
        });
        rankingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationActivity.this, ChatRoomActivity.class));
            }
        });
        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationActivity.this, settingActivity.class));
            }
        });
    }
}
