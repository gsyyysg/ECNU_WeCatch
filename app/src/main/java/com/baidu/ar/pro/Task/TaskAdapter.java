package com.baidu.ar.pro.Task;

import android.content.Context;
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

        holder.TaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Task task = mTask.get(position);
                if(onCityClickListener!=null){
                    onCityClickListener.onCityClick(task);
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