package com.example.yzcl.mvp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;

/**
 * Created by Lenovo on 2018/7/5.
 */

public class TranceActivity extends BaseActivity {
    private ImageView back;
    private TextView title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trance);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        back=findViewById(R.id.back);
        title=findViewById(R.id.title);
    }

    private void initData() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("实时跟踪");
    }

    private void initListener() {

    }
}
