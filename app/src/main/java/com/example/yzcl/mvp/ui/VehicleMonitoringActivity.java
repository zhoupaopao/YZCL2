package com.example.yzcl.mvp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by Lenovo on 2018/6/25.
 */

public class VehicleMonitoringActivity extends BaseActivity {
    //车辆监控主页面
    private TextView title;
    private ImageView back;
    private ImageView add;
    private RelativeLayout rl_search;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_monitoring);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();

    }

    private void initView() {
        title=findViewById(R.id.title);
        back=findViewById(R.id.back);
        add=findViewById(R.id.add);
        rl_search=findViewById(R.id.rl_search);
    }

    private void initData() {

    }

    private void initListener() {
        title.setText(R.string.car_veh);
        add.setImageResource(R.mipmap.search);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rl_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
