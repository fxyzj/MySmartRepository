package com.xq.fuxiaoyong.recyclerview1208;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private List<String> mDatas;
    private MyRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        initData();

        recyclerAdapter = new MyRecyclerAdapter(MainActivity.this,mDatas);

        LinearLayoutManager manger = new LinearLayoutManager(this);

        //设置垂直布局
        manger.setOrientation(OrientationHelper.VERTICAL);

        //设置布局管理器
       recyclerView.setLayoutManager(manger);

        //设置Adapter
        recyclerView.setAdapter(recyclerAdapter);

        //设置删除或增加条目动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());





    }

    private void initData() {

        mDatas = new ArrayList<String>();
        for(int i = 0;i <40;i++){
            mDatas.add("item"+i);
        }

    }
}
