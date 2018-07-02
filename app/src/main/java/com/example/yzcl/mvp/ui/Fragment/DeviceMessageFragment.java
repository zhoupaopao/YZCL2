package com.example.yzcl.mvp.ui.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.carDetailGPSBeans;

import java.util.HashMap;

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
        giveData();
        return view;
    }

    private void giveData() {
        device_name_type.setText(datalist.getInternalnum()+"/"+datalist.getCategory());
        if(datalist.getCategory().equals("有线设备")){
            xfzl.setVisibility(View.GONE);
        }else{
            xfzl.setVisibility(View.VISIBLE);
        }
        //报警类型
        if (datalist.getDgm().getAlarm().equals("-1")){
            //断电
            warning_type.setText("断电");
//            warning_type.setTextColor(getResources().getColor(R.color.black));
        }else{
            warning_type.setText("无");
//            warning_type.setTextColor(getResources().getColor(R.color.black));
        }
        //电量
        if(datalist.getDgm().getBl().equals("-1")){
            //没有电量
            dl.setText("断电");
        }else{
            dl.setText("电量"+datalist.getDgm().getBl()+"%");
        }
        //设备状态
        online_status.setText(datalist.getOnline_status());
        //最后定位时间
        last_loc_time.setText(datalist.getDgm().getTime());
        //定位类型
        loc_type.setText(datalist.getDgm().getType());
        //最后定位地址
        loc_address.setText("最后定位地址:"+datalist.getDgm().getPostion());

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
