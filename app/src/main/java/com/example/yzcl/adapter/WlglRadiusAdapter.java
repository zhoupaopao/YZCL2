package com.example.yzcl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.WlglRadiusBean;
import com.example.yzcl.mvp.ui.EnclosureRadiusManagerActivity;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/2/1.
 */

public class WlglRadiusAdapter extends BaseAdapter {
private ArrayList<WlglRadiusBean.EnclosureBean>datalist;
private Context context;
private LayoutInflater mInflater;
String all_name="";
private EnclosureRadiusManagerActivity.AllCheckListener allCheckListener;

    public ArrayList<WlglRadiusBean.EnclosureBean> getDatalist() {
        return datalist;
    }

    public WlglRadiusAdapter(Context context, ArrayList<WlglRadiusBean.EnclosureBean> datalist, EnclosureRadiusManagerActivity.AllCheckListener allCheckListener){
        this.context=context;
        this.datalist=datalist;
        this.allCheckListener=allCheckListener;
        mInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View contentView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        //初始化
        if(contentView==null){
            contentView=mInflater.inflate(R.layout.layout_wlgl_list_radius,null);
            viewHolder=new ViewHolder();
            //围栏关联的车辆数量
            viewHolder.contect_car_num=contentView.findViewById(R.id.contect_car_num);
            //围栏名字
            viewHolder.wlgl_name=contentView.findViewById(R.id.wlgl_name);
            //进围栏布局（图标+字）判断是否显示
            viewHolder.jin_ll=contentView.findViewById(R.id.jin_ll);
            //出围栏布局（图标+字）判断是否显示
            viewHolder.chu_ll=contentView.findViewById(R.id.chu_ll);
            //进图标，判断显示图标是什么
            viewHolder.jin_img=contentView.findViewById(R.id.jin_img);
            //出图标，判断显示图标是什么
            viewHolder.chu_img=contentView.findViewById(R.id.chu_img);
            //选择框
            viewHolder.cb=contentView.findViewById(R.id.cb);
            contentView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) contentView.getTag();
        }
        final WlglRadiusBean.EnclosureBean enclosureBean=datalist.get(i);
        //开始赋值，处理事件
        viewHolder.wlgl_name.setText(enclosureBean.getFencename());
        // 围栏形状(1:圆形;2:多边形;3:区域围栏)
//        enclosureBean.getFencearea();
        //围栏类型 0:出围栏,1:进围栏 2进出围栏 ,
        //按钮的亮暗代表围栏是否开启
        switch (enclosureBean.getFencetype()){
            case 0:
                //进围栏布局取消
                viewHolder.jin_ll.setVisibility(View.GONE);
                viewHolder.chu_ll.setVisibility(View.VISIBLE);
                //判断是否有效，1：有效，0：无效，通过按钮 亮暗显示,这里不用管进围栏
                if(enclosureBean.getIsactive()==1){
                    //有效
                    viewHolder.chu_img.setImageResource(R.mipmap.u5920);
                }else{
                    //无效
                    viewHolder.chu_img.setImageResource(R.mipmap.u5979);
                }
                break;
            case 1:
                //出围栏布局取消
                viewHolder.chu_ll.setVisibility(View.GONE);
                viewHolder.jin_ll.setVisibility(View.VISIBLE);
                if(enclosureBean.getIsactive()==1){
                    //有效
                    viewHolder.jin_img.setImageResource(R.mipmap.u5920);
                }else{
                    //无效
                    viewHolder.jin_img.setImageResource(R.mipmap.u5979);
                }
                break;
            case 2:
                //都显示
                viewHolder.jin_ll.setVisibility(View.VISIBLE);
                viewHolder.chu_ll.setVisibility(View.VISIBLE);
                if(enclosureBean.getIsactive()==1){
                    //有效

                    viewHolder.jin_img.setImageResource(R.mipmap.u5920);
                    viewHolder.chu_img.setImageResource(R.mipmap.u5920);
                }else{
                    //无效
                    viewHolder.jin_img.setImageResource(R.mipmap.u5979);
                    viewHolder.chu_img.setImageResource(R.mipmap.u5979);
                }
                break;
        }
        viewHolder.cb.setChecked(enclosureBean.isCb());
        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enclosureBean.isCb()){
                    //取消选中
                    datalist.get(i).setCb(false);
                    allCheckListener.onCheckedChanged(false);
                }else{
                    datalist.get(i).setCb(true);
                }
            }
        });
//        viewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//                    //被勾选了
//                    datalist.get(i).setCb(true);
//                }else{
//                    //勾选取消
//                    datalist.get(i).setCb(false);
//                }
//            }
//        });
        final ViewHolder finalViewHolder = viewHolder;
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalViewHolder.cb.isChecked()){
                    finalViewHolder.cb.setChecked(false);
                    datalist.get(i).setCb(false);
                    allCheckListener.onCheckedChanged(false);
                }else{
                    finalViewHolder.cb.setChecked(true);
                    datalist.get(i).setCb(true);
                    //更改全选按钮的状态
//                    allCheckListener.onCheckedChanged(true);


                }
            }
        });


        return contentView;
    }
    public  static class ViewHolder{
        private TextView wlgl_name;
        private TextView contect_car_num;
        private LinearLayout jin_ll;
        private LinearLayout chu_ll;
        private ImageView jin_img;
        private ImageView chu_img;
        private CheckBox cb;
    }
}
