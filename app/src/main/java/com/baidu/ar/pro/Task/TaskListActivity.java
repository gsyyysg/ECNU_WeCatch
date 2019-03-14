package com.baidu.ar.pro.Task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.baidu.ar.pro.R;
import com.baidu.ar.pro.Collection.Collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends Activity {
    List<Task> TaskList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist_layout);

        initTask();

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        TaskAdapter mTaskAdapter = new TaskAdapter(TaskList,this);
        mRecyclerView.setAdapter(mTaskAdapter);

        mTaskAdapter.setOnCityClickListener(new TaskAdapter.OnCityClickListener() {

            @Override
            public void onCityClick(Task task) {
                int imageid = task.getTask_image_ID();
                int status = task.getTask_status();
                String name = task.getTask_name();
                String story = task.getTask_background();
                List<Collection> collection = task.getTask_collection();

                Intent intent = new Intent();
                intent.setClass(TaskListActivity.this,SpecificTask.class);
                intent.putExtra("name",name);
                intent.putExtra("story",story);
                intent.putExtra("imageid",imageid);
                intent.putExtra("status",status);
                intent.putExtra("collection",(Serializable) collection);
                startActivity(intent);
            }

            @Override
            public void onLocateClick() {
            }
        });
    }

    //自打表系列
    private void initTask()//后期应该是从数据库中读取文件，然后将每个用户的任务完成情况记录进去，name表示任务名称，后面的
    {
        List<Integer> finished_collection_id = new ArrayList<>();
        Task one = new Task("校徽寻找之旅",1,1,10,R.drawable.task1);
        one.Set_Task_Background("我是华东师范大学最伟大的校徽，我是校徽，我是校徽，我是校徽，重要的事情说三遍！！！！快来领取我吧！");
        one.Set_Task_ad_imageID(R.drawable.task_ad_1);
        TaskList.add(one);

        Task two = new Task("美食总动员",2,2,8,R.drawable.task2);
        two.Set_Task_Background("坐标上海，美食馆名：华东吃饭大学--玉米炒提子，原谅绿鸡腿，只有你想不到，没有华东师大的厨师大大做不到！");
        one.Set_Task_ad_imageID(R.drawable.task_ad_2);
        //比如这个进行中就应该插入collectedid列表
        TaskList.add(two);

        Task three = new Task("华东师大女子图鉴",3,3,5,R.drawable.task3);TaskList.add(three);
        one.Set_Task_ad_imageID(R.drawable.task_ad_3);
        Task four = new Task("喵喵在哪里",4,2,15,R.drawable.task4);
        four.Set_Task_Background("华东猫咪大学有各种各样的瞄：时而高贵冷艳，时而肥胖懒惰，是爱猫的同学们的心头好，快来体验收集十款华东猫咪！");
        one.Set_Task_ad_imageID(R.drawable.task4);

        List<Collection> collectionlist = new ArrayList<>();
        Collection aiji = new Collection(1, R.drawable.collection1);
        aiji.Set_Collection_Name("此猫猫超级超级好看！！！！！");
        aiji.Set_Collection_story("华师大最胖的猫男子没有之一，最喜欢眼巴巴的盯着你的好吃的");
        aiji.Set_Collection_hint("它最喜欢出没于你上学路上解决饥饿的美好地点");
        collectionlist.add(aiji);
        Collection amercancurcat = new Collection(2,R.drawable.collection2); collectionlist.add(amercancurcat);
        Collection bolila = new Collection(3,R.drawable.collection3); collectionlist.add(bolila);
        Collection bosicat = new Collection(4,R.drawable.collection4); collectionlist.add(bosicat);
        Collection britishcat = new Collection(5,R.drawable.collection5); collectionlist.add(britishcat);
        Collection dollcat = new Collection(6,R.drawable.collection6); collectionlist.add(dollcat);
        Collection yiguo = new Collection(7,R.drawable.collection7); collectionlist.add(yiguo);
        Collection norwaycat = new Collection(8,R.drawable.collection8); collectionlist.add(norwaycat);
        Collection sugelancat = new Collection(9,R.drawable.collection9); collectionlist.add(sugelancat);
        Collection xinjiapocat = new Collection(10,R.drawable.collection10); collectionlist.add(xinjiapocat);

        four.Set_Task_Collection(collectionlist);

        // finished_collection_id.add(1);finished_collection_id.add(2);finished_collection_id.add(3);finished_collection_id.add(4);finished_collection_id.add(5);finished_collection_id.add(6);finished_collection_id.add(7);
        // four.Set_Task_CollectedId(finished_collection_id);
        TaskList.add(four);
        Task five = new Task("Hello My Buddy",5,2,3,R.drawable.task5);
        one.Set_Task_ad_imageID(R.drawable.task_ad_5);
        TaskList.add(five) ;
    }


}

