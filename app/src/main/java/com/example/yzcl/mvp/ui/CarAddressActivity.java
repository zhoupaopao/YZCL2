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

import com.alibaba.fastjson.JSONArray;
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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
import com.dou361.dialogui.DialogUIUtils;
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
    private carDetailGPSBeans.carDetailGPSBean carDetailGPSBean;
    private JSONArray arraycar;
    private String TAG="CarAddressActivity";
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private InfoWindow mInfoWindow;// 点击覆盖物显示的窗口
    private double maxlat=0;
    private double maxlon=0;
    private double minlat=0;
    private double minlon=0;
    ArrayList<Fragment>fs;
    private PopupWindow popupWindow;// popupwindow
    private String carId="";//车辆id
    private CarMessageBean carMessageBean;
    private CarDeviceBean carDeviceBean;
    private SharedPreferences sp;
    private View zzc;
    private ArrayList<carDetailGPSBeans.carDetailGPSBean>datalist=new ArrayList<>();
    private LinearLayout singler;
    private RelativeLayout question;
    private int nowwarn=1;

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

    }

    private void initOverlay() {
        for(int i=0;i<arraycar.size();i++){
            JSONObject jsonObject=arraycar.getJSONObject(i);
            carDetailGPSBeans.carDetailGPSBean carDetailBean=JSONObject.parseObject(jsonObject.toString(),carDetailGPSBeans.carDetailGPSBean.class);
            //给list赋值，用于显示viewpager里面的信息
            datalist.add(carDetailBean);
            Log.i(TAG, Double.parseDouble(carDetailBean.getDgm().getBlat())+","+Double.parseDouble(carDetailBean.getDgm().getBlng()));
            LatLng markerll = new LatLng(Double.parseDouble(carDetailBean.getDgm().getBlat()),Double.parseDouble( carDetailBean.getDgm().getBlng()));
            //计算最大最小经纬度
            if(maxlat==0){
                //一个经纬度都没有，就直接赋值
                maxlat=Double.parseDouble(carDetailBean.getDgm().getBlat());
                minlat=Double.parseDouble(carDetailBean.getDgm().getBlat());
            }else if(Double.parseDouble(carDetailBean.getDgm().getBlat())>maxlat){
                //有经纬度后，判断大小
                maxlat=Double.parseDouble(carDetailBean.getDgm().getBlat());
            }else if(Double.parseDouble(carDetailBean.getDgm().getBlat())<minlat){
                minlat=Double.parseDouble(carDetailBean.getDgm().getBlat());
            }
            if(maxlon==0){
                maxlon=Double.parseDouble(carDetailBean.getDgm().getBlng());
                minlon=Double.parseDouble(carDetailBean.getDgm().getBlng());
            }else if(Double.parseDouble(carDetailBean.getDgm().getBlng())>maxlon){
                //有经纬度后，判断大小
                maxlon=Double.parseDouble(carDetailBean.getDgm().getBlng());
            }else if(Double.parseDouble(carDetailBean.getDgm().getBlat())<minlon){
                minlon=Double.parseDouble(carDetailBean.getDgm().getBlng());
            }

            MarkerOptions ooA;
            if(carDetailBean.getOnline_status().equals("在线")){

               ooA = new MarkerOptions().position(markerll).icon(getBitmapDescriptor(1,carDetailBean.getInternalnum()))
                        .zIndex(9).draggable(true);

            }else if(carDetailBean.getOnline_status().equals("离线")){
                ooA = new MarkerOptions().position(markerll).icon(getBitmapDescriptor(2,carDetailBean.getInternalnum()))
                        .zIndex(9).draggable(true);
            }else{
                ooA = new MarkerOptions().position(markerll).icon(getBitmapDescriptor(3,carDetailBean.getInternalnum()))
                        .zIndex(9).draggable(true);
            }
            //掉落特效
//            ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
            mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
            //将列表索引放在title里面
            mMarkerA.setTitle(""+i);
//            showInfowindow(i);

//                bounds.contains(markerll);
//
//            if(i==arraycar.size()-1){
//            //最后一组数据

//            }
        }
        //计算地图中心点
        double midlat=(maxlat+minlat)/2;
        double midlon=(maxlon+minlon)/2;
        LatLng ll = new LatLng(midlat, midlon);
        //计算地图缩放度
        int jl = (int) DistanceUtil.getDistance(new LatLng(maxlat, maxlon),
                new LatLng(minlat,minlon));
        int j;
        int[] zoomLevel = {50,100,200,500,1000,2000,5000,10000,20000,25000,50000,100000,200000,500000,1000000,2000000};//级别18到3。
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
        fs=new ArrayList<Fragment>();
        for(int i=0;i<datalist.size();i++){
            if(i==0){
                //是第一个
                fs.add(new DeviceMessageFragment(datalist.get(datalist.size()-1)));
            }
            fs.add(new DeviceMessageFragment(datalist.get(i)));
            if(i==datalist.size()-1){
                //是最后一个了
                fs.add(new DeviceMessageFragment(datalist.get(0)));
            }
            Log.i(TAG, "setViewpager: 2222");
        }
        FragmentManager fm=getSupportFragmentManager();
        DeviceMsPagerAdapter adapter=new DeviceMsPagerAdapter(fm,fs,datalist);
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
                Log.i(TAG, "onPageScrolled: "+position);
                if(position==0||position>datalist.size()){
                    //0和超过位数的不做处理，是自己添加的
                }else{
                    LatLng movell=new LatLng(Double.parseDouble(datalist.get(position-1).getDgm().getBlat()),Double.parseDouble(datalist.get(position-1).getDgm().getBlng()));
                    MapStatusUpdate mapstatusupdate =  MapStatusUpdateFactory.newLatLng(movell);
                    //对地图的中心点进行更新，
//                    mBaiduMap.setMapStatus(mapstatusupdate);
                    mBaiduMap.animateMapStatus(mapstatusupdate);
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
        viewPager.setCurrentItem(1,false);
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
        JSONObject JOdevice=arraycar.getJSONObject(0);
        carDetailGPSBean=JSONObject.parseObject(JOdevice.toString(),carDetailGPSBeans.carDetailGPSBean.class);
        carId=carDetailGPSBean.getCar_id();
        car_name.setText(carDetailGPSBean.getCar_no());
        car_vin.setText(carDetailGPSBean.getVin());
        //显示marker覆盖物
        initOverlay();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i(TAG, marker.getTitle());
                //设置经纬度
                int item=Integer.parseInt(marker.getTitle());
                viewPager.setCurrentItem(item+1);
                MapStatusUpdate mapstatusupdate =  MapStatusUpdateFactory.newLatLng(marker.getPosition());
                //对地图的中心点进行更新，
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
        car_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取车辆信息
                achieveCarMessage();

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
                achieveDeviceMessage();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);

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
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);

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
        @SuppressLint("WrongViewCast") TextView car_status=menuView.findViewById(R.id.car_status);//车辆报警情况
        TextView phone_num=menuView.findViewById(R.id.phone_num);//手机号码
        TextView car_vin=menuView.findViewById(R.id.car_vin);//车架号
        TextView car_num=menuView.findViewById(R.id.car_num);//车牌号
        TextView car_type=menuView.findViewById(R.id.car_type);//车型
//        TextView home_address=menuView.findViewById(R.id.home_address);//家庭地址
        TextView work_address=menuView.findViewById(R.id.work_address);//工作地址
        TextView customer=menuView.findViewById(R.id.customer);//所属客户
        owner_name.setText("车主姓名："+carMessageBean.getObject().getPledger().getName());
        car_status.setText("未知没写");
        phone_num.setText("手机号码："+carMessageBean.getObject().getPledger().getPhone());
        car_vin.setText("车架号："+carMessageBean.getObject().getVin());
        car_num.setText("车牌号："+carMessageBean.getObject().getCar_no());
        car_type.setText("车型："+carMessageBean.getObject().getCar_brand());
//        home_address.setText(carMessageBean.getObject().getCar_brand());
        work_address.setText("联系地址："+carMessageBean.getObject().getPledger().getPledger_loc().get(0).getAddress());
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
//        bdA.recycle();
//        bdB.recycle();
//        bdC.recycle();
    }
}
