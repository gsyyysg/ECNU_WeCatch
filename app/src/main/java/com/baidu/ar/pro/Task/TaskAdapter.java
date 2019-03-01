package com.baidu.ar.pro.Task;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ar.pro.R;

import java.util.List;
import java.util.Map;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
    private List<Task> mTask;
    private Context mContext;
    private LayoutInflater mInfalter;
    private static final int VIEW_TYPE_TITLE =0;
    private static final int VIEW_TYPE_ITEM  =1;
    int IS_TITLE_OR_NOT =1;
    int MESSAGE =2;
    int ColumnNum;
    List<Map<Integer,String>> mData;  //????

    public TaskAdapter(Context context , List<Map<Integer,String>> mData, int ColumnNum)
    {
        this.mContext=context;
        this.ColumnNum=ColumnNum;
        this.mData= mData;
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh=null;
        mInfalter =LayoutInflater.from(mContext);
        switch(viewType){
            case VIEW_TYPE_TITLE:
                vh = new HolderOne(mInfalter.inflate(R.layout.title,parent,false));
                break;
            case VIEW_TYPE_ITEM:
                vh = new HolderTwo(mInfalter.inflate(R.layout.item,parent,false));
                break;
        }
          return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if("true".equals(mData.get(position).get(IS_TITLE_OR_NOT)))
        {
            holder.mTitle.setText(mData.get(position).get(MESSAGE));
        }
        else
        {
            holder.mItem.setText(mData.get(position).get(MESSAGE));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if("true".equals(mData.get(position).get(IS_TITLE_OR_NOT)))
            return VIEW_TYPE_TITLE;
        return VIEW_TYPE_ITEM;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
            @Override
            public int getSpanSize(int position){
                if("flase".equals(mData.get(position).get(IS_TITLE_OR_NOT))){
                    return 1;
                }
                else return ColumnNum;
            }
        });
     }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if(payloads.isEmpty())
            onBindViewHolder(holder,position);
        else onBindViewHolder(holder,position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
       public TextView mTitle;
       public TextView mItem;
       public ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class HolderOne extends ViewHolder{
         public HolderOne(View viewHolder){
             super(viewHolder);
             mTitle= (TextView) viewHolder.findViewById(R.id.title);
                 }
    }

    public class HolderTwo extends  ViewHolder{

        public HolderTwo(final View viewHolder)
        {
            super(viewHolder);
            mItem = viewHolder.findViewById(R.id.item);
            mImageView = viewHolder.findViewById(R.id.image);
            //第二项应该还有项目的布局
        }
    }
}