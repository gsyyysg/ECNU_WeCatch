package com.baidu.ar.pro.Task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.ar.pro.R;
import com.baidu.ar.pro.Collection.Collection;

import java.io.File;
import java.util.List;

public class SpecificTaskAdapter extends RecyclerView.Adapter<SpecificTaskAdapter.ViewHolder> {

    private List<Collection> mCollectionList;
    private Context mcontext;

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

    public SpecificTaskAdapter(List<Collection> collectionList, Context context)
    {
       mCollectionList = collectionList;
       this.mcontext = context;
    }

    @Override
    public SpecificTaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sepecific_task_item,parent,false);
        final SpecificTaskAdapter.ViewHolder holder = new SpecificTaskAdapter.ViewHolder(view);

        holder.collection_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer position = holder.getAdapterPosition();
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
        //holder.collection_image.setImageResource(collection.getCollection_imageId());
        String path = findpath(collection.getCollection_image_name(), "Image");
        if(collection.getCollection_image_name() == null)
        {
            Log.d("ImagePath","WRONG!!!");
        }
        Log.d("ImagePath","special"+path);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        holder.collection_image.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return mCollectionList.size();
    }

    public String findpath(String imagename, String path)
    {
        File Folder = new File(this.mcontext.getFilesDir()+"/"+path);
        return Folder.getAbsolutePath()+"/"+imagename;
    }


}
