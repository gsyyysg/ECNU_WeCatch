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
import android.widget.ImageView;

import com.baidu.ar.pro.AR.ARModel;
import com.baidu.ar.pro.R;

import java.io.File;
import java.util.List;

public class ARChooseAdapter extends RecyclerView.Adapter<ARChooseAdapter.ViewHolder> {

private List<ARModel> mARList;
private Context mContext;
public static final int CHOOSE_AR = 3;


static class ViewHolder extends RecyclerView.ViewHolder{
    ImageView ar_image;

    public ViewHolder(View view){
        super(view);
        ar_image=view.findViewById(R.id.ar_image);
    }
}

    public ARChooseAdapter(List<ARModel> arList, Context context){
        mARList = arList;
        this.mContext = context;
    }

    @Override
    public ARChooseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ar_item,parent,false);
        ARChooseAdapter.ViewHolder holder = new ARChooseAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ARChooseAdapter.ViewHolder holder, int position) {
        final ARModel ar = mARList.get(position);
        String path = findpath(ar.getAR_image_name(), "Image");
        if(ar.getAR_image_name() == null)
        {
            Log.d("ImagePath","WRONG!!!");
        }
        Log.d("ImagePath",path);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        holder.ar_image.setImageBitmap(bitmap);

        holder.ar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("AR_ID",ar.getAR_ID());
                intent.putExtra("AR_image_name",ar.getAR_image_name());
                intent.putExtra("AR_name",ar.getAR_Key());
                ((ARModelChooseActivity)mContext).setResult(CHOOSE_AR, intent);
                ((ARModelChooseActivity)mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mARList.size();
    }

    public String findpath(String imagename, String path)
    {
        File Folder = new File(this.mContext.getFilesDir()+"/"+path);
        return Folder.getAbsolutePath()+"/"+imagename;
    }
}
