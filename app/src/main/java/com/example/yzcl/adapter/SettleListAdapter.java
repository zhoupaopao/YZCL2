package com.example.yzcl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.SettleListBean;
import com.example.yzcl.mvp.ui.EnclosureAllManagerActivity;
import com.example.yzcl.mvp.ui.JieQingActivity;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/2/5.
 */

public class SettleListAdapter extends BaseAdapter {
    ArrayList<SettleListBean.SettleBean>list;
    JieQingActivity context;
    LayoutInflater mInflater;
    SettleListBean.SettleBean settleBean;
    public  SettleListAdapter(JieQingActivity context,ArrayList<SettleListBean.SettleBean> list){
        this.context=context;
        this.list=list;
        this.mInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
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
    public View getView(int i, View contentview, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(contentview==null){
            viewHolder=new ViewHolder();
            contentview=mInflater.inflate(R.layout.layout_settler_list_item,null);
            viewHolder.name=contentview.findViewById(R.id.name);
            viewHolder.qxjq=contentview.findViewById(R.id.qxjq);
            viewHolder.vinid=contentview.findViewById(R.id.vinid);
            viewHolder.equipment=contentview.findViewById(R.id.equipment);
            viewHolder.data_jq_car=contentview.findViewById(R.id.data_jq_car);
            viewHolder.belong=contentview.findViewById(R.id.belong);
            contentview.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) contentview.getTag();
        }
        settleBean=list.get(i);
        viewHolder.name.setText(settleBean.getPledgename());
        viewHolder.vinid.setText(settleBean.getVin());
        viewHolder.equipment.setText(settleBean.getCar_brand()+settleBean.getCar_version());
        viewHolder.data_jq_car.setText(settleBean.getSettledtime());
        viewHolder.belong.setText(settleBean.getGroupname());
        if(settleBean.getIsactive()==0){
            //不显示取消结清
            viewHolder.qxjq.setVisibility(View.GONE);
        }else{
            viewHolder.qxjq.setVisibility(View.VISIBLE);
        }
        viewHolder.qxjq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUIUtils.showAlert(context, null, "确定取消该笔结清吗？", "", "", "取消", "确定", false, true, true, new DialogUIListener() {
                            @Override
                            public void onPositive() {
                                //取消
                            }

                            @Override
                            public void onNegative() {
                                //确定
                                context.qxjq(settleBean.getId());
                            }
                        }).show();

            }
        });
        return contentview;
    }
    public class ViewHolder{
        //借款人
        TextView name;
        //取消结清按钮
        Button qxjq;
        TextView vinid;
        //现在是设备数量
        TextView equipment;
        //结清时间
        TextView data_jq_car;
        //所属客户
        TextView belong;
    }
}
