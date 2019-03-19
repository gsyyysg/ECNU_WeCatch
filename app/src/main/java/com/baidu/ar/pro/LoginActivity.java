package com.baidu.ar.pro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.ar.pro.Map.MapActivity;


public class LoginActivity extends Activity {

    private Button loginButton;

    private Button touristButton;

    private Button registerButton;

    private EditText emailText;

    private EditText passwordText;

    private TextView appName;

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

        appName.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/小单纯体.ttf"));

        touristButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //传递用户信息到MapActivity

                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Email", "Tourist");
                bundle.putInt("money",0);
                intent.putExtras(bundle);
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

                //验证登录信息

                //传递用户信息到MapActivity

                //存储账号密码

                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Email", emailText.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}