package com.example.yzcl.mvp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.overlayutil.DrivingRouteOverlay;
import com.example.overlayutil.OverlayManager;
import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.carDetailGPSBeans;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.example.yzcl.mvp.ui.mvpactivity.MainActivity;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/7/5.
 */

public class TranceActivity extends BaseActivity implements OnGetRoutePlanResultListener {
    Button mBtnPre = null; // 上一个节点
    Button mBtnNext = null; // 下一个节点
    int nodeIndex = -1; // 节点索引,供浏览节点时使用
    DrivingRouteResult nowResultdrive = null;
    boolean hasShownDialogue = false;
    private BuildBean dialog;
    private boolean isTraced=false;//是否开启追踪
    boolean isFirstruote=true;
    private int nowintere = 0;
    private ImageView refresh;
    private ImageView loc;
    private ImageView back;
    private Marker mMarkerA;
    private TextView title;
    private SharedPreferences sp;
    private String deviceid="";
    private boolean isfff=true;
    private double longitude1=120;//人经度
    private double latitude1=30;//人纬度
    private double longitude2=120;//车经度
    private double latitude2=30;//车纬度
    RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    MapView mMapView = null;    // 地图View
    RouteLine route = null;
    RouteLine ro1ute = null;
    private BaiduMap mBaiduMap;
    private PoiSearch mPoiSearch = null;
    OverlayManager routeOverlay = null;
    LocationClient mLocClient;
    String[] intraPoint;
    Polyline mPolyline;
    String dev_name="";
    String dev_last_time="";
    String dev_address="";
    private String interestPoint = "停车场,加油站,维修厂";
    private int totalintrer = 0;
    private Boolean isfirst=true;
    private long mExitTime=0;
    private double maxlat=0;
    private double maxlon=0;
    private double minlat=0;
    private double minlon=0;
    BitmapDescriptor bdstart = BitmapDescriptorFactory
            .fromResource(R.mipmap.img_start);
//    BitmapDescriptor bdend = BitmapDescriptorFactory
//            .fromResource(R.mipmap.img_end);
//计时用
    private int runtime = 10000;
    ArrayList<LatLng>list_device;//存放设备坐标列表
    Timer timer;
    public MyLocationListenner myListener = new MyLocationListenner();
//    PoiOverlay poiOverlay;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trance);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();

    }

    private void initView() {
        list_device=new ArrayList<>();
        back=findViewById(R.id.back);
        title=findViewById(R.id.title);
        mMapView = (MapView) findViewById(R.id.mapview);
        mPoiSearch = PoiSearch.newInstance();
        mMapView.showZoomControls(true);
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        mBaiduMap = mMapView.getMap();
//        poiOverlay = new MyPoiOverlay(mBaiduMap);
//        // 开启定位图层
//        mBaiduMap.setMyLocationEnabled(true);
//        // 定位初始化
//        mLocClient = new LocationClient(this);
//        mLocClient.registerLocationListener(myListener);
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true); // 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000);
//        mLocClient.setLocOption(option);
//        mLocClient.start();
        // 初始化搜索模块，注册事件监听
//        mSearch = RoutePlanSearch.newInstance();
//        mSearch.setOnGetRoutePlanResultListener(this);
        refresh=findViewById(R.id.refresh);
        loc=findViewById(R.id.loc);
        //根据坐标添加覆盖物

    }
    public BitmapDescriptor getBitmapDescriptor(String device_name1,String now_address2,String last_time3) {
        BitmapDescriptor bttmap = null;
        View item_view = LayoutInflater.from(this).inflate(R.layout.layout_trance,null);
        TextView device_name=item_view.findViewById(R.id.device_name);
        TextView now_address = (TextView) item_view.findViewById(R.id.now_address);
        TextView last_time = (TextView) item_view.findViewById(R.id.last_time);
        device_name.setText(device_name1);
        now_address.setText(now_address2);
        last_time.setText(last_time3);

        bttmap = BitmapDescriptorFactory.fromView(item_view);
        return bttmap;

    }

    private void initData() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("实时跟踪");
        Intent intent=getIntent();
        longitude2=Double.valueOf(intent.getStringExtra("blng"));
        latitude2=Double.valueOf(intent.getStringExtra("blat"));
//        HashMap<String,Double>hm_device=new HashMap<>();
//        hm_device.put("blat",latitude2);
//        hm_device.put("blng",longitude2);
        list_device.add(new LatLng(latitude2,longitude2));
        deviceid=intent.getStringExtra("deviceid");
        //获取设备定位
         timer=new Timer();
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                Message mm=new Message();
                mm.what=0;
                handler.sendMessage(mm);

            }
        },0,20000);

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0){
                if(!Constant.isNetworkConnected(TranceActivity.this)) {
                    //判断网络是否可用
                    Toast.makeText(TranceActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                }else{
                    achieveDeviceLL();
                }

            }
            super.handleMessage(msg);
        }
    };
    private void initListener() {
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((System.currentTimeMillis()-mExitTime>1000)){ //如果两次按键时间间隔大于2000毫秒，则不退出
                    //不能让他连续点击登录
                    if(!Constant.isNetworkConnected(TranceActivity.this)) {
                        //判断网络是否可用
                        Toast.makeText(TranceActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                    }else{
                        achieveDeviceLL();
                    }
                    mExitTime = System.currentTimeMillis();// 更新mExitTime
                }else{
                    Toast.makeText(TranceActivity.this,"请勿连续点击",Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();// 更新mExitTime
                }
                //刷新数据
                //重新请求设备数据
//                needrun=true;
//                achieveDevie(carId);
                //获取单个设备的经纬度


            }
        });
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //定位
                // 开启定位图层
                isfirst=true;
                mBaiduMap.setMyLocationEnabled(true);
                // 定位初始化
                mLocClient = new LocationClient(TranceActivity.this);
                mLocClient.registerLocationListener(myListener);
                LocationClientOption option = new LocationClientOption();
                option.setOpenGps(true); // 打开gps
                option.setCoorType("bd09ll"); // 设置坐标类型
                option.setScanSpan(1000);
                mLocClient.setLocOption(option);
                mLocClient.start();
            }
        });
    }

    private void achieveDeviceLL() {
        Log.i("getTerminalMarker: ", "achieveDeviceLL: ");
        RequestParams params1=new RequestParams();
        //因为传递的是json数据，所以需要设置header和body
        params1.addHeader("Content-Type","application/json");
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("deviceid",deviceid);
        params1.setRequestBody(MediaType.parse("application/json"),jsonObject1.toString());
        HttpRequest.post(Api.queDeviceGpsInfo+"?token="+sp.getString(Constant.Token,""),params1,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i("onSuccess: ", jsonObject.toString());
                if(jsonObject.getBoolean("success")){
                    String sblx="";
                    if(jsonObject.getJSONObject("object").getString("category").equals("有线设备")){
                        sblx="有线";
                    }else{
                        sblx="无线";
                    }
                    dev_name="设备名称："+jsonObject.getJSONObject("object").getString("internalnum")+"("+sblx+")";
                    dev_last_time="定位时间："+jsonObject.getJSONObject("object").getJSONObject("dgm").getString("stime")+"（20秒刷新一次）";
                    dev_address="当前位置："+jsonObject.getJSONObject("object").getJSONObject("dgm").getString("postion");
                    if(longitude2==Double.parseDouble(jsonObject.getJSONObject("object").getJSONObject("dgm").getString("blng"))&&Double.parseDouble(jsonObject.getJSONObject("object").getJSONObject("dgm").getString("blat"))==latitude2){
                        //经纬度与上一次的相同
                        //不做处理
                        //只有一个点
                        if(isfff){
                            //第一次请求位置
                            isfff=false;
                            Log.i("onSuccess: ", "相同1: ");
                            MarkerOptions ooD;
                            ooD = new MarkerOptions().position(list_device.get(0)).icon(getBitmapDescriptor(dev_name,dev_address,dev_last_time))
                                    .zIndex(9).draggable(true);
                            mMarkerA = (Marker) (mBaiduMap.addOverlay(ooD));
                            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(list_device.get(0), 13);
                            mBaiduMap.setMapStatus(u);
                            mBaiduMap.animateMapStatus(u);
                        }else{
                            Log.i("onSuccess: ", "相同: ");
                        }


                    }else{
                        //不同，首先将最新的坐标赋值
                        mBaiduMap.clear();
                        longitude2=Double.parseDouble(jsonObject.getJSONObject("object").getJSONObject("dgm").getString("blng"));
                        latitude2=Double.parseDouble(jsonObject.getJSONObject("object").getJSONObject("dgm").getString("blat"));
                        list_device.add(new LatLng(latitude2,longitude2));

                        //绘制路线
                        if(list_device.size()==1){
                            //只有一个点
                            MarkerOptions ooD;
                            ooD = new MarkerOptions().position(list_device.get(0)).icon(getBitmapDescriptor(dev_name,dev_address,dev_last_time))
                                    .zIndex(9).draggable(true);
                            mMarkerA = (Marker) (mBaiduMap.addOverlay(ooD));
                        }else{
                            //规划路线
                            ghlx();
                        }
                    }
//                    BitmapDescriptor adasd=getBitmapDescriptor(jsonObject.getJSONObject("object").getString("internalnum"),jsonObject.getJSONObject("object").getJSONObject("dgm").getString("postion"),jsonObject.getJSONObject("object").getJSONObject("dgm").getString("stime"));

                    dialog.dialog.dismiss();
                }else{
                    Toast.makeText(TranceActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    dialog.dialog.dismiss();
                }
                    //请求成功
                    //显示点
                    Log.i("onSuccess: ", jsonObject.toString());
//                }else{
//
//                }
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(TranceActivity.this,"加载中...",true,false,false,true);
                dialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }

    private void ghlx() {
        //划线和添加起始点和终止点
        MarkerOptions ooC;
        MarkerOptions ooD;
        ooC = new MarkerOptions().position(list_device.get(0)).icon(bdstart)
                .zIndex(9).draggable(true);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooC));
        ooD = new MarkerOptions().position(list_device.get(list_device.size()-1)).icon(getBitmapDescriptor(dev_name,dev_address,dev_last_time))
                .zIndex(9).draggable(true);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooD));
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(list_device);
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(list_device.get(list_device.size()-1));
        mBaiduMap.setMapStatus(u);
        mBaiduMap.animateMapStatus(u);
    }

//    void searchRoute()
//    {
//
//        PlanNode stNode = PlanNode.withLocation(new LatLng(latitude1,longitude1));
//
////        System.out.println("searchroute-->" + lat + "," + lon);
//        PlanNode enNode = PlanNode.withLocation(new LatLng(latitude2,longitude2));
//        Log.i("searchRoute: ", latitude1+"---"+longitude1);
//        System.out.println("------------------lol"+latitude1+"---"+longitude1+"---"+latitude2+"----"+longitude2);
//        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(
//                enNode));// 发起驾车路线规划
//
//    }
//    //倒计时显示在textview中
//    class TimeCount extends CountDownTimer {
//        public TimeCount(long millisInFuture,
//                         long countDownInterval) {
//            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
//
//        }
//        @Override
//        public void onFinish() {//计时完毕时触发
//            achieveDeviceLL();
//            startTimer();
//        }
//        @Override
//        public void onTick(long millisUntilFinished){//计时过程显示
//        }
//
//    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            latitude1=location.getLatitude();
            longitude1=location.getLongitude();
            Log.i("onReceiveLocation: ", latitude1+"--"+longitude1);
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isfirst) {
//                searchRoute();
                isfirst = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                maxlat=location.getLatitude();
                minlat=location.getLatitude();
                maxlon=location.getLongitude();
                minlon=location.getLongitude();
                //计算地图中心点和缩放
                for(int i=0;i<list_device.size();i++){
                    if(maxlat==0){
                        //一个经纬度都没有，就直接赋值
                        maxlat=list_device.get(i).latitude;
                        minlat=list_device.get(i).latitude;
                    }else if(list_device.get(i).latitude>maxlat){
                        //有经纬度后，判断大小
                        maxlat=list_device.get(i).latitude;
                    }else if(list_device.get(i).latitude<minlat){
                        minlat=list_device.get(i).latitude;
                    }
                    if(maxlon==0){
                        maxlon=list_device.get(i).longitude;
                        minlon=list_device.get(i).longitude;
                    }else if(list_device.get(i).longitude>maxlon){
                        //有经纬度后，判断大小
                        maxlon=list_device.get(i).longitude;
                    }else if(list_device.get(i).longitude<minlon){
                        minlon=list_device.get(i).longitude;
                    }
                }
                double midlat=(maxlat+minlat)/2;
                double midlon=(maxlon+minlon)/2;
                LatLng llllll = new LatLng(midlat, midlon);
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
                //可以适当的减1
                float zoom = 18-j+3 ;
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(llllll, zoom);
                mBaiduMap.setMapStatus(u);
                mBaiduMap.animateMapStatus(u);
//                MapStatus.Builder builder = new MapStatus.Builder();
//                builder.target(ll).zoom(18.0f);
//                isfirst=false;
//                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }
    public static Bitmap getTransparentBitmap(Bitmap sourceImg, int number){
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];

        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg

                .getWidth(), sourceImg.getHeight());// 获得图片的ARGB值

        number = number * 255 / 100;

        for (int i = 0; i < argb.length; i++) {

            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);

        }

        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg

                .getHeight(), Bitmap.Config.ARGB_8888);

        return sourceImg;
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(TranceActivity.this, "定位服务未开启", Toast.LENGTH_SHORT).show();
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;

            if ( drivingRouteResult.getRouteLines().size() >= 1 ) {
                mBaiduMap.clear();

//
//                MarkerOptions ooA;
//                LatLng mark111=new LatLng(latitude2,longitude2);
//                ooA = new MarkerOptions().position(mark111).icon(adasd1)
//                        .zIndex(9).draggable(true);
//                Log.i("onSuccess: ", mark111.toString());
//                mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
                route = drivingRouteResult.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                routeOverlay = overlay;
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(drivingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                nowintere=0;
                searchInterest();
                if(isFirstruote)
                {
                    overlay.zoomToSpan();
                    isFirstruote=false;
                }
//                if(isTraced) {  startTimer();}

//                mBtnPre.setVisibility(View.VISIBLE);
//                mBtnNext.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }
    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {

            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_st);

        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            Log.i("getTerminalMarker: ", dev_name+"/"+dev_address+"/"+dev_last_time);
            BitmapDescriptor adasd=getBitmapDescriptor(dev_name,dev_address,dev_last_time);
            //调整图片的透明度
//            BitmapDescriptor adasd1=BitmapDescriptorFactory.fromBitmap(getTransparentBitmap(adasd.getBitmap(),60));
            return adasd;

        }
    }
    void searchInterest() {
        intraPoint = interestPoint.split(",");
        totalintrer = intraPoint.length;
        System.out.println(intraPoint.length + "-->" + nowintere);
        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.radius(5000);//距离
        option.pageCapacity(5);
        option.location(new LatLng(latitude2,longitude2));
        option.keyword(intraPoint[nowintere]);
        mPoiSearch.searchNearby(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearch != null) {
            mSearch.destroy();
        }
        mMapView.onDestroy();
        if(mLocClient!=null){
            mLocClient.stop();
        }
        if(timer!=null){
            timer.cancel();
        }

    }
}
