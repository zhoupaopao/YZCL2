package com.example.yzcl.mvp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

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
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.mipmap.ico_car_on);

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
        //添加marker
        LatLng ll=new LatLng(40.098529,116.132046);
        MarkerOptions ooA;
        ooA = new MarkerOptions().position(ll).icon(bdA)
                .zIndex(9).draggable(true);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 14.0f);
        mBaiduMap.setMapStatus(u);
    }

    private void initListener() {

    }
}
