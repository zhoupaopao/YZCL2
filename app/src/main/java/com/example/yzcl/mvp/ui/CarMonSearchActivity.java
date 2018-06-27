package com.example.yzcl.mvp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/6/26.
 */
//车辆监控页面的搜索页面
public class CarMonSearchActivity extends BaseActivity {
    private TextView cancel;
    private TextView clear;
    private EditText et_search;
    private RecyclerView recycleview;
    private ArrayList<String>arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_mon_search);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        cancel=findViewById(R.id.cancel);
        clear=findViewById(R.id.clear);
        et_search=findViewById(R.id.et_search);
        recycleview=findViewById(R.id.recycleview);
        arrayList=new ArrayList<>();
    }

    private void initData() {

    }

    private void initListener() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
