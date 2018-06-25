package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by Lenovo on 2017/12/12.
 */

/**
 * 关于我们
 */
public class AboutActivity extends BaseActivity {
    ImageView back;
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
//        ImmersionBar.with(this).init();
//        ImmersionBar.with(this)
//                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
//                .statusBarColor(R.color.title_color)
//                .init();
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();

    }

    private void initView() {
        back=findViewById(R.id.back);
        title=findViewById(R.id.title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("关于我们");
    }
}
