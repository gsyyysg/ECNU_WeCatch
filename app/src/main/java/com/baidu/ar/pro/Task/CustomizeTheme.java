package com.baidu.ar.pro.Task;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ar.pro.LoginActivity;
import com.baidu.ar.pro.R;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomizeTheme extends Activity {

    public static final int CHOOSE_PHOTO = 2;
    ImageView task_image;
    Button image_choose;
    String path = null;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customize_layout);

        ImageButton back_step = findViewById(R.id.imageButton);
        final EditText name = findViewById(R.id.textView2);
        final EditText introduction = findViewById(R.id.textView3);
        task_image = findViewById(R.id.imageView2);
        final Spinner collection_choose = findViewById(R.id.spinner);
        Button next_step = findViewById(R.id.next_button);
        image_choose = findViewById(R.id.button7);

        //you will need it when you upload to web server
        //LitePal.deleteAll(Task.class,"task_ID is ?","1000000");

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 Editable editable = introduction.getText();
                 int t = editable.length()/10;
                 if(t > 0)
                 {
                     for(int m = 1; m < t; m++)
                     {
                         editable.insert(10*m,"\n");
                         Log.d("test1","I have done");
                     }
                 }
                 if(editable.length() > t*10) editable.insert(t*10,"\n");
                 introduction.setText(editable);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };



        back_step.setOnClickListener(new View.OnClickListener() {  //back键使其跳回
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomizeTheme.this, CustomizeCollection.class);
                startActivity(intent);
            }
        });

         image_choose.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(ContextCompat.checkSelfPermission(CustomizeTheme.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                 != PackageManager.PERMISSION_GRANTED)
                 {
                     ActivityCompat.requestPermissions(CustomizeTheme.this, new String[]{
                             Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                 }
                 else{
                     openAlbum();
                 }
             }
         });


        next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task_name = name.getText().toString();
                String task_introduction = introduction.getText().toString();
                String collection_num = (String) collection_choose.getSelectedItem();
                int number = Integer.parseInt(collection_num);

                //后续应该加入判断有无选择图片的检测
                if(task_name.isEmpty()||task_introduction.isEmpty() || path.isEmpty())
                {
                    Toast.makeText(CustomizeTheme.this, "请补全任务信息！", Toast.LENGTH_SHORT).show();
                    Log.d("successful_question","implement all mess");
                    Log.d("collection_num",collection_num);
                    return;
                }

                Task CustomizeTask = new Task();
                CustomizeTask.setTask_name(task_name);
                CustomizeTask.setTask_imagePath(task_name+".png");
                CustomizeTask.setTask_background(task_introduction);
                CustomizeTask.setTask_status(3);
                CustomizeTask.setTask_used_person(0);
                CustomizeTask.setTask_ID(1000000);

                CustomizeTask.save();

                saveBitmapToFile("Image", bitmap, task_name+".png");


//                CustomizeTask.setTask_image_ID(BitmapFactory.decodeFile(path));
//                Drawable drawable = getBaseContext().getResources().getDrawable()

                Intent intent = new Intent(CustomizeTheme.this, CustomizeCollection.class);

                intent.putExtra("number",number);
                intent.putExtra("ID", CustomizeTask.getTask_ID());

                startActivity(intent);
            }
        });

    }



       private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
       }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this,"Open the  Album", Toast.LENGTH_SHORT).show();
                    openAlbum();
                }
                else
                {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode)
        {
            case CHOOSE_PHOTO:
                handleImageBeforeKitKat(data);
                break;
            default:
                break;
        }
    }


    private void handleImageBeforeKitKat(Intent data)
    {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection)
    {
        Cursor cursor = getContentResolver().query(uri, null, selection, null,null);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        Log.d("imagePath","lalallalallala");
        Log.d("imagePath",path);
        return path;
    }

    private void displayImage(String imagePath)
    {
        if(imagePath != null)
        {
            bitmap = BitmapFactory.decodeFile(imagePath);
            task_image.setImageBitmap(bitmap);
            image_choose.setText("重新上传(小于2M)");
//            Toast.makeText(this,"succeed to load image", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBitmapToFile(String path, Bitmap bm, String picName)
    {
        File Folder = new File(getFilesDir()+"/"+path);
        File f = new File(Folder.getAbsolutePath()+"/"+picName);
        FileOutputStream out = null;

        try{
            if(!Folder.exists())
            {
                Folder.mkdir();
                Log.d("ImageSave","Create directory succeed!");
            }
            if(!f.exists())
            {
                f.createNewFile();
                Log.d("ImageSave","Create File succeed!");
            }
            out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            Log.d("ImageSave","Has Succeed!");
            Log.d("ImageSave",Folder.getAbsolutePath());
        }
           catch(Exception e){
            Log.d("ImageSave",e.toString());
            e.printStackTrace();
           }
         finally {
            try{
                out.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}

