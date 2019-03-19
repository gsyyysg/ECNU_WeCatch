package com.baidu.ar.pro.Collection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
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

    private TextView collectSuccessText;

    private TextView addMoneyText;

    private int collectionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_success_layout);

        backButton = findViewById(R.id.back_button);
        IntroductionText = findViewById(R.id.introduction_text);
        collectionImage = findViewById(R.id.collection_success_image);
        collectSuccessText = findViewById(R.id.collect_success_text);
        addMoneyText = findViewById(R.id.add_money_text);

        addMoneyText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/小单纯体.ttf"));
        IntroductionText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/小单纯体.ttf"));
        collectSuccessText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/小单纯体.ttf"));

        collectionID = getIntent().getExtras().getInt("collection");


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CollectSucceedActivity.this, MapActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("money", 100);
//                intent.putExtras(bundle);
                intent.putExtra("money",100);
                startActivity(intent);
            }
        });
    }
}
