package com.baidu.ar.pro.Collection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ar.pro.Map.MapActivity;
import com.baidu.ar.pro.R;

public class CollectSucceedActivity extends Activity {

    private Button backButton;

    private TextView IntroductionText;

    private ImageView collectionImage;

    private int collectionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_success_layout);

        backButton = findViewById(R.id.back_button);
        IntroductionText = findViewById(R.id.introduction_text);
        collectionImage = findViewById(R.id.collection_success_image);

        collectionID = getIntent().getExtras().getInt("collection");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CollectSucceedActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
}
