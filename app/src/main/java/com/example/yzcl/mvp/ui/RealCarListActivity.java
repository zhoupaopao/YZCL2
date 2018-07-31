package com.example.yzcl.mvp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.yzcl.R;
import com.example.yzcl.adapter.EasyRecycleAdapter;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

/**
 * Created by 13126 on 2018/7/31.
 */

public class RealCarListActivity extends BaseActivity {
    RecyclerView recyclerView;
    ArrayList<String>arrayList;
    EasyRecycleAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_carlist);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        recyclerView=findViewById(R.id.recyclerView);
        arrayList=new ArrayList<>();
        recyclerView.scrollTo(0,-100);
    }

    private void initData() {
        for(int i=0;i<30;i++){
            arrayList.add("aaa"+i);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new EasyRecycleAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);
    }

    private void initListener() {

    }
}
