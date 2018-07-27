package com.example.yzcl.mvp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.tree.Node3;
import com.example.yzcl.R;
import com.example.yzcl.adapter.SimpleTreeeAdapter;
import com.example.yzcl.adapter.TreeListViewwAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CustomerOrganizationBeans;
import com.example.yzcl.mvp.model.bean.DeviceStatusBeans;
import com.example.yzcl.mvp.model.bean.NewFileBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
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
    private BuildBean dialog1;
    private TextView device_online;//在线设备
    private TextView device_offline;//离线设备
    private TextView device_unuse;//未启用设备
    private TextView car_all;//车辆总数
    private TextView car_yq;//车辆逾期数
    private TextView car_zdgz;//车辆重点关注
    private TextView groupname;//账户名字
    private CheckBox show_child;//是否展示下级数据
    private TextView all_car_num;//所有车辆的数量
    private TextView all_device_num;//所有设备数量
    private TextView jq_all;//所有结清设备
    CustomerOrganizationBeans CustomerMBean;
    private String ids="";
//    private String group_dna="";
//    private String group_id="";

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
//        spinner=findViewById(R.id.spinner);
        car_list=findViewById(R.id.car_list);
        choose_customer=findViewById(R.id.choose_customer);
        jq_list=findViewById(R.id.jq_list);
        device_online=findViewById(R.id.device_online);
        device_offline=findViewById(R.id.device_offline);
        device_unuse=findViewById(R.id.device_unuse);
        car_all=findViewById(R.id.car_all);
        car_yq=findViewById(R.id.car_yq);
        car_zdgz=findViewById(R.id.car_zdgz);
        groupname=findViewById(R.id.groupname);
        show_child=findViewById(R.id.show_child);
        all_car_num=findViewById(R.id.all_car_num);
        all_device_num=findViewById(R.id.all_device_num);
        jq_all=findViewById(R.id.jq_all);
    }

    private void initData() {
        data_list=new ArrayList<>();
        data_list.add("全部");
        data_list.add("有线");
        data_list.add("无线");
        //需要先请求当前的账户名dnagroupid等等信息
//        achieveMesg();
        //获取设备状态统计
        groupname.setText("全部用户");
        achieveDeviceStatus("");

    }

    private void achieveMesg() {
            AsyncHttpClient client=new AsyncHttpClient();
            com.loopj.android.http.RequestParams params=new com.loopj.android.http.RequestParams();
            String token=sp.getString("Token",null);
            Log.i("GetCustomer", token);
            params.put("token",token);
            params.setContentEncoding("UTF-8");
            client.get(Api.getCustomer, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try {
                        String json=new String(bytes,"UTF-8").toString();
                        Log.i("GetCustomer", json);
                        CustomerMBean= JSONObject.parseObject(json,CustomerOrganizationBeans.class);
                        if(CustomerMBean.isSuccess()){
                            if(CustomerMBean.getObject().getTree()==null){
                                Toast.makeText(CarManagerRealActivity.this,"没有子账户",Toast.LENGTH_SHORT).show();
                            }else{
                                groupname.setText(CustomerMBean.getObject().getTree().get(0).getGroup_name());
//                                group_id=CustomerMBean.getObject().getTree().get(0).getId();
//                                group_dna=CustomerMBean.getObject().getTree().get(0).getGroup_dna();

                            }
                        }else{
                            Toast.makeText(CarManagerRealActivity.this,CustomerMBean.getMessage(),Toast.LENGTH_SHORT).show();
                        }


                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStart() {
                    super.onStart();
                    dialog= DialogUIUtils.showLoading(CarManagerRealActivity.this,"加载中...",true,true,false,true);
                    dialog.show();

                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    dialog.dialog.dismiss();
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    dialog.dialog.dismiss();
                }
            });
    }

    //获取设备状态统计
    private void achieveDeviceStatus(final String idss) {
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        if(idss.equals("")){

        }else{
            jsonObject.put("groupids",idss);
        }
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queDeviceIsUsedStati+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                if(jsonObject.getBoolean("success")){
                    //统计最终的在离线数量
                    String bindCounts="";
                    String unbindCounts="";
                    String expireCounts="";
                    JSONObject all_device=jsonObject.getJSONObject("object");
                    if(all_device.getString("bindCounts")!=null){
                        bindCounts=all_device.getString("bindCounts");
                    }else{
                        bindCounts="0";
                    }
                    if(all_device.getString("unbindCounts")!=null){
                        unbindCounts=all_device.getString("unbindCounts");
                    }else{
                        unbindCounts="0";
                    }
                    if(all_device.getString("expireCounts")!=null){
                        expireCounts=all_device.getString("expireCounts");
                    }else{
                        expireCounts="0";
                    }
                    //已绑车
                    device_online.setText(bindCounts);
                    //未绑车
                    device_offline.setText(unbindCounts);
                    device_unuse.setText(expireCounts);
                    int alldevice=Integer.parseInt(bindCounts)+Integer.parseInt(unbindCounts);
                    all_device_num.setText("共"+alldevice+"个");
                    //请求车辆逾期重点关注的数量
                    achieveCarNum(idss);
                }else{
                    Toast.makeText(CarManagerRealActivity.this,deviceStatusBeans.getMessage(),Toast.LENGTH_SHORT).show();
                    dialog1.dialog.dismiss();
                }


            }

            @Override
            public void onStart() {
                super.onStart();
                dialog1= DialogUIUtils.showLoading(CarManagerRealActivity.this,"加载中...",true,true,false,true);
                dialog1.show();
            }
        });
    }

    private void achieveCarNum(final String idss) {
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        if(idss.equals("")){

        }else{
            jsonObject.put("groupids",idss);
        }
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.getRedBind+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                if(jsonObject.getBoolean("success")){
                    JSONObject jsonObject1=jsonObject.getJSONObject("object");
                    int normal=jsonObject1.getInteger("bindCar")-jsonObject1.getInteger("overdueCar")-jsonObject1.getInteger("careCar");
                    car_all.setText(normal+"");
                    all_car_num.setText("共"+jsonObject1.getInteger("bindCar")+"台");
                    car_yq.setText(jsonObject1.getInteger("overdueCar")+"");
                    car_zdgz.setText(jsonObject1.getInteger("careCar")+"");
                    //获取结清统计
                    achievejq(idss);
                }else{
                    Toast.makeText(CarManagerRealActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    dialog1.dialog.dismiss();
                }

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(CarManagerRealActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                dialog1.dialog.dismiss();
            }
        });
    }

    private void achievejq(String idss) {
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("page",1);
        jsonObject.put("pagesize",1);
        if(idss.equals("")){

        }else{
            jsonObject.put("groupids",idss);
        }
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.querySettleList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                if(jsonObject.getBoolean("success")){
                    jq_all.setText("共"+jsonObject.getInteger("count")+"台");
                }else{
                    Toast.makeText(CarManagerRealActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();

                }
                dialog1.dialog.dismiss();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(CarManagerRealActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                dialog1.dialog.dismiss();
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
        AddCar.setVisibility(View.GONE);
        AddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(CarManagerRealActivity.this,AddCarActivity.class);
                startActivity(intent);
            }
        });

        choose_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(CarManagerRealActivity.this,CustomerChooseActivity.class);
                startActivityForResult(intent,1);
            }
        });

        car_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("ids",ids);
                intent.setClass(CarManagerRealActivity.this,MyCarListActivity.class);
                startActivity(intent);
            }
        });
        jq_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("jq_list", "onClick: ");
                Intent intent=new Intent();
                intent.putExtra("ids",ids);
                intent.setClass(CarManagerRealActivity.this,JieQingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 10:
                //选择了客户的
                 ids=data.getStringExtra("ids");
                String names=data.getStringExtra("names");
                groupname.setText(names.substring(0,names.length()-1));
                achieveDeviceStatus(ids);
                break;
        }
    }
}
