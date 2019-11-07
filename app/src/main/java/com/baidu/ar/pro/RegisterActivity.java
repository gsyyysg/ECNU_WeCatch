package com.baidu.ar.pro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class RegisterActivity extends Activity {

    private EditText emailText;

    private EditText passwordText;

    private Button submitButton;

    private String url = "http://47.100.58.47:5000/auth/register";

    private String[] validEmail = new String[]{"@qq.com", "@126.com", "@163.com","@hotmail.bom","@stu.ecnu.edu.cn","@msn.com","@gamil.com","@yahoo.com"
            ,"@mail.com","@googlemail.com", "@sina.com", "@sogou.com"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        submitButton = findViewById(R.id.submit_button);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);

        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证注册信息是否合法


                String password, email;
                password = passwordText.getText().toString();
                email = emailText.getText().toString();

                if(password.length() < 8){
                    Toast.makeText(getApplication(), "密码长度至少为8位，注册失败", Toast.LENGTH_SHORT).show();
                    return;
                }

                int flag = 0;
                for(String test : validEmail){
                    if(email.endsWith(test)) {
                        flag = 1;
                        break;
                    }
                }
                if(flag == 0) {
                    Toast.makeText(getApplication(), "无效的邮箱地址，注册失败", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject JSONmessage = new JSONObject();
                try {
                    JSONmessage.put("password", password)
                            .put("email", email);
                    Log.d("test", JSONmessage.toString());
                }catch (Exception e){
                    e.fillInStackTrace();
                    Log.d("test", e.toString());
                }

                HttpUtil.sendPostRequest(url, JSONmessage.toString(), new okhttp3.Callback(){

                    @Override
                    public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("test", responseData);

                        Looper.prepare();
                        Toast.makeText(getApplication(), "注册成功", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                        //Toast.makeText(getApplication(), "注册失败", Toast.LENGTH_SHORT).show();
                        Log.d("test", e.toString());
                        e.fillInStackTrace();
                    }

                });
                //注册成功提示

            }
        });
    }
}