package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIListener;
import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.example.yzcl.mvp.ui.mvpactivity.HomePage;
import com.example.yzcl.mvp.ui.mvpactivity.MainActivity;
import com.example.yzcl.utils.StatusBarUtil;
import com.gyf.barlibrary.ImmersionBar;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;

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
    SharedPreferences sp;
    FrameLayout first_title_fl;
    BuildBean jq_dia;
    private String TAG="PersonMessageActivity";
    private TextView username;
    private TextView groupname;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personmessage);
        ImmersionBar.with(this)
                .statusBarColor(R.color.jb_head_color)
                .init();

        initView();
        if(!Constant.isNetworkConnected(PersonMessageActivity.this)) {
            //判断网络是否可用
            Toast.makeText(PersonMessageActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
        }else{
            initData();
        }


    }

    private void initData() {
        RequestParams params=new RequestParams();
        params.addFormDataPart("token",sp.getString(Constant.Token,""));
        HttpRequest.get(Api.getUserGeneralInfo,params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, "onSuccess: "+jsonObject.toString());
                if(jsonObject.getBoolean("success")){
                    //请求成功
                    //获取用户名和所属组织
                    username.setText(jsonObject.getJSONObject("object").getString("username"));
                    groupname.setText(jsonObject.getJSONObject("object").getString("groupname"));
                }else{
                    Toast.makeText(PersonMessageActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                }
                DialogUIUtils.dismiss(jq_dia);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }

            @Override
            public void onStart() {
                super.onStart();
                jq_dia = DialogUIUtils.showLoading(PersonMessageActivity.this, "加载中...", false, true, false, false);
                jq_dia.show();
            }
        });
    }

    private void initView() {
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        back=findViewById(R.id.back);
        title=findViewById(R.id.title);
        accountmsg=findViewById(R.id.accountmsg);
        notification=findViewById(R.id.notification);
        customer_service=findViewById(R.id.customer_service);
        about=findViewById(R.id.about);
        username=findViewById(R.id.username);
        groupname=findViewById(R.id.groupname);
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
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
//                        Intent intent=new Intent();
//                                intent.setClass(PersonMessageActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                finish();
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
