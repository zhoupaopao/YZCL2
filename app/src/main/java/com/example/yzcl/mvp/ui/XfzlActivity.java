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
import com.example.yzcl.mvp.model.bean.XfzlBean;
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
    XfzlBean xfzlBean;
    private String TAG="XfzlActivity";
    private String wuc="";//用于存放如果是星期模式和闹钟模式每段时间
    private int interval=0;//时间间隔
    private int nowstatus;//用于判断当前是什么状态

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfzl);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        if(!Constant.isNetworkConnected(XfzlActivity.this)) {
            //判断网络是否可用
            Toast.makeText(XfzlActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            initData();
        }

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
        jsonObject1.put("id",deviceid);
        params1.setRequestBody(MediaType.parse("application/json"),jsonObject1.toString());
        HttpRequest.post(Api.queDeviceSetting+"?token="+sp.getString(Constant.Token,""),params1,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                xfzlBean=JSONObject.parseObject(jsonObject.toString(),XfzlBean.class);

                if(xfzlBean.isSuccess()){
                    //请求成功
                    //判断是什么模式
                    switch (xfzlBean.getObject().getType()){
                        case 0:
                            //定时模式0
                            interval=xfzlBean.getObject().getInterval();
                            ds_status.setVisibility(View.VISIBLE);
                            nowstatus=0;
                            break;
                        case 1:
                            //闹钟模式1
                            wuc=xfzlBean.getObject().getWuc();
                            nz_status.setVisibility(View.VISIBLE);
                            nowstatus=1;
                            break;
                        case 2:
                            //星期模式
                            wuc=xfzlBean.getObject().getWuc();
                            xq_status.setVisibility(View.VISIBLE);
                            nowstatus=2;
                            break;
                    }

                }else{
                    Toast.makeText(XfzlActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                }
                dialog.dialog.dismiss();
                //请求成功
                //显示点

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
                    //当前是闹钟模式
                intent.putExtra("wuc",wuc);
                intent.putExtra("deviceid",deviceid);
                intent.setClass(XfzlActivity.this, PatternSettingActivity.class);
                startActivityForResult(intent,1);
//                startActivity(intent);
                break;
                //定时模式
            case R.id.dsms:
                intent=new Intent();
                intent.putExtra("interval",interval);
                intent.putExtra("deviceid",deviceid);
                intent.setClass(XfzlActivity.this,TimingSettingActivity.class);
                startActivityForResult(intent,1);
//                startActivity(intent);
                break;
                //星期模式
            case R.id.xqms:
                intent=new Intent();
                intent.putExtra("wuc",wuc);
                intent.putExtra("deviceid",deviceid);
                intent.setClass(XfzlActivity.this,WeekSettingActivity.class);
                startActivityForResult(intent,1);
//                startActivity(intent);
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }
}
