package com.baidu.ar.pro.AR;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ar.pro.Collection.Collection;
import com.baidu.ar.pro.LoginActivity;
import com.baidu.ar.pro.Map.MapActivity;
import com.baidu.ar.pro.R;
import com.baidu.ar.pro.User;

import org.litepal.LitePal;

import java.util.List;

public class CollectSucceedActivity extends Activity {

    private Button backButton;

    private TextView IntroductionText;

    private ImageView collectionImage;

    private TextView collectSuccessText;

    private TextView addMoneyText;

    private Collection collection;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_success_layout);

        backButton = findViewById(R.id.back_button3);
        IntroductionText = findViewById(R.id.introduction_text);
        collectionImage = findViewById(R.id.collection_success_image);
        collectSuccessText = findViewById(R.id.collect_success_text);
        addMoneyText = findViewById(R.id.add_money_text);

        addMoneyText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/小单纯体.ttf"));
        IntroductionText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/小单纯体.ttf"));
        collectSuccessText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/小单纯体.ttf"));

        List<User> tempList = LitePal.where("owner = ?", "1").find(User.class);

        if(tempList.size() == 1)
            user = tempList.get(0);
        else if(tempList.size() == 0){
            //没有owner，重新登录
            Intent intent = new Intent(CollectSucceedActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else{
            //owner人数大于1，数据库数据出问题
        }

        List<Collection> tempList1 = LitePal.where("status = ?", "1").find(Collection.class);
        if(tempList1.size() > 0)
            collection = tempList1.get(0);
        else
            collection = null;

        if(collection != null){

           int tempGold = user.getUser_golds();
           user.setUser_golds(tempGold + collection.getCollection_gold());

           //更新藏品状态为已完成、检查任务状态是否完成、上传服务器

           collection.setStatus(1);

        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CollectSucceedActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
}
