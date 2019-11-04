package com.baidu.ar.pro.Collection;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.baidu.ar.pro.R;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends Activity {

    private List<Collection> collectionList = new ArrayList<>();

    private ImageButton backButton;

    private RecyclerView mRecyclerView;

    private GridLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_layout);
        backButton = findViewById(R.id.collection_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findview();
        initCollection();
        CollectionAdapter adapter = new CollectionAdapter(collectionList,this);
        mRecyclerView.setAdapter(adapter);
    }

    private void findview()
    {
        mRecyclerView =  findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(this, 5);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(12));
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
    }

    private void initCollection()  //之后应该正经从数据库加载
    {
        collectionList = LitePal.findAll(Collection.class);
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
