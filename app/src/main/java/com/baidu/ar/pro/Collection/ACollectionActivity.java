package com.baidu.ar.pro.Collection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ar.pro.LoginActivity;
import com.baidu.ar.pro.Map.MapActivity;
import com.baidu.ar.pro.R;
import com.baidu.ar.pro.User;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;

public class ACollectionActivity extends Activity {

    private ImageView collectionImage;

    private TextView collectionStory;

    private Button collectionName;

    private Button trackButton;

    private User user;

    private Collection thisCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_collection_layout);

        Intent intent = getIntent();

        List<Collection> tempList1 = LitePal.where("collection_ID = ?", Integer.toString(intent.getIntExtra("collection_ID", 1))).find(Collection.class);
        if(tempList1.size() > 0) {
            thisCollection = tempList1.get(0);
            Integer id = thisCollection.getCollection_ID();
            Log.d("collection_choose", id.toString());
        }

        else
            thisCollection = null;

        collectionImage = findViewById(R.id.a_collection_image);
        collectionStory = findViewById(R.id.a_collection_story);
        collectionName = findViewById(R.id.button_collection);
        trackButton = findViewById(R.id.track_Button);

        collectionImage.setImageResource(thisCollection.getCollection_imageId());
        collectionName.setText(thisCollection.getCollection_name());
        collectionStory.setText(thisCollection.getCollection_hint());

        List<User> tempList = LitePal.where("owner = ?", "1").find(User.class);

        Log.d("test", Integer.toString(tempList.size()));
        if(tempList.size() >= 1)
            user = tempList.get(0);
        else if(tempList.size() == 0){
            //没有owner，重新登录
            intent = new Intent(ACollectionActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else{
            //owner人数大于1，数据库数据出问题
        }

        String path = findpath(thisCollection.getCollection_image_name(), "Image");
        if(thisCollection.getCollection_image_name() == null)
        {
            Log.d("ImagePath","WRONG!!!");
        }
        Log.d("ImagePath",path);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        collectionImage.setImageBitmap(bitmap);

        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "正在寻找" + collectionName.getText(), Toast.LENGTH_SHORT).show();
                Log.d("test", "Userid"+user.getUser_ID()+"tracking"+thisCollection.getCollection_ID());
                user.setTrackingCollectionID(thisCollection.getCollection_ID());
                user.save();
                Intent intent = new Intent(ACollectionActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

    }

    public String findpath(String imagename, String path)
    {
        File Folder = new File(getFilesDir()+"/"+path);
        return Folder.getAbsolutePath()+"/"+imagename;
    }
}
