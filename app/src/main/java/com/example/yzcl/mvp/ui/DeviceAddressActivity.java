package com.example.yzcl.mvp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.TieBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.DeviceListBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2018/8/1.
 */

public class DeviceAddressActivity extends BaseActivity {
    private TextView device_name;
    private TextView waring_name;
    private TextView device_status;
    private TextView device_status_time;
    private TextView dl;
    private TextView address_time;
    private TextView address;
    private TextView bind_time;
    private TextView bind_msg;
    private TextView use_age;
    private TextView customer_group;
    private TextView xfzl;
    private TextView daohang;
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private Marker mMarkerA;
    private ImageView back;
    private TextView title;
//    private String str_deviceLLBean;
//    private DeviceListBean.DeviceLLBean deviceLLBean;
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.mipmap.ico_car_on);
    String blng;
    String blat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_address);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        title=findViewById(R.id.title);
        back=findViewById(R.id.back);
        //我要提交啊
        device_name=findViewById(R.id.device_name);
        waring_name=findViewById(R.id.waring_name);
        device_status=findViewById(R.id.device_status);
        device_status_time=findViewById(R.id.device_status_time);
        dl=findViewById(R.id.dl);
        address_time=findViewById(R.id.address_time);
        address=findViewById(R.id.address);
        bind_time=findViewById(R.id.bind_time);
        bind_msg=findViewById(R.id.bind_msg);
        use_age=findViewById(R.id.use_age);
        customer_group=findViewById(R.id.customer_group);
        xfzl=findViewById(R.id.xfzl);
        daohang=findViewById(R.id.daohang);
        mapView=findViewById(R.id.mapview);
        mBaiduMap=mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
    }

    private void initData() {
         blat=getIntent().getStringExtra("blat");
         blng=getIntent().getStringExtra("blng");
        //添加marker
        LatLng ll=new LatLng(Double.parseDouble(blat),Double.parseDouble(blng));
        MarkerOptions ooA;
        ooA = new MarkerOptions().position(ll).icon(bdA)
                .zIndex(9).draggable(true);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 14.0f);
        mBaiduMap.setMapStatus(u);
    }

    private void initListener() {
        String Internalnum=getIntent().getStringExtra("Internalnum");
        String Devicetype=getIntent().getStringExtra("Devicetype");
        String Alaram=getIntent().getStringExtra("Alaram");
        String GpsStates=getIntent().getStringExtra("GpsStates");
        String Bl=getIntent().getStringExtra("Bl");
        String LastLocTime=getIntent().getStringExtra("LastLocTime");
        String Postion=getIntent().getStringExtra("Postion");
        String Bindtime=getIntent().getStringExtra("Bindtime");
        String PledgerName=getIntent().getStringExtra("PledgerName");
        String Vin=getIntent().getStringExtra("Vin");
        String Valid_from=getIntent().getStringExtra("Valid_from");
        String Valid_end=getIntent().getStringExtra("Valid_end");
        String Group_name=getIntent().getStringExtra("Group_name");
        final String Id=getIntent().getStringExtra("Id");




        title.setText(Internalnum);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        device_name.setText(Internalnum+"-"+Devicetype);
        if(Alaram.equals("正常")){
            waring_name.setVisibility(View.GONE);
        }else{
            waring_name.setText(Alaram);
        }
        device_status.setText(GpsStates);
        device_status_time.setVisibility(View.GONE);
        if(Devicetype.equals("有线设备")){
            dl.setVisibility(View.GONE);
            xfzl.setVisibility(View.GONE);
        }else{
            xfzl.setVisibility(View.VISIBLE);
            if(Bl!=null){
                dl.setText("电量"+Bl+"%");
                if(Integer.parseInt(Bl)<30){
                    dl.setBackgroundResource(R.drawable.dl_radius);
                    dl.setTextColor(getResources().getColor(R.color.device_msg));
                }else{
                    dl.setBackgroundResource(R.drawable.green_dl_radius);
                    dl.setTextColor(getResources().getColor(R.color.tv_online));
                }
                dl.setVisibility(View.VISIBLE);
            }else{
                dl.setVisibility(View.GONE);
            }
        }
        address_time.setText("定位时间："+LastLocTime);
        address.setText("定位地址："+Postion);
        bind_time.setText("绑车时间："+Bindtime);
        bind_msg.setText("绑车信息："+PledgerName+","+Vin);
        use_age.setText("服务期限："+Valid_from+"~"+Valid_end);
        customer_group.setText("所属客户："+Group_name);
        xfzl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("deviceid",Id);
                intent.setClass(DeviceAddressActivity.this, XfzlActivity.class);
                startActivity(intent);
            }
        });
        daohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //召唤弹出框
                    List<TieBean> strings = new ArrayList<TieBean>();
                    strings.add(new TieBean("百度地图"));
                    strings.add(new TieBean("高德地图"));
                    strings.add(new TieBean("腾讯地图"));
                    DialogUIUtils.showSheet(DeviceAddressActivity.this, strings, "取消", Gravity.BOTTOM, true, true, new DialogUIItemListener() {
                        @Override
                        public void onItemClick(CharSequence text, int position) {
                            switch (position){
                                case 0:
                                    //先判断是否安装了第三方地图软件
                                    if(isPackageInstalled("com.baidu.BaiduMap")){
                                        Toast.makeText(DeviceAddressActivity.this,"正在为你打开百度地图",Toast.LENGTH_SHORT).show();
                                        // 百度地图
                                        Intent naviIntent = new Intent("android.intent.action.VIEW", Uri.parse("baidumap://map/geocoder?location=" + blat + "," +blng ));
                                        startActivity(naviIntent);
                                    }else{
                                        Toast.makeText(DeviceAddressActivity.this,"您未安装百度地图",Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 1:
                                    if(isPackageInstalled("com.autonavi.minimap")){
                                        Toast.makeText(DeviceAddressActivity.this,"正在为你打开高德地图",Toast.LENGTH_SHORT).show();
                                        // 高德地图
//                                        Log.i(TAG, "onItemClick: "+ datalist.getDgm().getBlat() +","+datalist.getDgm().getBlng());
                                        Intent naviIntent = new Intent("android.intent.action.VIEW", Uri.parse("androidamap://route?sourceApplication=appName&slat=&slon=&sname=我的位置&dlat="+ blat +"&dlon="+blng +"&dname=目的地&dev=0&t=2"));
                                        startActivity(naviIntent);
                                    }else{
                                        Toast.makeText(DeviceAddressActivity.this,"您未安装高德地图",Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 2:
                                    if(isPackageInstalled("com.tencent.map")){
                                        // 腾讯地图

                                        Intent naviIntent = new Intent("android.intent.action.VIEW", Uri.parse("qqmap://map/routeplan?type=drive&from=&fromcoord=&to=目的地&tocoord=" + blat + "," + blng + "&policy=0&referer=appName"));
                                        startActivity(naviIntent);
                                        Toast.makeText(DeviceAddressActivity.this,"正在为你打开腾讯地图",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(DeviceAddressActivity.this,"您未安装腾讯地图",Toast.LENGTH_SHORT).show();
                                    }
                                    break;

                            }
                        }

                        @Override
                        public void onBottomBtnClick() {
                        }
                    }).show();

            }
        });

    }
    //判断是否安装第三方软件
    public static boolean isPackageInstalled(String packageName) {
        return new File("/data/data/" + packageName).exists();
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
    }
}
