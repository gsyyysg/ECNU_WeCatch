package com.baidu.ar.pro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.baidu.ar.pro.ChatRoom.ChatRoomActivity;

public class settingActivity extends Activity {

    private Button logOutButton;

    private ImageButton changePasswordButton;

    private ImageButton helpButton;

    private ImageButton feedBackButton;

    private ImageButton aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);


        logOutButton = findViewById(R.id.log_out_button);
        changePasswordButton = findViewById(R.id.change_password_button);
        helpButton = findViewById(R.id.help_button);
        feedBackButton = findViewById(R.id.feedback_button);
        aboutButton = findViewById(R.id.about_button);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingActivity.this, ChatRoomActivity.class);
                startActivity(intent);
            }
        });
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingActivity.this, ChatRoomActivity.class);
                startActivity(intent);
            }
        });
        feedBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingActivity.this, ChatRoomActivity.class);
                startActivity(intent);
            }
        });
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingActivity.this, ChatRoomActivity.class);
                startActivity(intent);
            }
        });
    }
}
