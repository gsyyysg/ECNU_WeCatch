package com.baidu.ar.pro.Task;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ar.pro.AR.ARModel;
import com.baidu.ar.pro.Collection.Collection;
import com.baidu.ar.pro.HttpUtil;
import com.baidu.ar.pro.LoginActivity;
import com.baidu.ar.pro.R;
import com.baidu.ar.pro.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

class pack{
    Integer coll_no;
    String coll_name;
    String coll_hint;
    Double latitude;
    Double longitude;
    Integer reward;
};



public class CustomizeCollection extends Activity {

    static int Map = 1;
    static List<pack> list = new ArrayList<pack>();
    static int hard = 0;
    static int i = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int CHOOSE_PLACE = 1;
    public static final int CHOOSE_AR = 3;
    ARModel ar = new ARModel();
    String path;
    String place_name;
    Integer new_task_Id;
    Bitmap bitmap;
    double longitude = -1;
    double latitude = -1;
    Button place_choose;
    int startnumber = 10000;
    private String url = "http://47.100.58.47:5000/test/add/task";

    ImageView image ;
    ImageView ARImage;
    Button image_text;
    Button next_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.customize_collection);


        Intent intent = getIntent();


        final Integer coll_num = intent.getIntExtra("number", -1);
        final Integer task_id = intent.getIntExtra("ID",-1);



        place_choose = findViewById(R.id.place_choose);
        Button next_step = findViewById(R.id.next_button);
        final TextView coll_name = findViewById(R.id.textView2);
        final TextView coll_notify = findViewById(R.id.textView3);
        final Spinner golds = findViewById(R.id.spinner);
        final Button titile = findViewById(R.id.button3);
        final Button ar_choose = findViewById(R.id.choose_ar_model);
        image = findViewById(R.id.imageView4);
        ARImage = findViewById(R.id.ar_image);
        image_text = findViewById(R.id. button11);
        next_button = findViewById(R.id.next_button);

        if(coll_num == 1)
        {
            next_button.setText("提交");
            titile.setText("我的自定义藏品");
        }
        else titile.setText("我的自定义藏品1");



        image_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(CustomizeCollection.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(CustomizeCollection.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                else{
                    openAlbum();
                }
            }
        });



        place_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomizeCollection.this, Place_choose.class);
                startActivityForResult(intent, CHOOSE_PLACE);
            }
        });


        ar_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomizeCollection.this, ArModelChooseActivity.class);
                startActivityForResult(intent, CHOOSE_AR);
            }
        });



        next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pack single = new pack();
                Collection single_collection = new Collection();
                ARModel single_ar = new ARModel();
                String name = coll_name.getText().toString();
                String hint = coll_notify.getText().toString();
                Integer reward = Integer.parseInt((String)golds.getSelectedItem());


                single_collection.setCollection_name(name);
                single_collection.setCollection_hint(hint);
                single_collection.setCollection_gold(reward);
                single_collection.setCollection_image_name(name+".png");
                saveBitmapToFile("Image", bitmap, name+".png");
                single_collection.setLatitude(latitude);
                single_collection.setLongitude(longitude);
                single_collection.setCollection_ID(startnumber+i);
                hard+=reward;


                if(name.isEmpty()||hint.isEmpty()||path.isEmpty()||longitude == -1 || latitude == -1)
                {
                    Toast.makeText(CustomizeCollection.this, "请完善藏品的基本信息！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(i <= coll_num)
                {
                    //list.add(single);
                    single_collection.save();
                    i = i+1;
                    if(i<=coll_num){
                        coll_name.setHint("Name of your collection");
                        coll_name.setText("");
                        coll_notify.setHint("Hint about your collection");
                        coll_notify.setText("");
                        golds.setSelection(0);
                        place_choose.setText("Put it in the place you want");
                        titile.setText("我的自定义藏品"+i);
                        image_text.setText("请选择一张图片上传\\n(小于2M)\"");
                        image.setImageBitmap(null);}

                        if(i == coll_num)
                        {
                            next_button.setText("提交");
                        }
                }
                if(i > coll_num)
                {
                    if(task_id == -1) {
                        Toast.makeText(getApplication(),"出现错误，请重新设置任务全局信息！", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        //Task now_task = LitePal.where("task_ID is ?", Integer.toString(task_id)).find(Task.class).get(0);
                        Task now_task = new Task(), customize_task = new Task();
                        List<Integer> customize_collection_id = new ArrayList<>();
                        List<String> collection_name = new ArrayList<>();
                        for(int j=1;j<=coll_num;j++)
                        {
                            customize_collection_id.add(j+startnumber);
                            collection_name.add(LitePal.where("collection_ID is ?", Integer.toString(j+startnumber)).find(Collection.class).get(0).getCollection_name());
                            Log.d("Collection","j");
                        }
                        now_task.setTask_collected_ID(customize_collection_id);
                        now_task.updateAll("task_ID = ?",task_id.toString());
                        hard = (hard/coll_num)/10;
                        hard = hard*10;

                        Map<String, Object> map = new HashMap<>();
                        JSONObject JSONTask = null;
                        customize_task = LitePal.where("task_ID is ?", Integer.toString(task_id)).find(Task.class).get(0);

                        try
                        {
                            map.put("task_name", customize_task.getTask_name());
                            map.put("task_difficulty", hard);
                            map.put("task_description", customize_task.getTask_background());
                            map.put("collections",collection_name);

                            for(int j=1; j<=coll_num;j++)
                            {
                                Map<String, Object> map_ = new HashMap<>();
                                Collection customize_collection ;
                                customize_collection = LitePal.where("collection_ID is ?", Integer.toString(j+startnumber)).find(Collection.class).get(0);
                                map_.put("name",customize_collection.getCollection_name());
                                map_.put("reward",customize_collection.getCollection_gold());
                                map_.put("longitude",customize_collection.getLongitude());
                                map_.put("latitude",customize_collection.getLatitude());
                                map_.put("description",customize_collection.getCollection_story());
                                map_.put("hint", customize_collection.getCollection_hint());
                                map_.put("difficulty",customize_collection.getCollection_gold());
                                JSONObject JSONCollection = new JSONObject(map_);
                                map.put(customize_collection.getCollection_name(), map_);
                                //JSONTask.put(customize_collection.getCollection_name(), JSONCollection);
                            }
                            JSONTask = new JSONObject(map);
                        } catch (Exception e){
                            e.fillInStackTrace();
                            Log.d("test", e.toString());
                        }

                        Log.d("json",JSONTask.toString());

                        List<User> tempList = LitePal.where("owner is ?", "1").find(User.class);
                        User user = new User();

                        if(tempList.size()>=1) user = tempList.get(0);
                        else {
                            Toast.makeText(CustomizeCollection.this, "登陆失败，请重新登陆", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CustomizeCollection.this, LoginActivity.class);
                            startActivity(intent);
                        }

                        String table[] = new String[2];
                        table[0] = "Authorization";
                        table[1] = user.getCookie();
                        HttpUtil.sendPostRequest(url, JSONTask.toString(), table, new okhttp3.Callback()
                        {
                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                try{
                                    String responseData = response.body().string();
                                    Log.d("haha", responseData);
                                    new_task_Id = parseJSONWithJSONObject(responseData);
                                    Task Database_task = LitePal.where("task_ID is ?", task_id.toString()).find(Task.class).get(0);
                                    if(new_task_Id == -1)
                                    {
                                        Toast.makeText(CustomizeCollection.this,"任务上传失败，请重新上传", Toast.LENGTH_SHORT).show();
                                        return;

                                    }
                                    Database_task.setTask_ID(new_task_Id);
                                    Database_task.save();
                                    LitePal.deleteAll(Task.class,"task_ID is ?",task_id.toString());
                                    Toast.makeText(CustomizeCollection.this, "设置成功！", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CustomizeCollection.this, TaskListActivity.class);
                                    startActivity(intent);

                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                                //Toast.makeText(getApplication(), "任务上传失败", Toast.LENGTH_LONG).show();
                                Log.d("test", e.toString());
                                e.fillInStackTrace();
                            }

                        });

                        //需要有正在上传的标识
                    }
                }
            }
        });
    }

    private int parseJSONWithJSONObject(String jsonData)
    {
        Integer id = -1;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            id = jsonObject.getInt("task_id");
            Log.d("New Task id is", id.toString());

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return id;
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
            case CHOOSE_PLACE:
                latitude = data.getDoubleExtra("latitude",0);
                longitude = data.getDoubleExtra("longitude",0);
                place_name = data.getStringExtra("placeName");
                place_choose.setText(place_name);
            case CHOOSE_AR:
                ar.setAr_ID(data.getIntExtra("AR_ID",0));
                ar.setAr_image_name(data.getStringExtra("AR_image_name"));
                ar.setAr_name(data.getStringExtra("AR_name"));
                String path = findpath(ar.getAr_image_name(), "Image");

                if(ar.getAr_image_name() == null)
                {
                    Log.d("ImagePath","WRONG!!!");
                }
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                ARImage.setImageBitmap(bitmap);

               //ar json need to ask Mr.Lu
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
            image.setImageBitmap(bitmap);
            image_text.setText("重新上传(小于2M)");
//            Toast.makeText(this,"succeed to load image", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    public String findpath(String imagename, String path)
    {
        File Folder = new File(this.getFilesDir()+"/"+path);
        return Folder.getAbsolutePath()+"/"+imagename;
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




