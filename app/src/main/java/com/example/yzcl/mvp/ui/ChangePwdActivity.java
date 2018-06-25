package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by Lenovo on 2017/12/13.
 */

/**
 * 修改密码
 */

public class ChangePwdActivity extends BaseActivity {
    TextView title;
    ImageView back;
    Button sure;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepwd);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
    }

    private void initView() {
        title=findViewById(R.id.title);
        back=findViewById(R.id.back);
        sure=findViewById(R.id.sure);
        title.setText("修改密码");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChangePwdActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
