package com.baidu.ar.pro.Task;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.ar.pro.R;
import com.baidu.ar.pro.Collection.Collection;

import java.util.List;

public class SpecificTaskAdapter extends RecyclerView.Adapter<SpecificTaskAdapter.ViewHolder> {

    private List<Collection> mCollectionList;

    private SpecificTaskAdapter.OnSTaskClickListener onSTaskClickListener;

    public interface OnSTaskClickListener{
        void onCityClick(Collection aCollection);

        void onLocateClick();
    }

    public void setOnCityClickListener(SpecificTaskAdapter.OnSTaskClickListener listener)
    {
        this.onSTaskClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView collection_image;
        View CollectionView;

        public ViewHolder(View view){
            super(view);
            CollectionView = view;
            collection_image = view.findViewById(R.id.sepecific_task_collection_image);
        }
    }

    public SpecificTaskAdapter(List<Collection> collectionList){
       mCollectionList = collectionList;
    }

    @Override
    public SpecificTaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sepecific_task_item,parent,false);
        final SpecificTaskAdapter.ViewHolder holder = new SpecificTaskAdapter.ViewHolder(view);

        holder.CollectionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Collection aCollection = mCollectionList.get(position);
                if (onSTaskClickListener != null) {
                    onSTaskClickListener.onCityClick(aCollection);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(SpecificTaskAdapter.ViewHolder holder, int position) {
        com.baidu.ar.pro.Collection.Collection collection = mCollectionList.get(position);
        holder.collection_image.setImageResource(collection.getCollection_imageId());
    }

    @Override
    public int getItemCount() {
        return mCollectionList.size();
    }


}
