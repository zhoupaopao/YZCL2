package com.example.yzcl.mvp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/7/3.
 */

public class XfzlActivity extends BaseActivity implements OnClickListener{
    private RelativeLayout nzms;
    private RelativeLayout dsms;
    private RelativeLayout xqms;
    private TextView cancel;
    private TextView nz_status;
    private TextView ds_status;
    private TextView xq_status;
    BuildBean dialog;
    private String deviceid;
    SharedPreferences sp;

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
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        Intent intent=getIntent();
        deviceid=intent.getStringExtra("deviceid");
        nzms=findViewById(R.id.nzms);
        dsms=findViewById(R.id.dsms);
        xqms=findViewById(R.id.xqms);
        cancel=findViewById(R.id.cancel);
        nzms.setOnClickListener(this);
        dsms.setOnClickListener(this);
        xqms.setOnClickListener(this);
        cancel.setOnClickListener(this);
        nz_status=findViewById(R.id.nz_status);
        ds_status=findViewById(R.id.ds_status);
        xq_status=findViewById(R.id.xq_status);
    }

    private void initData() {
        RequestParams params1=new RequestParams();
        //因为传递的是json数据，所以需要设置header和body
        params1.addHeader("Content-Type","application/json");
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("deviceid",deviceid);
        params1.setRequestBody(MediaType.parse("application/json"),jsonObject1.toString());
        HttpRequest.post(Api.queDeviceSetting+"?token="+sp.getString(Constant.Token,""),params1,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                if(jsonObject.getBoolean("success")){
//                    if(){
//
//                    }
                    dialog.dialog.dismiss();
                }else{
                    Toast.makeText(XfzlActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    dialog.dialog.dismiss();
                }
                //请求成功
                //显示点
                Log.i("onSuccess: ", jsonObject.toString());
//                }else{
//
//                }
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(XfzlActivity.this,"加载中...",true,false,false,true);
                dialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }

    private void initListener() {

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            //闹钟模式
            case R.id.nzms:
                intent=new Intent();
                intent.setClass(XfzlActivity.this, PatternSettingActivity.class);
                startActivity(intent);
                break;
                //定时模式
            case R.id.dsms:
                intent=new Intent();
                intent.setClass(XfzlActivity.this,TimingSettingActivity.class);
                startActivity(intent);
                break;
                //星期模式
            case R.id.xqms:
                intent=new Intent();
                intent.setClass(XfzlActivity.this,WeekSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }
}
