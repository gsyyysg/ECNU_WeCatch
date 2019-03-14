package com.baidu.ar.pro.AR;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.baidu.ar.constants.ARConfigKey;
import com.baidu.ar.pro.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ARActivity extends FragmentActivity {


    private mFragment mARFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        if (findViewById(R.id.bdar_id_fragment_container) != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // 准备调起AR的必要参数
            // AR_KEY:AR内容平台里申请的每个case的key
            // AR_TYPE:AR类型，目前0代表2D跟踪类型，5代表SLAM类型，后续会开放更多类型
            // AR_PATH:本地AR内容的路径
            String arkey = getIntent().getStringExtra(ARConfigKey.AR_KEY);
            int arType = getIntent().getIntExtra(ARConfigKey.AR_TYPE, 0);
            String arPath = getIntent().getStringExtra(ARConfigKey.AR_PATH);
            Bundle data = new Bundle();
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put(ARConfigKey.AR_KEY, arkey);
                jsonObj.put(ARConfigKey.AR_TYPE, arType);
                jsonObj.put(ARConfigKey.AR_PATH, arPath);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            data.putString(ARConfigKey.AR_VALUE, jsonObj.toString());
            data.putInt("collection",getIntent().getExtras().getInt("collection"));
            mARFragment = new mFragment();
            mARFragment.setArguments(data);
            fragmentTransaction.replace(R.id.bdar_id_fragment_container, mARFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        boolean backFlag = false;
        if (mARFragment != null) {
            backFlag = mARFragment.onFragmentBackPressed();
        }
        if (!backFlag) {
            super.onBackPressed();
        }
    }

}