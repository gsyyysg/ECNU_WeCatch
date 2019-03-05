package com.baidu.ar.pro.Task;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.baidu.ar.pro.R;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends Activity {

    List<Task> TaskList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist_layout);

        initTask();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mlayoutManager);
        TaskAdapter mTaskAdapter = new TaskAdapter(TaskList);
        mRecyclerView.setAdapter(mTaskAdapter);
    }

    private void initTask()//后期应该是从数据库中读取文件，然后将每个用户的任务完成情况记录进去，name表示任务名称，后面的
    {
        Task one = new Task("校徽寻找之旅",1,1,10, R.drawable.task1);TaskList.add(one);
        Task two = new Task("美食总动员",2,2,8,R.drawable.task2);TaskList.add(two);
        Task three = new Task("华东师大女子图鉴",3,3,5,R.drawable.task3);TaskList.add(three);
        Task four = new Task("喵喵在哪里",4,2,15,R.drawable.task4);TaskList.add(four);
        Task five = new Task("Hello My Buddy",5,2,3,R.drawable.task5);TaskList.add(five) ;
    }
}

