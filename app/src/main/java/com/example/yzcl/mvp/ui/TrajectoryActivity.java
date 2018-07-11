package com.example.yzcl.mvp.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.TrajectoryBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/7/10.
 */
//轨迹回放
public class TrajectoryActivity extends BaseActivity {
    private ProgressBar progressBarHorizontal;//进度条
    private ImageView play;//播放进度
    private ImageView back;
    private BuildBean dialog;
    private Marker mMarkerA;
    private TextView car_vin;//设备名和设置状态
    private TextView car_name;//借款人
    private TextView car_detail;//无效隐藏
    private String device_id;//设备名
    private TextView stop_num;//停留时间
    private TextView mileage;//里程数
    private LinearLayout show_time;//弹出时间框
    private TextView choose_time;//选择时间后显示
    private TextView all_number;//总的公里数和好
    private TextView now_number;//当前公里数
    int mYear, mMonth, mDay,mHour,mMinutes;
    final int DATE_DIALOG=1;
    private String TAG="TrajectoryActivity";
    private SharedPreferences sp;
    private TrajectoryBean trajectoryBean;
    private double allmileage=0;//总里程
    List<LatLng> points;
    MapView mapview;
    private BaiduMap mBaiduMap;
    // 普通折线，点击时改变宽度
    private ArrayList<HashMap<String,Object>>list_tlmark;//用于存放停留点的数据
    Polyline mPolyline;
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
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        back=findViewById(R.id.back);
        car_vin=findViewById(R.id.car_vin);
        car_detail=findViewById(R.id.car_detail);
        car_name=findViewById(R.id.car_name);
        car_detail.setVisibility(View.INVISIBLE);
        choose_time=findViewById(R.id.choose_time);
        show_time=findViewById(R.id.show_time);
        mileage=findViewById(R.id.mileage);
        stop_num=findViewById(R.id.stop_num);
        progressBarHorizontal=findViewById(R.id.progressBarHorizontal);
        all_number=findViewById(R.id.all_number);
        now_number=findViewById(R.id.now_number);
        play=findViewById(R.id.play);
        mapview=findViewById(R.id.mapview);
        mBaiduMap=mapview.getMap();
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
        display();
        //获取轨迹信息
        achieveGj();
//        progressBarHorizontal.setMax(1200);
//        progressBarHorizontal.setProgress(10);
    }

    private void achieveGj() {
        RequestParams params1=new RequestParams();
        //因为传递的是json数据，所以需要设置header和body
        params1.addHeader("Content-Type","application/json");
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("endTime",choose_time.getText().toString()+" "+"23:59:59");
        jsonObject1.put("startTime",choose_time.getText().toString()+" "+"00:00:00");
        jsonObject1.put("onLineTrun",1);
        jsonObject1.put("imei",device_id);
        params1.setRequestBody(MediaType.parse("application/json"),jsonObject1.toString());
        HttpRequest.post(Api.getTrack+"?token="+sp.getString(Constant.Token,""),params1,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                trajectoryBean=JSONObject.parseObject(jsonObject.toString(),TrajectoryBean.class);
                if(trajectoryBean.isSuccess()){
                    points = new ArrayList<LatLng>();
                    list_tlmark=new ArrayList<>();
                    //h是 时间    i  是速度
                    //o是  lat     n  是 lng        s 是里程
                    float nowlat=0;
                    float nowlon=0;
                    long nowtime=0;
                    int tlcs=0;
                    Toast.makeText(TrajectoryActivity.this,"请求成功",Toast.LENGTH_SHORT).show();
                    for(int i=0;i<trajectoryBean.getList().size();i++){
                        allmileage=allmileage+trajectoryBean.getList().get(i).getS();
//                        String nowtime=gshtime(trajectoryBean.getList().get(i).getH());
                        points.add(new LatLng(trajectoryBean.getList().get(i).getO(),trajectoryBean.getList().get(i).getN()));
                        //比较时间，判断停留
                        if(i==0){
                            nowtime=trajectoryBean.getList().get(i).getH();
                        }
                        if(trajectoryBean.getList().get(i).getH()-nowtime>1800000){
                            //时间差大于30分钟
                            HashMap<String,Object>hashMap=new HashMap<>();
                            hashMap.put("lat",trajectoryBean.getList().get(i).getO());
                            hashMap.put("lng",trajectoryBean.getList().get(i).getN());
                            //停留时长
                            hashMap.put("tlsc",timeduring(nowtime,trajectoryBean.getList().get(i).getH()));
                            list_tlmark.add(hashMap);
                            //显示mark
                            MarkerOptions ooA;
                            ooA = new MarkerOptions().position(new LatLng(trajectoryBean.getList().get(i).getO(),trajectoryBean.getList().get(i).getN())).icon(getBitmapDescriptor())
                                    .zIndex(9).draggable(true);
                            mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
                            //将列表索引放在title里面
                            mMarkerA.setTitle(""+tlcs);
                            tlcs=tlcs+1;
                        }
                        nowtime=trajectoryBean.getList().get(i).getH();
//                        Log.i(TAG, nowtime);
//                        Log.i(TAG, trajectoryBean.getList().get(i).getO()+"");

                    }
                    all_number.setText("/"+m2(allmileage/1000)+"Km");
                    mileage.setText(m2(allmileage/1000)+"Km");
                    OverlayOptions ooPolyline = new PolylineOptions().width(10)
                            .color(0xAAFF0000).points(points);
                    mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
                    Log.i(TAG, allmileage+"");
                    stop_num.setText(tlcs+"次");

                }else{
                    Toast.makeText(TrajectoryActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                }
                dialog.dialog.dismiss();
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(TrajectoryActivity.this,"加载中...",true,false,false,true);
                dialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }
    //类似于将一个布局变成图片
    public BitmapDescriptor getBitmapDescriptor() {
        BitmapDescriptor bttmap = null;
        View item_view = LayoutInflater.from(this).inflate(R.layout.marker_stop,null);
        bttmap = BitmapDescriptorFactory.fromView(item_view);
        return bttmap;

    }
    //计算时间差
    public String timeduring(long starttime,long endtime){
        long time_during=endtime-starttime;
        long hours=time_during/(1000*60*60);//获取间隔小时
        long mintues=time_during-(hours*(1000*60*60));//获取间隔分钟
        mintues=mintues/(60*1000);
        long days=hours/24;
        hours=hours-days*24;
        if(days==0&&hours==0){
            //天和小时都是0
            return  mintues+"分钟";
        }else if(days==0){
            return  hours+"小时"+mintues+"分钟";
        }else{
            return days+"天"+hours+"小时"+mintues+"分钟";
        }

    }
    //保留小数点后两位
    public String m2(double f) {
        DecimalFormat df = new DecimalFormat("#.00");
        return  df.format(f);
    }
    //时间格式化
    public String gshtime(long time){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new Date(time);
        String str = sdf.format(date);
        return  str;//2017-09-15 13:18:44:672
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
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击播放
//                mBaiduMap

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

    //判断当前点是否在可视范围内
    private void isinmap(){
        Point pt= mBaiduMap.getMapStatus().targetScreen;

        Point point= mBaiduMap.getProjection().toScreenLocation(points.get(0));
        if(point.x < 0 || point.x > pt.x*2 || point.y < 0 || point.y > pt.y*2)
        {
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(points.get(0)));
        }
    }
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
