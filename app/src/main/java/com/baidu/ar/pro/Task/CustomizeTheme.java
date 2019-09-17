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

import java.util.ArrayList;
import java.util.List;

public class CustomizeTheme extends Activity {

    public static final int CHOOSE_PHOTO = 2;
    ImageView task_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customize_layout);

        ImageButton back_step = findViewById(R.id.imageButton);
        final EditText name = findViewById(R.id.textView2);
        final EditText introduction = findViewById(R.id.textView3);
        task_image = findViewById(R.id.imageView2);
        Button image_choose = findViewById(R.id.button7);
        final Spinner collection_choose = findViewById(R.id.spinner);
        Button next_step = findViewById(R.id.next_button);


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
                if(task_name.isEmpty()||task_introduction.isEmpty())
                {
                    Toast.makeText(CustomizeTheme.this, "请补全任务信息！", Toast.LENGTH_SHORT).show();
                    Log.d("successful_question","implement all mess");
                    Log.d("collection_num",collection_num);
                    return;
                }

                Intent intent = new Intent(CustomizeTheme.this, CustomizeCollection.class);

                intent.putExtra("number",number);
                intent.putExtra("name",task_name);
                intent.putExtra("info",task_introduction);
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
        switch(requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
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
                if(requestCode == RESULT_OK)
                {
                    if(Build.VERSION.SDK_INT >= 19)
                    {
                        handleImageOnKitKat(data);
                    }
                    else handleImageBeforeKitKat(data);
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private  void handleImageOnKitKat(Intent data)
    {
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this, uri))
        //如果是document类型的uri，则通过document id来处理
        {
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.document".equals(uri.getAuthority()))
            {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }
            else if("com.android.providers.downloads.document".equals(uri.getAuthority()))
            {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }}
        else if("content".equalsIgnoreCase(uri.getScheme()))
        {
            imagePath = getImagePath(uri, null);
        }
        else if("file".equalsIgnoreCase(uri.getScheme()))
        {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data)
    {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection)
    {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null,null);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath)
    {
        if(imagePath != null)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            task_image.setImageBitmap(bitmap);
        }
        else
        {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

}

