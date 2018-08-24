package com.example.yzcl.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshViewFooter;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIListener;
import com.example.yzcl.R;
import com.example.yzcl.adapter.CarDeviceListAdapter;
import com.example.yzcl.adapter.SearchHistoryAdapter;
import com.example.yzcl.adapter.SearchHistoryDeviceAdapter;
import com.example.yzcl.adapter.SearchRecyclerAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CarMonSearchListBean;
import com.example.yzcl.mvp.model.bean.DeviceListBean;
import com.example.yzcl.mvp.model.bean.carDetailGPSBeans;
import com.example.yzcl.mvp.presenter.RecyclerItemClickListener;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/8/1.
 */

public class DeviceSearchAcrivity extends BaseActivity {
    private TextView cancel;
    private TextView clear;
    private EditText et_search;
    private RecyclerView recycleview;
    SearchHistoryDeviceAdapter adapter;
    private ArrayList<String> arrayList;
    private SharedPreferences sp;
    private String ids="";
    private BuildBean dialog;
    private RecyclerView search_recycleview;
    private RelativeLayout rl_search_history;
    private ArrayList<CarMonSearchListBean.CarSearchBean>carSearchBeans=new ArrayList<>();
    private String TAG="DeviceSearchAcrivity";
    private ImageView delete;
    CarDeviceListAdapter searchRecyclerAdapter1;//这个到时候在变
    ArrayList<DeviceListBean.DeviceLLBean>deviceLLBeans;//实际的数据

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_search);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }
    private void initView() {
        deviceLLBeans=new ArrayList<>();
        cancel=findViewById(R.id.cancel);
        clear=findViewById(R.id.clear);
        et_search=findViewById(R.id.et_search);
        recycleview=findViewById(R.id.recycleview);
        arrayList=new ArrayList<>();
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        search_recycleview=findViewById(R.id.search_recycleview);
        rl_search_history=findViewById(R.id.rl_search_history);
        delete=findViewById(R.id.delete);
    }

    private void initData() {
        ids=getIntent().getStringExtra("ids");
        //虚拟赋值，用于显示历史记录
        String his_list=sp.getString("search_list",",");
        String[]his=his_list.split(",");
        Log.i(TAG, his_list);
        Log.i(TAG, "initData: "+his.length);
        if(his.length>0){
            //代表有历史
            rl_search_history.setVisibility(View.VISIBLE);
            for(int i=0;i<his.length;i++){
                arrayList.add(his[i]);
            }
        }else{
            rl_search_history.setVisibility(View.GONE);
        }
//        for(int i=0;i<6;i++){
//            arrayList.add("张三"+i);
//        }
        adapter=new SearchHistoryDeviceAdapter(this,arrayList);


        searchRecyclerAdapter1=new CarDeviceListAdapter(DeviceSearchAcrivity.this,deviceLLBeans);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DeviceSearchAcrivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        search_recycleview.setLayoutManager(layoutManager);
        search_recycleview.setAdapter(searchRecyclerAdapter1);
        search_recycleview.addItemDecoration(new DividerItemDecoration(DeviceSearchAcrivity.this, DividerItemDecoration.VERTICAL));
//        search_recycleview.addOnItemTouchListener(new RecyclerItemClickListener(DeviceSearchAcrivity.this,new RecyclerItemClickListener.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(View view, int position) {
//                if(!Constant.isNetworkConnected(DeviceSearchAcrivity.this)) {
//                    //判断网络是否可用
//                    Toast.makeText(DeviceSearchAcrivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
//                }else{
//                    CarMonSearchListBean.CarSearchBean carSearchBean= carSearchBeans.get(position);
//                    //点击具体的车辆
//                    //请求具体的车辆信息
//                    RequestParams params1=new RequestParams();
//                    //因为传递的是json数据，所以需要设置header和body
//                    params1.addHeader("Content-Type","application/json");
//                    JSONObject jsonObject1=new JSONObject();
//                    jsonObject1.put("car_id",carSearchBean.getId());
////                        jsonObject1.put("pagesize",10);
////                        jsonObject1.put("page",1);
////                            String carid=carSearchBean.getId();
//                    Log.i(TAG, Api.queCarDeviceGps+"?token="+sp.getString(Constant.Token,""));
//                    params1.setRequestBody(MediaType.parse("application/json"),jsonObject1.toString());
//                    HttpRequest.post(Api.queCarDeviceGps+"?token="+sp.getString(Constant.Token,""),params1,new JsonHttpRequestCallback(){
//                        @Override
//                        protected void onSuccess(Headers headers, JSONObject jsonObject) {
//                            super.onSuccess(headers, jsonObject);
//                            Log.i(TAG, jsonObject.toString());
//                            carDetailGPSBeans carDetailGPSBeans=JSONObject.parseObject(jsonObject.toString(),carDetailGPSBeans.class);
//                            if(carDetailGPSBeans.isSuccess()){
//                                //请求成功
//                                //显示点
//                                ArrayList<com.example.yzcl.mvp.model.bean.carDetailGPSBeans.carDetailGPSBean>carDetailGPSBean=carDetailGPSBeans.getList();
//                                Intent intent=new Intent();
//                                intent.setClass(DeviceSearchAcrivity.this,CarAddressActivity.class);
////                                    Bundle b=new Bundle();
////                                    b.put("pose_title", pose_title);
////                                    intent.putExtras(b);
//                                intent.putExtra("carDetailGPS", jsonObject.get("list").toString());
////                                        intent.putExtra("carDetailGPS", carSearchBean.getId());
//                                startActivity(intent);
//
//                            }else{
//                                Toast.makeText(DeviceSearchAcrivity.this,carDetailGPSBeans.getMessage(),Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onStart() {
//                            super.onStart();
//                            dialog= DialogUIUtils.showLoading(DeviceSearchAcrivity.this,"加载中...",true,true,false,true);
//                            dialog.show();
//                        }
//
//                        @Override
//                        public void onFinish() {
//                            super.onFinish();
//                            dialog.dialog.dismiss();
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onLongClick(View view, int posotion) {
//
//            }
//        }));
    }

    private void initListener() {
        et_search.setHint("请输入设备名、设备号");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleview.setLayoutManager(layoutManager);
        recycleview.setAdapter(adapter);
        //添加Android自带的分割线
        recycleview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycleview.setItemAnimator( new DefaultItemAnimator());
//        recycleview.addOnItemTouchListener(new RecyclerItemClickListener(CarMonSearchActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if(!Constant.isNetworkConnected(CarMonSearchActivity.this)) {
//                    //判断网络是否可用
//                    Toast.makeText(CarMonSearchActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
//                }else{
//                    queVehicleListForSea(arrayList.get(position));
//                }
//
//            }
//
//            @Override
//            public void onLongClick(View view, int posotion) {
//
//            }
//        }));
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUIUtils.showAlert(DeviceSearchAcrivity.this, null, "您确定清空搜索历史吗？", "", "", "确定", "取消", false, true, true, new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        SharedPreferences.Editor editor=sp.edit();
                        editor.remove("search_list");
                        editor.commit();
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNegative() {
//                        Toast.makeText(PersonMessageActivity.this,"onNegative",Toast.LENGTH_SHORT).show();
                    }

                }).show();

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_search.setText("");
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    delete.setVisibility(View.VISIBLE);
                }else{
                    delete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) et_search.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(DeviceSearchAcrivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    // 搜索，进行自己要的操作...
                    if(et_search.getText().toString().trim().equals("")){
                        Toast.makeText(DeviceSearchAcrivity.this,"搜索字符不能为空",Toast.LENGTH_SHORT).show();
                    }else{
                        String search_list_last=sp.getString("search_list","");
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("search_list",et_search.getText().toString().trim()+","+search_list_last);
                        editor.commit();
                        if(!Constant.isNetworkConnected(DeviceSearchAcrivity.this)) {
                            //判断网络是否可用
                            Toast.makeText(DeviceSearchAcrivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                        }else{
                            queVehicleListForSea(et_search.getText().toString().trim());
                        }

                    }

                    return true;
                }
                return false;
            }
        });
    }

    public void queVehicleListForSea(String tt) {
        //展示搜索列表

            RequestParams params=new RequestParams();
            params.addHeader("Content-Type","application/json");
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("page",1);
            jsonObject.put("pagesize",100);
            if(ids.equals("")){

            }else{
                jsonObject.put("groupids",ids);
            }
        jsonObject.put("search",tt);
            params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
            HttpRequest.post(Api.queryDeviceList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
                @Override
                protected void onSuccess(Headers headers, JSONObject jsonObject) {
                    super.onSuccess(headers, jsonObject);
                    Log.i(TAG, jsonObject.toString());
                    search_recycleview.setVisibility(View.VISIBLE);
                    recycleview.setVisibility(View.GONE);
                    rl_search_history.setVisibility(View.GONE);
                    DeviceListBean deviceListBean=JSONObject.parseObject(jsonObject.toString(),DeviceListBean.class);
                    deviceLLBeans=deviceListBean.getList();
                    if(deviceLLBeans.size()==0){
                        //没有数据
                        Toast.makeText(DeviceSearchAcrivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
                    }
                    searchRecyclerAdapter1=new CarDeviceListAdapter(DeviceSearchAcrivity.this,deviceLLBeans);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(DeviceSearchAcrivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    search_recycleview.setLayoutManager(layoutManager);
                    search_recycleview.setAdapter(searchRecyclerAdapter1);
                    //设置增加或删除条目的动画
                    search_recycleview.setItemAnimator( new DefaultItemAnimator());
                    dialog.dialog.dismiss();
                }

                @Override
                public void onStart() {
                    super.onStart();
                    dialog= DialogUIUtils.showLoading(DeviceSearchAcrivity.this,"加载中...",true,true,false,true);
                    dialog.show();
                }

                @Override
                public void onFailure(int errorCode, String msg) {
                    super.onFailure(errorCode, msg);
                }
            });

    }
}
