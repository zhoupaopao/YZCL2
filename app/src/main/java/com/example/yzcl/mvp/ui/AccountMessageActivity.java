package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.content.Intent;
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
 * Created by Lenovo on 2017/12/12.
 */

public class AccountMessageActivity extends BaseActivity {
    ImageView back;
    TextView title;
    RelativeLayout name;
    RelativeLayout mobile;
    RelativeLayout email;
    RelativeLayout changepwd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_message);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();

    }

    private void initView() {
        name=findViewById(R.id.changename);
        mobile=findViewById(R.id.mobile);
        email=findViewById(R.id.email);
        changepwd=findViewById(R.id.changepsd);
        back=findViewById(R.id.back);
        title=findViewById(R.id.title);
        title.setText("账户信息");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //不同按钮对应不同数据，不同请求接口
        //设置姓名
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("title","设置姓名");
                intent.putExtra("StringText","老王");
                intent.putExtra("prompt","输入2~10个汉字的中文姓名");
                intent.setClass(AccountMessageActivity.this,ChangeMessageActivity.class);
                startActivity(intent);
            }
        });
        //设置手机号
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("title","设置手机号");
                intent.putExtra("StringText","110119120");
                intent.putExtra("prompt","");
                intent.setClass(AccountMessageActivity.this,ChangeMessageActivity.class);
                startActivity(intent);
            }
        });
        //设置邮箱
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("title","设置邮箱");
                intent.putExtra("StringText","110119120@163.com");
                intent.putExtra("prompt","");
                intent.setClass(AccountMessageActivity.this,ChangeMessageActivity.class);
                startActivity(intent);
            }
        });
        //修改密码
        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(AccountMessageActivity.this,ChangePwdActivity.class);
                startActivity(intent);
            }
        });
    }
}
