package com.baidu.ar.pro.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ar.pro.AR.ARActivity;
import com.baidu.ar.pro.ChatRoom.ChatRoomActivity;
import com.baidu.ar.pro.Collection.Collection;
import com.baidu.ar.pro.Collection.CollectionActivity;
import com.baidu.ar.pro.Information.InformationActivity;
import com.baidu.ar.pro.LoginActivity;
import com.baidu.ar.pro.R;
import com.baidu.ar.pro.Task.Task;
import com.baidu.ar.pro.Task.TaskListActivity;
import com.baidu.ar.pro.User;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends Activity {


    private MapView mMapView;

    private BaiduMap mBaiduMap;

    private LocationClient mLocationClient;

    private UiSettings mUiSettings;

    private ImageButton cameraButton;

    private ImageButton menuButton;

    private ImageButton collectionButton;

    private ImageButton missionButton;

    private ImageButton informationButton;

    private ImageButton chatRoomButton;

    private ImageView bokehImage;

    private TextView collectionText;

    private TextView missionText;

    private TextView informationText;

    private TextView chatRoomText;

    private TextView locationInformation;

    private TextView moneyText;

    private RelativeLayout menuLayout;

    private ConstraintLayout moneyLayout;

    private User user;

    private Collection trackingCollection;

    private double myLongtitude;

    private double myLatitude;

    private double targetLongtitude;

    private double targetLatitude;

    private double distance = 0;

    private static final double EARTH_RADIUS = 6378.137;

    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 纬度
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        // 经度
        double lng1 = Math.toRadians(longitude1);
        double lng2 = Math.toRadians(longitude2);
        // 纬度之差
        double a = lat1 - lat2;
        // 经度之差
        double b = lng1 - lng2;
        // 计算两点距离的公式
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // 弧长乘地球半径, 返回单位: 千米
        s =  s * EARTH_RADIUS;
        return s;
    }

    private void initUI(){

        mMapView = findViewById(R.id.bmapView);
        menuLayout = findViewById(R.id.menu_layout);
        cameraButton = findViewById(R.id.camera_button);
        menuButton = findViewById(R.id.menu_button);
        missionButton = findViewById(R.id.mission_button);
        informationButton = findViewById(R.id.information_button);
        collectionButton = findViewById(R.id.collection_button);
        chatRoomButton = findViewById(R.id.chatroom_button);
        collectionText = findViewById(R.id.collection_text);
        missionText = findViewById(R.id.mission_text);
        informationText = findViewById(R.id.information_text);
        chatRoomText = findViewById(R.id.chatroom_text);
        bokehImage = findViewById(R.id.bokeh_image);
        moneyLayout = findViewById(R.id.money_layout);
        moneyText = findViewById(R.id.money_text);
        locationInformation = findViewById(R.id.location_information);

        bokehImage.setVisibility(View.GONE);

        //改变字体
        collectionText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/小单纯体.ttf"));
        missionText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/小单纯体.ttf"));
        informationText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/小单纯体.ttf"));
        chatRoomText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/小单纯体.ttf"));
        moneyText.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/小单纯体.ttf"));
    }

    private void initData() {

        List<User> tempList = LitePal.where("owner = ?", "1").find(User.class);

        Log.d("test", "Usercnt" + Integer.toString(tempList.size()));
        if(tempList.size() >= 1)
            user = tempList.get(0);
        else if(tempList.size() == 0){
            //没有owner，重新登录
            Intent intent = new Intent(MapActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else{
            //owner人数大于1，数据库数据出问题
        }


        Log.d("test", "Userid"+user.getUser_ID()+"tracking"+user.getTrackingCollectionID());
        if(user.getTrackingCollectionID() != 0) {
            List<Collection> tempList1 = LitePal.where("collection_ID = ?", Integer.toString(user.getTrackingCollectionID())).find(Collection.class);
            if (tempList1.size() > 0)
                trackingCollection = tempList1.get(0);
            else
                trackingCollection = null;

            targetLatitude = trackingCollection.getLatitude();
            targetLongtitude = trackingCollection.getLongitude();

            locationInformation.setText("我的经度：" + myLongtitude + "\n" +
                    "我的纬度：" + myLatitude + "\n" +
                    "目标经度：" + targetLongtitude + "\n" +
                    "目标纬度：" + targetLatitude + "\n" +
                    "距离" + distance + "\n" +
                    "追踪目标：" + trackingCollection.getCollection_name());
        }

        moneyText.setText(Integer.toString(user.getUser_golds()));



    }

    private void initPermission(){

        //申请权限
        List<String> permissionList=new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.
                permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.
                permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.
                permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.
                permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!permissionList.isEmpty()){
            String[] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MapActivity.this,permissions,1);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //setMapCustomFile(this, "custom_map_config_pokemongo.json");

        setContentView(R.layout.map_layout);

        initUI();
        initData();
        initPermission();

        List<Task> tttt = LitePal.findAll(Task.class);
        Log.d("test", Integer.toString(tttt.size()));

        /**
         * 地图部分
         */
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getApplicationContext());
        initLocationOption();
        MapView.setMapCustomEnable(true);
        mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setOverlookingGesturesEnabled(true);
        //自定义定位
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,
                true,
                null));
        //默认缩放
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(20.0f);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        //关闭缩放工具
        mMapView.showZoomControls(false);
        //监听位置

        Resources res = getResources();

        collectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, CollectionActivity.class);
                startActivity(intent);
            }
        });
        missionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, TaskListActivity.class);
                startActivity(intent);
            }
        });
        chatRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, ChatRoomActivity.class);
                startActivity(intent);
            }
        });
        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, InformationActivity.class);
                startActivity(intent);
            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, ARActivity.class);

                targetLatitude = myLatitude;
                targetLongtitude = myLongtitude;

                //判断是否到达地点以及到达了哪个藏品的地点
                if(distance != 0 && distance < 30) {
                    Bundle bundle = new Bundle();
                    MapActivity.ListItemBean listItemBean = new MapActivity.ListItemBean(5, "10299568", null);
                    bundle.putString("ar_key", listItemBean.getARKey());
                    bundle.putInt("ar_type", listItemBean.getARType());
                    bundle.putString("ar_path", listItemBean.getARPath());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtras(bundle);
                }
                //将藏品信息传递给ARActivity

                startActivity(intent);
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menuLayout.getVisibility() == View.GONE) {
                    moneyLayout.setVisibility(View.GONE);
                    cameraButton.setVisibility(View.GONE);
                    menuLayout.setVisibility(View.VISIBLE);
                    bokehImage.setVisibility(View.VISIBLE);
                    menuButton.setBackground(getResources().getDrawable(R.drawable.chevronup));
                }
                else if(menuLayout.getVisibility() == View.VISIBLE) {
                    moneyLayout.setVisibility(View.VISIBLE);
                    cameraButton.setVisibility(View.VISIBLE);
                    menuLayout.setVisibility(View.GONE);
                    bokehImage.setVisibility(View.GONE);
                    menuButton.setBackground(getResources().getDrawable(R.drawable.chevrondown));
                }
            }
        });
    }
    @Override
    protected void onResume() {
        Log.d("test", "123341354");
        initData();
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    private void setMapCustomFile(Context context, String fileName) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        String moduleName = null;
        try {
            inputStream = context.getAssets().open("customConfigDir/" + fileName);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            moduleName = context.getFilesDir().getAbsolutePath();
            File file = new File(moduleName + "/" + fileName);
            if (file.exists()) file.delete();
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MapView.setCustomMapStylePath(moduleName + "/" + fileName);
    }


    private class ListItemBean {
        String mARKey;
        int mARType;
        String mARPath;

        ListItemBean(int arType, String arKey, String arPath) {
            this.mARType = arType;
            this.mARKey = arKey;
            this.mARPath = arPath;
        }

        public String getARKey() {
            return mARKey;
        }

        public int getARType() {
            return mARType;
        }

        public String getARPath() {
            return mARPath;
        }
    }

    private void initLocationOption() {
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1001);
        //可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(true);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
        //可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(0,0, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(locationOption);
        mLocationClient.registerLocationListener(myLocationListener);
        //开始定位
        mLocationClient.start();
    }
    /**
     * 实现定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location){

            if (location == null || mMapView == null){
                return;
            }
/*
            if (location.getFloor() != null) {
                // 当前支持高精度室内定位
                String buildingID = location.getBuildingID();// 百度内部建筑物ID
                String buildingName = location.getBuildingName();// 百度内部建筑物缩写
                String floor = location.getFloor();// 室内定位的楼层信息，如 f1,f2,b1,b2
                mLocationClient.startIndoorMode();// 开启室内定位模式（重复调用也没问题），开启后，定位SDK会融合各种定位信息（GPS,WI-FI，蓝牙，传感器等）连续平滑的输出定位结果；
                myLongtitude = location.getLongitude();
                myLatitude = location.getLatitude();
                locationInformation.setText("我的经度：" + myLongtitude +"\n我的纬度："+ myLatitude+"\n目标经度："+targetLongtitude+"\n目标纬度："+targetLatitude);
            }
            */
            double temp;
            myLongtitude = location.getLongitude();
            myLatitude = location.getLatitude();
            if(distance == 0){
                distance = getDistance(myLongtitude, myLatitude, targetLongtitude, targetLatitude);
            }
            else{
                temp = getDistance(myLongtitude, myLatitude, targetLongtitude, targetLatitude);
                if(temp < distance){
                    Toast.makeText(getApplication(), "靠近中", Toast.LENGTH_SHORT);
                }
                else{
                    Toast.makeText(getApplication(), "远离中", Toast.LENGTH_SHORT);
                }
                distance = temp;
            }
            locationInformation.setText("我的经度：" + myLongtitude + "\n" +
                    "我的纬度：" + myLatitude + "\n" +
                    "目标经度：" + targetLongtitude + "\n" +
                    "目标纬度：" + targetLatitude + "\n" +
                    "距离" + distance + "\n" +
                    "追踪目标：" + trackingCollection.getCollection_name());

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();

            mBaiduMap.setMyLocationData(locData);


        }
    }


}
