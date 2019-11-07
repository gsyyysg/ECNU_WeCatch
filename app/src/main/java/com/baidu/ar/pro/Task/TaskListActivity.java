package com.baidu.ar.pro.Task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.ar.pro.LoginActivity;
import com.baidu.ar.pro.R;
import com.baidu.ar.pro.Collection.Collection;
import com.baidu.ar.pro.User;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends Activity {

    private List<Task> TaskList = new ArrayList<>();

    private Button customize_button;

    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

    private RecyclerView mRecyclerView;

    private TaskAdapter mTaskAdapter;

    private EditText searchTaskInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist_layout);

        initTask();

        mRecyclerView = findViewById(R.id.recyclerView);
        searchTaskInput = findViewById(R.id.search_task_input);

        mTaskAdapter = new TaskAdapter(TaskList,this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mTaskAdapter);

        final int requestcode = 0;

        customize_button = findViewById(R.id.customize);
        customize_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> tempList = LitePal.where("owner is ?", "1").find(User.class);
                User user = new User();

                if(tempList.size()>=1) user = tempList.get(0);
                else {
                    Toast.makeText(TaskListActivity.this, "登陆失败，请重新登陆", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TaskListActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                Log.d("usercookie", user.getCookie());
                if(user.getCookie().equals("visitor"))  //only non visitors can make their own task
                {
                    Toast.makeText(TaskListActivity.this, "游客登陆无法自定义主题", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent i = new Intent(TaskListActivity.this, CustomizeTheme.class);
                    Log.d("Task","yes");
                    startActivity(i);
                }
            }
        });



        mTaskAdapter.setOnCityClickListener(new TaskAdapter.OnCityClickListener() {

            @Override
            public void onCityClick(Task task) {
                int imageid = task.getTask_image_ID();
                int status = task.getTask_status();
                String name = task.getTask_name();
                String story = task.getTask_background();
                List<Collection> collection = task.getTask_collection();

                Intent intent = new Intent();
                intent.setClass(TaskListActivity.this, SpecificTaskActivity.class);
//                intent.putExtra("name",name);
//                intent.putExtra("story",story);
//                intent.putExtra("imageid",imageid);
//                intent.putExtra("status",status);
//                intent.putExtra("collection",(Serializable) collection);
                intent.putExtra("task_id",task.getTask_ID());
                startActivity(intent);
//                startActivityForResult(intent, requestcode);
            }

            @Override
            public void onLocateClick() {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //onCreate(null);
    }

    //自打表系列
    private void initTask()//后期应该是从数据库中读取文件，然后将每个用户的任务完成情况记录进去，name表示任务名称，后面的
    {
        List<Integer> finished_collection_id = new ArrayList<>();
        List<Integer> certain_collection_id = new ArrayList<>();

        TaskList = LitePal.findAll(Task.class);
        Log.d("Tasksize",Integer.toString(TaskList.size()));

        //LitePal.deleteAll(Task.class, "task_ID is ?", "1000000");

//        for(Task task: TaskList){
//            Log.d("status",(task.getTask_name()));
//            certain_collection_id = task.getTask_collected_ID();
//            if(certain_collection_id != null) {
//                List<Collection> certain_collection = new ArrayList<>();
//                for(Integer id:certain_collection_id)
//                    certain_collection.add(LitePal.where("collection_ID is ?", Integer.toString(id)).find(Collection.class).get(0));
//                task.setTask_collection(certain_collection);
//                task.save();
            //}
        //}

    }


}

