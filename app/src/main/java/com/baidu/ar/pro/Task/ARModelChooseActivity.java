package com.baidu.ar.pro.Task;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.baidu.ar.pro.AR.ARModel;
import com.baidu.ar.pro.R;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ARModelChooseActivity extends Activity {

    private ImageButton backButton;

    private RecyclerView mRecyclerView;

    private GridLayoutManager mLayoutManager;

    private List<ARModel> ARList = new ArrayList<ARModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_model_choose);

        backButton = findViewById(R.id.ArChoose_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ARModelChooseActivity.this, CustomizeCollection.class);
                startActivity(intent);
            }
        });



        findview();
        initARChoose();
        ARChooseAdapter adapter = new ARChooseAdapter(ARList, this);
        mRecyclerView.setAdapter(adapter);

    }

    private void findview()
    {
        mRecyclerView =  findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(this, 2);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(12)); //can adjust!!!!
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
    }

    private void initARChoose()  //之后应该正经从数据库加载
    {
        ARList = LitePal.findAll(ARModel.class);
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
            if(parent.getChildLayoutPosition(view)%2==0){
                outRect.left = 0;
            }
        }
    }
}