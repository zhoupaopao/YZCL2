package com.example.yzcl.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.CarListBean;
import com.example.yzcl.mvp.ui.MyCarListActivity;

import java.util.List;

/**
 * Created by Lenovo on 2018/2/3.
 */

public class CarListAdapter1 extends RecyclerView.Adapter<CarListAdapter1.ViewHolder> {
    private MyCarListActivity context;
    private List<CarListBean.CarBean> carlist;
    private LayoutInflater mInflater;
    public CarListAdapter1(MyCarListActivity context, List<CarListBean.CarBean> carlist){
        this.context=context;
        this.carlist=carlist;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.layout_car_list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//开始处理数据
        CarListBean.CarBean carBean=carlist.get(position);
        //借款人姓名
        holder.name.setText(carBean.getPledgename());
        //显示逾期按钮
        if(carBean.getStatus().equals("1")){
            //正常，图标不可见
            holder.car_status.setVisibility(View.GONE);
        }else{
            //没有逾期
            //讲逾期图标不可见
            holder.car_status.setText(carBean.getSign_status());
            holder.car_status.setVisibility(View.VISIBLE);
        }
        holder.vinid.setText(carBean.getVin());
        if(carBean.getDevices()!=null){
            StringBuffer sb=new StringBuffer();
            for(int j=0;j<carBean.getDevices().size();j++){
                sb.append(carBean.getDevices().get(j).getInternalnum()+"/");
            }
            String equipments=sb.toString();
            holder.equipment.setText(equipments.substring(0,equipments.length()-1));
        }else{
            holder.equipment.setText("未绑设备");
        }

        holder.data_bind_car.setText(carBean.getCreatetime());
        holder.belong.setText(carBean.getGroupname());
    }

    @Override
    public int getItemCount() {
        return carlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Button gz;
        private TextView name;
        private TextView car_status;
        private TextView vinid;
        private TextView equipment;
        private TextView data_bind_car;
        private TextView belong;
        private RadioGroup handle_car;
        private RadioButton address;
        private RadioButton detail;
        private RadioButton mark;

        public ViewHolder(View itemView) {
            super(itemView);
            gz=itemView.findViewById(R.id.gz);
            name=itemView.findViewById(R.id.name);
            car_status=itemView.findViewById(R.id.car_status);
            vinid=itemView.findViewById(R.id.vinid);
            equipment=itemView.findViewById(R.id.equipment);
            data_bind_car=itemView.findViewById(R.id.data_bind_car);
            belong=itemView.findViewById(R.id.belong);
            handle_car=itemView.findViewById(R.id.handle_car);
            address=itemView.findViewById(R.id.address);
            detail=itemView.findViewById(R.id.detail);
            mark=itemView.findViewById(R.id.mark);
        }
    }
    public void setupRecyclerView(RecyclerView recyclerView) {
        if (recyclerView != null) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        }
    }
}
