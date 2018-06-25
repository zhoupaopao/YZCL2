package com.example.yzcl.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.CarListBean;
import com.example.yzcl.mvp.ui.MyCarListActivity;

import java.util.List;

/**
 * Created by Lenovo on 2018/2/3.
 */

public class CarListAdapter1 extends BaseAdapter {
    private MyCarListActivity context;
    private List<CarListBean.CarBean> carlist;
    private LayoutInflater mInflater;
    public CarListAdapter1(MyCarListActivity context, List<CarListBean.CarBean> carlist){
        this.context=context;
        this.carlist=carlist;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return carlist.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View contentView, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(contentView==null){
            contentView=mInflater.inflate(R.layout.layout_car_list_item,null);
            viewHolder=new ViewHolder();
            viewHolder.gz=contentView.findViewById(R.id.gz);
            viewHolder.name=contentView.findViewById(R.id.name);
            viewHolder.pic_status=contentView.findViewById(R.id.pic_status);
            viewHolder.vinid=contentView.findViewById(R.id.vinid);
            viewHolder.equipment=contentView.findViewById(R.id.equipment);
            viewHolder.data_bind_car=contentView.findViewById(R.id.data_bind_car);
            viewHolder.belong=contentView.findViewById(R.id.belong);
            viewHolder.handle_car=contentView.findViewById(R.id.handle_car);
            viewHolder.address=contentView.findViewById(R.id.address);
            viewHolder.detail=contentView.findViewById(R.id.detail);
            viewHolder.yuqi=contentView.findViewById(R.id.yuqi);
            viewHolder.Settle=contentView.findViewById(R.id.Settle);
            contentView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) contentView.getTag();
        }
        //开始处理数据
        CarListBean.CarBean carBean=carlist.get(i);
        //借款人姓名
        viewHolder.name.setText(carBean.getPledgename());
        //显示逾期按钮
        if(carBean.getIsoverdue()==1){
            //逾期
            //讲逾期图标可见
            viewHolder.pic_status.setVisibility(View.VISIBLE);
            //逾期按钮设置为取消逾期
            viewHolder.yuqi.setText("取消逾期");
        }else{
            //没有逾期
            //讲逾期图标不可见
            viewHolder.pic_status.setVisibility(View.GONE);
            //逾期按钮设置为=逾期
            viewHolder.yuqi.setText("逾期");
        }
        viewHolder.vinid.setText(carBean.getVin());
        viewHolder.equipment.setText(carBean.getDevicecount()+"个");
        viewHolder.data_bind_car.setText(carBean.getCreatetime());
        viewHolder.belong.setText(carBean.getGroupname());
        //是否重点关注
        if(carBean.getIscare()==1){
            //重点关注
            viewHolder.gz.setText("已关注");
            viewHolder.gz.setBackgroundResource(R.drawable.button_radius_change_gzed);
        }else{
            viewHolder.gz.setText("关注");
            viewHolder.gz.setBackgroundResource(R.drawable.button_radius_change);
        }

        return contentView;
    }
    public static class ViewHolder{
        private Button gz;
        private TextView name;
        private ImageView pic_status;
        private TextView vinid;
        private TextView equipment;
        private TextView data_bind_car;
        private TextView belong;
        private RadioGroup handle_car;
        private RadioButton address;
        private RadioButton detail;
        private RadioButton yuqi;
        private RadioButton Settle;
    }
}
