package com.baidu.ar.pro.Task;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ar.pro.R;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.ArrayList;
import java.util.List;

public class Place_choose extends Activity implements View.OnClickListener {

    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private PoiCitySearchOption poiCitySearchOption = null;     // 城市内检索
//    private PoiBoundSearchOption poiBoundSearchOption = null;   // 范围内检索

    private EditText start_adress;
    private View view;
    private ListView poi_lv;
    private List<PoiSearchResults> list = new ArrayList<PoiSearchResults>();
    private MyAdapter_StartAddr adapter = null;

    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Y or N","Y");

        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.place_choose);


        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

        mSuggestionSearch = SuggestionSearch.newInstance();

        start_adress = findViewById(R.id.start_et);
        poi_lv = findViewById(R.id.addre_poi);
        Button cancel_button = findViewById(R.id.cancel);
        cancel_button.setOnClickListener(this);


        city_back = findViewById(R.id.city_back);
        city_back.setOnClickListener(this);


        start_adress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length()<0)
                {
                    return;
                }

                Log.d("test", start_adress.getText().toString());

                //I do not know it's function

                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(charSequence.toString()).city("上海"));
//                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(charSequence.toString()).keyword("上海市华东师范大学中山北路校区"));


                //poiCitySearchOption = new PoiCitySearchOption().city("上海").keyword("华东师范大学中山北路校区");

                poiCitySearchOption = new PoiCitySearchOption().city("上海").keyword(start_adress.getText().toString());
                mPoiSearch.searchInCity(poiCitySearchOption);
//                poiBoundSearchOption = new PoiBoundSearchOption().bound("上海市华东师范大学中山北路校区").keyword(start_adress.getText().toString());
//                mPoiSearch.searchInBound(poiBoundSearchOption);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (start_adress.getText().toString().equals("")) {
                    list.clear();
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_back:
                finish();
                break;
            case R.id.cancel:// 确认按钮
                if (!start_adress.getText().toString().equals("")
                        && start_adress.getText().toString() != null) {
                    start_adress.setText("");
                    list.clear();
                    adapter.notifyDataSetChanged();
                }
                break;

            default:
                break;
        }

    }

    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){
        private String poiname;
        private String poiadd;


        public void onGetPoiResult(PoiResult result) {

            Log.d("test", "???");
            // 获取POI检索结果
            if (result == null
                    || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                Toast.makeText(Place_choose.this, "未找到结果", Toast.LENGTH_LONG)
                        .show();
                return;
            }

            list.clear();

            Log.d("test", "???2");
            if (result.getAllPoi() == null) {
                Toast.makeText(Place_choose.this, "未找到结果,请重新输入111",
                        Toast.LENGTH_LONG).show();
                return;
            } else {
                Log.d("test", "???3");
                for (int i = 0; i < result.getAllPoi().size(); i++) {
                    poiname = result.getAllPoi().get(i).name;
                    poiadd = result.getAllPoi().get(i).address;
                    LatLng poilocation = result.getAllPoi().get(i).location;
                    Log.d("test1",String.valueOf(poilocation));
                    latitude= poilocation.latitude;
                    longitude = poilocation.longitude;

                    if (poilocation != null) {
                        Toast.makeText(Place_choose.this, "找到结果,不用重新输入",
                                Toast.LENGTH_LONG).show();



                        // 实例化一个地理编码查询对象
                        GeoCoder geoCoder = GeoCoder.newInstance();
                        // 设置反地理编码位置坐标
                        ReverseGeoCodeOption op = new ReverseGeoCodeOption();
                        op.location(poilocation);
                        // 发起反地理编码请求(经纬度->地址信息)
                        geoCoder.reverseGeoCode(op);

                        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {


                            @Override
                            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                                poiadd = reverseGeoCodeResult.getAddress();
                            }

                            @Override
                            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                            }


                        });
                        PoiSearchResults results = new PoiSearchResults(
                                poiname, poiadd, latitude, longitude);

                        list.add(results);



                    }

                    else {
                        Toast.makeText(Place_choose.this, "未找到结果,请重新输入",
                                Toast.LENGTH_LONG).show();
                    }
                }


            }
            adapter = new MyAdapter_StartAddr(Place_choose.this, list);
            adapter.notifyDataSetChanged();
            poi_lv.setAdapter(adapter);
        }

        public void onGetPoiIndoorResult(PoiIndoorResult result){

        }

        public void onGetPoiDetailResult(PoiDetailSearchResult result){
            //获取Place详情页检索结果
        }

        public void onGetPoiDetailResult(PoiDetailResult result){

        }

    };



    private Button cancel;
    private TextView city_back;

    public boolean onMapPoiClick(MapPoi poi) {
        return false;
    }

    @Override
    public void onDestroy() {
        mPoiSearch.destroy();
        super.onDestroy();
    }


}
