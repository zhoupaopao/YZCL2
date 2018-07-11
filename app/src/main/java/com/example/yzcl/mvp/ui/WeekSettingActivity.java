package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
//下发指令-星期模式
public class WeekSettingActivity extends BaseActivity implements View.OnClickListener{
    //没有使用列表，直接使用布局，点击布局触发显示隐藏
    private RelativeLayout rl1;
    private RelativeLayout rl2;
    private RelativeLayout rl3;
    private RelativeLayout rl4;
    private RelativeLayout rl5;
    private RelativeLayout rl6;
    private RelativeLayout rl7;
    private ImageView check1;
    private ImageView check2;
    private ImageView check3;
    private ImageView check4;
    private ImageView check5;
    private ImageView check6;
    private ImageView check7;
    private TextView title;
    private ImageView back;
    private String TAG="WeekSettingActivity";
    int newHour,newMinutes;
    final int TIME_DIALOG1=1;
    private EditText nz_time1;
    //保存
    private TextView textview2;
    String[]wuclist;
    BuildBean dialog;
    private String wuc;
    //需要上传的参数
    private StringBuffer weekModel;//2,3,4,
    private String weekTime;//1:30
    private StringBuffer postwuc;//1|4:40;2|4:40
    private String deviceid;
    private int interval=0;
    private int type=2;
    private SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_setting);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        Intent intent=getIntent();
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        deviceid=intent.getStringExtra("deviceid");
        wuc=intent.getStringExtra("wuc");
        rl1=findViewById(R.id.rl1);
        rl2=findViewById(R.id.rl2);
        rl3=findViewById(R.id.rl3);
        rl4=findViewById(R.id.rl4);
        rl5=findViewById(R.id.rl5);
        rl6=findViewById(R.id.rl6);
        rl7=findViewById(R.id.rl7);
        check1=findViewById(R.id.check1);
        check2=findViewById(R.id.check2);
        check3=findViewById(R.id.check3);
        check4=findViewById(R.id.check4);
        check5=findViewById(R.id.check5);
        check6=findViewById(R.id.check6);
        check7=findViewById(R.id.check7);
        nz_time1=findViewById(R.id.nz_time1);
        title=findViewById(R.id.title);
        back=findViewById(R.id.back);
        textview2=findViewById(R.id.textview2);
        rl1.setOnClickListener(this);
        rl2.setOnClickListener(this);
        rl3.setOnClickListener(this);
        rl4.setOnClickListener(this);
        rl5.setOnClickListener(this);
        rl6.setOnClickListener(this);
        rl7.setOnClickListener(this);
        back.setOnClickListener(this);
        textview2.setOnClickListener(this);
    }

    private void initData() {
        textview2.setText("保存");
        title.setText("星期模式");
        if(wuc.equals("")){
            //说明当前不是这个模式
        }else{
            wuclist=wuc.split(";");
            for(int i=0;i<wuclist.length;i++){
                String[]listsp=wuclist[i].split("\\|");
                String weekday=listsp[0];
                String daytime=listsp[1];
                nz_time1.setText(daytime);
                switch (Integer.parseInt(weekday)){
                    //根据前面的数字判断是那个星期
                    case 1:
                        check1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        check2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        check3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        check4.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        check5.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        check6.setVisibility(View.VISIBLE);
                        break;
                    case 7:
                        check7.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    private void initListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl1:
                //检查当前状态，是隐藏还是显示
                checkstatus(check1);
                break;
            case R.id.rl2:
                checkstatus(check2);
                break;
            case R.id.rl3:
                checkstatus(check3);
                break;
            case R.id.rl4:
                checkstatus(check4);
                break;
            case R.id.rl5:
                checkstatus(check5);
                break;
            case R.id.rl6:
                checkstatus(check6);
                break;
            case R.id.rl7:
                checkstatus(check7);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.textview2:
                weekTime="";
                weekModel=new StringBuffer();
                postwuc=new StringBuffer();
                //保存
                //先判断当前时间有没有填写
                if(nz_time1.getText().toString().equals("")){
                    Toast.makeText(WeekSettingActivity.this,"请选择时间",Toast.LENGTH_SHORT).show();
                }else{
                    //如果一个都没选中也提示
                    //全是不可见的
                    if(check1.getVisibility()==View.GONE&&check2.getVisibility()==View.GONE&&check3.getVisibility()==View.GONE&&check4.getVisibility()==View.GONE&&check5.getVisibility()==View.GONE&&check6.getVisibility()==View.GONE&&check7.getVisibility()==View.GONE){
                        Toast.makeText(WeekSettingActivity.this,"请至少选择一个时间",Toast.LENGTH_SHORT).show();
                    }else{
                        weekTime=nz_time1.getText().toString();
                        if(check1.getVisibility()==View.VISIBLE){
                            postwuc.append(1+"|"+nz_time1.getText().toString()+";");
                            weekModel.append(1+",");
                        }
                        if(check2.getVisibility()==View.VISIBLE){
                            postwuc.append(2+"|"+nz_time1.getText().toString()+";");
                            weekModel.append(2+",");
                        }
                        if(check3.getVisibility()==View.VISIBLE){
                            postwuc.append(3+"|"+nz_time1.getText().toString()+";");
                            weekModel.append(3+",");
                        }
                        if(check4.getVisibility()==View.VISIBLE){
                            postwuc.append(4+"|"+nz_time1.getText().toString()+";");
                            weekModel.append(4+",");
                        }
                        if(check5.getVisibility()==View.VISIBLE){
                            postwuc.append(5+"|"+nz_time1.getText().toString()+";");
                            weekModel.append(5+",");
                        }
                        if(check6.getVisibility()==View.VISIBLE){
                            postwuc.append(6+"|"+nz_time1.getText().toString()+";");
                            weekModel.append(6+",");
                        }
                        if(check7.getVisibility()==View.VISIBLE){
                            postwuc.append(7+"|"+nz_time1.getText().toString()+";");
                            weekModel.append(7+",");
                        }
                        RequestParams params1=new RequestParams();
                        //因为传递的是json数据，所以需要设置header和body
                        params1.addHeader("Content-Type","application/json");
                        JSONObject jsonObject1=new JSONObject();
                        jsonObject1.put("wuc",postwuc.toString().substring(0,postwuc.toString().length()-1));
                        jsonObject1.put("weekTime",weekTime);
                        jsonObject1.put("type",type);
                        jsonObject1.put("interval",interval);
                        jsonObject1.put("weekModel",weekModel.toString());
                        jsonObject1.put("deviceid",deviceid);
                        params1.setRequestBody(MediaType.parse("application/json"),jsonObject1.toString());
                        HttpRequest.post(Api.saveOrUpdateDeviceSetting+"?token="+sp.getString(Constant.Token,""),params1,new JsonHttpRequestCallback(){
                            @Override
                            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                                super.onSuccess(headers, jsonObject);
                                Log.i(TAG, jsonObject.toString());
                                if(jsonObject.getBoolean("success")){
                                    Toast.makeText(WeekSettingActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                }else{
                                    Toast.makeText(WeekSettingActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                }
                                dialog.dialog.dismiss();
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                                dialog= DialogUIUtils.showLoading(WeekSettingActivity.this,"加载中...",true,false,false,true);
                                dialog.show();
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();

                            }
                        });
                    }
                }
                break;
        }
    }

    private void checkstatus(ImageView nowimg) {
        if(nowimg.getVisibility()==View.GONE){
            //当前是不可见的
            nowimg.setVisibility(View.VISIBLE);
        }else{
            nowimg.setVisibility(View.GONE);
        }
    }
    public void nztime(View view){
        switch (view.getId()){
            case R.id.nz_time1:
                showDialog(TIME_DIALOG1);
                break;
        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case TIME_DIALOG1:
//                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
                return new TimePickerDialog(this,mtimeListener1,newHour,newMinutes,true);
            }

        return null;
    }
    private TimePickerDialog.OnTimeSetListener mtimeListener1=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            display(hourOfDay,minute);
        }
    };

    private void display(int hourOfDay, int minute) {
        String hour="";
        String minu="";
        if(hourOfDay<10){
            hour="0"+hourOfDay;
        }else{
            hour=""+hourOfDay;
        }
        if(minute<10){
            minu="0"+minute;
        }else{
            minu=""+minute;
        }
        nz_time1.setText(hour+":"+minu);
    }
}
