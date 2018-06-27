package com.example.yzcl.mvp.ui;

import android.content.Context;
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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.adapter.SearchHistoryAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
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
    }

    private void initData() {
        for(int i=0;i<6;i++){
            arrayList.add("张三"+i);
        }
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
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    queVehicleListForSea();
                    return true;
                }
                return false;
            }
        });
    }

    private void queVehicleListForSea() {
        //展示搜索列表
        RequestParams params=new RequestParams();
//        params.setRequestBodyString();
//        params.setRequestBody();
        //因为传递的是json数据，所以需要设置header和body
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("search","11");
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
//        params.addFormDataPart("token",sp.getString(Constant.Token,""));
//        params.addFormDataPart("search",et_search.getText().toString().trim());
        Log.i(TAG, Api.queVehicleListForSea+"?token="+sp.getString(Constant.Token,""));

        HttpRequest.post(Api.queVehicleListForSea+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                DialogUIUtils.showToast("请求成功");
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
