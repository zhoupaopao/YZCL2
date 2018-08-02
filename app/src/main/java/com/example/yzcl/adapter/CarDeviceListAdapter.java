package com.example.yzcl.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.DeviceListBean;
import com.example.yzcl.mvp.ui.DeviceAddressActivity;
import com.example.yzcl.mvp.ui.XfzlActivity;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/7/30.
 */

public class CarDeviceListAdapter extends BaseRecyclerAdapter<CarDeviceListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DeviceListBean.DeviceLLBean>list;
    private SharedPreferences sp;
    private boolean canxfzl=false;
    public CarDeviceListAdapter(Context context,ArrayList<DeviceListBean.DeviceLLBean>list){
        this.context=context;
        this.list=list;
        sp=context.getSharedPreferences("YZCL",Context.MODE_PRIVATE);
        checkqx();
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
        final DeviceListBean.DeviceLLBean deviceLLBean=list.get(position);
        holder.tv_name.setText(deviceLLBean.getInternalnum()+"-"+deviceLLBean.getDevicetype());
        holder.device_status.setText(deviceLLBean.getGpsStates());
        if(deviceLLBean.getGpsStates().substring(0,2).equals("休眠")||deviceLLBean.getGpsStates().substring(0,2).equals("静止")){
            holder.device_status.setTextColor(context.getResources().getColor(R.color.device_status_xm));
        }else if(deviceLLBean.getGpsStates().substring(0,2).equals("离线")){
            holder.device_status.setTextColor(context.getResources().getColor(R.color.tv_offline));
        }else if(deviceLLBean.getGpsStates().equals("行驶中")||deviceLLBean.getGpsStates().equals("在线")){
            holder.device_status.setTextColor(context.getResources().getColor(R.color.tv_online));
        }else{
            holder.device_status.setTextColor(context.getResources().getColor(R.color.black));
        }
        if(deviceLLBean.getAlaram().equals("正常")){
            //没有报警
            holder.warning_name.setText("暂无报警");
            holder.warning_name.setTextColor(context.getResources().getColor(R.color.black));
        }else{
            holder.warning_name.setText(deviceLLBean.getAlaram());
            holder.warning_name.setTextColor(context.getResources().getColor(R.color.tv_offline));
        }

        if(deviceLLBean.getDevicetype().equals("有线设备")){
            holder.dl.setVisibility(View.GONE);
            holder.xfzl.setVisibility(View.GONE);
        }else{
            if(canxfzl){
                holder.xfzl.setVisibility(View.VISIBLE);
            }else{
                holder.xfzl.setVisibility(View.GONE);
            }
            if(deviceLLBean.getBl()!=null){
                holder.dl.setVisibility(View.VISIBLE);
//                holder.xfzl.setVisibility(View.VISIBLE);
                holder.dl.setText("电量"+deviceLLBean.getBl()+"%");
                if(Integer.parseInt(deviceLLBean.getBl())<30){
                    holder.dl.setTextColor(context.getResources().getColor(R.color.tv_warning));
                }else{
                    holder.dl.setTextColor(context.getResources().getColor(R.color.tv_online));
                }
            }else{
                holder.dl.setVisibility(View.GONE);
//                holder.xfzl.setVisibility(View.VISIBLE);

            }

        }
        //绑车信息
        if(deviceLLBean.getBindtime()!=null){
            //有绑车时间
            holder.car_message.setText("绑车信息："+deviceLLBean.getPledgerName()+","+deviceLLBean.getVin());
        }else{
            //未绑车
            holder.car_message.setText("绑车信息：未绑车");
            holder.device_status.setVisibility(View.GONE);
            holder.warning_name.setVisibility(View.GONE);
        }
        if(deviceLLBean.getGpsStates().equals("未定位")){
            holder.car_loc.setText("当前定位：设备未开启，无法获取GPS定位信息");
        }else{
            holder.car_loc.setText("当前定位："+deviceLLBean.getLastLocTime()+"，"+deviceLLBean.getPostion());
        }

        holder.xfzl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("deviceid",deviceLLBean.getId());
                intent.setClass(context, XfzlActivity.class);
                context.startActivity(intent);
            }
        });
        holder.look_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查看定位
                //没有定位
                if(deviceLLBean.getGpsStates().equals("未定位")||deviceLLBean.getBlat()==null){
                    Toast.makeText(context,"设备未定位",Toast.LENGTH_SHORT).show();
                }else{
                    //到位置页面
                    Intent intent=new Intent();
                    intent.putExtra("blat",deviceLLBean.getBlat());
                    intent.putExtra("blng",deviceLLBean.getBlng());
                    intent.putExtra("Internalnum",deviceLLBean.getInternalnum());
                    intent.putExtra("Devicetype",deviceLLBean.getDevicetype());
                    intent.putExtra("Alaram",deviceLLBean.getAlaram());
                    intent.putExtra("GpsStates",deviceLLBean.getGpsStates());
                    intent.putExtra("Bl",deviceLLBean.getBl());
                    intent.putExtra("LastLocTime",deviceLLBean.getLastLocTime());

                    intent.putExtra("Postion",deviceLLBean.getPostion());
                    intent.putExtra("Bindtime",deviceLLBean.getBindtime());
                    intent.putExtra("PledgerName",deviceLLBean.getPledgerName());
                    intent.putExtra("Vin",deviceLLBean.getVin());
                    intent.putExtra("Valid_from",deviceLLBean.getValid_from());
                    intent.putExtra("Valid_end",deviceLLBean.getValid_end());
                    intent.putExtra("Group_name",deviceLLBean.getGroup_name());
                    intent.putExtra("Id",deviceLLBean.getId());

                    intent.setClass(context, DeviceAddressActivity.class);
                    context.startActivity(intent);
                }
            }
        });



    }
    private void checkqx() {
        String list_Jurisdiction=sp.getString("list_Jurisdiction","");
        String[]list_jur=list_Jurisdiction.split(",");
        for(int i=0;i<list_jur.length;i++){
            if (list_jur[i].equals("100")){
                canxfzl=true;

            }

        }
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
