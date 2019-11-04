package com.baidu.ar.pro.Task;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ar.pro.Collection.ACollectionActivity;
import com.baidu.ar.pro.R;
import com.baidu.ar.pro.Collection.Collection;

import org.litepal.LitePal;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpecificTaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String status;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sepecific_task_layout);

        final String s_task_name,s_task_background;
        final Integer  s_task_status, s_task_id;
        final String s_task_imagepath;
        List<Integer> certain_collection_id = new ArrayList<>();
        List<Collection> s_task_collection = new ArrayList<>();
        List<Collection> collection_temp1 = new ArrayList<>();
        List<Task> tempList1 = new ArrayList<>();

        Intent intent = getIntent();
        s_task_id = intent.getIntExtra("task_id", 0);
        Log.d("taskID",s_task_id.toString());
        Task now_task = new Task();
        tempList1 = LitePal.where("task_ID is ?", Integer.toString(s_task_id)).find(Task.class);
        if(tempList1.size() > 0) {
            now_task = tempList1.get(0);
        }
        else
        {
            Log.d("error","cannot find task in terms of taskID");
        }

        if(now_task == null)  Log.d("Specific Task","error!");

        s_task_name = now_task.getTask_name();
        s_task_background = now_task.getTask_background();
        s_task_imagepath = now_task.getTask_imagePath();
        s_task_status = now_task.getTask_status();
        certain_collection_id = now_task.getTask_collected_ID();


        for(Integer id:certain_collection_id) {
            Log.d("collection_id",id.toString());
            collection_temp1 = LitePal.where("collection_ID is ?", Integer.toString(id)).find(Collection.class);
            if(collection_temp1.size() > 0)
            {
                s_task_collection.add(collection_temp1.get(0));
            }
            else
            {
                Log.d("error","cannot find collections in terms of collectionID");
            }
        }
         now_task.setTask_collection(s_task_collection);

        //can change more simple

//        List<Collection> s_task_collection = (ArrayList<Collection>) intent.getSerializableExtra("collection");
//        s_task_id = intent.getIntExtra("task_id",0);
//        s_task_name = intent.getStringExtra("name");
//        s_task_background = intent.getStringExtra("story");
//        s_task_imageID = intent.getIntExtra("imageid",0);
//        s_task_status = intent.getIntExtra("status",0);


        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(25));
        SpecificTaskAdapter mCollectionAdapter = new  SpecificTaskAdapter(s_task_collection,this);
        mRecyclerView.setAdapter(mCollectionAdapter);

        mCollectionAdapter.setOnCityClickListener(new SpecificTaskAdapter.OnSTaskClickListener() {
            @Override
            public void onCityClick(Collection acollection) {
                Collection thisCollection = acollection;
                Intent intent = new Intent();
                intent.setClass(SpecificTaskActivity.this, ACollectionActivity.class);
                intent.putExtra("collection_ID",thisCollection.getCollection_ID());
                startActivity(intent);
            }

            @Override
            public void onLocateClick() {

            }
        });


        ImageView task_image = findViewById(R.id.sepecific_task_image);
        TextView task_name = findViewById(R.id.sepecific_task_name);
        final Button task_status = findViewById(R.id.sepecific_task_get);
        Button task_delete = findViewById(R.id.sepecific_task_delete);
        TextView task_story = findViewById(R.id.sepecific_task_background);
        final Button get_task = findViewById(R.id.sepecific_task_get);

        task_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(s_task_status == 2)
                {
                    Toast.makeText(view.getContext(), "已将"+s_task_name+"从您的任务包中删除", Toast.LENGTH_SHORT).show();
                    task_status.setText("未领取");
                    Task update_task = new Task();
                    update_task.setTask_status(3);
                    update_task.updateAll("task_ID = ?", Integer.toString(s_task_id));
                }
            }
        });

        task_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(s_task_status == 3)
                {
                    Toast.makeText(view.getContext(),"已添加"+s_task_name,Toast.LENGTH_SHORT).show();
                    Task update_task = new Task();
                    update_task.setTask_status(2);
                    update_task.updateAll("task_ID = ?", Integer.toString(s_task_id));
                    task_status.setText("进行中");
                }
            }
        });

        get_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(s_task_status == 3)
                {
                    Toast.makeText(view.getContext(),"已添加"+s_task_name,Toast.LENGTH_SHORT).show();
                    Task update_task = new Task();
                    update_task.setTask_status(2);
                    update_task.updateAll("task_ID = ?", Integer.toString(s_task_id));
                    get_task.setText("进行中");
                }
            }
        });


        //task_image.setImageResource(s_task_imageID);
        String path = findpath(s_task_imagepath, "Image");
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        task_image.setImageBitmap(bitmap);
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

    public String findpath(String imagename, String path)
    {
        File Folder = new File(this.getFilesDir()+"/"+path);
        return Folder.getAbsolutePath()+"/"+imagename;
    }
}
