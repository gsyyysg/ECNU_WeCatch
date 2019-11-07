package com.baidu.ar.pro;

import android.app.Application;

import com.baidu.ar.bean.DuMixARConfig;
import com.baidu.ar.util.Res;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import org.litepal.LitePal;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        SDKInitializer.setHttpsEnable(true);

        Res.addResource(this);
        // 设置App Id
        DuMixARConfig.setAppId("17720947");
        // 设置API Key
        DuMixARConfig.setAPIKey("Wq3INP1VkhKGePQViB0T9c6N");
        // 设置Secret Key
        DuMixARConfig.setSecretKey("eH9BWcxMDn54F2x6bmg3sVLEA7K0Tqac");

        LitePal.initialize(this);
    }
}