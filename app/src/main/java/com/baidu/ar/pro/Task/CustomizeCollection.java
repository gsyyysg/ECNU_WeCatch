package com.baidu.ar.pro.Task;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ar.pro.LoginActivity;
import com.baidu.ar.pro.R;

import java.util.ArrayList;
import java.util.List;

class pack{
    Integer coll_no;
    String coll_name;
    String coll_hint;
    Double latitude;
    Double longitude;
    Integer reward;
};



public class CustomizeCollection extends Activity {

    static int Map = 1;
    static List<pack> list = new ArrayList<pack>();
    static int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
//        setContentView(R.layout.customize_collection);
        setContentView(R.layout.customize_collection);


        Intent intent = getIntent();
//        String task_name = intent.getStringExtra("name");
//        String task_info = intent.getStringExtra("info");

        final Integer coll_num = intent.getIntExtra("number", -1);



        final Button place_chose = findViewById(R.id.place_choose);
        Button next_step = findViewById(R.id.next_button);
        final TextView coll_name = findViewById(R.id.textView2);
        final TextView coll_notify = findViewById(R.id.textView3);
        final Spinner golds = findViewById(R.id.spinner);
        final Button titile = findViewById(R.id.button3);
        titile.setText("我的自定义藏品1");

        place_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomizeCollection.this, Place_choose.class);
                startActivityForResult(intent, 1);
            }
        });

        next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pack single = new pack();
                String name = coll_name.getText().toString();
                String hint = coll_notify.getText().toString();
                Integer reward = Integer.parseInt((String)golds.getSelectedItem());

                single.coll_name = name;
                single.coll_no = i;
                single.reward = reward;
                single.coll_hint = hint;

                if(name.isEmpty()||hint.isEmpty())
                {
                    Toast.makeText(CustomizeCollection.this, "请完善藏品的基本信息！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(i < coll_num)
                {
                    list.add(single);
                    i = i+1;
                    coll_name.setHint("Name of your collection");
                    coll_name.setText("");
                    coll_notify.setHint("Hint about your collection");
                    coll_notify.setText("");
                    golds.setSelection(0);
                    place_chose.setText("Put it in the place you want");
                    titile.setText("我的自定义藏品"+i);
                }
                else
                {
                    Toast.makeText(CustomizeCollection.this, "设置成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CustomizeCollection.this, TaskListActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


}
