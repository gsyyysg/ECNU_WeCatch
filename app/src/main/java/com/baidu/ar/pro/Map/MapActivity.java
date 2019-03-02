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
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;

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


    public static final String ASSETS_CASE_FOLDER = "ardebug";
    public static final String DEFAULT_PATH =
            Environment.getExternalStorageDirectory().toString() + "/" + ASSETS_CASE_FOLDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //个性化地图
        setMapCustomFile(this, "custom_map_config.json");



        setContentView(R.layout.map_layout);

        //地图
        //获取地图控件引用
        mMapView = findViewById(R.id.bmapView);
        //定位初始化
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        //自定义地图
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,
                true,
                BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background),
                0xffffffff,
                0xffffffff));
        //默认缩放
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(20.0f);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        //关闭缩放工具
        mMapView.showZoomControls(true);

        //监听位置
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerNotify(myListener);
        //调用BDNotifyListener的setNotifyLocation方法，实现设置位置消息提醒。
        //设置位置提醒，四个参数分别是：纬度、精度、半径、坐标类型
        myListener.SetNotifyLocation(0f, 0f, 3000, mLocationClient.getLocOption().getCoorType());

        initLocationOption();

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
        initLocationOption();

        initData();

        cameraButton = findViewById(R.id.camera_button);
        menuButton = findViewById(R.id.menu_button);
        collectionButton = findViewById(R.id.collection_button);
        collectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, CollectionActivity.class);
                startActivity(intent);
            }
        });
        missionButton = findViewById(R.id.mission_button);
        missionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, TaskListActivity.class);
                startActivity(intent);
            }
        });
        informationButton = findViewById(R.id.information_button);
        chatRoomButton = findViewById(R.id.chatroom_button);
        chatRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, ChatRoomActivity.class);
                startActivity(intent);
            }
        });
        cameraButton = findViewById(R.id.camera_button);
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
        menuLayout = findViewById(R.id.menu_layout);

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
            //将自定义样式文件写入本地
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
        //设置自定义样式文件
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

    public static class CopyFileTask extends AsyncTask {
        private final Intent intent;
        private final WeakReference<Context> contextRef;

        public CopyFileTask(Intent intent, Context context) {
            this.intent = intent;
            this.contextRef = new WeakReference<>(context);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Context context = contextRef.get();
            if (context != null) {
                AssetsCopyToSdcard assetsCopyTOSDcard = new AssetsCopyToSdcard(context);
                assetsCopyTOSDcard.assetToSD(ASSETS_CASE_FOLDER, DEFAULT_PATH);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (contextRef.get() != null) {
                Toast.makeText(contextRef.get(), "拷贝完成", Toast.LENGTH_SHORT).show();
                contextRef.get().startActivity(intent);
            }
        }
    }

    private void initLocationOption() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
        mLocationClient.registerLocationListener(myLocationListener);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("gcj02");
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1000);
        //可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
        //可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
        //可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
        //可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(locationOption);
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
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位成功
                Log.i("baidu_location_result", "offline location success");
                double lat = location.getLatitude();
                double lon = location.getLongitude();

            } else if (location.getLocType() == BDLocation.TypeOffLineLocationFail) {
                // 离线定位失败
                Log.i("baidu_location_result", "offline location fail");
            } else {

                Log.i("baidu_location_result", "location type = " + location.getLocType());
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
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
