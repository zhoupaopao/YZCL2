package com.example.yzcl.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.GwListBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lenovo on 2018/2/8.
 */

public class GwListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GwListBean.GwBean>list;
    private LayoutInflater mInflater;
    public  GwListAdapter(Context context, ArrayList<GwListBean.GwBean>list){
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
            contentview=mInflater.inflate(R.layout.item_gw_list,null);
            viewHolder=new ViewHolder();
            viewHolder.name=contentview.findViewById(R.id.name);
            viewHolder.pic_status=contentview.findViewById(R.id.pic_status);
            viewHolder.lookaddress=contentview.findViewById(R.id.lookaddress);
            viewHolder.vinid=contentview.findViewById(R.id.vinid);
            viewHolder.belong=contentview.findViewById(R.id.belong);
            viewHolder.warning_starttime=contentview.findViewById(R.id.warning_starttime);
            viewHolder.warning_during=contentview.findViewById(R.id.warning_during);
            contentview.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) contentview.getTag();
        }
        GwListBean.GwBean gwBean=list.get(i);
        viewHolder.name.setText(gwBean.getPledgename());
        viewHolder.vinid.setText(gwBean.getVin());
        viewHolder.belong.setText(gwBean.getGroupname());
        viewHolder.warning_starttime.setText(gwBean.getStarttime());
        long starttime=getTimeMillis(gwBean.getStarttime());
        long endtime=0;
String getEndtime=gwBean.getEndtime();
        Log.i("getEndtime", getEndtime);
        //判断结束时间是否为空
        if(TextUtils.isEmpty(getEndtime) ){
            //没有结束时间代表取当前时间
            endtime=System.currentTimeMillis();
        }else{
            endtime=getTimeMillis(gwBean.getEndtime());
        }
        long time_during=endtime-starttime;
        long hours=time_during/(1000*60*60);//获取间隔小时
        long mintues=time_during-(hours*(1000*60*60));//获取间隔分钟
        mintues=mintues/(60*1000);
        long days=hours/24;
        hours=hours-days*24;
        viewHolder.warning_during.setText(days+"天"+hours+"小时"+mintues+"分钟");
        return contentview;
    }
    private class ViewHolder{
        TextView name;
        ImageView pic_status;
        ImageView lookaddress;
        TextView vinid;
        TextView belong;
        TextView warning_starttime;
        TextView warning_during;
    }
    private long getTimeMillis(String time){
        long newtime=0;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date=null;
        try {
            date=sdf.parse(time);
            newtime=date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newtime;
    }
}
