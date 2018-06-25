package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.example.yzcl.mvp.ui.mvpactivity.HomePage;
import com.example.yzcl.mvp.ui.mvpactivity.MainActivity;
import com.example.yzcl.utils.StatusBarUtil;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by Lenovo on 2017/12/12.
 */

public class PersonMessageActivity extends BaseActivity {
    ImageView back;
    TextView title;
    RelativeLayout accountmsg;
    RelativeLayout notification;
    RelativeLayout customer_service;
    RelativeLayout about;
    TextView exit;
    FrameLayout first_title_fl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personmessage);
        ImmersionBar.with(this)
                .statusBarColor(R.color.jb_head_color)
                .init();

        initView();


    }

    private void initView() {
        back=findViewById(R.id.back);
        title=findViewById(R.id.title);
        accountmsg=findViewById(R.id.accountmsg);
        notification=findViewById(R.id.notification);
        customer_service=findViewById(R.id.customer_service);
        about=findViewById(R.id.about);
        exit=findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUIUtils.showAlert(PersonMessageActivity.this, null, "确定退出?", "", "", "确定", "取消", false, true, true, new DialogUIListener() {
                    @Override
                    public void onPositive() {
//                        showToast("onPositive");
                        Intent intent=new Intent();
                                intent.setClass(PersonMessageActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                    }

                    @Override
                    public void onNegative() {
//                        Toast.makeText(PersonMessageActivity.this,"onNegative",Toast.LENGTH_SHORT).show();
                    }

                }).show();
            }
        });
//        exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new AlertDialog.Builder(PersonMessageActivity.this).setTitle("确定退出?")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent intent=new Intent();
//                                intent.setClass(PersonMessageActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        }).show();
//
//
//            }
//        });
        title.setText("我的");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
        accountmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(PersonMessageActivity.this,AccountMessageActivity.class);
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(PersonMessageActivity.this,NotificationActivity.class);
                startActivity(intent);
            }
        });
        customer_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(PersonMessageActivity.this,CustomerServiceActivity.class);
                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(PersonMessageActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });
    }
}
