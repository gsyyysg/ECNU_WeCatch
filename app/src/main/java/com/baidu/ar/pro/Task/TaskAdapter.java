package com.baidu.ar.pro.Task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ar.pro.R;

import java.io.File;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

    private List<Task> mTask;
    private Context mContext;
    private OnCityClickListener onCityClickListener;

    public interface OnCityClickListener{
        void onCityClick(Task task);

        void onLocateClick();
    }

    public void setOnCityClickListener(OnCityClickListener listener)
    {
        this.onCityClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mUsed;
        TextView mTitle;
        Button mButton;
        View TaskView;

        public ViewHolder(View view)
        {
            super(view);
            TaskView = view;
            mImageView = view.findViewById(R.id.image);
            mUsed = view.findViewById(R.id.item_used);
            mTitle = view.findViewById(R.id.task_item);
            mButton = view.findViewById(R.id.task_button);
        }
    }

    public TaskAdapter(List<Task> taskList,Context mContext){
        mTask = taskList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent ,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position_About_Button = holder.getAdapterPosition();
                Task Task_About_Button = mTask.get(position_About_Button);
                if(Task_About_Button.getTask_status()==3)
                {
                    //在添加as数据库后应该判断该用户正在进行中的任务个数是否小于等于3，否则是不可以添加该任务
                    Toast.makeText(view.getContext(),"已添加"+Task_About_Button.getTask_name(),Toast.LENGTH_SHORT).show();
//                    Task_About_Button.setTask_status(2);//这里也应该有向服务器发送数据的代码添加
//                    Task_About_Button.update(Task_About_Button.getTask_ID());
//                    Task_About_Button.updateAll("")
                    Task update_task = new Task();
                    update_task.setTask_status(2);
                    update_task.updateAll("task_ID = ?",Integer.toString(Task_About_Button.getTask_ID()));
                    holder.mButton.setText("进行中");
                }
            }
        });

        holder.TaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Task task = mTask.get(position);
                if(onCityClickListener!=null){
                    onCityClickListener.onCityClick(task);
                    //Intent intent = new Intent();
                    //intent.setClass(mContext, SpecificTaskActivity.class);

                }



               /* int imageid = task.getTask_image_ID();
                int status = task.getTask_status();
                String name = task.getTask_name();
                String story = task.getTask_background();
                List<Collection> collection =task.getTask_collection();

                Intent intent = new Intent();
                intent.setClass(mContext,Specific_Task.class);

                Bundle bundle = new Bundle();

                TaskParcelable tp = new TaskParcelable();
                tp.Set_S_task_name(name);
                tp.Set_S_task_imageid(imageid);
                tp.Set_S_task_status(status);
                tp.Set_S_task_background(story);
                tp.Set_S_task_collection(collection);

                bundle.putParcelable(parcelableKey, tp);
                intent.putExtras(bundle);



                //((TaskListActivity)mContext).startActivityForResult(intent,1);

               */
            }
        });
        return holder;
    }




    @Override
    public int getItemCount() {
        return mTask.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String status,head = "已有",tail = "人领取",mid;
        Task task = mTask.get(position);

        holder.mTitle.setText(task.getTask_name());
        mid = task.getTask_used_person()+ "";
        holder.mUsed.setText(head + mid + tail);
//        holder.mImageView.setImageResource(task.getTask_image_ID());

        String path = findpath(task.getTask_imagePath(), "Image");
        //Log.d("ImagePath",task.getTask_imagePath());
        //Log.d("imagePath",task.getTask_imagePath());
        if(task.getTask_imagePath() == null)
        {
            Log.d("ImagePath","WRONG!!!");
        }
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        holder.mImageView.setImageBitmap(bitmap);
        switch (task.getTask_status())
        {
            case 1:
                status="已完成";
                holder.mButton.setText(status);
                break;
            case 2:
                status="进行中";
                holder.mButton.setText(status);
                break;
            case 3:
                status="未领取";
                holder.mButton.setText(status);
                break;
        }
    }



    /*@Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if(payloads.isEmpty())
            onBindViewHolder(holder,position);
        else onBindViewHolder(holder,position);
    }*/

    public String findpath(String imagename, String path)
    {
        File Folder = new File(this.mContext.getFilesDir()+"/"+path);
        return Folder.getAbsolutePath()+"/"+imagename;
    }



}