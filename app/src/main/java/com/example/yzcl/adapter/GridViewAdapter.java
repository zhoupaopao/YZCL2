package com.example.yzcl.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.CarManagerRealActivity;
import com.example.yzcl.mvp.ui.RealCarListActivity;
import com.example.yzcl.mvp.ui.VehicleMonitoringActivity;
import com.example.yzcl.mvp.ui.mvpactivity.HomePage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by Lenovo on 2018/7/23.
 */

public class GridViewAdapter extends BaseAdapter {
    private List<Map<String,Object>> data;
    private Context context;
    Intent intent;
    private int[] icon = { R.mipmap.group1, R.mipmap.group2,
            R.mipmap.group3,R.mipmap.group4, R.mipmap.group5,
            R.mipmap.group5, R.mipmap.group7,R.mipmap.group8};
    private String[] text={"车辆监控","风控预警","车辆管理","下单中心","围栏管理","指令设置","客户管理"};
    public GridViewAdapter(Context context,List<Map<String,Object>>data){
        this.context=context;
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Map<String,Object>map=new HashMap<String,Object>();
        map=data.get(i);
        ViewHolder viewHolder=null;
        if(view==null){
            viewHolder=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.item_gridview,viewGroup,false);
            viewHolder.text1=view.findViewById(R.id.text1);
            viewHolder.coverimg=view.findViewById(R.id.coverimg);
            viewHolder.image1=view.findViewById(R.id.image1);
            viewHolder.grid_ll=view.findViewById(R.id.grid_ll);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.image1.setImageResource((int)map.get("image"));
        viewHolder.text1.setText((String)map.get("text"));
        if(map.get("coverimg").equals("")){
            viewHolder.coverimg.setVisibility(View.GONE);
        }else{
            viewHolder.coverimg.setVisibility(View.VISIBLE);
        }
        final Map<String, Object> finalMap = map;
        viewHolder.grid_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch ((String) finalMap.get("text")){
                    case "车辆监控":
                        intent=new Intent();
//                        intent.setClass(HomePage.this, GunDongActivity.class);
                        intent.setClass(context, VehicleMonitoringActivity.class);
                        context.startActivity(intent);
                        break;
                    case "风控预警":
                        //车辆监控（实际）
//                        intent=new Intent();
//                        //风控预警（实际）
//                        intent.setClass(HomePage.this, RiskWarningActivity.class);
//                        startActivity(intent);
                        Toast.makeText(context,"敬请期待",Toast.LENGTH_SHORT).show();
                        break;
                    case "车辆管理":
                        //车辆管理列表
                        intent=new Intent();
//                        //2.0的车辆管理
////                        intent.setClass(HomePage.this, CarManagerActivity.class);
                        intent.setClass(context, CarManagerRealActivity.class);
                        context.startActivity(intent);
//                                            Toast.makeText(HomePage.this,"敬请期待",Toast.LENGTH_SHORT).show();
                        break;
                    case "下单中心":
                        //围栏功能（现）
                        //下单中心（实际）
//                        intent=new Intent();
//                        intent.setClass(HomePage.this,VPageActivity .class);
//                        startActivity(intent);
                        Toast.makeText(context,"敬请期待",Toast.LENGTH_SHORT).show();
                        break;
                    case "围栏管理":
                        //围栏管理（实际）
//                        intent=new Intent();
////                        intent.setClass(HomePage.this,EnclosureManagerActivity.class);
//                        intent.setClass(HomePage.this,EnclosureAllManagerActivity.class);
//                        startActivity(intent);

                        Toast.makeText(context,"敬请期待",Toast.LENGTH_SHORT).show();

                        break;
                    case "指令设置":
                        //指令设置（实际）
                        //账号管理，有展开
                        Toast.makeText(context,"敬请期待",Toast.LENGTH_SHORT).show();
//                         intent=new Intent();
//                        intent.setClass(HomePage.this, CustomerManagementActivity.class);
//                        startActivity(intent);
                        break;
                    case "客户管理":
                        //客户管理
//                        intent=new Intent();
////                        intent.setClass(HomePage.this, VPageActivity.class);
////                        intent.setClass(HomePage.this, LowerAccountActivity.class);
//                        intent.setClass(HomePage.this, CustomerManagerActivity.class);
//                        startActivity(intent);
                        Toast.makeText(context,"敬请期待",Toast.LENGTH_SHORT).show();
                        break;
                    case "更多":
                        //更多
//                        intent=new Intent();
////                        intent.setClass(HomePage.this, CarManagerFragmentActivity.class);
//                                                intent.setClass(HomePage.this, GunDongActivity.class);
//                        startActivity(intent);
                        Toast.makeText(context,"敬请期待",Toast.LENGTH_SHORT).show();
                        break;
                    case "车辆列表":
                        //更多
                        intent=new Intent();
//                        intent.setClass(HomePage.this, CarManagerFragmentActivity.class);
                        intent.setClass(context, RealCarListActivity.class);
                        context.startActivity(intent);
                        Toast.makeText(context,"敬请期待",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return view;
    }
    private class ViewHolder{
        TextView text1;
        ImageView coverimg;
        ImageView image1;
        LinearLayout grid_ll;
    }
}
