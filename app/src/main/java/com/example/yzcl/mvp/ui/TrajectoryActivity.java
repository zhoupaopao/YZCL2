package com.example.yzcl.mvp.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.TrajectoryBean;
import com.example.yzcl.mvp.model.bean.carDetailGPSBeans;
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
import java.util.Timer;
import java.util.TimerTask;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/7/10.
 */
//轨迹回放
public class TrajectoryActivity extends BaseActivity implements OnGetGeoCoderResultListener{
    private ProgressBar progressBarHorizontal;//进度条
    private ImageView play;//播放进度
    private ImageView back;
    private BuildBean dialog;
    private Marker mMarkerA;
    private Marker mMarkerB;
    private InfoWindow mInfoWindow;// 点击覆盖物显示的窗口
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
    private TextView current_time;//播放的时间
    int mYear, mMonth, mDay,mHour,mMinutes;
    final int DATE_DIALOG=1;
    private String TAG="TrajectoryActivity";
    private SharedPreferences sp;
    private TrajectoryBean trajectoryBean;
    private double allmileage=0;//总里程
    List<LatLng> points;
    List<HashMap<String,String>>time_distance;//存放当前点的时间和距离

    private long mExitTime=0;//记录时间用，防止dialog连续弹出两次
    MapView mapview;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private BaiduMap mBaiduMap;
    // 普通折线，点击时改变宽度
    private ArrayList<HashMap<String,Object>>list_tlmark;//用于存放停留点的数据
    Polyline mPolyline;
    private String jx_stop_address;//解析的地址信息
    private ArrayList<String>stop_address_list;
    private int playStatus=0;//点击0播放，点击1暂停
    BitmapDescriptor bdB = BitmapDescriptorFactory
            .fromResource(R.mipmap.interestpoint_spot);
    BitmapDescriptor bdstart = BitmapDescriptorFactory
            .fromResource(R.mipmap.img_start);
    BitmapDescriptor bdend = BitmapDescriptorFactory
            .fromResource(R.mipmap.img_end);
    Timer timer;
    //弹出框显示
    LatLng ll;
    //停留时长
    String tl_starttime;
    String tl_endtime;
    String tlsc;
    private int playnum=0;//当前播放进度
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
        current_time=findViewById(R.id.current_time);
        play=findViewById(R.id.play);
        mapview=findViewById(R.id.mapview);
        mBaiduMap=mapview.getMap();
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
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
        display(0);
        //获取轨迹信息

        if(!Constant.isNetworkConnected(TrajectoryActivity.this)) {
            //判断网络是否可用
            Toast.makeText(TrajectoryActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
        }else{
            achieveGj();
        }
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i(TAG, "onMarkerClick: "+marker.getTitle());
                if(marker.getTitle()!=null){
                    showInfowindow(Integer.parseInt(marker.getTitle()));
                }

                return true;
            }
        });
//        progressBarHorizontal.setMax(1200);
//        progressBarHorizontal.setProgress(10);
    }

    //展示mark弹出框
    public void showInfowindow(int index){
//        showstatus.setVisibility(View.INVISIBLE);
        mBaiduMap.hideInfoWindow();// 隐藏infowindow
        mInfoWindow = null;
        System.out.println("------------>showinfowindow");
        HashMap<String,Object>hashMap=list_tlmark.get(index);
        //当前的坐标点
        ll = new LatLng((double)hashMap.get("lat"),(double)hashMap.get("lng"));
        //停留时长
        tl_starttime= gshtime((Long) hashMap.get("tl_starttime")) ;
        tl_endtime= gshtime((Long) hashMap.get("tl_endtime"));
       tlsc=timeduring((Long) hashMap.get("tl_starttime"),(Long) hashMap.get("tl_endtime"));
        //解析该经纬度的地址信息
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll).newVersion(0));

        MapStatusUpdate mu = MapStatusUpdateFactory.newLatLngZoom(ll, 13); // 设置地图中心点以及缩放级别
        mBaiduMap.animateMapStatus(mu);
        //先获取地址信息后显示
//        secshowinfo();

    }

    private void secshowinfo() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View linear = inflater.inflate(R.layout.layout_marker_stop, null);
        TextView marker_stopstarttime=linear.findViewById(R.id.marker_stopstarttime);
        TextView marker_stopendtime=linear.findViewById(R.id.marker_stopendtime);
        TextView marker_stoptime=linear.findViewById(R.id.marker_stoptime);
        TextView marker_address=linear.findViewById(R.id.marker_address);
        marker_stopstarttime.setText("停留开始时间："+tl_starttime);
        marker_stopendtime.setText("停留结束时间："+tl_endtime);
        marker_stoptime.setText("停留时长："+tlsc);
        marker_address.setText("地址："+jx_stop_address);
        mInfoWindow = new InfoWindow(linear, ll, -40);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    private void achieveGj() {
        RequestParams params1=new RequestParams();
        //因为传递的是json数据，所以需要设置header和body
        params1.addHeader("Content-Type","application/json");
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("endTime",choose_time.getText().toString()+" "+"23:59:59");
        jsonObject1.put("startTime",choose_time.getText().toString()+" "+"00:00:00");
//        jsonObject1.put("endTime","2018-7-11"+" "+"23:59:59");
//        jsonObject1.put("startTime","2018-7-11"+" "+"00:00:00");
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
                    time_distance=new ArrayList<>();
                    list_tlmark=new ArrayList<>();
                    stop_address_list=new ArrayList<>();
                    //h是 时间    i  是速度
                    //o是  lat     n  是 lng        s 是里程
                    allmileage=0;
                    float nowlat=0;
                    float nowlon=0;
                    long nowtime=0;
                    int tlcs=0;
//                    Toast.makeText(TrajectoryActivity.this,"请求成功",Toast.LENGTH_SHORT).show();
//                    dialog.dialog.dismiss();
                    for(int i=0;i<trajectoryBean.getList().size();i++){
                        allmileage=allmileage+trajectoryBean.getList().get(i).getS();
//                        String nowtime=gshtime(trajectoryBean.getList().get(i).getH());
                        points.add(new LatLng(trajectoryBean.getList().get(i).getO(),trajectoryBean.getList().get(i).getN()));
                        HashMap<String,String>ti_dis=new HashMap<>();
                        ti_dis.put("time",gshtime(trajectoryBean.getList().get(i).getH()));
                        ti_dis.put("distance",allmileage+"");
                        time_distance.add(ti_dis);
                        //比较时间，判断停留
                        if(i==0){
                            nowtime=trajectoryBean.getList().get(i).getH();
                        }
                        if(trajectoryBean.getList().get(i).getH()-nowtime>1800000){
                            //时间差大于30分钟
                            HashMap<String,Object>hashMap=new HashMap<>();
                            hashMap.put("lat",trajectoryBean.getList().get(i).getO());
                            hashMap.put("lng",trajectoryBean.getList().get(i).getN());
                            //停留开始时间
                            hashMap.put("tl_starttime",nowtime);
                            //停留结束时间
                            hashMap.put("tl_endtime",trajectoryBean.getList().get(i).getH());

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
                    //如果点少于2个的话不能划线
                    if(points.size()<2){
                        Toast.makeText(TrajectoryActivity.this, "暂无轨迹", Toast.LENGTH_SHORT).show();
                        dialog.dialog.dismiss();
                    }else{
                        //划线和添加起始点和终止点
                        MarkerOptions ooC;
                        MarkerOptions ooD;
                        ooC = new MarkerOptions().position(points.get(0)).icon(bdstart)
                                .zIndex(9).draggable(true);
                        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooC));
                        ooD = new MarkerOptions().position(points.get(points.size()-1)).icon(bdend)
                                .zIndex(9).draggable(true);
                        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooD));
                        progressBarHorizontal.setMax(points.size());
                        progressBarHorizontal.setProgress(0);
                        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                                .color(0xAAFF0000).points(points);
                        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
                        Log.i(TAG, allmileage+"");
                        stop_num.setText(tlcs+"次");
                        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(points.get(0)));
                        dialog.dialog.dismiss();
                    }


                }else{
                    Toast.makeText(TrajectoryActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    dialog.dialog.dismiss();
                }

            }

            @Override
            public void onStart() {
                super.onStart();
                Log.i(TAG, "onStart: ");
                if((System.currentTimeMillis()-mExitTime>1000)){ //如果两次按键时间间隔大于2000毫秒，则不退出
                    dialog= DialogUIUtils.showLoading(TrajectoryActivity.this,"加载中...",true,true,false,true);
                    dialog.show();
                }else{
                }
                mExitTime = System.currentTimeMillis();// 更新mExitTime

            }

            @Override
            public void onFinish() {
                super.onFinish();

//                dialog.dialog.cancel();
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
                if(playStatus==0){
                    //播放
                    play.setImageResource(R.mipmap.pause);
                    playStatus=1;
                    //设置不可点击
//                    play.setClickable(false);
                    //从头开始播放
                    //画点
//                    for(int i=0;i<points.size();i++){
//                        LatLng ll = mMarkerA.getPosition();
                        if(points.size()<2){
                            //如果点数少于2 ，不能播放
                            Toast.makeText(TrajectoryActivity.this,"暂无轨迹",Toast.LENGTH_SHORT).show();
                            play.setImageResource(R.mipmap.shape);
                            playStatus=0;
                            play.setClickable(true);
                        }else{
                            timer=new Timer();
                            timer.schedule(new TimerTask()
                            {
                                public void run()
                                {
                                    Message mm=new Message();
                                    mm.what=0;
                                    mm.arg1= playnum;
                                    handler.sendMessage(mm);

                                }
                            },0,500);
                        }
                }else{
                    timer.cancel();
                    play.setImageResource(R.mipmap.shape);
                    playStatus=0;
                }
            }
        });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0){
                if(msg.arg1>points.size()-1){
                    //播放结束结束，不能大于数组的上线
                    timer.cancel();
                    playnum=0;
                    play.setClickable(true);
                    play.setImageResource(R.mipmap.shape);
                    playStatus=0;
                }else{
                    if(msg.arg1==0){
                        MarkerOptions markerOptions;
                        markerOptions = new MarkerOptions().position(points.get(msg.arg1)).icon(bdB)
                                .zIndex(9).draggable(true);
                        mMarkerB = (Marker) (mBaiduMap.addOverlay(markerOptions));

                    }else{
                        LatLng llNew =points.get(msg.arg1);
                        mMarkerB.setPosition(llNew);
                    }
                    isinmap(points.get(msg.arg1));//判断当前点是否在可视区域
                    progressBarHorizontal.setProgress(playnum+1);
                    current_time.setText(time_distance.get(playnum).get("time"));

                    now_number.setText(m2(Double.parseDouble(time_distance.get(playnum).get("distance"))/1000)+"Km");
                    playnum=playnum+1;
                }

            }
            super.handleMessage(msg);
        }
    };
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
            display(1);
        }


    };

    //判断当前点是否在可视范围内
    private void isinmap(LatLng pppoint){
        Point pt= mBaiduMap.getMapStatus().targetScreen;

        Point point= mBaiduMap.getProjection().toScreenLocation(pppoint);
        if(point.x < 0 || point.x > pt.x*2 || point.y < 0 || point.y > pt.y*2)
        {
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(pppoint));
        }
    }
    private void display(int sstatus) {
        //用sstatus判断是初始化0，还是改时间1
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
        //需要请求轨迹数据
        //取消定时
            if(sstatus==0){

            }else{
                if(timer!=null){
                    timer.cancel();
                }
                playnum=0;
                play.setClickable(true);
                play.setImageResource(R.mipmap.shape);
                playStatus=0;
                mBaiduMap.clear();
                if(!Constant.isNetworkConnected(TrajectoryActivity.this)) {
                    //判断网络是否可用
                    Toast.makeText(TrajectoryActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                }else{
                    achieveGj();
                }
            }

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(TrajectoryActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        jx_stop_address=result.getAddress();
//        stop_address_list.add(result.getAddress());
        secshowinfo();

    }
    @Override
    protected void onResume() {
        mapview.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mapview.onDestroy();
        mSearch.destroy();
        if(timer!=null){
            timer.cancel();
        }

        super.onDestroy();
    }
}
