package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by Lenovo on 2018/1/3.
 */

public class LowerEditAccountActivity extends BaseActivity {
    TextView title;
    Button save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lowereditaccount);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initListener();
    }

    private void initView() {
        save = findViewById(R.id.save);
        title = findViewById(R.id.title);
    }

    private void initListener() {
        title.setText("编辑账户信息");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LowerEditAccountActivity.this, LowerAccountActivity.class);
                intent.putExtra("email", "119@12312.com");
                LowerEditAccountActivity.this.setResult(2,intent);
                finish();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            //这里面的数据可以放上个页面传过来的数据
            Intent intent = new Intent();
            intent.setClass(LowerEditAccountActivity.this, LowerAccountActivity.class);
            intent.putExtra("email", "110@12312.com");
            LowerEditAccountActivity.this.setResult(2,intent);
            finish();
        }
        return true;
    }
}
