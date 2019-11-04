package com.baidu.ar.pro.Information;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.ar.pro.HttpUtil;
import com.baidu.ar.pro.LoginActivity;
import com.baidu.ar.pro.R;
import com.baidu.ar.pro.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

public class changePasswordActivity extends Activity {

    private User user;

    private EditText oldPasswordInput;

    private EditText newPasswordInput;

    private Button confirmButton;

    private ImageButton backButton;

    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_layout);

        oldPasswordInput = findViewById(R.id.old_Password_Input);
        newPasswordInput = findViewById(R.id.new_Password_Input);
        confirmButton = findViewById(R.id.confirm_Change_Button);
        backButton = findViewById(R.id.pw_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获取用户信息
        List<User> tempList = LitePal.where("owner = ?", "1").find(User.class);

        if(tempList.size() == 1)
            user = tempList.get(0);
        else if(tempList.size() == 0){
            //没有owner，重新登录
            Intent intent = new Intent(changePasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else{
            //owner人数大于1，数据库数据出问题
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(oldPasswordInput.getText().toString().equals(user.getPassword_hash())){
                    Log.d("debug", user.getPassword_hash());

                    final JSONObject JSONmessage = new JSONObject();
                    try {
                        //将密码加入
                    } catch (Exception e) {
                        e.fillInStackTrace();
                        Log.d("test", e.toString());
                    }

                    HttpUtil.sendPostRequest(url, JSONmessage.toString(), new okhttp3.Callback(){

                        @Override
                        public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                            Log.d("test", JSONmessage.toString());
                            String responseData = response.body().string();

                            //修改好密码


                        }

                        @Override
                        public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                            Log.d("test", e.toString());
                            e.fillInStackTrace();
                        }

                    });

                    User temp = new User();
                    temp.setPassword_hash(newPasswordInput.getText().toString());
                    temp.updateAll("user_id = ?", Integer.toString(user.getUser_ID()));
                    user = LitePal.where("owner = ?", "1").find(User.class).get(0);
                    Log.d("debug", user.getPassword_hash());
                    oldPasswordInput.setText("");
                    newPasswordInput.setText("");
                    Toast.makeText(getApplication(), "修改成功", Toast.LENGTH_SHORT).show();
                }
                else{
                    oldPasswordInput.setText("");
                    newPasswordInput.setText("");
                    Toast.makeText(getApplication(), "密码错误", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
