package com.example.yzcl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yzcl.R;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/7/30.
 */

public class CarDeviceListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String>list;
    public CarDeviceListAdapter(Context context,ArrayList<String>list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null){
            viewHolder=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.list_device_item,viewGroup,false);
            viewHolder.tv_name=view.findViewById(R.id.tv_name);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.tv_name.setText(list.get(i));

        return view;
    }
    public class ViewHolder{
        TextView tv_name;
    }
}
