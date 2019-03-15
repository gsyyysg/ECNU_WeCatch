package com.baidu.ar.pro.Collection;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.ar.pro.R;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {

    private List<Collection> mCollectionList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView colleciton_image;

        public ViewHolder(View view){
            super(view);
            colleciton_image=view.findViewById(R.id.collectioin_image);
        }
    }

    public CollectionAdapter(List<Collection> collecTionList){
        mCollectionList = collecTionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Collection collection = mCollectionList.get(position);
        holder.colleciton_image.setImageResource(collection.getCollection_imageId());
    }

    @Override
    public int getItemCount() {
        return mCollectionList.size();
    }
}
