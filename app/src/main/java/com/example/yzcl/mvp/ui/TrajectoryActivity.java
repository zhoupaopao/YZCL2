package com.example.yzcl.mvp.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Created by Lenovo on 2018/7/10.
 */
//轨迹回放
public class TrajectoryActivity extends BaseActivity {
    private ImageView back;
    private TextView car_vin;//设备名和设置状态
    private TextView car_name;//借款人
    private TextView car_detail;//无效隐藏
    private String device_id;//设备名
    private TextView stop_num;//停留时间
    private TextView mileage;//里程数
    private LinearLayout show_time;//弹出时间框
    private TextView choose_time;//选择时间后显示
    int mYear, mMonth, mDay,mHour,mMinutes;
    final int DATE_DIALOG=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajectory);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();

    }

    private void initView() {
        back=findViewById(R.id.back);
        car_vin=findViewById(R.id.car_vin);
        car_detail=findViewById(R.id.car_detail);
        car_name=findViewById(R.id.car_name);
        car_detail.setVisibility(View.INVISIBLE);
        choose_time=findViewById(R.id.choose_time);
        show_time=findViewById(R.id.show_time);
        mileage=findViewById(R.id.mileage);
        stop_num=findViewById(R.id.stop_num);

    }

    private void initData() {
        Intent intent=getIntent();
        car_name.setText(intent.getStringExtra("pledge_name"));
        car_vin.setText(intent.getStringExtra("Internalnum")+"/"+intent.getStringExtra("Category"));
        device_id=intent.getStringExtra("deviceid");
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        mHour = ca.get(Calendar.HOUR_OF_DAY);
        mMinutes=ca.get(Calendar.MINUTE);
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        show_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                DatePickerDialog dpd = new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
                return dpd;
        }
        return super.onCreateDialog(id);
    }
    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month;
            mDay = dayOfMonth;
            display();
        }


    };

    private void display() {
        String hour="";
        String minu="";
        int nmonth=mMonth+1;
        if(nmonth<10){

            hour="0"+nmonth;
        }else{
            hour=""+nmonth;
        }
        if(mDay<10){
            minu="0"+mDay;
        }else{
            minu=""+mDay;
        }
        choose_time.setText(mYear+"-"+hour+"-"+minu);
    }
}
