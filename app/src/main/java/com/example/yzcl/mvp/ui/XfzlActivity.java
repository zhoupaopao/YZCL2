package com.example.yzcl.mvp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by Lenovo on 2018/7/3.
 */

public class XfzlActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfzl);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initView() {

    }

    private void initData() {

    }

    private void initListener() {

    }
}
