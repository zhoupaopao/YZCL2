package com.example.yzcl.mvp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.DeviceStatusBeans;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/2/2.
 */

/**
 * 最新的车辆管理界面
 */
public class CarManagerRealActivity extends BaseActivity {
    private ImageView back;
    private TextView AddCar;
    private TextView title;
    private Spinner spinner;
    ArrayList<String>data_list;
    private RelativeLayout choose_customer;
    private RelativeLayout car_list;
    private RelativeLayout jq_list;
    private BuildBean dialog;
    private TextView device_online;//在线设备
    private TextView device_offline;//离线设备
    private TextView device_unuse;//未启用设备
    private TextView car_all;//车辆总数
    private TextView car_yq;//车辆逾期数
    private TextView car_zdgz;//车辆重点关注

    private String TAG="CarManagerRealActivity";
    private SharedPreferences sp;
    private DeviceStatusBeans deviceStatusBeans;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manager_real);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        title=findViewById(R.id.title);
        back=findViewById(R.id.back);
        AddCar=findViewById(R.id.textview2);
        spinner=findViewById(R.id.spinner);
        car_list=findViewById(R.id.car_list);
        choose_customer=findViewById(R.id.choose_customer);
        jq_list=findViewById(R.id.jq_list);
        device_online=findViewById(R.id.device_online);
        device_offline=findViewById(R.id.device_offline);
        device_unuse=findViewById(R.id.device_unuse);
        car_all=findViewById(R.id.car_all);
        car_yq=findViewById(R.id.car_yq);
        car_zdgz=findViewById(R.id.car_zdgz);
    }

    private void initData() {
        data_list=new ArrayList<>();
        data_list.add("全部");
        data_list.add("有线");
        data_list.add("无线");
        //获取设备状态统计
        achieveDeviceStatus();

    }
    //获取设备状态统计
    private void achieveDeviceStatus() {
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queDeviceIsUsedStati+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                deviceStatusBeans=JSONObject.parseObject(jsonObject.toString(),DeviceStatusBeans.class);
                if(deviceStatusBeans.isSuccess()){
                    //统计最终的在离线数量
                    int unuse=0;
                    int lx=0;
                    int zx=0;
                    ArrayList<DeviceStatusBeans.DeviceStatus>arrayList=deviceStatusBeans.getList();
                    for(int i=0;i<arrayList.size();i++){
                        unuse=unuse+arrayList.get(i).getUnbindCounts();
                        lx=lx+arrayList.get(i).getLxcounts();
                        zx=zx+arrayList.get(i).getBindCounts()-arrayList.get(i).getLxcounts();
                    }
                    device_online.setText(zx+"");
                    device_offline.setText(lx+"");
                    device_unuse.setText(unuse+"");
                    //请求车辆逾期重点关注的数量
                    achieveCarNum();
                }else{
                    Toast.makeText(CarManagerRealActivity.this,deviceStatusBeans.getMessage(),Toast.LENGTH_SHORT).show();
                    dialog.dialog.dismiss();
                }


            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(CarManagerRealActivity.this,"加载中...",true,true,false,true);
                dialog.show();
            }
        });
    }

    private void achieveCarNum() {
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.getRedBind+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                if(jsonObject.getBoolean("success")){
                    JSONObject jsonObject1=jsonObject.getJSONObject("object");
                    car_all.setText(jsonObject1.getInteger("bindCar")+"");
                    car_yq.setText(jsonObject1.getInteger("overdueCar")+"");
                    car_zdgz.setText(jsonObject1.getInteger("careCar")+"");

                }else{
                    Toast.makeText(CarManagerRealActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                }
                dialog.dialog.dismiss();
            }
        });
    }

    private void initListener() {
        title.setText("车辆管理");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        AddCar.setText("新增车辆");
        AddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(CarManagerRealActivity.this,AddCarActivity.class);
                startActivity(intent);
            }
        });
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,R.layout.drop_list,data_list);
        adapter.setDropDownViewResource(R.layout.drop_list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("onItemSelected", data_list.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        choose_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(CarManagerRealActivity.this,CustomerChooseActivity.class);
                startActivity(intent);
            }
        });
        car_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(CarManagerRealActivity.this,MyCarListActivity.class);
                startActivity(intent);
            }
        });
        jq_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("jq_list", "onClick: ");
                Intent intent=new Intent();
                intent.setClass(CarManagerRealActivity.this,JieQingActivity.class);
                startActivity(intent);
            }
        });
    }
}
