package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.bigkoo.pickerview.OptionsPickerView;
import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.CityListBean;
import com.example.yzcl.mvp.presenter.JsonFileReader;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/12/5.
 */
//围栏
public class EnclosureActivity extends Activity {
    private RadioGroup group;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private int mCurrentDirection = 0;
    private double mCurrentLat=0.0;//当前坐标
    private double mCurrentLon=0.0;//当前坐标
    private float mCurrentAccracy;//此处设置开发者获取到的方向信息
    boolean isFirstLoc = true; // 是否首次定位
    private MapView bmap;
    private BaiduMap mbaidumap;
    private LocationClient mLocClient;
    private MyLocationData locData;
    private TextView nowcity;
    private TextView nocity;
    private TextView choosecity;
    private LinearLayout choose;
    CityListBean cityListBean;
    private ArrayList<CityListBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    public MyLocationListenner myListener = new MyLocationListenner();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enclosure);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        group=findViewById(R.id.group);
        rb1=findViewById(R.id.rb1);
        rb2=findViewById(R.id.rb2);
        rb3=findViewById(R.id.rb3);
        bmap=findViewById(R.id.bmap);
        nowcity=findViewById(R.id.textView59);
        nocity=findViewById(R.id.city);
        choosecity=findViewById(R.id.textView61);
        choose=findViewById(R.id.choosecity);
        choose.setVisibility(View.GONE);
    }

    private void initData() {
        //地图初始化
        mbaidumap=bmap.getMap();
        //开启定位图层
        mbaidumap.setMyLocationEnabled(true);
        // 隐藏logo
        View child = bmap.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }

//地图上比例尺
        bmap.showScaleControl(false);
// 隐藏缩放控件
        bmap.showZoomControls(false);
        //定位初始化
        mLocClient=new LocationClient(this);
        mLocClient.registerLocationListener(myListener);//注册定位位置监听回调
        LocationClientOption option = new LocationClientOption();//定位方式参数设置
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//周期性请求定位，1秒返回一次位置
        mLocClient.setLocOption(option);//参数设置
        mLocClient.start();//开始定位
        String JsonData = JsonFileReader.getJson(this, "province_data.json");
//        ArrayList<CityListBean> jsonBean = parseData(JsonData);//用Gson 转成实体
//        cityListBean=  JSONObject.parseObject(JsonData,CityListBean.class);
        options1Items= (ArrayList<CityListBean>) JSONObject.parseArray(JsonData,CityListBean.class);
        for(int i=0;i<options1Items.size();i++){
            ArrayList<String>cityList=new ArrayList<>();//该省的城市列表
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int j=0;j<options1Items.get(i).getCity().size();j++){
                String cityname=options1Items.get(i).getCity().get(j).getName();
                cityList.add(cityname);
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if(options1Items.get(i).getCity().get(j).getArea()==null||options1Items.get(i).getCity().get(j).getArea().size()==0){
                    City_AreaList.add("");
                }else{
                    for(int k=0;k<options1Items.get(i).getCity().get(j).getArea().size();k++){
                        String AreaName=options1Items.get(i).getCity().get(j).getArea().get(k);
                        City_AreaList.add(AreaName);
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据

            }
            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);

        }

    }

    private void initListener() {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(rb1.getId()==i){
                    Toast.makeText(EnclosureActivity.this,i+"",Toast.LENGTH_SHORT).show();
                    //区域
                    area();
                }else if(rb2.getId()==i){
                    Toast.makeText(EnclosureActivity.this,i+"",Toast.LENGTH_SHORT).show();
                }else if(rb3.getId()==i){
                    Toast.makeText(EnclosureActivity.this,i+"",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //定位位置回调
            /**
             * MyLocationData 定位数据类，地图上的定位位置需要经纬度、精度、方向这几个参数，所以这里我们把这个几个参数传给地图
             * 如果不需要要精度圈，推荐builder.accuracy(0);
             * mCurrentDirection 是通过手机方向传感器获取的方向；也可以设置option.setNeedDeviceDirect(true)获取location.getDirection()，
             但是这不会时时更新位置的方向，因为周期性请求定位有时间间隔。
             * location.getLatitude()和location.getLongitude()经纬度，如果你只需要在地图上显示某个固定的位置，完全可以写入固定的值，
             如纬度36.958454，经度114.137588，这样就不要要同过定位sdk来获取位置了
             */
            // map view 销毁后不在处理新接收的位置
            if (location == null || bmap == null) {
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
            mbaidumap.setMyLocationData(locData);
            /**
             *当首次定位时，记得要放大地图，便于观察具体的位置
             * LatLng是缩放的中心点，这里注意一定要和上面设置给地图的经纬度一致；
             * MapStatus.Builder 地图状态构造器
             */
            if (isFirstLoc) {
                Log.i("isFirstLoc", "true");
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                Log.e("坐标", location.getLatitude()+"||"+location.getLongitude() );
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);//设置缩放中心点；缩放比例；
                //给地图设置状态
                mbaidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }else{
                Log.i("isFirstLoc", "false");
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    private void area(){
        //直接新建
        choose.setVisibility(View.VISIBLE);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //不采用页面跳转，直接下面弹出选择
//                Intent intent=new Intent();
//                intent.setClass(EnclosureActivity.this,CityList.class);
//                startActivity(intent);
                OptionsPickerView pvOptions=new OptionsPickerView.Builder(EnclosureActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //点击确定的时候，获取当前的信息
                        String text = options1Items.get(options1).getPickerViewText() +
                                options2Items.get(options1).get(options2) +
                                options3Items.get(options1).get(options2).get(options3);
                    }
                }).setTitleText("").setDividerColor(Color.BLUE)
                        .setTextColorCenter(Color.GRAY)
                        .setContentTextSize(18)
                        .setOutSideCancelable(false)


//                        .setSubmitText("确定")//确定按钮文字
//                        .setCancelText("取消")//取消按钮文字
//                        .setTitleText("城市选择")//标题
//                        .setSubCalSize(18)//确定和取消文字大小
//                        .setTitleSize(20)//标题文字大小
//                        .setTitleColor(Color.BLACK)//标题文字颜色
//                        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
//                        .setCancelColor(Color.BLUE)//取消按钮文字颜色
//                        .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
//                        .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
//                        .setContentTextSize(18)//滚轮文字大小
//                        .setLinkage(false)//设置是否联动，默认true
//                        .setLabels("省", "市", "区")//设置选择的三级单位
//                        .setCyclic(false, false, false)//循环与否
//                        .setSelectOptions(1, 1, 1)  //设置默认选中项
//                        .setOutSideCancelable(false)//点击外部dismiss default true
                        .build();
//                pvOptions.setPicker(options1Items);//一级选择器
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
                pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
                pvOptions.show();
            }
        });

    }
}
