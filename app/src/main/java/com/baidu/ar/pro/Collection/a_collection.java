package com.baidu.ar.pro.Collection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ar.pro.R;

public class a_collection extends Activity {

    private ImageView collectionImage;

    private TextView collectionStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_collection_layout);

        Intent intent = getIntent();
        Collection thisCollection = (Collection)intent.getSerializableExtra("acollection");
        collectionImage = findViewById(R.id.a_collection_image);
        collectionStory = findViewById(R.id.a_collection_story);
        String background,hint,all,transger ="\n\n";
        collectionImage.setImageResource(thisCollection.getCollection_imageId());
        background = thisCollection.getCollection_story();
        hint = thisCollection.getCollection_hint();
        all = background + transger + hint;
        collectionStory.setText(all);
    }
}
