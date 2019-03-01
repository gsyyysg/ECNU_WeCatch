package com.baidu.ar.pro.Collection;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baidu.ar.pro.R;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends Activity {

    private List<Collection> collectionList = new ArrayList<>();
    RecyclerView mrecyclerview;
    GridLayoutManager mlayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_layout);
        findview();
        initCollection();
        CollectionAdapter adapter = new CollectionAdapter(collectionList);
        mrecyclerview.setAdapter(adapter);
    }

    private void findview()
    {
        mrecyclerview =  findViewById(R.id.recycler_view);
        mlayoutManager = new GridLayoutManager(this, 5);
        mlayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mrecyclerview.setLayoutManager(mlayoutManager);
        mrecyclerview.addItemDecoration(new SpaceItemDecoration(12));
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
    }

    private void initCollection()  //之后应该正经从数据库加载
    {
        Collection aiji = new Collection("埃及猫",R.drawable.aiji_pic); collectionList.add(aiji);
        Collection amercancurcat = new Collection("美国短毛猫",R.drawable.amercancurlcat_pic); collectionList.add(amercancurcat);
        Collection bolila = new Collection("波米拉猫",R.drawable.bolila_pic); collectionList.add(bolila);
        Collection bosicat = new Collection("波斯猫",R.drawable.bosicat_pic); collectionList.add(bosicat);
        Collection britishcat = new Collection("英国短毛猫",R.drawable.british_pic); collectionList.add(britishcat);
        Collection dollcat = new Collection("布偶猫",R.drawable.buoumao_pic); collectionList.add(dollcat);
        Collection yiguo = new Collection("异国短毛猫",R.drawable.yiguoduanmao_pic); collectionList.add(yiguo);
        Collection norwaycat = new Collection("挪威猫",R.drawable.norway_pic); collectionList.add(norwaycat);
        Collection sugelancat = new Collection("苏格兰折耳猫",R.drawable.sglzheer_pic); collectionList.add(sugelancat);
        Collection xinjiapocat = new Collection("新加坡猫",R.drawable.xinjiapo_pic); collectionList.add(xinjiapocat);
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration{
        private int space;
        public SpaceItemDecoration(int space){
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.bottom = space;
            if(parent.getChildLayoutPosition(view)%5==0){
                outRect.left = 0;
            }
        }
    }
}
