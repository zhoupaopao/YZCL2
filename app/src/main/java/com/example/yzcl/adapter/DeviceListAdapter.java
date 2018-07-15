package com.example.yzcl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.CarDeviceBean;

/**
 * Created by 13126 on 2018/7/2.
 */

public class DeviceListAdapter extends BaseAdapter {
    private CarDeviceBean carDeviceBean;
    private Context context;
    public DeviceListAdapter(Context context,CarDeviceBean carDeviceBean){
        this.carDeviceBean=carDeviceBean;
        this.context=context;
    }
    @Override
    public int getCount() {
        return carDeviceBean.getList().size();
    }

    @Override
    public Object getItem(int i) {
        return carDeviceBean.getList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null){
            viewHolder=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.list_device,viewGroup,false);
            viewHolder.bind_time=view.findViewById(R.id.bind_time);
            viewHolder.device_name=view.findViewById(R.id.device_name);
            viewHolder.install_loc=view.findViewById(R.id.install_loc);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        String bindtime=carDeviceBean.getList().get(i).getBindtime();
//        String[]aaa=bindtime.split(" ");
        String aaatime=bindtime.substring(0,bindtime.length()-3);
        viewHolder.bind_time.setText("绑车时间:"+aaatime);
        String sblx="";
        if(carDeviceBean.getList().get(i).getDevicetypename().equals("有线设备")){
            sblx="有线";
        }else{
            sblx="无线";
        }
        viewHolder.device_name.setText(carDeviceBean.getList().get(i).getInternalnum()+"/"+sblx);
        viewHolder.install_loc.setText("安装位置："+carDeviceBean.getList().get(i).getInstall_part());
        return view;
    }
    private class ViewHolder{
        TextView device_name;
        TextView bind_time;
        TextView install_loc;
    }
}
