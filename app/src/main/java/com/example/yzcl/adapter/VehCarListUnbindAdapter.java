package com.example.yzcl.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.CarDetailBeans;
import com.example.yzcl.mvp.model.bean.DeviceListBean;
import com.example.yzcl.mvp.ui.DeviceAddressActivity;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/8/7.
 */

public class VehCarListUnbindAdapter  extends BaseRecyclerAdapter<VehCarListUnbindAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DeviceListBean.DeviceLLBean> list;
    public VehCarListUnbindAdapter(Context context, ArrayList<DeviceListBean.DeviceLLBean> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_item_car_unbind,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, boolean isItem) {
        final DeviceListBean.DeviceLLBean deviceLLBean=list.get(position);
        holder.dev_name.setText(deviceLLBean.getInternalnum());
        holder.line_unbind.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dev_name;
        LinearLayout line_unbind;
        public ViewHolder(View itemView) {
            super(itemView);
            dev_name=itemView.findViewById(R.id.dev_name);
            line_unbind=itemView.findViewById(R.id.line_unbind);

        }
    }
}
