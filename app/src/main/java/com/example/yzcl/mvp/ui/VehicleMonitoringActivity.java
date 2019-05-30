package com.example.yzcl.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.adapter.CarDeviceListAdapter;
import com.example.yzcl.adapter.VehCarListAdapter;
import com.example.yzcl.adapter.VehCarListUnbindAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CarDetailBeans;
import com.example.yzcl.mvp.model.bean.CarMonSearchListBean;
import com.example.yzcl.mvp.model.bean.DeviceListBean;
import com.example.yzcl.mvp.model.bean.carDetailGPSBeans;
import com.example.yzcl.mvp.presenter.RecyclerItemClickListener;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.example.yzcl.mvp.ui.baseactivity.CheckPermissionsActivity;
import com.example.yzcl.mvp.ui.mvpactivity.HomePage;
import com.example.yzcl.mvp.ui.mvpactivity.MainActivity;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/6/25.
 */

public class VehicleMonitoringActivity extends CheckPermissionsActivity {
    //车辆监控主页面
    //请求定位权限，在5.0以后需要手动获取用户定位权限
    private TextView title;
    private ImageView back;
    private ImageView add;
    private MapView bmapview;
    private RelativeLayout rl_search;
    BaiduMap mBaiduMap;
    Boolean candismis=false;
    private int nowpage=1;
    private int nowstatus=10;//初始全部
    BitmapDescriptor mCurrentMarker;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    boolean isFirstLoc = true; // 是否首次定位
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    private MyLocationData locData;
    private SensorManager mSensorManager;
    private BuildBean dialog;
    private LinearLayout heead;
//    private ListView listview;
    private XRefreshView xrefreshview;
    private RecyclerView carlist;
    private ArrayList<String> list;
    ArrayList<CarDetailBeans.CarDetailBean> carDetailBeanArrayList;
    ArrayList<DeviceListBean.DeviceLLBean> devDetailBeanArrayList;
    VehCarListAdapter adapter;
    VehCarListUnbindAdapter unbind__adapter;
    int width;
    int height;
    boolean headshow=false;
    int initHeigh;
    private SharedPreferences sp;
    private String TAG="VehicleMonitoringActivity";
    private ArrayList<CarMonSearchListBean.CarSearchBean>carSearchBeans=new ArrayList<>();
    private RadioGroup radio_status;
    private TextView tongji;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_monitoring);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
//        dialog= DialogUIUtils.showLoading(VehicleMonitoringActivity.this,"加载中...",true,false,false,true);
//        dialog.show();
        initView();
        initData();
        initListener();

    }

    private void initView() {
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        title=findViewById(R.id.title);
        back=findViewById(R.id.back);
        add=findViewById(R.id.add);
        rl_search=findViewById(R.id.rl_search);
        bmapview=findViewById(R.id.bmap);
        heead=findViewById(R.id.heead);
        carlist=findViewById(R.id.carlist);
        xrefreshview=findViewById(R.id.xrefreshview);
        radio_status=findViewById(R.id.radio_status);
        list=new ArrayList<>();
        carDetailBeanArrayList=new ArrayList<>();
        devDetailBeanArrayList=new ArrayList<>();
        tongji=findViewById(R.id.tongji);
    }

    private void initData() {
        if(!Constant.isNetworkConnected(VehicleMonitoringActivity.this)) {
            //判断网络是否可用
            Toast.makeText(VehicleMonitoringActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
        }else{
            queVehicleListForSea();
        }
        //获取屏幕宽高
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
         width = wm.getDefaultDisplay().getWidth();
         height = wm.getDefaultDisplay().getHeight();
        XRefreshView linearLayout = (XRefreshView) findViewById(R.id.xrefreshview);//找到xml上的控件
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        initHeigh=params.height;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

//        mCurrentMarker = null;
//        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
//                mCurrentMode, true, mCurrentMarker));
//        MapStatus.Builder builder1 = new MapStatus.Builder();
//        builder1.overlook(0);
//        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
        // 地图初始化
        mBaiduMap = bmapview.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    private void initListener() {
//        adapter= new CarDeviceListAdapter(VehicleMonitoringActivity.this,list);
//        listview.setAdapter(adapter);
        title.setText(R.string.car_veh);
        add.setImageResource(R.mipmap.search);
        add.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rl_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(VehicleMonitoringActivity.this,CarMonSearchActivity.class);
                startActivity(intent);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
        heead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //让listview变大
                if(headshow){
                    XRefreshView linearLayout = (XRefreshView) findViewById(R.id.xrefreshview);//找到xml上的控件
                    ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
                    params.height = initHeigh;
                    linearLayout.setLayoutParams(params);
                    headshow=false;
                }else{
                    XRefreshView linearLayout = (XRefreshView) findViewById(R.id.xrefreshview);//找到xml上的控件
                    ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
                    params.height = height-700;
                    linearLayout.setLayoutParams(params);
                    headshow=true;
                }

            }
        });
        radio_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.status_all:
                        nowpage=1;
                        nowstatus=10;
                        queVehicleListForSea();

                        break;
                    case R.id.status_online:
                        nowpage=1;
                        nowstatus=1;
                        queVehicleListForSea();
                        break;
                    case R.id.status_offline:
                        nowpage=1;
                        nowstatus=0;
                        queVehicleListForSea();
                        break;
                    case R.id.status_unuse:
                        nowpage=1;
                        nowstatus=5;//是5的话代表是未绑车设备
                        queVehicleListUnbind();
                        break;
                }
            }
        });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if(candismis){
                        Log.i(TAG, "candismis-true");
                        dialog.dialog.dismiss();
                    }else{
                        Log.i(TAG, "candismis-true");
                        candismis=true;
                    }
                    break;
                case 1:

                    break;
            }
        }
    };
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || bmapview == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    private void loadmore(){
//0是离线1是在线
        RequestParams params=new RequestParams();
//        params.setRequestBodyString();
//        params.setRequestBody();
        //因为传递的是json数据，所以需要设置header和body
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("page",nowpage);
        jsonObject.put("pagesize",10);
        if(nowstatus==10){

        }else{
            jsonObject.put("online_status",nowstatus);
        }

        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queVehicleList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                CarDetailBeans carDetailBeans=JSONObject.parseObject(jsonObject.toString(),CarDetailBeans.class);
                if(carDetailBeans.isSuccess()){

                    carDetailBeanArrayList.addAll(carDetailBeans.getList());

                    xrefreshview.stopLoadMore(true);
                }else{
                    Toast.makeText(VehicleMonitoringActivity.this,carDetailBeans.getMessage(),Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(VehicleMonitoringActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                xrefreshview.stopLoadMore(false);
//                DialogUIUtils.showToast("网络错误");
            }

            @Override
            public void onStart() {
                super.onStart();
//                dialog= DialogUIUtils.showLoading(VehicleMonitoringActivity.this,"加载中...",true,true,false,true);
//                dialog.show();


            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }
    private void loadmore_unbind(){
//展示未绑车列表
        RequestParams params=new RequestParams();
//        params.setRequestBodyString();
//        params.setRequestBody();
        //因为传递的是json数据，所以需要设置header和body
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("page",nowpage);
        jsonObject.put("pagesize",10);
        jsonObject.put("isactive","0");


        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queryDeviceList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                DeviceListBean carDetailBeans=JSONObject.parseObject(jsonObject.toString(),DeviceListBean.class);
                if(carDetailBeans.isSuccess()){
                    devDetailBeanArrayList.addAll(carDetailBeans.getList());

                    xrefreshview.stopLoadMore(true);

                }else{
                    Toast.makeText(VehicleMonitoringActivity.this,carDetailBeans.getMessage(),Toast.LENGTH_SHORT).show();
                    xrefreshview.stopLoadMore(false);
                }

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(VehicleMonitoringActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }
    public void queVehicleListForSea() {
        //展示搜索列表
        RequestParams params=new RequestParams();
//        params.setRequestBodyString();
//        params.setRequestBody();
        //因为传递的是json数据，所以需要设置header和body
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("page",nowpage);
        jsonObject.put("pagesize",10);
        if(nowstatus==10){
            //所有车辆
        }else{
            jsonObject.put("online_status",nowstatus);
        }

        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queVehicleList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                CarDetailBeans carDetailBeans=JSONObject.parseObject(jsonObject.toString(),CarDetailBeans.class);
                if(carDetailBeans.isSuccess()){
                     carDetailBeanArrayList.clear();
                    carDetailBeanArrayList=carDetailBeans.getList();
                    xrefreshview.setPullLoadEnable(true);
                    //设置刷新完成以后，headerview固定的时间
                    xrefreshview.setPinnedTime(1000);
                    xrefreshview.setMoveForHorizontal(true);
                    //是否自动加载更新
                    xrefreshview.setAutoLoadMore(true);
                    xrefreshview.enablePullUpWhenLoadCompleted(true);
//        xRefreshView.setCustomFooterView(new XRefreshViewFooter(this));
                    xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

                        @Override
                        public void onRefresh(boolean isPullDown) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    nowpage=1;
                                    queVehicleListForSea();
                                    xrefreshview.stopRefresh();
                                }
                            }, 500);
                        }

                        @Override
                        public void onLoadMore(boolean isSilence) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    nowpage++;
//                                    loadData(dev_status,nowPages);
                                    loadmore();
                                }
                            }, 500);
                        }
                    });
                    adapter=new VehCarListAdapter(VehicleMonitoringActivity.this,carDetailBeanArrayList);
                    adapter.setCustomLoadMoreView(new XRefreshViewFooter(VehicleMonitoringActivity.this));
                    LinearLayoutManager layoutManager = new LinearLayoutManager(VehicleMonitoringActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    carlist.setLayoutManager(layoutManager);
                    carlist.setAdapter(adapter);
                    //设置增加或删除条目的动画
                    carlist.setItemAnimator( new DefaultItemAnimator());

//                    carlist.setNestedScrollingEnabled(false);
                    carlist.setAdapter(adapter);
                    Log.i("carDetailBeanArrayList", carDetailBeanArrayList.toString());
                    carlist.addOnItemTouchListener(new RecyclerItemClickListener(VehicleMonitoringActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if(!Constant.isNetworkConnected(VehicleMonitoringActivity.this)) {
                                //判断网络是否可用
                                Toast.makeText(VehicleMonitoringActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                            }else if(carDetailBeanArrayList.size()==0){
//                                Toast.makeText(VehicleMonitoringActivity.this, "是设备？", Toast.LENGTH_SHORT).show();
                                Log.i("carDetailBeanArrayList", "是设备？");
                            }else{
                                CarDetailBeans.CarDetailBean carSearchBean= carDetailBeanArrayList.get(position);
                                //点击具体的车辆
                                //请求具体的车辆信息
                                RequestParams params1=new RequestParams();
                                //因为传递的是json数据，所以需要设置header和body
                                params1.addHeader("Content-Type","application/json");
                                JSONObject jsonObject1=new JSONObject();
                                jsonObject1.put("car_id",carSearchBean.getCarid());
//                        jsonObject1.put("pagesize",10);
//                        jsonObject1.put("page",1);
//                            String carid=carSearchBean.getId();
                                Log.i(TAG, Api.queCarDeviceGps+"?token="+sp.getString(Constant.Token,""));
                                params1.setRequestBody(MediaType.parse("application/json"),jsonObject1.toString());
                                HttpRequest.post(Api.queCarDeviceGps+"?token="+sp.getString(Constant.Token,""),params1,new JsonHttpRequestCallback(){
                                    @Override
                                    protected void onSuccess(Headers headers, JSONObject jsonObject) {
                                        super.onSuccess(headers, jsonObject);
                                        Log.i(TAG, jsonObject.toString());
                                        carDetailGPSBeans carDetailGPSBeans=JSONObject.parseObject(jsonObject.toString(),carDetailGPSBeans.class);
                                        if(carDetailGPSBeans.isSuccess()){
                                            //请求成功
                                            //显示点
                                            ArrayList<com.example.yzcl.mvp.model.bean.carDetailGPSBeans.carDetailGPSBean>carDetailGPSBean=carDetailGPSBeans.getList();
                                            Intent intent=new Intent();
                                            intent.setClass(VehicleMonitoringActivity.this,CarAddressActivity.class);
//                                    Bundle b=new Bundle();
//                                    b.put("pose_title", pose_title);
//                                    intent.putExtras(b);
                                            intent.putExtra("carDetailGPS", jsonObject.get("list").toString());
//                                        intent.putExtra("carDetailGPS", carSearchBean.getId());
                                            startActivity(intent);

                                        }else{
                                            Toast.makeText(VehicleMonitoringActivity.this,carDetailGPSBeans.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onStart() {
                                        super.onStart();
                                        dialog= DialogUIUtils.showLoading(VehicleMonitoringActivity.this,"加载中...",true,true,false,true);
                                        dialog.show();
                                    }

                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        dialog.dialog.dismiss();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onLongClick(View view, int posotion) {

                        }
                    }));
                    //统计数量
                    queVehicleListCount();

                }else{
                    Toast.makeText(VehicleMonitoringActivity.this,carDetailBeans.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(VehicleMonitoringActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(VehicleMonitoringActivity.this,"加载中...",true,true,false,true);
                dialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });

    }
    public void queVehicleListUnbind() {
        //展示未绑车列表
        RequestParams params=new RequestParams();
//        params.setRequestBodyString();
//        params.setRequestBody();
        //因为传递的是json数据，所以需要设置header和body
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("page",nowpage);
        jsonObject.put("pagesize",10);
        jsonObject.put("isactive","0");


        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queryDeviceList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                DeviceListBean carDetailBeans=JSONObject.parseObject(jsonObject.toString(),DeviceListBean.class);
                if(carDetailBeans.isSuccess()){
                    carDetailBeanArrayList.clear();
                    devDetailBeanArrayList.clear();
                    devDetailBeanArrayList=carDetailBeans.getList();
                    xrefreshview.setPullLoadEnable(true);
                    //设置刷新完成以后，headerview固定的时间
                    xrefreshview.setPinnedTime(1000);
                    xrefreshview.setMoveForHorizontal(true);
                    //是否自动加载更新
                    xrefreshview.setAutoLoadMore(true);
                    xrefreshview.enablePullUpWhenLoadCompleted(true);
//        xRefreshView.setCustomFooterView(new XRefreshViewFooter(this));
                    xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

                        @Override
                        public void onRefresh(boolean isPullDown) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    nowpage=1;
                                    queVehicleListUnbind();
                                    xrefreshview.stopRefresh();
                                }
                            }, 500);
                        }

                        @Override
                        public void onLoadMore(boolean isSilence) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    nowpage++;
//                                    loadData(dev_status,nowPages);
                                    loadmore_unbind();
                                }
                            }, 500);
                        }
                    });
                    unbind__adapter=new VehCarListUnbindAdapter(VehicleMonitoringActivity.this,devDetailBeanArrayList);
                    unbind__adapter.setCustomLoadMoreView(new XRefreshViewFooter(VehicleMonitoringActivity.this));
                    LinearLayoutManager layoutManager = new LinearLayoutManager(VehicleMonitoringActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    carlist.setLayoutManager(layoutManager);
                    carlist.setAdapter(unbind__adapter);
                    //设置增加或删除条目的动画
                    carlist.setItemAnimator( new DefaultItemAnimator());

//                    carlist.setNestedScrollingEnabled(false);
                    carlist.setAdapter(unbind__adapter);
                    dialog.dialog.dismiss();
                    //统计数量
//                    queVehicleListCount();

                }else{
                    Toast.makeText(VehicleMonitoringActivity.this,carDetailBeans.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(VehicleMonitoringActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(VehicleMonitoringActivity.this,"加载中...",true,true,false,true);
                dialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });

    }
    public void queVehicleListCount(){
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        if(nowstatus==10){

        }else{
            jsonObject.put("online_status",nowstatus);
        }
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queVehicleListCount+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                if(jsonObject.getBoolean("success")){
                    int carnum=jsonObject.getJSONObject("object").getInteger("cnt");
                    Log.i("carnum", carnum+"");
                    tongji.setText("共"+carnum+"台车");
                }else{
                    Toast.makeText(VehicleMonitoringActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                }
                if(candismis){
                    dialog.dialog.dismiss();
                    Log.i(TAG, "candismis-true");
                }else{
                    candismis=true;
                    Log.i(TAG, "candismis-false");
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(VehicleMonitoringActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bmapview.onDestroy();
        mLocClient.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bmapview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bmapview.onResume();
    }
}
