package com.example.yzcl.mvp.ui.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.yzcl.mvp.ui.TrajectoryActivity;
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
    private TextView waring;//显示低电报警用
    //可以下发指令
    private Boolean canxfzl=false;
    private SharedPreferences sp;
    @SuppressLint("ValidFragment")
    public DeviceMessageFragment(carDetailGPSBeans.carDetailGPSBean datalist){
        this.datalist=datalist;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //判断是否有权限
        sp=getActivity().getSharedPreferences("YZCL",Context.MODE_PRIVATE);

        Log.i(TAG, "onCreateView");
        View view=inflater.inflate(R.layout.fragment_device_message,null);
        device_name_type=view.findViewById(R.id.device_name_type);//设备名和设备类型
        warning_type=view.findViewById(R.id.warning_type);//报警类型
        waring=view.findViewById(R.id.waring);//低电报警
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
        checkqx();
        //给fragment页面赋值
        try {
            giveData();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void checkqx() {
        String list_Jurisdiction=sp.getString("list_Jurisdiction","");
        String[]list_jur=list_Jurisdiction.split(",");
        for(int i=0;i<list_jur.length;i++){
            if (list_jur[i].equals("100")){
                canxfzl=true;

            }
            if (list_jur[i].equals("165")){
                open_colse_loc.setVisibility(View.VISIBLE);

            }
            if (list_jur[i].equals("222")){
                trajectory.setVisibility(View.VISIBLE);
                break;
            }

        }
    }

    @SuppressLint("ResourceAsColor")
    private void giveData() throws java.text.ParseException {
        String sblx="";
        if(datalist.getCategory().equals("有线设备")){
            xfzl.setVisibility(View.GONE);
            dl.setVisibility(View.GONE);
            sblx="有线";
        }else{
            if(canxfzl){
                xfzl.setVisibility(View.VISIBLE);
            }else{
                xfzl.setVisibility(View.GONE);
            }

            dl.setVisibility(View.VISIBLE);
            sblx="无线";
        }
        device_name_type.setText(datalist.getInternalnum()+"/"+sblx);

        //报警类型
        if(datalist.getDgm().getAlarm()!=null){
            if (datalist.getDgm().getAlarm().equals("-1")){
                //断电
                warning_type.setVisibility(View.GONE);
//            warning_type.setTextColor(getResources().getColor(R.color.black));
            }else if(datalist.getDgm().getAlarm().equals("1")){
                //有线是拔除无线是光感
                warning_type.setVisibility(View.VISIBLE);
                warning_type.setBackgroundResource(R.drawable.bg_warning);

                if(datalist.getCategory().equals("有线设备")){

                    warning_type.setText("断电报警");
                }else{
                    warning_type.setText("光感异常");
                }

//            warning_type.setTextColor(getResources().getColor(R.color.black));
            }else if(datalist.getDgm().getAlarm().equals("0")){
                warning_type.setVisibility(View.VISIBLE);
                warning_type.setBackgroundResource(R.drawable.bg_normal);
                warning_type.setText("正常");
            }
            if(sblx.equals("有线")){

            }else{
                //电量
                if(datalist.getDgm().getBl().equals("-1")){
                    //没有电量
                    dl.setVisibility(View.GONE);
                }else{
                    if(Integer.parseInt(datalist.getDgm().getBl())>30){
                        //电量高于30%
                        dl.setBackgroundResource(R.drawable.green_dl_radius);
                        waring.setVisibility(View.GONE);
                        dl.setTextColor(getContext().getResources().getColor(R.color.tv_online));
                    }else{
                        //电量低于30%
                        //显示低电报警
                        if(datalist.getDgm().getAlarm().equals("0")){
                            //如果之前显示报警状态是正常的话，就在状态里面改
                            warning_type.setBackgroundResource(R.drawable.bg_warning);
                            warning_type.setText("低电报警");
                            waring.setVisibility(View.GONE);
                        }else{
//                    waring.setText("低电报警");
//                    waring.setVisibility(View.VISIBLE);
                        }

                        dl.setBackgroundResource(R.drawable.dl_radius);
                        dl.setTextColor(getContext().getResources().getColor(R.color.device_msg));
                    }
                    dl.setText("电量"+datalist.getDgm().getBl()+"%");
//            dl.setVisibility(View.VISIBLE);

                }
            }

        }else{
            //设备没定位
            dl.setVisibility(View.GONE);
        }



            if(datalist.getOnline_status().equals("在线")){
                //判断是否在线，在线隐藏离线时间
                offline_time.setVisibility(View.GONE);
                if(datalist.getDgm().getSpeed()!=null){
                    if(datalist.getDgm().getSpeed().equals("0")){
                        //在线的话速度为0状态就是静止
                        //设备状态
                        online_status.setText("静止");

                    }else{
                        online_status.setText("行驶中");
                    }
                    online_status.setTextColor(getContext().getResources().getColor(R.color.tv_online));
                }else{
                    online_status.setTextColor(getContext().getResources().getColor(R.color.tv_offline));
                    online_status.setText("暂无定位信息");
                }


            }else{
                if(datalist.getDgm().getStime()!=null){
                    online_status.setTextColor(getContext().getResources().getColor(R.color.tv_offline));
                    //计算时间差
                    long endtime=System.currentTimeMillis();
                    long starttme=stringToLong(datalist.getDgm().getStime(),"yyyy-MM-dd HH:mm:ss");
//            long starttme=datalist.getDgm().getTime();
                    long time_during=endtime-starttme;
                    long hours=time_during/(1000*60*60);//获取间隔小时
                    long mintues=time_during-(hours*(1000*60*60));//获取间隔分钟
                    mintues=mintues/(60*1000);
                    long days=hours/24;
                    hours=hours-days*24;
                    offline_time.setText("("+days+"天"+hours+"小时"+mintues+"分钟"+")");
                }else{
                    online_status.setTextColor(getContext().getResources().getColor(R.color.tv_offline));
                    online_status.setText("暂无定位信息");
                    offline_time.setVisibility(View.GONE);
                }


            }


        //最后定位时间
        if(datalist.getDgm().getTime()!=null){
            last_loc_time.setText(datalist.getDgm().getTime());
            //定位类型
            loc_type.setText(datalist.getDgm().getType());
            //最后定位地址
            if(datalist.getDgm().getPostion()!=null){
                loc_address.setText("最后定位地址:   "+datalist.getDgm().getPostion());
            }else{
                loc_address.setText("最后定位地址:   "+"当前无位置信息");
            }

        }else{
            last_loc_time.setText("");
            //定位类型
            loc_type.setText("");
            //最后定位地址
            loc_address.setText("最后定位地址:   "+"");
        }

        //导航
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datalist.getDgm().getBlat()!=null){
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
                }else{
                    Toast.makeText(getActivity(),"暂无定位",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //下发指令
        xfzl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datalist.getDgm().getDeviceId()!=null){
                    Intent intent=new Intent();
                    intent.putExtra("deviceid",datalist.getDgm().getDeviceId());
                    intent.setClass(getActivity(), XfzlActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(),"暂无定位",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //轨迹回放
        trajectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datalist.getDgm().getDeviceId()!=null){
                Intent intent=new Intent();
                intent.putExtra("deviceid",datalist.getDgm().getDeviceId());
                intent.putExtra("Internalnum",datalist.getInternalnum());//设备名
                intent.putExtra("Category",datalist.getCategory());//有线无线设备
                intent.putExtra("pledge_name",datalist.getPledge_name());
                intent.setClass(getActivity(), TrajectoryActivity.class);
                startActivity(intent);
                }else{
                    Toast.makeText(getActivity(),"暂无定位",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //追踪
        open_colse_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datalist.getDgm().getBlng()!=null&&datalist.getDgm().getDeviceId()!=null){
                Intent intent=new Intent();
                intent.putExtra("blat",datalist.getDgm().getBlat());
                intent.putExtra("blng",datalist.getDgm().getBlng());
                intent.putExtra("deviceid",datalist.getDgm().getDeviceId());
                intent.setClass(getActivity(), TranceActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getActivity(),"暂无定位",Toast.LENGTH_SHORT).show();
            }
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
