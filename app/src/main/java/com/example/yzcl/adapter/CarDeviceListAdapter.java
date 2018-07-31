package com.example.yzcl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.DeviceListBean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/7/30.
 */

public class CarDeviceListAdapter extends BaseRecyclerAdapter<CarDeviceListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DeviceListBean.DeviceLLBean>list;
    public CarDeviceListAdapter(Context context,ArrayList<DeviceListBean.DeviceLLBean>list){
        this.context=context;
        this.list=list;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view=LayoutInflater.from(context).inflate(R.layout.list_device_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, boolean isItem) {
        //处理数据
        DeviceListBean.DeviceLLBean deviceLLBean=list.get(position);
        holder.tv_name.setText(deviceLLBean.getInternalnum()+"-"+deviceLLBean.getDevicetype());


    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView device_status;
        TextView warning_name;
        TextView dl;
        TextView car_message;
        TextView car_loc;
        TextView xfzl;
        TextView look_address;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            device_status=itemView.findViewById(R.id.device_status);
            warning_name=itemView.findViewById(R.id.warning_name);
            dl=itemView.findViewById(R.id.dl);
            car_message=itemView.findViewById(R.id.car_message);
            car_loc=itemView.findViewById(R.id.car_loc);
            xfzl=itemView.findViewById(R.id.xfzl);
            look_address=itemView.findViewById(R.id.look_address);

        }
    }
}
