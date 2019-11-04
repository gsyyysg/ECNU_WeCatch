package com.baidu.ar.pro.Information;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.ar.pro.R;

public class aboutUsActivity extends Activity {

    private ImageButton backButton;

    private TextView aboutUsText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_layout);

        backButton = findViewById(R.id.about_back_button);
        aboutUsText = findViewById(R.id.about_us_Text);
        aboutUsText.setText("组长：高思宇\n成员：曹雯琳、陆劲竹、王晗阳");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
