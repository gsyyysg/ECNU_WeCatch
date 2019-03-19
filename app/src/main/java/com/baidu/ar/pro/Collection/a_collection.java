package com.baidu.ar.pro.Collection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ar.pro.R;

public class a_collection extends Activity {

    private ImageView collectionImage;

    private TextView collectionStory;

    private Button collectionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_collection_layout);

        Intent intent = getIntent();
        Collection thisCollection = (Collection)intent.getSerializableExtra("acollection");
        collectionImage = findViewById(R.id.a_collection_image);
        collectionStory = findViewById(R.id.a_collection_story);
        collectionName = findViewById(R.id.button_collection);
        collectionImage.setImageResource(thisCollection.getCollection_imageId());
        collectionName.setText(thisCollection.getCollection_name());
        collectionStory.setText(thisCollection.getCollection_hint());
    }
}
