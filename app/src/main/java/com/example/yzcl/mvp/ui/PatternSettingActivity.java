package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.XfzlBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.fanwe.lib.switchbutton.ISDSwitchButton;
import com.fanwe.lib.switchbutton.SDSwitchButton;
import com.gyf.barlibrary.ImmersionBar;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/7/4.
 */
//模式设置页面，三大模式，闹钟，定时，星期
public class PatternSettingActivity extends BaseActivity {
    final int TIME_DIALOG1=1;
    final int TIME_DIALOG2=2;
    final int TIME_DIALOG3=3;
    final int TIME_DIALOG4=4;
    TextView nz_time1;
    TextView nz_time2;
    TextView nz_time3;
    TextView nz_time4;
    TextView time_status1;
    TextView time_status2;
    TextView time_status3;
    TextView time_status4;
    TextView title;
    TextView textview2;
    ImageView back;
    SDSwitchButton sb1;
    SDSwitchButton sb2;
    SDSwitchButton sb3;
    SDSwitchButton sb4;

    int mYear, mMonth, mDay,mHour,mMinutes;
    int newHour,newMinutes;
    private String wuc;
    private String TAG="PatternSettingActivity";
    String[]wuclist;
    BuildBean dialog;
    //需要上传的参数
    private StringBuffer clockModel;//4:40,5:45,
    private StringBuffer postwuc;//0|4:40;0|5:45
    private String deviceid;
    private int interval=0;
    private int type=1;
    private SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_setting);
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
        nz_time1=findViewById(R.id.nz_time1);
        nz_time2=findViewById(R.id.nz_time2);
        nz_time3=findViewById(R.id.nz_time3);
        nz_time4=findViewById(R.id.nz_time4);
        time_status1=findViewById(R.id.time_status1);
        time_status2=findViewById(R.id.time_status2);
        time_status3=findViewById(R.id.time_status3);
        time_status4=findViewById(R.id.time_status4);
        back=findViewById(R.id.back);
        title=findViewById(R.id.title);
        textview2=findViewById(R.id.textview2);
        sb1=findViewById(R.id.sb1);
        sb2=findViewById(R.id.sb2);
        sb3=findViewById(R.id.sb3);
        sb4=findViewById(R.id.sb4);

    }

    private void initData() {
        textview2.setText("保存");
        title.setText("闹钟模式");
        if(wuc.equals("")){
            //说明当前不是这个模式
        }else{
            wuclist=wuc.split(";");
            for(int i=0;i<wuclist.length;i++){
                String[]listsp=wuclist[i].split("\\|");
                switch (i){
                    case 0:
                        nz_time1.setText(listsp[1]);
                        time_status1.setText("闹钟，每天");
                        /**
                         * 设置选中状态
                         *
                         * @param checked        true-选中，false-未选中
                         * @param anim           是否需要动画
                         * @param notifyCallback 是否需要通知回调
                         */
                        sb1.setChecked(true,false,false);

                        break;
                    case 1:
                        nz_time2.setText(listsp[1]);
                        time_status2.setText("闹钟，每天");
                        sb2.setChecked(true,false,false);
                        break;
                    case 2:
                        nz_time3.setText(listsp[1]);
                        time_status3.setText("闹钟，每天");
                        sb3.setChecked(true,false,false);
                        break;
                    case 3:
                        nz_time4.setText(listsp[1]);
                        time_status4.setText("闹钟，每天");
                        sb4.setChecked(true,false,false);
                        break;
                }
            }
        }
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sb1.setOnCheckedChangedCallback(new ISDSwitchButton.OnCheckedChangedCallback() {
            @Override
            public void onCheckedChanged(boolean checked, SDSwitchButton view) {
                if(checked){
                    time_status1.setText("闹钟，每天");
                }else{
                    time_status1.setText("未设置时间");
                }
            }
        });
        sb2.setOnCheckedChangedCallback(new ISDSwitchButton.OnCheckedChangedCallback() {
            @Override
            public void onCheckedChanged(boolean checked, SDSwitchButton view) {
                if(checked){
                    time_status2.setText("闹钟，每天");
                }else{
                    time_status2.setText("未设置时间");
                }
            }
        });
        sb3.setOnCheckedChangedCallback(new ISDSwitchButton.OnCheckedChangedCallback() {
            @Override
            public void onCheckedChanged(boolean checked, SDSwitchButton view) {
                if(checked){
                    time_status3.setText("闹钟，每天");
                }else{
                    time_status3.setText("未设置时间");
                }
            }
        });
        sb4.setOnCheckedChangedCallback(new ISDSwitchButton.OnCheckedChangedCallback() {
            @Override
            public void onCheckedChanged(boolean checked, SDSwitchButton view) {
                if(checked){
                    time_status4.setText("闹钟，每天");
                }else{
                    time_status4.setText("未设置时间");
                }
            }
        });

        textview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Constant.isNetworkConnected(PatternSettingActivity.this)) {
                    //判断网络是否可用
                    Toast.makeText(PatternSettingActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                }else{
                    clockModel=new StringBuffer();
                    postwuc=new StringBuffer();
                    if(sb1.isChecked()||sb2.isChecked()||sb3.isChecked()||sb4.isChecked()){
                        //正常情况
                        //选择框被选中，且时间设置
                        if(sb1.isChecked()){
                            postwuc.append(0+"|"+nz_time1.getText().toString()+";");
                            clockModel.append(nz_time1.getText().toString()+",");
                        }
                        if(sb2.isChecked()){
                            postwuc.append(1+"|"+nz_time2.getText().toString()+";");
                            clockModel.append(nz_time2.getText().toString()+",");
                        }
                        if(sb3.isChecked()){
                            postwuc.append(2+"|"+nz_time3.getText().toString()+";");
                            clockModel.append(nz_time3.getText().toString()+",");
                        }
                        if(sb4.isChecked()){
                            postwuc.append(3+"|"+nz_time4.getText().toString()+";");
                            clockModel.append(nz_time4.getText().toString()+",");
                        }
                        if(checkrepeat(clockModel)){
                            //有重复的
                            Toast.makeText(PatternSettingActivity.this,"不能选择相同的时间",Toast.LENGTH_SHORT).show();
                        }else{
                            RequestParams params1=new RequestParams();
                            //因为传递的是json数据，所以需要设置header和body
                            params1.addHeader("Content-Type","application/json");
                            JSONObject jsonObject1=new JSONObject();
                            jsonObject1.put("clockModel",clockModel.toString().substring(0,clockModel.toString().length()-1));
                            jsonObject1.put("type",type);
                            jsonObject1.put("interval",interval);
                            jsonObject1.put("wuc",postwuc.toString());
                            jsonObject1.put("deviceid",deviceid);
                            params1.setRequestBody(MediaType.parse("application/json"),jsonObject1.toString());
                            HttpRequest.post(Api.saveOrUpdateDeviceSetting+"?token="+sp.getString(Constant.Token,""),params1,new JsonHttpRequestCallback(){
                                @Override
                                protected void onSuccess(Headers headers, JSONObject jsonObject) {
                                    super.onSuccess(headers, jsonObject);
                                    Log.i(TAG, jsonObject.toString());
                                    if(jsonObject.getBoolean("success")){
                                        Toast.makeText(PatternSettingActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK);
                                        finish();
                                    }else{
                                        Toast.makeText(PatternSettingActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dialog.dismiss();
                                }

                                @Override
                                public void onStart() {
                                    super.onStart();
                                    dialog= DialogUIUtils.showLoading(PatternSettingActivity.this,"加载中...",true,false,false,true);
                                    dialog.show();
                                }

                                @Override
                                public void onFinish() {
                                    super.onFinish();

                                }
                            });
                        }



                    }else{
                        //如果都不选中
                        Toast.makeText(PatternSettingActivity.this,"请至少选择一组并设置时间",Toast.LENGTH_SHORT).show();

                    }
                }



            }
        });

    }
//判断是否有重复的
    private boolean checkrepeat(StringBuffer clockModel) {
        String []clockModellist=clockModel.toString().split(",");
        for(int i=0;i<clockModellist.length;i++){
            for(int j=i+1;j<clockModellist.length;j++){
                if(clockModellist[i].equals(clockModellist[j])){
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case TIME_DIALOG1:
//                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
                return new TimePickerDialog(this,mtimeListener1,newHour,newMinutes,true);
            case TIME_DIALOG2:
//                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
                return new TimePickerDialog(this,mtimeListener2,newHour,newMinutes,true);
            case TIME_DIALOG3:
//                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
                return new TimePickerDialog(this,mtimeListener3,newHour,newMinutes,true);
            case TIME_DIALOG4:
//                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
                return new TimePickerDialog(this,mtimeListener4,newHour,newMinutes,true);
        }

        return null;
    }
    private TimePickerDialog.OnTimeSetListener mtimeListener1=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mYear = hourOfDay;
            mMonth = minute;
            Log.i(TAG, "onTimeSet: "+mYear+"/"+mMonth);
            display(1,hourOfDay,minute);
        }
    };
    private TimePickerDialog.OnTimeSetListener mtimeListener2=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mYear = hourOfDay;
            mMonth = minute;
            Log.i(TAG, "onTimeSet: "+mYear+"/"+mMonth);

            display(2,hourOfDay,minute);
        }
    };
    private TimePickerDialog.OnTimeSetListener mtimeListener3=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mYear = hourOfDay;
            mMonth = minute;
            Log.i(TAG, "onTimeSet: "+mYear+"/"+mMonth);
            display(3,hourOfDay,minute);
        }
    };
    private TimePickerDialog.OnTimeSetListener mtimeListener4=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mYear = hourOfDay;
            mMonth = minute;
            Log.i(TAG, "onTimeSet: "+mYear+"/"+mMonth);
            display(4,hourOfDay,minute);
        }
    };
    private void display(int i, int hourOfDay, int minute) {
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
        switch (i){
            case 1:
                //第一条时间
                nz_time1.setText(hour+":"+minu);
                time_status1.setText("闹钟，每天");
                break;
            case 2:
                //第二条时间
                nz_time2.setText(hour+":"+minu);
                time_status2.setText("闹钟，每天");
                break;
            case 3:
                //第三条时间
                nz_time3.setText(hour+":"+minu);
                time_status3.setText("闹钟，每天");
                break;
            case 4:
                //第四条时间
                nz_time4.setText(hour+":"+minu);
                time_status4.setText("闹钟，每天");
                break;
        }
    }
    public void nztime(View view){
        switch (view.getId()){
            case R.id.nz_time1:
                showDialog(TIME_DIALOG1);
                break;
            case R.id.nz_time2:
                showDialog(TIME_DIALOG2);
                break;
            case R.id.nz_time3:
                showDialog(TIME_DIALOG3);
                break;
            case R.id.nz_time4:
                showDialog(TIME_DIALOG4);
                break;
        }
    }
}
