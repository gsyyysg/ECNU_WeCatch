package com.baidu.ar.pro.Information;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.ar.pro.ChatRoom.ChatRoomActivity;
import com.baidu.ar.pro.LoginActivity;
import com.baidu.ar.pro.R;
import com.baidu.ar.pro.User;
import com.baidu.ar.pro.settingActivity;
import com.baidu.ar.pro.Ranking.rankingActivity;

import org.litepal.LitePal;

import java.util.List;

public class InformationActivity extends Activity {

    private ConstraintLayout rankingLayout;

    private ConstraintLayout settingLayout;

    private ConstraintLayout friendLayout;

    private ImageButton backButton;

    private TextView nickNameText;

    private TextView EmailText;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_layout);

        friendLayout = findViewById(R.id.friend_Layout);
        rankingLayout = findViewById(R.id.ranking_Layout);
        settingLayout = findViewById(R.id.settingLayout);
        nickNameText = findViewById(R.id.nick_name_text);
        backButton = findViewById(R.id.info_back_button);
        EmailText = findViewById(R.id.email_text);

        //获取用户信息
        List<User> tempList = LitePal.where("owner = ?", "1").find(User.class);

        if(tempList.size() == 1)
            user = tempList.get(0);
        else if(tempList.size() == 0){
            //没有owner，重新登录
            Intent intent = new Intent(InformationActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else{
            //owner人数大于1，数据库数据出问题
        }

        nickNameText.setText(user.getNickname());
        EmailText.setText(user.getEmail());

        friendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationActivity.this, ChatRoomActivity.class));
            }
        });
        rankingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationActivity.this, rankingActivity.class));
            }
        });
        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationActivity.this, settingActivity.class));
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
