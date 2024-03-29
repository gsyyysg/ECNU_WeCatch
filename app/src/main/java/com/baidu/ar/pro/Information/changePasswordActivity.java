package com.baidu.ar.pro.Information;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
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
import com.baidu.ar.pro.settingActivity;

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

    private String url = "http://47.100.58.47:5000/auth/change";

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


                final JSONObject JSONmessage = new JSONObject();
                try {
                    JSONmessage.put("old_password", oldPasswordInput.getText().toString())
                            .put("new_password", newPasswordInput.getText().toString());
                    //将密码加入
                } catch (Exception e) {
                    e.fillInStackTrace();
                    Log.d("test", e.toString());
                }

                String header[] = new String[2];
                header[0] = "Authorization";
                header[1] = user.getCookie();
                int flag = 0;
                HttpUtil.sendPostRequest(url, JSONmessage.toString(), header, new okhttp3.Callback() {

                    @Override
                    public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("test", responseData);

                        //修改好密码
                        if(responseData.equals("Change succeeded")){
                            user = LitePal.where("owner = ?", "1").find(User.class).get(0);
                            user.setPassword(newPasswordInput.getText().toString());
                            user.save();
                            Looper.prepare();
                            Toast.makeText(getApplication(), "修改成功", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            Intent intent = new Intent(changePasswordActivity.this, settingActivity.class);
                            startActivity(intent);

                        }
                        else{
                            Looper.prepare();
                            Toast.makeText(getApplication(), "密码错误，修改失败", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        oldPasswordInput.setText("");
                        newPasswordInput.setText("");



                    }

                    @Override
                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                        Log.d("test", e.toString());
                        e.fillInStackTrace();
                    }

                });

            }

        });

    }

}
