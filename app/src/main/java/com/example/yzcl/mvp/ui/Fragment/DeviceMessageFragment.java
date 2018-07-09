package com.example.yzcl.mvp.ui.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.TieBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.carDetailGPSBeans;
import com.example.yzcl.mvp.ui.TranceActivity;
import com.example.yzcl.mvp.ui.XfzlActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 2018/6/29.
 */

@SuppressLint("ValidFragment")
public class DeviceMessageFragment extends Fragment{
    private carDetailGPSBeans.carDetailGPSBean datalist;
    private String TAG="DeviceMessageFragment";
    private TextView device_name_type;//设备名和设备类型
    private TextView warning_type;//报警类型
    private TextView dl;//电量
    private TextView online_status;//设备状态
    private TextView offline_time;//离线时间
    private TextView last_loc_time;//最后定位时间
    private TextView loc_type;//定位类型
    private TextView loc_address;//最后定位地址
    private TextView open_colse_loc;//开关追踪
    private TextView xfzl;//下发指令
    private TextView trajectory;//轨迹
    private TextView navigation;//导航
    @SuppressLint("ValidFragment")
    public DeviceMessageFragment(carDetailGPSBeans.carDetailGPSBean datalist){
        this.datalist=datalist;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view=inflater.inflate(R.layout.fragment_device_message,null);
        device_name_type=view.findViewById(R.id.device_name_type);//设备名和设备类型
        warning_type=view.findViewById(R.id.warning_type);//报警类型
        dl=view.findViewById(R.id.dl);//电量
        online_status=view.findViewById(R.id.online_status);//设备状态
        offline_time=view.findViewById(R.id.offline_time);//离线时间
        last_loc_time=view.findViewById(R.id.last_loc_time);//最后定位时间
        loc_type=view.findViewById(R.id.loc_type);//定位类型
        loc_address=view.findViewById(R.id.loc_address);//最后定位地址
        open_colse_loc=view.findViewById(R.id.open_colse_loc);//开关追踪
        xfzl=view.findViewById(R.id.xfzl);//下发指令
        trajectory=view.findViewById(R.id.trajectory);//轨迹
        navigation=view.findViewById(R.id.navigation);//导航
        //给fragment页面赋值
        try {
            giveData();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return view;
    }

    @SuppressLint("ResourceAsColor")
    private void giveData() throws java.text.ParseException {
        device_name_type.setText(datalist.getInternalnum()+"/"+datalist.getCategory());
        if(datalist.getCategory().equals("有线设备")){
            xfzl.setVisibility(View.GONE);
        }else{
            xfzl.setVisibility(View.VISIBLE);
        }
        //报警类型
        if (datalist.getDgm().getAlarm().equals("-1")){
            //断电
            warning_type.setVisibility(View.GONE);
//            warning_type.setTextColor(getResources().getColor(R.color.black));
        }else if(datalist.getDgm().getAlarm().equals("1")){
            //有线是拔除无线是光感
            warning_type.setVisibility(View.VISIBLE);
            warning_type.setBackgroundResource(R.drawable.bg_warning);

            if(datalist.getCategory().equals("有线设备")){

                warning_type.setText("拔除");
            }else{
                warning_type.setText("光感异常");
            }

//            warning_type.setTextColor(getResources().getColor(R.color.black));
        }else if(datalist.getDgm().getAlarm().equals("0")){
            warning_type.setVisibility(View.VISIBLE);
            warning_type.setBackgroundResource(R.drawable.bg_normal);
            warning_type.setText("正常");
        }
        //电量
        if(datalist.getDgm().getBl().equals("-1")){
            //没有电量
            dl.setVisibility(View.GONE);
        }else{
            dl.setVisibility(View.VISIBLE);
            dl.setText("电量"+datalist.getDgm().getBl()+"%");
        }
        //设备状态
        online_status.setText(datalist.getOnline_status());
        if(datalist.getOnline_status().equals("在线")){
            //判断是否在线，在线隐藏离线时间
            offline_time.setVisibility(View.GONE);
        }else{
            //计算时间差
            long endtime=System.currentTimeMillis();
            long starttme=stringToLong(datalist.getDgm().getTime(),"yyyy-MM-dd HH:mm:ss");
//            long starttme=datalist.getDgm().getTime();
            long time_during=endtime-starttme;
            long hours=time_during/(1000*60*60);//获取间隔小时
            long mintues=time_during-(hours*(1000*60*60));//获取间隔分钟
            mintues=mintues/(60*1000);
            long days=hours/24;
            hours=hours-days*24;
            offline_time.setText("("+days+"天"+hours+"小时"+mintues+"分钟"+")");

        }
        //最后定位时间
        last_loc_time.setText(datalist.getDgm().getTime());
        //定位类型
        loc_type.setText(datalist.getDgm().getType());
        //最后定位地址
        loc_address.setText("最后定位地址:"+datalist.getDgm().getPostion());
        //导航
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //召唤弹出框
                List<TieBean> strings = new ArrayList<TieBean>();
                strings.add(new TieBean("百度地图"));
                strings.add(new TieBean("高德地图"));
                strings.add(new TieBean("腾讯地图"));
                DialogUIUtils.showSheet(getActivity(), strings, "取消", Gravity.BOTTOM, true, true, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        switch (position){
                            case 0:
                                //先判断是否安装了第三方地图软件
                                if(isPackageInstalled("com.baidu.BaiduMap")){
                                    Toast.makeText(getActivity(),"正在为你打开百度地图",Toast.LENGTH_SHORT).show();
                                    // 百度地图
                                    Intent naviIntent = new Intent("android.intent.action.VIEW", Uri.parse("baidumap://map/geocoder?location=" + datalist.getDgm().getBlat() + "," +datalist.getDgm().getBlng() ));
                                    getContext().startActivity(naviIntent);
                                }else{
                                    Toast.makeText(getActivity(),"您未安装百度地图",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                if(isPackageInstalled("com.autonavi.minimap")){
                                    Toast.makeText(getActivity(),"正在为你打开高德地图",Toast.LENGTH_SHORT).show();
                                    // 高德地图
                                    Log.i(TAG, "onItemClick: "+ datalist.getDgm().getBlat() +","+datalist.getDgm().getBlng());
                                    Intent naviIntent = new Intent("android.intent.action.VIEW", Uri.parse("androidamap://route?sourceApplication=appName&slat=&slon=&sname=我的位置&dlat="+ datalist.getDgm().getBlat() +"&dlon="+datalist.getDgm().getBlng() +"&dname=目的地&dev=0&t=2"));
                                    getContext().startActivity(naviIntent);
                                }else{
                                    Toast.makeText(getActivity(),"您未安装高德地图",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:
                                if(isPackageInstalled("com.tencent.map")){
                                    // 腾讯地图

                                    Intent naviIntent = new Intent("android.intent.action.VIEW", Uri.parse("qqmap://map/routeplan?type=drive&from=&fromcoord=&to=目的地&tocoord=" + datalist.getDgm().getBlat() + "," + datalist.getDgm().getBlng() + "&policy=0&referer=appName"));
                                    getContext().startActivity(naviIntent);
                                    Toast.makeText(getActivity(),"正在为你打开腾讯地图",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity(),"您未安装腾讯地图",Toast.LENGTH_SHORT).show();
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
        //下发指令
        xfzl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("deviceid",datalist.getDgm().getDeviceId());
                intent.setClass(getActivity(), XfzlActivity.class);
                startActivity(intent);
            }
        });
        //追踪
        open_colse_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("blat",datalist.getDgm().getBlat());
                intent.putExtra("blng",datalist.getDgm().getBlng());
                intent.putExtra("deviceid",datalist.getDgm().getDeviceId());
                intent.setClass(getActivity(), TranceActivity.class);
                startActivity(intent);
            }
        });
    }
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws java.text.ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws  java.text.ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }
    //判断是否安装第三方软件
    public static boolean isPackageInstalled(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
    }
}
