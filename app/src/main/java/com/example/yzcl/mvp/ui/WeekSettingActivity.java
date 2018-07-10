package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

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
    int newHour,newMinutes;
    final int TIME_DIALOG1=1;
    private EditText nz_time1;
    //保存
    private TextView textview2;
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
                //保存
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
