package com.example.yzcl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.CarMonSearchListBean;
import com.example.yzcl.mvp.ui.CarMonSearchActivity;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/6/28.
 */

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SRViewHolder> {
    private ArrayList<CarMonSearchListBean.CarSearchBean> carSearchBeans;
    private CarMonSearchActivity context;
    public SearchRecyclerAdapter(CarMonSearchActivity context,ArrayList<CarMonSearchListBean.CarSearchBean> carSearchBeans){
        this.context=context;
        this.carSearchBeans=carSearchBeans;
    }

    @Override
    public SRViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_search_list,parent,false);
        SRViewHolder srViewHolder=new SRViewHolder(view);
        return srViewHolder;
    }

    @Override
    public void onBindViewHolder(SRViewHolder holder, int position) {
        CarMonSearchListBean.CarSearchBean carSearchBean=carSearchBeans.get(position);
        holder.tv_name.setText(carSearchBean.getName());
        holder.vin.setText("车架号:"+carSearchBean.getVin());
        if(carSearchBean.getCustom_team_id()!=null){
            //假设是有设备名的
            holder.device_names.setText(carSearchBean.getCustom_team_id());
            holder.device_names.setVisibility(View.VISIBLE);
        }else{
            holder.device_names.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return carSearchBeans.size();
    }

    class SRViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        private TextView vin;
        //貌似设备名现在还没有数据
        private TextView device_names;
        public SRViewHolder(View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            vin=itemView.findViewById(R.id.vin);
            device_names=itemView.findViewById(R.id.device_names);
        }
    }
}
