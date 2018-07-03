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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.Listener.OnRecyclerItemClickListener;
import com.example.yzcl.R;
import com.example.yzcl.adapter.SearchHistoryAdapter;
import com.example.yzcl.adapter.SearchRecyclerAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CarDetailBeans;
import com.example.yzcl.mvp.model.bean.CarMonSearchListBean;
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
 * Created by Lenovo on 2018/6/26.
 */
//车辆监控页面的搜索页面
public class CarMonSearchActivity extends BaseActivity {
    private TextView cancel;
    private TextView clear;
    private EditText et_search;
    private RecyclerView recycleview;
    SearchHistoryAdapter adapter;
    private ArrayList<String>arrayList;
    private SharedPreferences sp;
    private BuildBean dialog;
    private RecyclerView search_recycleview;
    private RelativeLayout rl_search_history;
    private ArrayList<CarMonSearchListBean.CarSearchBean>carSearchBeans;
    private String TAG="CarMonSearchActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_mon_search);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        cancel=findViewById(R.id.cancel);
        clear=findViewById(R.id.clear);
        et_search=findViewById(R.id.et_search);
        recycleview=findViewById(R.id.recycleview);
        arrayList=new ArrayList<>();
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        search_recycleview=findViewById(R.id.search_recycleview);
        rl_search_history=findViewById(R.id.rl_search_history);
    }

    private void initData() {
        //虚拟赋值，用于显示历史记录
        String his_list=sp.getString("search_list",",");
        String[]his=his_list.split(",");
        Log.i(TAG, his_list);
        Log.i(TAG, "initData: "+his.length);
        if(his.length>0){
            //代表有历史
            for(int i=0;i<his.length;i++){
                arrayList.add(his[i]);
            }
        }
//        for(int i=0;i<6;i++){
//            arrayList.add("张三"+i);
//        }
        adapter=new SearchHistoryAdapter(this,arrayList);
    }

    private void initListener() {
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
        recycleview.addOnItemTouchListener(new RecyclerItemClickListener(CarMonSearchActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                queVehicleListForSea(arrayList.get(position));
            }

            @Override
            public void onLongClick(View view, int posotion) {

            }
        }));
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=sp.edit();
                editor.remove("search_list");
                editor.commit();
                arrayList.clear();
                adapter.notifyDataSetChanged();
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
                            .hideSoftInputFromWindow(CarMonSearchActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    // 搜索，进行自己要的操作...
                    String search_list_last=sp.getString("search_list","");
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("search_list",search_list_last+et_search.getText().toString().trim()+",");
                    editor.commit();
                    queVehicleListForSea(et_search.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
    }

    private void queVehicleListForSea(String tt) {
        //展示搜索列表

        RequestParams params=new RequestParams();
//        params.setRequestBodyString();
//        params.setRequestBody();
        //因为传递的是json数据，所以需要设置header和body
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("search",tt);
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
//        params.addFormDataPart("token",sp.getString(Constant.Token,""));
//        params.addFormDataPart("search",et_search.getText().toString().trim());
        Log.i(TAG, Api.queVehicleListForSea+"?token="+sp.getString(Constant.Token,""));

        HttpRequest.post(Api.queVehicleListForSea+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                Toast.makeText(CarMonSearchActivity.this,"请求成功",Toast.LENGTH_SHORT).show();
                CarMonSearchListBean carMonSearchListBean=JSONObject.parseObject(jsonObject.toString(),CarMonSearchListBean.class);
                carSearchBeans=carMonSearchListBean.getList();
                //当点击搜索后需要将搜索列表显示，历史记录列表需要隐藏
                search_recycleview.setVisibility(View.VISIBLE);
                recycleview.setVisibility(View.GONE);
                rl_search_history.setVisibility(View.GONE);
                SearchRecyclerAdapter searchRecyclerAdapter=new SearchRecyclerAdapter(CarMonSearchActivity.this,carSearchBeans);
                LinearLayoutManager layoutManager = new LinearLayoutManager(CarMonSearchActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                search_recycleview.setLayoutManager(layoutManager);
                search_recycleview.setAdapter(searchRecyclerAdapter);
                search_recycleview.addItemDecoration(new DividerItemDecoration(CarMonSearchActivity.this, DividerItemDecoration.VERTICAL));
                search_recycleview.addOnItemTouchListener(new RecyclerItemClickListener(CarMonSearchActivity.this,new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        CarMonSearchListBean.CarSearchBean carSearchBean= carSearchBeans.get(position);
                        //点击具体的车辆
                        //请求具体的车辆信息
                        RequestParams params1=new RequestParams();
                        //因为传递的是json数据，所以需要设置header和body
                        params1.addHeader("Content-Type","application/json");
                        JSONObject jsonObject1=new JSONObject();
                        jsonObject1.put("car_id",carSearchBean.getId());
//                        jsonObject1.put("pagesize",10);
//                        jsonObject1.put("page",1);
                        Log.i(TAG, Api.queCarDeviceGps+"?token="+sp.getString(Constant.Token,""));
                        params1.setRequestBody(MediaType.parse("application/json"),jsonObject1.toString());
                        HttpRequest.post(Api.queCarDeviceGps+"?token="+sp.getString(Constant.Token,""),params1,new JsonHttpRequestCallback(){
                            @Override
                            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                                super.onSuccess(headers, jsonObject);
                                Log.i(TAG, jsonObject.toString());
                                carDetailGPSBeans carDetailGPSBeans=JSONObject.parseObject(jsonObject.toString(),carDetailGPSBeans.class);
                                if(carDetailGPSBeans.isSuccess()){
                                    //请求成功
                                    //显示点
                                    ArrayList<com.example.yzcl.mvp.model.bean.carDetailGPSBeans.carDetailGPSBean>carDetailGPSBean=carDetailGPSBeans.getList();
                                    Intent intent=new Intent();
                                    intent.setClass(CarMonSearchActivity.this,CarAddressActivity.class);
//                                    Bundle b=new Bundle();
//                                    b.put("pose_title", pose_title);
//                                    intent.putExtras(b);
                                    intent.putExtra("carDetailGPS", jsonObject.get("list").toString());
                                    startActivity(intent);

                                }else{
                                    DialogUIUtils.showToast("网络异常");
                                }
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                                dialog= DialogUIUtils.showLoading(CarMonSearchActivity.this,"加载中...",true,false,false,true);
                                dialog.show();
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                dialog.dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onLongClick(View view, int posotion) {

                    }
                }));
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                DialogUIUtils.showToast("网络错误");
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(CarMonSearchActivity.this,"加载中...",true,false,false,true);
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
