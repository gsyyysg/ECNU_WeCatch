package com.baidu.ar.pro.Collection;

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

import java.io.File;
import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {

    private List<Collection> mCollectionList;
    private Context mContext;


    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView colleciton_image;

        public ViewHolder(View view){
            super(view);
            colleciton_image=view.findViewById(R.id.collectioin_image);
        }
    }

    public CollectionAdapter(List<Collection> collecTionList, Context context){
        mCollectionList = collecTionList;
        this.mContext = context;
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
        //holder.colleciton_image.setImageResource(collection.getCollection_imageId());
        String path = findpath(collection.getCollection_image_name(), "Image");
        if(collection.getCollection_image_name() == null)
        {
            Log.d("ImagePath","WRONG!!!");
        }
        Log.d("ImagePath",path);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        holder.colleciton_image.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mCollectionList.size();
    }

    public String findpath(String imagename, String path)
    {
        File Folder = new File(this.mContext.getFilesDir()+"/"+path);
        return Folder.getAbsolutePath()+"/"+imagename;
    }
}
