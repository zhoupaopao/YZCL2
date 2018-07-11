package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
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
 * Created by Lenovo on 2018/7/9.
 */
//下发指令-定时模式
public class TimingSettingActivity extends BaseActivity implements View.OnClickListener{
    //没有使用列表，直接使用布局，点击布局触发显示隐藏
    private RelativeLayout rl1;
    private RelativeLayout rl2;
    private RelativeLayout rl3;
    private RelativeLayout rl4;
    private RelativeLayout rl5;
    private ImageView check1;
    private ImageView check2;
    private ImageView check3;
    private ImageView check4;
    private ImageView check5;
    //当前选中的item
    private int nowid=0;
    private TextView title;
    private ImageView back;
    BuildBean dialog;
    private String TAG="TimingSettingActivity";
    //保存
    private TextView textview2;
    //需要上传的参数
    private StringBuffer clockModel;//4:40,5:45,
    private StringBuffer postwuc;//0|4:40;0|5:45
    private String deviceid;
    private int interval=0;
    private int type=0;

    private SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing_setting);
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
        interval=intent.getIntExtra("interval",0);
        deviceid=intent.getStringExtra("deviceid");
        rl1=findViewById(R.id.rl1);
        rl2=findViewById(R.id.rl2);
        rl3=findViewById(R.id.rl3);
        rl4=findViewById(R.id.rl4);
        rl5=findViewById(R.id.rl5);
        check1=findViewById(R.id.check1);
        check2=findViewById(R.id.check2);
        check3=findViewById(R.id.check3);
        check4=findViewById(R.id.check4);
        check5=findViewById(R.id.check5);
        title=findViewById(R.id.title);
        back=findViewById(R.id.back);
        textview2=findViewById(R.id.textview2);
        rl1.setOnClickListener(this);
        rl2.setOnClickListener(this);
        rl3.setOnClickListener(this);
        rl4.setOnClickListener(this);
        rl5.setOnClickListener(this);
        back.setOnClickListener(this);
        textview2.setOnClickListener(this);
    }

    private void initData() {
        title.setText("定时模式");
        textview2.setText("保存");

        switch (interval){
            case 0:
                //之前不是默认选项
                nowid=4;
                check4.setVisibility(View.VISIBLE);
                break;
            case 15:
                nowid=1;
                check1.setVisibility(View.VISIBLE);
                break;
            case 30:
                nowid=2;
                check2.setVisibility(View.VISIBLE);
                break;
            case 60:
                nowid=3;
                check3.setVisibility(View.VISIBLE);
                break;
            case 120:
                nowid=4;
                check4.setVisibility(View.VISIBLE);
                break;
            case 180:
                nowid=5;
                check5.setVisibility(View.VISIBLE);
                break;
            default:
                //其他
                nowid=4;
                check4.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl1:
                //切换当前显示和隐藏
                //用方法执行，第一个参数是之前选中的项，后一个参数是需要选中的项
                VisibleChange(nowid,1);
                check1.setVisibility(View.VISIBLE);
                interval=15;
                break;
            case R.id.rl2:
                //切换当前显示和隐藏
                //用方法执行，第一个参数是之前选中的项，后一个参数是需要选中的项
                VisibleChange(nowid,2);
                check2.setVisibility(View.VISIBLE);
                interval=30;
                break;
            case R.id.rl3:
                //切换当前显示和隐藏
                //用方法执行，第一个参数是之前选中的项，后一个参数是需要选中的项
                VisibleChange(nowid,3);
                check3.setVisibility(View.VISIBLE);
                interval=60;
                break;
            case R.id.rl4:
                //切换当前显示和隐藏
                //用方法执行，第一个参数是之前选中的项，后一个参数是需要选中的项
                VisibleChange(nowid,4);
                check4.setVisibility(View.VISIBLE);
                interval=120;
                break;
            case R.id.rl5:
                //切换当前显示和隐藏
                //用方法执行，第一个参数是之前选中的项，后一个参数是需要选中的项
                VisibleChange(nowid,5);
                check5.setVisibility(View.VISIBLE);
                interval=180;
                break;
            case R.id.back:
                finish();
                break;
            case R.id.textview2:
                //保存
                RequestParams params1=new RequestParams();
                //因为传递的是json数据，所以需要设置header和body
                params1.addHeader("Content-Type","application/json");
                JSONObject jsonObject1=new JSONObject();
                jsonObject1.put("clockModel","");
//                Log.i(TAG, clockModel.toString().substring(0,clockModel.toString().length()-1));
                jsonObject1.put("type",type);
                jsonObject1.put("interval",interval);
                jsonObject1.put("wuc","");
                jsonObject1.put("deviceid",deviceid);
                params1.setRequestBody(MediaType.parse("application/json"),jsonObject1.toString());
                HttpRequest.post(Api.saveOrUpdateDeviceSetting+"?token="+sp.getString(Constant.Token,""),params1,new JsonHttpRequestCallback(){
                    @Override
                    protected void onSuccess(Headers headers, JSONObject jsonObject) {
                        super.onSuccess(headers, jsonObject);
                        Log.i(TAG, jsonObject.toString());
                        if(jsonObject.getBoolean("success")){
                            Toast.makeText(TimingSettingActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }else{
                            Toast.makeText(TimingSettingActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }
                        dialog.dialog.dismiss();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        dialog= DialogUIUtils.showLoading(TimingSettingActivity.this,"加载中...",true,false,false,true);
                        dialog.show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                    }
                });
                break;
        }
    }

    private void VisibleChange(int nowidd, int i) {
        if(nowidd==i){
            //等于是选中了之前已经选中的项
        }else{
            //隐藏之前的一项
            Gone(nowidd);
            //将当前选中项变成i
            nowid=i;
        }
    }

    private void Gone(int nowidw) {
        switch (nowidw){
            case 1:
                check1.setVisibility(View.GONE);
                break;
            case 2:
                check2.setVisibility(View.GONE);
                break;
            case 3:
                check3.setVisibility(View.GONE);
                break;
            case 4:
                check4.setVisibility(View.GONE);
                break;
            case 5:
                check5.setVisibility(View.GONE);
                break;
        }
    }
}
