package com.example.yzcl.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.CarDetailBeans;
import com.example.yzcl.mvp.ui.CarAddressActivity;
import com.example.yzcl.mvp.ui.CarMonSearchActivity;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Lenovo on 2018/8/2.
 */

public class VehCarListAdapter extends BaseRecyclerAdapter<VehCarListAdapter.ViewHolder> {
    private Context context;
    private  ArrayList<CarDetailBeans.CarDetailBean> list;
    public VehCarListAdapter(Context context, ArrayList<CarDetailBeans.CarDetailBean> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_item_car_veh,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, boolean isItem) {
        CarDetailBeans.CarDetailBean carDetailBean=list.get(position);
        holder.device1.setVisibility(View.GONE);
        holder.device2.setVisibility(View.GONE);
        holder.device3.setVisibility(View.GONE);
        holder.car_name.setText(carDetailBean.getName());
        holder.vin.setText(carDetailBean.getVin());//没有车牌号，先不填
        //遍历所有的设备
        for(int i=0;i<carDetailBean.getDevice().size();i++){
            switch (i){
                case 0:
                    holder.device1.setVisibility(View.VISIBLE);
                    holder.device1.setText(carDetailBean.getDevice().get(i).getInternalnum());
                    //判断在离线
                    if(carDetailBean.getDevice().get(i).getOnline_status().equals("离线")){
                        holder.device1.setBackgroundResource(R.drawable.dl_radius);
                        holder.device1.setTextColor(context.getResources().getColor(R.color.device_msg));
                    }else if(carDetailBean.getDevice().get(i).getOnline_status().equals("在线")){
                        holder.device1.setBackgroundResource(R.drawable.green_dl_radius);
                        holder.device1.setTextColor(context.getResources().getColor(R.color.tv_online));
                    }else if(carDetailBean.getDevice().get(i).getOnline_status().equals("未定位")){
                        holder.device1.setBackgroundResource(R.drawable.gray_dl_radius);
                        holder.device1.setTextColor(context.getResources().getColor(R.color.biankuang));
                    }
                    break;
                case 1:
                    holder.device2.setVisibility(View.VISIBLE);
                    holder.device2.setText(carDetailBean.getDevice().get(i).getInternalnum());
                    //判断在离线
                    if(carDetailBean.getDevice().get(i).getOnline_status().equals("离线")){
                        holder.device2.setBackgroundResource(R.drawable.dl_radius);
                        holder.device2.setTextColor(context.getResources().getColor(R.color.device_msg));
                    }else if(carDetailBean.getDevice().get(i).getOnline_status().equals("在线")){
                        holder.device2.setBackgroundResource(R.drawable.green_dl_radius);
                        holder.device2.setTextColor(context.getResources().getColor(R.color.tv_online));
                    }else if(carDetailBean.getDevice().get(i).getOnline_status().equals("未定位")){
                        holder.device2.setBackgroundResource(R.drawable.gray_dl_radius);
                        holder.device2.setTextColor(context.getResources().getColor(R.color.biankuang));
                    }
                    break;
                case 2:
                    holder.device3.setVisibility(View.VISIBLE);
                    holder.device3.setText(carDetailBean.getDevice().get(i).getInternalnum());
                    //判断在离线
                    if(carDetailBean.getDevice().get(i).getOnline_status().equals("离线")){
                        holder.device3.setBackgroundResource(R.drawable.dl_radius);
                        holder.device3.setTextColor(context.getResources().getColor(R.color.device_msg));
                    }else if(carDetailBean.getDevice().get(i).getOnline_status().equals("在线")){
                        holder.device3.setBackgroundResource(R.drawable.green_dl_radius);
                        holder.device3.setTextColor(context.getResources().getColor(R.color.tv_online));
                    }else if(carDetailBean.getDevice().get(i).getOnline_status().equals("未定位")){
                        holder.device3.setBackgroundResource(R.drawable.gray_dl_radius);
                        holder.device3.setTextColor(context.getResources().getColor(R.color.biankuang));
                    }
                    break;

            }
        }
        if(carDetailBean.getDevice().size()<4){
            //不用加...
            holder.device_num.setText("  共"+carDetailBean.getDevice().size()+"个设备");
        }else{
            holder.device_num.setText("...  共"+carDetailBean.getDevice().size()+"个设备");
        }
//        holder.line_car_address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ArrayList<com.example.yzcl.mvp.model.bean.carDetailGPSBeans.carDetailGPSBean>carDetailGPSBean=carDetailGPSBeans.getList();
//                Intent intent=new Intent();
//                intent.setClass(context,CarAddressActivity.class);
////                                    Bundle b=new Bundle();
////                                    b.put("pose_title", pose_title);
////                                    intent.putExtras(b);
//                intent.putExtra("carDetailGPS", jsonObject.get("list").toString());
////                                        intent.putExtra("carDetailGPS", carSearchBean.getId());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView car_name;
        TextView vin;
        TextView device1;
        TextView device2;
        TextView device3;
        TextView device_num;
        LinearLayout line_car_address;

        public ViewHolder(View itemView) {
            super(itemView);
            car_name=itemView.findViewById(R.id.car_name);
            vin=itemView.findViewById(R.id.vin);
            device1=itemView.findViewById(R.id.device1);
            device2=itemView.findViewById(R.id.device2);
            device3=itemView.findViewById(R.id.device3);
            device_num=itemView.findViewById(R.id.device_num);
            line_car_address=itemView.findViewById(R.id.line_car_address);
        }
    }
}
