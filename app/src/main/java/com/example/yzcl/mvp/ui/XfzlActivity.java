package com.example.yzcl.mvp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by Lenovo on 2018/7/3.
 */

public class XfzlActivity extends BaseActivity implements OnClickListener{
    private RelativeLayout nzms;
    private RelativeLayout dsms;
    private RelativeLayout xqms;
    private TextView cancel;

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
        nzms=findViewById(R.id.nzms);
        dsms=findViewById(R.id.dsms);
        xqms=findViewById(R.id.xqms);
        cancel=findViewById(R.id.cancel);
        nzms.setOnClickListener(this);
        dsms.setOnClickListener(this);
        xqms.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void initData() {

    }

    private void initListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nzms:
                break;
            case R.id.dsms:
                break;
            case R.id.xqms:
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }
}
