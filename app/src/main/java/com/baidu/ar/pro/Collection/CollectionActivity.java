package com.baidu.ar.pro.Collection;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

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
        Collection aiji = new Collection(1,R.drawable.collection1); collectionList.add(aiji);
        Collection amercancurcat = new Collection(2,R.drawable.collection2); collectionList.add(amercancurcat);
        Collection bolila = new Collection(3,R.drawable.collection3); collectionList.add(bolila);
        Collection bosicat = new Collection(4,R.drawable.collection4); collectionList.add(bosicat);
        Collection britishcat = new Collection(5,R.drawable.collection5); collectionList.add(britishcat);
        Collection dollcat = new Collection(6,R.drawable.collection6); collectionList.add(dollcat);
        Collection yiguo = new Collection(7,R.drawable.collection7); collectionList.add(yiguo);
        Collection norwaycat = new Collection(8,R.drawable.collection8); collectionList.add(norwaycat);
        Collection sugelancat = new Collection(9,R.drawable.collection9); collectionList.add(sugelancat);
        Collection xinjiapocat = new Collection(10,R.drawable.collection10); collectionList.add(xinjiapocat);
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
