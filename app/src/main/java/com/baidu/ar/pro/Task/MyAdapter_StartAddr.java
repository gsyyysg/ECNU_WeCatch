package com.baidu.ar.pro.Task;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.ar.pro.R;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

public class MyAdapter_StartAddr extends BaseAdapter {
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
     ViewHolder holder;
     if(convertView == null){
         holder = new ViewHolder();
         convertView = View.inflate(context, R.layout.startadd_item, null);
         holder.address=(TextView) convertView.findViewById(R.id.address);
         holder.name = (TextView) convertView.findViewById(R.id.name);
         convertView.setTag(holder);
     }
     else{
         holder = (ViewHolder) convertView.getTag();
     }

     holder.address.setText(list.get(position).getMaddress());
     holder.name.setText(list.get(position).getMname());

     return convertView;
 }

 class ViewHolder{
        TextView name, address;
    }

}
