package com.example.yzcl.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CarListBean;
import com.example.yzcl.mvp.model.bean.CarMonSearchListBean;
import com.example.yzcl.mvp.model.bean.carDetailGPSBeans;
import com.example.yzcl.mvp.ui.CarAddressActivity;
import com.example.yzcl.mvp.ui.CarMonSearchActivity;
import com.example.yzcl.mvp.ui.CarSearchActivity;
import com.example.yzcl.mvp.ui.MyCarListActivity;
import com.example.yzcl.mvp.ui.RealCarListActivity;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/2/3.
 */

public class CarListAdapter1 extends BaseRecyclerAdapter<CarListAdapter1.ViewHolder> {
    private RealCarListActivity context;
    private CarSearchActivity context1;
    private List<CarListBean.CarBean> carlist;
    private LayoutInflater mInflater;
    private SharedPreferences sp;
    private BuildBean dialog;
    private int nowstatus;
    public CarListAdapter1(RealCarListActivity context, List<CarListBean.CarBean> carlist, int nowstatus){
        this.context=context;
        this.carlist=carlist;
        this.nowstatus=nowstatus;
        mInflater=LayoutInflater.from(context);
        this.sp=context.getSharedPreferences("YZCL",Context.MODE_PRIVATE);
    }




    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view=mInflater.inflate(R.layout.layout_car_list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position, boolean isItem) {
//开始处理数据
        final CarListBean.CarBean carBean=carlist.get(position);
        final ArrayList<String>data_list=new ArrayList<>();
        data_list.add("标记");
        data_list.add("正常");
        data_list.add("重点关注");
        data_list.add("逾期");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,R.layout.spinner_item,data_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //请求接口
                Log.i("onItemSelected: ", data_list.get(i));
                switch (i){
                    case 0:
                        //不处理
                        break;
                    case 1:
                        setRemark(1,carBean.getId());
                        break;
                    case 2:
                        setRemark(3,carBean.getId());
                        break;
                    case 3:
                        setRemark(2,carBean.getId());
                        break;
                }
                holder.spinner.setSelection(0,true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        holder.spinner.setAdapter(adapter);
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
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //请求接口
                achievemsg(carBean.getId());
//                context.lookAddress(carBean.getId());
            }
        });
    }

    private void setRemark(int status,String carid) {
        if(!Constant.isNetworkConnected(context)) {
            //判断网络是否可用
            Toast.makeText(context, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
        }else{
            //点击具体的车辆
            //请求具体的车辆信息
            RequestParams params1=new RequestParams();
            //因为传递的是json数据，所以需要设置header和body
            params1.addHeader("Content-Type","application/json");
            JSONObject jsonObject1=new JSONObject();
            jsonObject1.put("ids",carid);
            jsonObject1.put("status",status);
            params1.setRequestBody(MediaType.parse("application/json"),jsonObject1.toString());
            HttpRequest.post(Api.setRemark+"?token="+sp.getString(Constant.Token,""),params1,new JsonHttpRequestCallback(){
                @Override
                protected void onSuccess(Headers headers, JSONObject jsonObject) {
                    super.onSuccess(headers, jsonObject);
                    if(jsonObject.getBoolean("success")){
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        //刷新页面s
                        context.initData(nowstatus,1);
//                        notifyDataSetChanged();
                    }else{
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onStart() {
                    super.onStart();
                    dialog= DialogUIUtils.showLoading(context,"加载中...",true,true,false,true);
                    dialog.show();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    dialog.dialog.dismiss();
                }
            });
        }
    }

    private void achievemsg(String id) {
        if(!Constant.isNetworkConnected(context)) {
            //判断网络是否可用
            Toast.makeText(context, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
        }else{
            //点击具体的车辆
            //请求具体的车辆信息
            RequestParams params1=new RequestParams();
            //因为传递的是json数据，所以需要设置header和body
            params1.addHeader("Content-Type","application/json");
            JSONObject jsonObject1=new JSONObject();
            jsonObject1.put("car_id",id);
            params1.setRequestBody(MediaType.parse("application/json"),jsonObject1.toString());
            HttpRequest.post(Api.queCarDeviceGps+"?token="+sp.getString(Constant.Token,""),params1,new JsonHttpRequestCallback(){
                @Override
                protected void onSuccess(Headers headers, JSONObject jsonObject) {
                    super.onSuccess(headers, jsonObject);
                    carDetailGPSBeans carDetailGPSBeans=JSONObject.parseObject(jsonObject.toString(),carDetailGPSBeans.class);
                    if(carDetailGPSBeans.isSuccess()){
                        //请求成功
                        //显示点
                        ArrayList<com.example.yzcl.mvp.model.bean.carDetailGPSBeans.carDetailGPSBean>carDetailGPSBean=carDetailGPSBeans.getList();
                        if(jsonObject.getJSONArray("list").size()==0){
                            //就不用跳转了，没有设备
                            Toast.makeText(context, "该车辆未绑定设备，无法查看位置。", Toast.LENGTH_SHORT).show();
                        }else{
                            //跳转到车辆位置页面
                            Intent intent=new Intent();
                            intent.setClass(context,CarAddressActivity.class);
//                                    Bundle b=new Bundle();
//                                    b.put("pose_title", pose_title);
//                                    intent.putExtras(b);
                            Log.i("carDetailGPS", jsonObject.get("list").toString());

                            intent.putExtra("carDetailGPS", jsonObject.get("list").toString());
//                                        intent.putExtra("carDetailGPS", carSearchBean.getId());
                            context.startActivity(intent);
                        }


                    }else{
                        Toast.makeText(context,carDetailGPSBeans.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onStart() {
                    super.onStart();
                    dialog= DialogUIUtils.showLoading(context,"加载中...",true,true,false,true);
                    dialog.show();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    dialog.dialog.dismiss();
                }
            });
        }
    }

    @Override
    public int getAdapterItemCount() {
        return carlist.size();
    }

    public void changedata(ArrayList<CarListBean.CarBean> nowList) {
        this.carlist=nowList;
        notifyDataSetChanged();
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
        private Spinner spinner;

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
            spinner=itemView.findViewById(R.id.spinner);
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
