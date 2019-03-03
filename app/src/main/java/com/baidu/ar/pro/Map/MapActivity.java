package com.baidu.ar.pro.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.ar.bean.DuMixARConfig;
import com.baidu.ar.pro.AR.ARActivity;
import com.baidu.ar.pro.AR.utils.AssetsCopyToSdcard;
import com.baidu.ar.pro.ChatRoom.ChatRoomActivity;
import com.baidu.ar.pro.Collection.CollectionActivity;
import com.baidu.ar.pro.R;
import com.baidu.ar.pro.Task.TaskListActivity;
import com.baidu.ar.util.Res;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends Activity {


    private MapView mMapView;

    private BaiduMap mBaiduMap;

    private LocationClient mLocationClient;

    public BDNotifyListener myListener = new MyNotifyListener();

    private ImageButton cameraButton;

    private ImageButton menuButton;

    private ImageButton collectionButton;

    private ImageButton missionButton;

    private ImageButton informationButton;

    private ImageButton chatRoomButton;

    private RelativeLayout menuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setMapCustomFile(this, "custom_map_config.json");

        setContentView(R.layout.map_layout);

        mMapView = findViewById(R.id.bmapView);
        menuLayout = findViewById(R.id.menu_layout);
        cameraButton = findViewById(R.id.camera_button);
        menuButton = findViewById(R.id.menu_button);
        missionButton = findViewById(R.id.mission_button);
        informationButton = findViewById(R.id.information_button);
        collectionButton = findViewById(R.id.collection_button);

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

        /**
         * 地图部分
         */
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
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
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerNotify(myListener);
        //设置位置提醒，四个参数分别是：纬度、精度、半径、坐标类型
        myListener.SetNotifyLocation(31.2328910211, 121.4129429701, 3000, mLocationClient.getLocOption().getCoorType());
        initLocationOption();

        initData();

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
        chatRoomButton = findViewById(R.id.chatroom_button);
        chatRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, ChatRoomActivity.class);
                startActivity(intent);
            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, ARActivity.class);
                Bundle bundle = new Bundle();
                MapActivity.ListItemBean listItemBean = new MapActivity.ListItemBean(5, "10299285", null);
                bundle.putString("ar_key", listItemBean.getARKey());
                bundle.putInt("ar_type", listItemBean.getARType());
                bundle.putString("ar_path", listItemBean.getARPath());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menuLayout.getVisibility() == View.GONE) {
                    menuLayout.setVisibility(View.VISIBLE);
                    cameraButton.setVisibility(View.GONE);
                }
                else if(menuLayout.getVisibility() == View.VISIBLE) {
                    menuLayout.setVisibility(View.GONE);
                    cameraButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    protected void onResume() {
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

    private void initData() {
        Resources res = getResources();
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
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //精度
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationOption.setCoorType("bd09ll");
        //默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1000);
        //设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(true);
        //默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
        //默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
        //默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
        //默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        mLocationClient.setLocOption(locationOption);
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

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(locData);

        }
    }

    public class MyNotifyListener extends BDNotifyListener {
        public void onNotify(BDLocation mlocation, float distance){
            //已到达设置监听位置附近
            AlertDialog alertDialog1 = new AlertDialog.Builder(getApplicationContext())
                    .setTitle("到达位置")//标题
                    .create();
            alertDialog1.show();
        }
    }
}
