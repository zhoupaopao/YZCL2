package com.example.yzcl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.CarListBean;
import com.example.yzcl.mvp.model.bean.CustomerListBeans;
import com.example.yzcl.mvp.ui.MyCarListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2018/7/27.
 */

public class CustomerListAdapter extends BaseAdapter {
    private Context context;
    private static ArrayList<CustomerListBeans.CustomerList> list;
    public CustomerListAdapter(Context context, ArrayList<CustomerListBeans.CustomerList> list){
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null){
            view=LayoutInflater.from(context).inflate(R.layout.customer_list_item,viewGroup,false);
            viewHolder=new ViewHolder();
            viewHolder.ischeck=view.findViewById(R.id.ischeck);
            viewHolder.name=view.findViewById(R.id.name);
            viewHolder.ll_line=view.findViewById(R.id.ll_line);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.name.setText(list.get(i).getGroup_name());
        viewHolder.ll_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalViewHolder.ischeck.getVisibility()==View.VISIBLE){
                    //可见
                    finalViewHolder.ischeck.setVisibility(View.GONE);
                    list.get(i).setIschec(false);
                }else{
                    finalViewHolder.ischeck.setVisibility(View.VISIBLE);
                    list.get(i).setIschec(true);
                }
            }
        });
        return view;
    }
    public static class ViewHolder{
        private ImageView ischeck;
        private TextView name;
        private LinearLayout ll_line;
    }
    public static List<CustomerListBeans.CustomerList> getlist(){

        return list;
    }

}
