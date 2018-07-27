package com.example.yzcl.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.ui.CarAddressActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by 13126 on 2018/5/28.
 */

public class HorizonService extends Service{
    AlarmManager manager;
    PendingIntent pendingIntent1;
    SharedPreferences sp;
    int ii=0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("打印时间", startId+"onStartCommand: ");
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        //代表着开始服务
        //开启一个线程
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Log.i("run: 打印时间", new Date().toString());
                //请求接口
                achieve();


            }
        }).start();
//        开启alarmManager
//        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        int five=3600000;//这里设置间隔时间是5s
        long triggerAtTime= SystemClock.elapsedRealtime()+five;//获取的是系统启动后的时间
        Intent i=new Intent(this,AlarmReceiver.class);//跳转到这个广播接收器，让这个广播接收器运行这个服务
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,i,0);
        pendingIntent1=pendingIntent;


//        if(startId==5){
//            manager.cancel(pendingIntent);
//        }else{
            //我不需要手机关机运行，但是需要在休眠的时候也运行
        //定时服务
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
//        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void achieve() {
        if(!Constant.isNetworkConnected(getApplicationContext())) {
            //判断网络是否可用
//            Toast.makeText(getApplicationContext(), "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
        }else{
            //获取车辆的报警类型
            //通过vin获取车辆信息
            RequestParams params=new RequestParams();
            params.addHeader("Content-Type","application/json");
            JSONObject jsonObject=new JSONObject();
            params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
            HttpRequest.post(Api.queAlarmTopCount+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
                @Override
                protected void onSuccess(Headers headers, JSONObject jsonObject) {
                    super.onSuccess(headers, jsonObject);
                    Log.i("hORSER", jsonObject.toString());


                }

                @Override
                public void onFailure(int errorCode, String msg) {
                    super.onFailure(errorCode, msg);
                }
            });


        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager= (AlarmManager) getSystemService(ALARM_SERVICE);

        Log.i("打印时间", "onCreate: ");
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopservice会进入ondestory
        Log.i("打印时间", "onDestroy: ");
        manager.cancel(pendingIntent1);
    }

}
