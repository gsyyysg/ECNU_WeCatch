package com.baidu.ar.pro.Information;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.ar.pro.R;

public class feedbackActivity extends Activity {

    private ImageButton backButton;

    private TextView mainText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);

        backButton = findViewById(R.id.fd_back_button);
        mainText = findViewById(R.id.feedback_Maintext);

        mainText.setText("邮箱：10175102258@stu.ecnu.edu.cn\n");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
