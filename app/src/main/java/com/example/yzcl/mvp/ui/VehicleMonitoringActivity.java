package com.example.yzcl.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
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
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CarMonSearchListBean;
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
    private TextView heead;
//    private ListView listview;
    private XRefreshView xrefreshview;
    private RecyclerView carlist;
    private ArrayList<String> list;
    CarDeviceListAdapter adapter;
    int width;
    int height;
    private SharedPreferences sp;
    private String TAG="VehicleMonitoringActivity";
    private ArrayList<CarMonSearchListBean.CarSearchBean>carSearchBeans=new ArrayList<>();
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
        list=new ArrayList<>();
    }

    private void initData() {
        if(!Constant.isNetworkConnected(VehicleMonitoringActivity.this)) {
            //判断网络是否可用
            Toast.makeText(VehicleMonitoringActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
        }else{
//            queVehicleListForSea();
        }
        //获取屏幕宽高
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
         width = wm.getDefaultDisplay().getWidth();
         height = wm.getDefaultDisplay().getHeight();
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
                XRefreshView linearLayout = (XRefreshView) findViewById(R.id.xrefreshview);//找到xml上的控件
//                LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.linear3);//找到xml上的控件
                ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
                params.height = height-700;
                linearLayout.setLayoutParams(params);
            }
        });

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
//                    dialog.dialog.dismiss();
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
    public void queVehicleListForSea() {
        //展示搜索列表


        RequestParams params=new RequestParams();
//        params.setRequestBodyString();
//        params.setRequestBody();
        //因为传递的是json数据，所以需要设置header和body
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
//        params.addFormDataPart("token",sp.getString(Constant.Token,""));
//        params.addFormDataPart("search",et_search.getText().toString().trim());

        HttpRequest.post(Api.queVehicleListForSea+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                CarMonSearchListBean carMonSearchListBean=JSONObject.parseObject(jsonObject.toString(),CarMonSearchListBean.class);
                if(carMonSearchListBean.isSuccess()){
                    carSearchBeans=carMonSearchListBean.getList();

                    if(carSearchBeans.size()==0){
                        Toast.makeText(VehicleMonitoringActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(VehicleMonitoringActivity.this,carMonSearchListBean.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                DialogUIUtils.showToast("网络错误");
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
