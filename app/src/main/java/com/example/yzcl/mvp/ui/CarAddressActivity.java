package com.example.yzcl.mvp.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIListener;
import com.example.yzcl.R;
import com.example.yzcl.adapter.DeviceListAdapter;
import com.example.yzcl.adapter.DeviceMsPagerAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CarDeviceBean;
import com.example.yzcl.mvp.model.bean.CarMessageBean;
import com.example.yzcl.mvp.model.bean.carDetailGPSBeans;
import com.example.yzcl.mvp.presenter.AnimationUtil;
import com.example.yzcl.mvp.ui.Fragment.DeviceMessageFragment;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.example.yzcl.mvp.ui.mvpactivity.MainActivity;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/6/28.
 */

public class CarAddressActivity extends BaseActivity {
    private TextView car_name;
    private TextView car_vin;
    private TextView car_detail;
    private ImageView back;
    Intent intent;
    ViewPager viewPager;
//    RelativeLayout rll;
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private int currentPosition = 0;
    String carlist;
    String sign_status;
    String danger_type;
    int d_type;//报警类型
    Boolean isfirstdialog=true;//地图无定位，第一次提示
    private carDetailGPSBeans.carDetailGPSBean carDetailGPSBean;
    private JSONArray arraycar;
    private JSONArray newjsArray;
    private String TAG="CarAddressActivity";
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private InfoWindow mInfoWindow;// 点击覆盖物显示的窗口
    private double maxlat=0;
    private double maxlon=0;
    private double minlat=0;
    private double minlon=0;
    private boolean onlyone=false;//是否只有一个设备
    ArrayList<Fragment>fs;
    private PopupWindow popupWindow;// popupwindow
    private String carId="";//车辆id
    private CarMessageBean carMessageBean;
    private CarDeviceBean carDeviceBean;
    private SharedPreferences sp;
    private View zzc;
    private ArrayList<carDetailGPSBeans.carDetailGPSBean>datalist=new ArrayList<>();
    private ArrayList<carDetailGPSBeans.carDetailGPSBean>datallist=new ArrayList<>();
    private LinearLayout singler;
    private RelativeLayout question;
    private int nowwarn=1;
    private ImageView refresh;
    private ImageView loc;
    LocationClient mLocClient;
    boolean isFirstLoc = true; // 是否首次定位
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    private MyLocationData locData;
    private BuildBean dialog;
//    private String carid="";
    public MyLocationListenner myListener = new MyLocationListenner();
    private String nowdeviceid="";//当前显示的设备的id
    private Boolean needrun=false;
    private Boolean isfirst=true;//判断是否第一次进入页面，以防刚进页面定位第一个点
//    private RelativeLayout rl;

    LatLngBounds bounds = new LatLngBounds.Builder().build();
    // 初始化全局 bitmap 信息，不用时及时 recycle
//    BitmapDescriptor bdA = BitmapDescriptorFactory
//            .fromResource(R.mipmap.mark1);
//    BitmapDescriptor bdB = BitmapDescriptorFactory
//            .fromResource(R.mipmap.mark2);
//    BitmapDescriptor bdC = BitmapDescriptorFactory
//            .fromResource(R.mipmap.mark3);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_address);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        intent=getIntent();
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        car_name=findViewById(R.id.car_name);
        refresh=findViewById(R.id.refresh);
        loc=findViewById(R.id.loc);
        car_vin=findViewById(R.id.car_vin);
        car_detail=findViewById(R.id.car_detail);
        back=findViewById(R.id.back);
//        rl=findViewById(R.id.rl);
        mapView=findViewById(R.id.mapview);
        viewPager=findViewById(R.id.fl_vp);
        mBaiduMap=mapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);
//        rll=findViewById(R.id.rll);
        zzc=findViewById(R.id.zzc);
        question=findViewById(R.id.question);
        singler=findViewById(R.id.singler);
        mLocClient = new LocationClient(getApplicationContext());

    }

    private void initOverlay() {
        maxlat=0;
        minlat=0;
        maxlon=0;
        minlon=0;
        for(int i=0;i<arraycar.size();i++){
            JSONObject jsonObject=arraycar.getJSONObject(i);
            carDetailGPSBeans.carDetailGPSBean carDetailBean=JSONObject.parseObject(jsonObject.toString(),carDetailGPSBeans.carDetailGPSBean.class);
            datallist.add(carDetailBean);
            //给list赋值，用于显示viewpager里面的信息
            if(carDetailBean.getDgm().getBlng()!=null){
                datalist.add(carDetailBean);
//                Log.i(TAG, Double.parseDouble(carDetailBean.getDgm().getBlat())+","+Double.parseDouble(carDetailBean.getDgm().getBlng()));
                LatLng markerll = new LatLng(Double.parseDouble(carDetailBean.getDgm().getBlat()),Double.parseDouble( carDetailBean.getDgm().getBlng()));
                //计算最大最小经纬度
                if(maxlat==0){
                    //一个经纬度都没有，就直接赋值
                    maxlat=Double.parseDouble(carDetailBean.getDgm().getBlat());
                    minlat=Double.parseDouble(carDetailBean.getDgm().getBlat());
                    Log.i(TAG, "maxlat: "+maxlat);
                    Log.i(TAG, "minlat: "+minlat);
                }else if(Double.parseDouble(carDetailBean.getDgm().getBlat())>maxlat){
                    //有经纬度后，判断大小
                    maxlat=Double.parseDouble(carDetailBean.getDgm().getBlat());
                    Log.i(TAG, "maxlat: "+maxlat);
                }else if(Double.parseDouble(carDetailBean.getDgm().getBlat())<minlat){
                    minlat=Double.parseDouble(carDetailBean.getDgm().getBlat());
                    Log.i(TAG, "minlat: "+minlat);
                }
                if(maxlon==0){
                    maxlon=Double.parseDouble(carDetailBean.getDgm().getBlng());
                    minlon=Double.parseDouble(carDetailBean.getDgm().getBlng());
                    Log.i(TAG, "maxlat: "+maxlon);
                    Log.i(TAG, "minlon: "+minlon);
                }else if(Double.parseDouble(carDetailBean.getDgm().getBlng())>maxlon){
                    //有经纬度后，判断大小
                    maxlon=Double.parseDouble(carDetailBean.getDgm().getBlng());
                    Log.i(TAG, "maxlon: "+maxlon);
                }else if(Double.parseDouble(carDetailBean.getDgm().getBlng())<minlon){
                    minlon=Double.parseDouble(carDetailBean.getDgm().getBlng());
                    Log.i(TAG, "minlon: "+minlon);
                }

                MarkerOptions ooA;
                if(carDetailBean.getOnline_status().equals("在线")){
                    if(carDetailBean.getDgm().getAlarm().equals("1")){
                        //是 有报警
                        ooA = new MarkerOptions().position(markerll).icon(getBitmapDescriptor(3,carDetailBean.getInternalnum()))
                                .zIndex(9).draggable(true);
                    }else{
                        //正常显示在线
                        ooA = new MarkerOptions().position(markerll).icon(getBitmapDescriptor(1,carDetailBean.getInternalnum()))
                                .zIndex(9).draggable(true);
                    }
                }else if(carDetailBean.getOnline_status().equals("离线")){
                    if(carDetailBean.getDgm().getAlarm().equals("1")){
                        //是 有报警
                        ooA = new MarkerOptions().position(markerll).icon(getBitmapDescriptor(3,carDetailBean.getInternalnum()))
                                .zIndex(9).draggable(true);
                    }else {
                        //正常显示离线
                        ooA = new MarkerOptions().position(markerll).icon(getBitmapDescriptor(2, carDetailBean.getInternalnum()))
                                .zIndex(9).draggable(true);
                    }
                }else{
                    ooA = new MarkerOptions().position(markerll).icon(getBitmapDescriptor(3,carDetailBean.getInternalnum()))
                            .zIndex(9).draggable(true);
                }
                //掉落特效
//            ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
                mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
                //将列表索引放在title里面
                mMarkerA.setTitle(""+i);
            }

//            showInfowindow(i);

//                bounds.contains(markerll);
//
//            if(i==arraycar.size()-1){
//            //最后一组数据

//            }
        }
        if(datalist.size()==0){
            DialogUIUtils.showAlert(CarAddressActivity.this, null, "暂无车辆位置信息，原因可能是：\n" +
                    "\n" +
                    "1、无法获取车辆位置信息，请检查设备是否正常安装。" +
                    "\n" +
                    "2、可能因为车辆在隧道内，大桥下，GPS天线被遮挡等原因造成定位延迟。", "", "", "知道了", "", true, true, true, new DialogUIListener() {
                @Override
                public void onPositive() {
//                        showToast("onPositive");
                }

                @Override
                public void onNegative() {

                }

            }).show();
             }
        //计算地图中心点
        double midlat=(maxlat+minlat)/2;
        double midlon=(maxlon+minlon)/2;
        LatLng ll = new LatLng(midlat, midlon);
        Log.i(TAG, "initOverlay: "+ll.toString());
        //计算地图缩放度
        int jl = (int) DistanceUtil.getDistance(new LatLng(maxlat, maxlon),
                new LatLng(minlat,minlon));
        int j;
        int[] zoomLevel = {50,100,200,500,1000,2000,5000,10000,20000,25000,50000,100000,200000,500000,1000000,2000000,5000000};//级别18到3。
        for (j = 0;j < 17;j++) {
            if (zoomLevel[j] > jl) {
                break;
            }

        }
        Log.i(TAG, "zoom1: "+jl);
        Log.i(TAG, "zoom: "+j);
        //可以适当的减1
        float zoom = 18-j+3 ;
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, zoom);
        mBaiduMap.setMapStatus(u);
        mBaiduMap.animateMapStatus(u);
        //给viewpager赋值
        setViewpager();
    }

    private void setViewpager() {
        int posid=100;
        fs=new ArrayList<Fragment>();
        if(arraycar.size()==1){
            onlyone=true;
            JSONObject JOdevice=arraycar.getJSONObject(0);
            carDetailGPSBean=JSONObject.parseObject(JOdevice.toString(),carDetailGPSBeans.carDetailGPSBean.class);
            fs.add(new DeviceMessageFragment(carDetailGPSBean));
        }else{

            for(int i=0;i<arraycar.size();i++){
//            for(int i=0;i<datalist.size();i++){
                JSONObject JOdevice=arraycar.getJSONObject(i);
                carDetailGPSBean=JSONObject.parseObject(JOdevice.toString(),carDetailGPSBeans.carDetailGPSBean.class);
                if(i==0){
                    //是第一个
                    fs.add(new DeviceMessageFragment(carDetailGPSBean));
                }
                fs.add(new DeviceMessageFragment(carDetailGPSBean));
                if(i==arraycar.size()-1){
                    //是最后一个了
                    JOdevice=arraycar.getJSONObject(0);
                    carDetailGPSBean=JSONObject.parseObject(JOdevice.toString(),carDetailGPSBeans.carDetailGPSBean.class);
                    fs.add(new DeviceMessageFragment(carDetailGPSBean));
                }
                if(arraycar.getJSONObject(i).getString("devie_id").equals(nowdeviceid)){
                    //找到对应的下标，如果刷新的话，需要切换到对应的fragment
                    posid=i;
                }
            }
        }

        FragmentManager fm=getSupportFragmentManager();
        DeviceMsPagerAdapter adapter=new DeviceMsPagerAdapter(fm,fs,datallist);
        viewPager.setAdapter(adapter);
        viewPager.setPageMargin(25);
        // pageCount设置红缓存的页面数
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i(TAG, "onPageScrolled: "+position);
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                if(onlyone){
                    LatLng movell=new LatLng(Double.parseDouble(datalist.get(0).getDgm().getBlat()),Double.parseDouble(datalist.get(0).getDgm().getBlng()));
                    nowdeviceid=datalist.get(position-1).getDevie_id();
                    Log.i(TAG, "onPageSelected: "+nowdeviceid);
//                    MapStatusUpdate mapstatusupdate =  MapStatusUpdateFactory.newLatLng(movell);
                    if(isfirst){
                        //是第一次定位就不能让地图定位到第一个点，其他时候因为有viewpager所以需要绑定
                        isfirst=false;
                    }else{
                        MapStatusUpdate mapstatusupdate = MapStatusUpdateFactory.newLatLngZoom(movell, 18f);
                        //对地图的中心点进行更新，
//                    mBaiduMap.setMapStatus(mapstatusupdate);
                        mBaiduMap.animateMapStatus(mapstatusupdate);
                    }
                }else{
                    Log.i(TAG, "onPageScrolled: "+position);
                    if(position==0||position>arraycar.size()){
                        //0和超过位数的不做处理，是自己添加的
                    }else{
                        //判断有没有点，没有就不移动
                        carDetailGPSBeans.carDetailGPSBean nowGpsBean=JSONObject.parseObject(arraycar.getJSONObject(position-1).toString(),carDetailGPSBeans.carDetailGPSBean.class);
                        if(nowGpsBean.getDgm().getBlat()!=null){
                            LatLng movell=new LatLng(Double.parseDouble(nowGpsBean.getDgm().getBlat()),Double.parseDouble(nowGpsBean.getDgm().getBlng()));
                            nowdeviceid=nowGpsBean.getDevie_id();
                            Log.i(TAG, "onPageSelected: "+nowdeviceid);
//                    MapStatusUpdate mapstatusupdate =  MapStatusUpdateFactory.newLatLng(movell);
                            if(isfirst){
                                //是第一次定位就不能让地图定位到第一个点，其他时候因为有viewpager所以需要绑定
                                isfirst=false;
                            }else{
                                MapStatusUpdate mapstatusupdate = MapStatusUpdateFactory.newLatLngZoom(movell, 18f);
                                //对地图的中心点进行更新，
//                    mBaiduMap.setMapStatus(mapstatusupdate);
                                mBaiduMap.animateMapStatus(mapstatusupdate);
                            }
                        }else{

                        }


                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //        若viewpager滑动未停止，直接返回
                if (state != ViewPager.SCROLL_STATE_IDLE) return;
//        若当前为第一张，设置页面为倒数第二张
                if (currentPosition == 0) {
                    viewPager.setCurrentItem(fs.size()-2,false);
                    Log.i(TAG, "onPageScrollStateChanged: 113");
                } else if (currentPosition == fs.size()-1) {
//        若当前为倒数第一张，设置页面为第二张
                    viewPager.setCurrentItem(1,false);
                    Log.i(TAG, "onPageScrollStateChanged: 114");
                }
            }
        });
        if(needrun){
            //需要跳转
            viewPager.setCurrentItem(posid+1,false);
        }else{
            viewPager.setCurrentItem(1,false);
        }

    }

    //类似于将一个布局变成图片
    public BitmapDescriptor getBitmapDescriptor(int type,String name) {
        BitmapDescriptor bttmap = null;
        View item_view = LayoutInflater.from(this).inflate(R.layout.marker_window,null);
        ImageView iv_type=item_view.findViewById(R.id.iv_type);
        TextView tv_name = (TextView) item_view.findViewById(R.id.tv_name);
        tv_name.setText(name);
        switch (type){
            case 1:
                //有线
                iv_type.setImageResource(R.mipmap.mark1);
                break;
            case 2:
                //离线
                iv_type.setImageResource(R.mipmap.mark3);
                break;
            case 3:
                //报警
                iv_type.setImageResource(R.mipmap.mark2);
                break;
        }
//        TextView tv_storeName = (TextView) item_view.findViewById(R.id.frag_near_marker_tv_name);
//        ImageView imageView = (ImageView) item_view.findViewById(R.id.frag_near_marker_iv_logo);

//// 设置布局中文字
//        tv_storeName.setText("");
//
//// 设置图标
//        imageView.setImageResource(R.mipmap.home_icon_department);
        bttmap = BitmapDescriptorFactory.fromView(item_view);
        return bttmap;

    }
    //展示mark弹出框
    public void showInfowindow(int index){
//        showstatus.setVisibility(View.INVISIBLE);
        mBaiduMap.hideInfoWindow();// 隐藏infowindow
        mInfoWindow = null;
        System.out.println("------------>showinfowindow");
        JSONObject jsonObject=arraycar.getJSONObject(index);
        carDetailGPSBeans.carDetailGPSBean carDetailBean=JSONObject.parseObject(jsonObject.toString(),carDetailGPSBeans.carDetailGPSBean.class);
        LatLng ll = new LatLng(Double.parseDouble(carDetailBean.getDgm().getBlat()),Double.parseDouble( carDetailBean.getDgm().getBlng()));
        MapStatusUpdate mu = MapStatusUpdateFactory.newLatLngZoom(ll, 13); // 设置地图中心点以及缩放级别
        mBaiduMap.animateMapStatus(mu);

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View linear = inflater.inflate(R.layout.marker_window, null);
        mInfoWindow = new InfoWindow(linear, ll, -47);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }
    private void initData() {

        carlist= intent.getStringExtra("carDetailGPS");
        arraycar=JSONArray.parseArray(carlist);
        newjsArray=new JSONArray();
        JSONObject JOdevice=arraycar.getJSONObject(0);
        carDetailGPSBean=JSONObject.parseObject(JOdevice.toString(),carDetailGPSBeans.carDetailGPSBean.class);
        carId=carDetailGPSBean.getCar_id();
        car_name.setText(carDetailGPSBean.getPledge_name());
        car_vin.setText(carDetailGPSBean.getVin());
//        carId=carDetailGPSBean.getCar_id();
        //显示marker覆盖物
        initOverlay();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i(TAG, marker.getTitle());
                //设置经纬度
                int item=Integer.parseInt(marker.getTitle());
                viewPager.setCurrentItem(item+1);
//                MapStatusUpdate mapstatusupdate =  MapStatusUpdateFactory.newLatLng(marker.getPosition());
                //对地图的中心点进行更新，
                MapStatusUpdate mapstatusupdate = MapStatusUpdateFactory.newLatLngZoom(marker.getPosition(), 18f);
                mBaiduMap.setMapStatus(mapstatusupdate);
                //显示下方的滚动列表
                if(viewPager.getVisibility()==View.VISIBLE){
                    //当前已经显示了
                    //移动到指定的page

                }else{
                    viewPager.setVisibility(View.VISIBLE);
                    viewPager.setAnimation(AnimationUtil.moveToViewLocation());
                }

                return true;
            }
        });
    }

    private void initListener() {
        final Animation mShowAction = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        final Animation mHiddenAction = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //刷新数据
                //重新请求设备数据
                needrun=true;
                if(!Constant.isNetworkConnected(CarAddressActivity.this)) {
                    //判断网络是否可用
                    Toast.makeText(CarAddressActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                }else{
                    achieveDevie(carId);
                }

            }
        });
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //定位
                // 开启定位图层
                isfirstdialog=true;
                isFirstLoc=true;
                mBaiduMap.setMyLocationEnabled(true);
                // 定位初始化

                mLocClient.registerLocationListener(myListener);
                LocationClientOption option = new LocationClientOption();
                option.setOpenGps(true); // 打开gps
                option.setCoorType("bd09ll"); // 设置坐标类型
                option.setScanSpan(1000);
                mLocClient.setLocOption(option);
                mLocClient.start();
            }
        });
        car_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog= DialogUIUtils.showLoading(CarAddressActivity.this,"加载中...",true,true,false,true);
                dialog.show();
                //获取车辆信息
                if(!Constant.isNetworkConnected(CarAddressActivity.this)) {
                    //判断网络是否可用
                    Toast.makeText(CarAddressActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                }else{
                    //获取车辆的报警类型
                    //通过vin获取车辆信息
                    RequestParams params=new RequestParams();
                    params.addHeader("Content-Type","application/json");
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("carid",carDetailGPSBean.getCar_id());
                    jsonObject.put("pagesize",11);
                    jsonObject.put("page",1);
                    params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
                    HttpRequest.post(Api.queVehicleList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
                        @Override
                        protected void onSuccess(Headers headers, JSONObject jsonObject) {
                            super.onSuccess(headers, jsonObject);
                            Log.i(TAG, "onSuccess: "+jsonObject.toString());
//                            JSONArray jsonArray=jsonObject.getJSONArray("list");
//                            JSONObject sign_statusLLIST= (JSONObject) jsonArray.get(0);
//                            d_type=jsonObject.getJSONArray("list").getJSONObject(0).getInteger("type");
                            if(jsonObject.getJSONArray("list").getJSONObject(0).getInteger("sign_status")!=null){
                                //有数据的
                                sign_status=jsonObject.getJSONArray("list").getJSONObject(0).getString("sign_status");

                                if(sign_status.equals("3")||sign_status.equals("重点关注")){
                                    sign_status="重点关注";
                                }else if(sign_status.equals("2")||sign_status.equals("逾期")){
                                    sign_status="逾期";
                                }else{
                                    sign_status="";
                                }
                            }else{
                                sign_status="";
                            }
                            if(jsonObject.getJSONArray("list").getJSONObject(0).getString("type")!=null){
                                d_type=jsonObject.getJSONArray("list").getJSONObject(0).getInteger("type");
                                if(d_type==1){
                                    danger_type="拆除";
                                }else if(d_type==2){
                                    danger_type="屏蔽";
                                }else{
                                    danger_type="";
                                }
                            }else {
                                danger_type="";
                            }



                            //请求设备信息（设备列表）
                            if(!Constant.isNetworkConnected(CarAddressActivity.this)) {
                                //判断网络是否可用
                                Toast.makeText(CarAddressActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                            }else{
                                //再请求车辆是否有高危异常
//                                achieveDangerType();
                                achieveCarMessage();
                            }

                        }

                        @Override
                        public void onFailure(int errorCode, String msg) {
                            super.onFailure(errorCode, msg);
                            dialog.dialog.dismiss();
                        }
                    });


                }


//                popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//                popupWindow.setBackgroundDrawable(new BitmapDrawable());
//                popupWindow.setOutsideTouchable(true);
//                popupWindow.setFocusable(true);
//                popupWindow.showAtLocation(rootView, Gravity.CENTER , 0, 0);
//                View rootView = View.inflate(CarAddressActivity.this, R.layout.view_alert_window, null);
//                final Dialog dialog = DialogUIUtils.showCustomAlert(CarAddressActivity.this, rootView, Gravity.CENTER, true, false).show();
//                rootView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        DialogUIUtils.dismiss(dialog);
//                    }
//                });
            }
        });
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //伸展或者收缩提醒
                if(nowwarn==1){
                    //展示
                    singler.setVisibility(View.VISIBLE);
                    singler.startAnimation(mShowAction);
                    nowwarn=2;
                }else{
                    //收缩
                    singler.setVisibility(View.GONE);
                    singler.startAnimation(mHiddenAction);
                    nowwarn=1;
                }

            }
        });
    }

    private void achieveDangerType() {
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("search",carDetailGPSBean.getVin());
        jsonObject.put("pagesize",11);
        jsonObject.put("page",1);
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queryHighAlarmList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, "onSuccess1: "+jsonObject.toString());
                JSONArray jsonArray=jsonObject.getJSONArray("list");
                JSONObject sign_statusLLIST= (JSONObject) jsonArray.get(0);
                danger_type=sign_statusLLIST.getString("typename");
                //请求设备信息（设备列表）
                if(!Constant.isNetworkConnected(CarAddressActivity.this)) {
                    //判断网络是否可用
                    Toast.makeText(CarAddressActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                }else{
                    achieveCarMessage();
                }

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                dialog.dialog.dismiss();
            }
        });
    }

    private void achieveDevie(String carId) {
        //刷新数据请求设备信息
        RequestParams params1=new RequestParams();
        //因为传递的是json数据，所以需要设置header和body
        params1.addHeader("Content-Type","application/json");
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("car_id",carId);
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
                    arraycar=JSONArray.parseArray(jsonObject.get("list").toString());
                    mBaiduMap.clear();
                    isfirst=true;
                    initOverlay();
                }else{
                    Toast.makeText(CarAddressActivity.this,carDetailGPSBeans.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(CarAddressActivity.this,"加载中...",true,true,false,true);
                dialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dialog.dismiss();
            }
        });
    }

    private void achieveCarMessage() {
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id",carId);
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.getCarMessageById+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, "onSuccess: "+jsonObject.toString());
                carMessageBean=JSONObject.parseObject(jsonObject.toString(),CarMessageBean.class);
                //请求设备信息（设备列表）
                if(!Constant.isNetworkConnected(CarAddressActivity.this)) {
                    //判断网络是否可用
                    Toast.makeText(CarAddressActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                }else{
                    achieveDeviceMessage();
                }

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                dialog.dialog.dismiss();
            }
        });
    }

    private void achieveDeviceMessage() {
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id",carId);
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.getDeviceMessageById+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, "onSuccess: "+jsonObject.toString());
                carDeviceBean=JSONObject.parseObject(jsonObject.toString(),CarDeviceBean.class);
                //显示车辆弹出框
                showPopwindow();
                dialog.dialog.dismiss();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                dialog.dialog.dismiss();

            }
        });
    }
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    private void showPopwindow() {
        //显示车辆信息弹出框
        View rootView = findViewById(R.id.root_main4); // 當前頁面的根佈局
        zzc.setVisibility(View.VISIBLE);
        LayoutInflater mLayoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
                R.layout.view_alert_window, null, true);
        final PopupWindow pw = new PopupWindow(menuView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new BitmapDrawable());//设置背景透明以便点击外部消失
        pw.setOutsideTouchable(true); // 设置是否允许在外点击使其消失,到底有用没?
        pw.setTouchable(true); // 设置popupwindow可点击
        pw.setFocusable(true);
        pw.showAtLocation(rootView, Gravity.CENTER,0,0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                zzc.setVisibility(View.GONE);
            }
        });
//        backgroundAlpha((float) 0.5);
//                View popView = LayoutInflater.from(mContext).inflate(
//                        R.layout.choose_window, null);
        ImageView close=menuView.findViewById(R.id.close);
        ListView device_list=menuView.findViewById(R.id.device_list);//显示设备列表
        TextView owner_name=menuView.findViewById(R.id.owner_name);//车主姓名
//        @SuppressLint("WrongViewCast") TextView car_status=menuView.findViewById(R.id.car_status);//车辆报警情况
        TextView car_yuqi_status=menuView.findViewById(R.id.car_yuqi_status);
        TextView car_danger=menuView.findViewById(R.id.car_danger);//显示屏蔽和拆除报警
        TextView phone_num=menuView.findViewById(R.id.phone_num);//手机号码
        TextView car_vin=menuView.findViewById(R.id.car_vin);//车架号
        TextView car_num=menuView.findViewById(R.id.car_num);//车牌号
        TextView car_type=menuView.findViewById(R.id.car_type);//车型
//        TextView home_address=menuView.findViewById(R.id.home_address);//家庭地址
        TextView work_address=menuView.findViewById(R.id.work_address);//工作地址
        TextView customer=menuView.findViewById(R.id.customer);//所属客户
        owner_name.setText("车主姓名："+carMessageBean.getObject().getPledger().getName());
        if(sign_status.equals("逾期")){
            car_yuqi_status.setVisibility(View.VISIBLE);
            car_yuqi_status.setText("逾期");
        }else if(sign_status.equals("重点关注")){
            car_yuqi_status.setVisibility(View.VISIBLE);
            car_yuqi_status.setText("重点关注");
        }else{
            car_yuqi_status.setVisibility(View.GONE);
        }
        if(danger_type.equals("屏蔽")){
            car_danger.setVisibility(View.VISIBLE);
            car_danger.setText("屏蔽");
        }else if(danger_type.equals("拆除")){
            car_danger.setVisibility(View.VISIBLE);
            car_danger.setText("拆除");
        }else{
            car_danger.setVisibility(View.GONE);
        }
        phone_num.setText("手机号码："+carMessageBean.getObject().getPledger().getPhone());
        car_vin.setText("车架号："+carMessageBean.getObject().getVin());
        car_num.setText("车牌号："+carMessageBean.getObject().getCar_no());
        car_type.setText("车型："+carMessageBean.getObject().getCar_brand());
//        home_address.setText(carMessageBean.getObject().getCar_brand());
        if(carMessageBean.getObject().getPledger().getPledger_loc().size()==0){
            work_address.setText("家庭地址："+"暂无");
        }else{
            work_address.setText("家庭地址："+carMessageBean.getObject().getPledger().getPledger_loc().get(0).getAddress());
        }
//        work_address.setText("联系地址："+carMessageBean.getObject().getPledger().getPledger_loc().get(0).getAddress());
        customer.setText("所属客户："+carMessageBean.getObject().getSystemgroup().getGroup_name());
        DeviceListAdapter adapter=new DeviceListAdapter(CarAddressActivity.this,carDeviceBean);
        device_list.setAdapter(adapter);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });
    }
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mBaiduMap == null) {
//                Toast.makeText(CarAddressActivity.this,"定位服务未开启",Toast.LENGTH_SHORT).show();
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            if(mCurrentLat==4.9E-324){
                if(isfirstdialog){
                    DialogUIUtils.showAlert(CarAddressActivity.this, null, "请开启定位服务", "", "", "知道了", "", true, true, true, new DialogUIListener() {
                        @Override
                        public void onPositive() {
//                        showToast("onPositive");
                        }

                        @Override
                        public void onNegative(){
                        }

                    }).show();
                    isfirstdialog=false;
                }

            }else{
                mCurrentAccracy = location.getRadius();
                locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(mCurrentDirection).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                if (isFirstLoc) {
                    isFirstLoc = false;
//                LatLng ll = new LatLng(location.getLatitude(),
//                        location.getLongitude());
//                MapStatus.Builder builder = new MapStatus.Builder();
//                builder.target(ll).zoom(18.0f);
                    //不移动到当前定位点
                    if(maxlat==0){
                        //一个经纬度都没有，就直接赋值
                        maxlat=location.getLatitude();
                        minlat=location.getLatitude();
                    }else if(location.getLatitude()>maxlat){
                        //有经纬度后，判断大小
                        maxlat=location.getLatitude();
                        Log.i(TAG, "maxlat: "+maxlat);
                    }else if(location.getLatitude()<minlat){
                        minlat=location.getLatitude();
                        Log.i(TAG, "minlat: "+minlat);
                    }
                    if(maxlon==0){
                        maxlon=location.getLongitude();
                        minlon=location.getLongitude();
                        Log.i(TAG, "minlon: "+minlon);
                    }else if(location.getLongitude()>maxlon){
                        //有经纬度后，判断大小
                        maxlon=location.getLongitude();
                        Log.i(TAG, "maxlon: "+maxlon);
                    }else if(location.getLongitude()<minlon){
                        minlon=location.getLongitude();
                        Log.i(TAG, "minlon: "+minlon);
                    }

                    //计算地图中心点
                    double midlat=(maxlat+minlat)/2;
                    double midlon=(maxlon+minlon)/2;
                    LatLng llllll = new LatLng(midlat, midlon);
                    Log.i(TAG, "initOverlay: "+llllll.toString());
                    //计算地图缩放度
                    int jl = (int) DistanceUtil.getDistance(new LatLng(maxlat, maxlon),
                            new LatLng(minlat,minlon));
                    int j;
                    int[] zoomLevel = {50,100,200,500,1000,2000,5000,10000,20000,25000,50000,100000,200000,500000,1000000,2000000,5000000};//级别18到3。
                    for (j = 0;j < 17;j++) {
                        if (zoomLevel[j] > jl) {
                            break;
                        }

                    }
                    Log.i(TAG, "zoom1: "+jl);
                    Log.i(TAG, "zoom: "+j);
                    //可以适当的减1
                    float zoom = 18-j+3 ;
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(llllll, zoom);
                    mBaiduMap.setMapStatus(u);
                    mBaiduMap.animateMapStatus(u);
//                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mLocClient.stop();

//        bdA.recycle();
//        bdB.recycle();
//        bdC.recycle();
    }
}
