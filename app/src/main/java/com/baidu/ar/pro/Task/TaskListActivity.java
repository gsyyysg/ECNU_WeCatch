package com.baidu.ar.pro.Task;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.ar.pro.Collection.CollectionActivity;
import com.baidu.ar.pro.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskListActivity extends Activity {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    TaskAdapter mTaskAdapter;
    List<Task> taskList = new ArrayList<>();

    int IS_TITLE_OR_NOT =1;
    int MESSAGE = 2;

    List<Map<Integer,String>> mData =new ArrayList<>();
    Map<Integer,String> map = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist_layout);
        Button button1 = (Button) findViewById(R.id.button_experiment);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskListActivity.this, CollectionActivity.class);
                startActivity(intent);
            }
        });

        findView();
        initTASK();
        init();
        mTaskAdapter = new TaskAdapter(this,mData,2);
        mRecyclerView.setAdapter(mTaskAdapter);
    }

    private void initTASK()//后期应该是从数据库中读取文件，然后将每个用户的任务完成情况记录进去，name表示任务名称，后面的
    {
        Task one = new Task("华师大神器--校徽寻找之旅",1);
        taskList.add(one);
        Task two = new Task("华东吃饭大学--美食总动员",2);
        taskList.add(two);
        Task three = new Task("华东师大女子图鉴",3);
        taskList.add(three);
        Task four = new Task("华东猫咪大学--喵喵在哪里",2);
        taskList.add(four);
        Task five = new Task("华东英语大学--Hello My Buddy",2);
        taskList.add(five) ;
    }

    private void findView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new GridLayoutManager(this,2,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(12));
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void init()
    {
        int first_status_num=0,second_status_num=0,pos1,pos2;
        for(Task t: taskList)
        {
            map = new HashMap<Integer, String>();
           map.put(IS_TITLE_OR_NOT,"false");
           map.put(MESSAGE,t.getTask_name());
           mData.add(map);
           switch (t.getTask_status())
           {
               case 1:
                   first_status_num+=1;
                   break;
               case 2:
                   second_status_num+=1;
                   break;
               default: break;
           }
        }
            pos1 = first_status_num+1;
            pos2 = first_status_num+second_status_num+2;

        map=new HashMap<Integer, String>();
        map.put(IS_TITLE_OR_NOT,"true");
        map.put(MESSAGE, "进行中");
        mData.add(0,map);

        map=new HashMap<Integer, String>();
        map.put(IS_TITLE_OR_NOT,"true");
        map.put(MESSAGE,"待领取");
        mData.add(pos1,map);

        map= new HashMap<Integer, String>();
        map.put(IS_TITLE_OR_NOT,"true");
        map.put(MESSAGE,"已完成");
        mData.add(pos2,map);

        for(int i=0;i<mData.size();i++)
        {
            Log.d("Title",mData.get(i).get(IS_TITLE_OR_NOT));
            Log.d("Title_message",mData.get(i).get(MESSAGE));
        }
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration{
        private int space;

        public SpaceItemDecoration(int space){
            this.space=space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
           if(parent.getChildViewHolder(view).getItemViewType()==0){
               outRect.bottom =0;
               outRect.top=space/2;
           }
           else
           {
               outRect.bottom = space;
               outRect.top = space;
           }
           outRect.right = space;
           outRect.left = space;
        }
    }


}
