package com.baidu.ar.pro.Task;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ar.pro.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

    private List<Task> mTask;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mUsed;
        TextView mTitle;
        Button mButton;

        public ViewHolder(View view)
        {
            super(view);
            mImageView = view.findViewById(R.id.image);
            mUsed = view.findViewById(R.id.item_used);
            mTitle = view.findViewById(R.id.task_item);
            mButton = view.findViewById(R.id.task_button);
        }
    }

    public TaskAdapter(List<Task> taskList){
        mTask = taskList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent ,false);
        ViewHolder holder = new ViewHolder(view);
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
        holder.mUsed.setText(head+mid+tail);
        holder.mImageView.setImageResource(task.getTask_image_ID());
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



}