package com.baidu.ar.pro.AR;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.baidu.ar.ARFragment;
import com.baidu.ar.pro.Collection.CollectSucceedActivity;
import com.baidu.ar.pro.R;

public class mFragment extends ARFragment {

    private Button collectButton;

    private int collectionID;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        collectButton = getActivity().findViewById(R.id.collect_button);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CollectSucceedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("collection", collectionID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        collectionID = this.getArguments().getInt("collection");
    }
}
