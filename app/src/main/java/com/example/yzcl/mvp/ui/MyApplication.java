package com.example.yzcl.mvp.ui;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.yzcl.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

/**
 * Created by Lenovo on 2017/11/28.
 */

public class MyApplication extends Application {
    //进行各类初始化，包括推送，地图，其他
    public static List<?> images=new ArrayList<>();
    public static List<String> titles=new ArrayList<>();
    public static int H,W;
    public static MyApplication app;
    private Context mContext;

    public static MyApplication getInstance() {
        return app;
    }
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        app=this;

        String[] urls = getResources().getStringArray(R.array.url);
        String[] tips = getResources().getStringArray(R.array.title);
        List list = Arrays.asList(urls);
        images = new ArrayList(list);
        List list1 = Arrays.asList(tips);
        titles= new ArrayList(list1);
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());
    }
}
