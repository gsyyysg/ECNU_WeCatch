package com.baidu.ar.pro.Task;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ar.pro.R;
import com.baidu.ar.pro.Collection.a_collection;
import com.baidu.ar.pro.Collection.Collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpecificTask extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String status;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sepecific_task_layout);

        String s_task_name,s_task_background;
        int s_task_imageID, s_task_status;

        Intent intent = getIntent();
        List<Collection> s_task_collection = (ArrayList<Collection>) intent.getSerializableExtra("collection");
        s_task_name = intent.getStringExtra("name");
        s_task_background = intent.getStringExtra("story");
        s_task_imageID = intent.getIntExtra("imageid",0);
        s_task_status = intent.getIntExtra("status",0);


        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 4);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(25));
        SpecificTaskAdapter mCollectionAdapter = new  SpecificTaskAdapter(s_task_collection);
        mRecyclerView.setAdapter(mCollectionAdapter);

        mCollectionAdapter.setOnCityClickListener(new SpecificTaskAdapter.OnSTaskClickListener() {
            @Override
            public void onCityClick(Collection acollection) {
                Collection thisCollection = acollection;
                Intent intent = new Intent();
                intent.setClass(SpecificTask.this, a_collection.class);
                intent.putExtra("acollection",(Serializable)thisCollection);
                startActivity(intent);
            }

            @Override
            public void onLocateClick() {

            }
        });


        ImageView task_image = findViewById(R.id.sepecific_task_image);
        TextView task_name = findViewById(R.id.sepecific_task_name);
        Button task_status = findViewById(R.id.sepecific_task_get);
        TextView task_story = findViewById(R.id.sepecific_task_background);


        task_image.setImageResource(s_task_imageID);
        task_name.setText(s_task_name);
        task_story.setText(s_task_background);
        switch (s_task_status)
        {
            case 1:
                status="已完成";
                task_status.setText(status);
                break;
            case 2:
                status="进行中";
                task_status.setText(status);
                break;
            case 3:
                status="未领取";
                task_status.setText(status);
                break;
        }
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.bottom = space;
            if (parent.getChildLayoutPosition(view) % 4 == 0) {
                outRect.left = 0;
            }
        }
    }
}
