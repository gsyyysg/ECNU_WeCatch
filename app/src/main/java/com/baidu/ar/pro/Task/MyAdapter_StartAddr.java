package com.baidu.ar.pro.Task;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.ar.pro.R;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

public class MyAdapter_StartAddr extends BaseAdapter {
    public static final int CHOOSE_PLACE = 1;
    private Context context;
    private List<PoiSearchResults> list;


    public MyAdapter_StartAddr(Context context, List<PoiSearchResults> list){
        super();
        this.context = context;
        this.list = list;
    }


 public int getCount(){
        return list.size();
 }

 public Object getItem(int position){
        return list.get(position);
 }

 public long getItemId(int postion){
        return postion;
 }

 public View getView(int position, View convertView, ViewGroup parent){
     final ViewHolder holder;
     final Double latitude;
     final Double longitude;
     final String name;

     if(convertView == null){
         holder = new ViewHolder();
         convertView = View.inflate(context, R.layout.startadd_item, null);
         holder.address=(TextView) convertView.findViewById(R.id.address);
         holder.name = (TextView) convertView.findViewById(R.id.name);
         holder.all = convertView.findViewById(R.id.a_palce);
         convertView.setTag(holder);
     }
     else{
         holder = (ViewHolder) convertView.getTag();
     }

     holder.address.setText(list.get(position).getMaddress());
     holder.name.setText(list.get(position).getMname());
     latitude = list.get(position).getMlatitude();
     longitude = list.get(position).getMlongitude();
     name = list.get(position).getMname();

     holder.all.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             //int position_click = holder.getAdapterposition();
             Intent intent = new Intent();
             intent.putExtra("latitude", latitude);
             intent.putExtra("longitude",longitude);
             intent.putExtra("placeName",name);
             Log.d("RightPlaceBack","right!");
             ((Place_choose)context).setResult(CHOOSE_PLACE, intent);
             ((Place_choose)context).finish();
         }
     });

     return convertView;
 }

 class ViewHolder{
        TextView name, address;
        View all;
    }

}
