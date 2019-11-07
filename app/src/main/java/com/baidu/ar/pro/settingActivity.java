package com.baidu.ar.pro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.baidu.ar.pro.Information.aboutUsActivity;
import com.baidu.ar.pro.Information.changePasswordActivity;
import com.baidu.ar.pro.Information.feedbackActivity;
import com.baidu.ar.pro.Information.helpActivity;
import com.baidu.ar.pro.Information.changeNicknameActivity;

import org.litepal.LitePal;

import java.util.List;

public class settingActivity extends Activity {

    private Button logOutButton;

    private ConstraintLayout changePasswordLayout;

    private ConstraintLayout helpLayout;

    private ConstraintLayout feedBackLayout;

    private ConstraintLayout aboutLayout;

    private ConstraintLayout changenicknameLayout;

    private ImageButton backButton;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        logOutButton = findViewById(R.id.log_out_button);
        changePasswordLayout = findViewById(R.id.change_password_Layout);
        helpLayout = findViewById(R.id.help_Layout);
        feedBackLayout = findViewById(R.id.feedback_Layout);
        aboutLayout = findViewById(R.id.about_Layout);
        backButton = findViewById(R.id.set_back_button);
        changenicknameLayout = findViewById(R.id.change_nickname_Layout);

        List<User> tempList = LitePal.where("owner = ?", "1").find(User.class);

        if(tempList.size() == 1)
            user = tempList.get(0);
        else if(tempList.size() == 0){
            //没有owner，重新登录
            Intent intent = new Intent(settingActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else{
            //owner人数大于1，数据库数据出问题
        }

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingActivity.this, changePasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        changenicknameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(settingActivity.this, changeNicknameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        helpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingActivity.this, helpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        feedBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingActivity.this, feedbackActivity.class);
                startActivity(intent);
                finish();
            }
        });
        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingActivity.this, aboutUsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onCreate(null);
    }
}
