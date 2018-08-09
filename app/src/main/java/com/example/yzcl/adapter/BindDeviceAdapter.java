package com.example.yzcl.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.TieBean;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.dou361.dialogui.listener.DialogUIListener;
import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.BindDeviceBean;
import com.example.yzcl.mvp.ui.DeviceInstallAddActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2018/8/9.
 */

public class BindDeviceAdapter extends BaseRecyclerAdapter<BindDeviceAdapter.ViewHolder> {
    private Context context;
    private ArrayList<BindDeviceBean.BindDeviceBeanMsg>list;
    public BindDeviceAdapter(Context context,ArrayList<BindDeviceBean.BindDeviceBeanMsg>list){
        this.context=context;
        this.list=list;
    }
    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_device_add,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position, boolean isItem) {
        BindDeviceBean.BindDeviceBeanMsg bindDeviceBeanMsg=list.get(position);
        holder.dev_name.setText("设备名称："+bindDeviceBeanMsg.getInternalnum());
        holder.dev_type.setText("设备类型："+bindDeviceBeanMsg.getCategory());
        if(bindDeviceBeanMsg.getLoc()!=null){
            holder.install_location.setText(bindDeviceBeanMsg.getLoc());
            holder.install_location.setTextColor(context.getResources().getColor(R.color.black));
        }else{
            holder.install_location.setText("请选择安装位置(必填)");
            holder.install_location.setTextColor(context.getResources().getColor(R.color.biankuang));
        }
        holder.choose_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //选择安装界面
//                Intent intent=new Intent();
//                intent.setClass(context, DeviceInstallAddActivity.class);
//                context.startActivity(intent);
                List<TieBean> strings = new ArrayList<TieBean>();
                strings.add(new TieBean("主驾驶保险丝盒"));
                strings.add(new TieBean("副驾驶踏板"));
                strings.add(new TieBean("后座座椅右侧"));
                strings.add(new TieBean("后座座椅左侧"));
                strings.add(new TieBean("后备箱右侧"));
                strings.add(new TieBean("后备箱左侧"));
                strings.add(new TieBean("手动输入"));
                DialogUIUtils.showSheet(context, strings, "", Gravity.CENTER, true, true, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, final int positionn) {
                        Log.i("onItemClick: ", text+""+positionn);
                        if(positionn==6){
                            //手动输入
                            DialogUIUtils.showAlert((Activity) context, "安装位置", "", "请输入安装位置", null, "确定", "取消", false, true, true, new DialogUIListener() {
                                @Override
                                public void onPositive() {

                                }

                                @Override
                                public void onNegative() {

                                }

                                @Override
                                public void onGetInput(CharSequence input1, CharSequence input2) {
                                    super.onGetInput(input1, input2);
                                    holder.install_location.setText(input1);
                                    list.get(position).setLoc((String) input1);
                                    holder.install_location.setTextColor(context.getResources().getColor(R.color.black));

                                }
                            }).show();
                        }else{
                            holder.install_location.setText(text);
                            list.get(position).setLoc((String) text);
                            holder.install_location.setTextColor(context.getResources().getColor(R.color.black));

                        }

                    }
                }).show();
            }
        });
        holder.dev_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除当前行，数据操作
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,list.size()-position);//通知数据与界面重新绑定
            }
        });

    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView dev_name;
        private TextView dev_delete;
        private TextView dev_type;
        private TextView install_location;
        private RelativeLayout choose_loc;
        public ViewHolder(View itemView) {
            super(itemView);
            dev_name=itemView.findViewById(R.id.dev_name);
            dev_delete=itemView.findViewById(R.id.dev_delete);
            dev_type=itemView.findViewById(R.id.dev_type);
            install_location=itemView.findViewById(R.id.install_location);
            choose_loc=itemView.findViewById(R.id.choose_loc);
        }
    }
}
