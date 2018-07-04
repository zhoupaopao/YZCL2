package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.yzcl.R;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by Lenovo on 2018/7/4.
 */
//模式设置页面，三大模式，闹钟，定时，星期
public class PatternSettingActivity extends Activity {
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
    int mYear, mMonth, mDay,mHour,mMinutes;
    int newHour,newMinutes;
    private String TAG="PatternSettingActivity";
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
        nz_time1=findViewById(R.id.nz_time1);
        nz_time2=findViewById(R.id.nz_time2);
        nz_time3=findViewById(R.id.nz_time3);
        nz_time4=findViewById(R.id.nz_time4);
        time_status1=findViewById(R.id.time_status1);
        time_status2=findViewById(R.id.time_status2);
        time_status3=findViewById(R.id.time_status3);
        time_status4=findViewById(R.id.time_status4);
        back=findViewById(R.id.back);
        textview2=findViewById(R.id.textview2);

    }

    private void initData() {
        textview2.setText("保存");
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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
