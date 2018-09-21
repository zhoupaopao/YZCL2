package com.example.yzcl.mvp.ui;

import android.content.Context;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshViewFooter;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.adapter.CarDeviceListAdapter;
import com.example.yzcl.adapter.CarListAdapter1;
import com.example.yzcl.adapter.CarListAdapter2;
import com.example.yzcl.adapter.SearchHistoryDeviceAdapter;
import com.example.yzcl.adapter.SearchHistoryDeviceCarAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CarListBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/7/27.
 */

public class CarSearchActivity extends BaseActivity {
    private RecyclerView car_list;
    private TextView cancel;
    private ImageView delete;//删除按钮
    private EditText et_search;
    private SharedPreferences sp;
    private String TAG="CarSearchActivity";
    private String ids="";
    private CarListBean carListBean;
    ArrayList<CarListBean.CarBean> nowList=new ArrayList<>();
    CarListAdapter2 adapter;
    private BuildBean dialog;
//搜索相关
SearchHistoryDeviceCarAdapter searchadapter;
//    private RecyclerView search_recycleview;
    private RelativeLayout rl_search_history;
    private RecyclerView recycleview;
    private ArrayList<String> arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_search);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        car_list=findViewById(R.id.car_list);
        cancel=findViewById(R.id.cancel);
        delete=findViewById(R.id.delete);
        et_search=findViewById(R.id.et_search);
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        arrayList=new ArrayList<>();
        recycleview=findViewById(R.id.recycleview);
//        search_recycleview=findViewById(R.id.search_recycleview);
        rl_search_history=findViewById(R.id.rl_search_history);
    }

    private void initData() {
        ids=getIntent().getStringExtra("ids");
        //虚拟赋值，用于显示历史记录
        String his_list=sp.getString("search_list_car",",");
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
        searchadapter=new SearchHistoryDeviceCarAdapter(this,arrayList);


//        searchRecyclerAdapter1=new CarDeviceListAdapter(DeviceSearchAcrivity.this,deviceLLBeans);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(DeviceSearchAcrivity.this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        search_recycleview.setLayoutManager(layoutManager);
//        search_recycleview.setAdapter(searchRecyclerAdapter1);
//        search_recycleview.addItemDecoration(new DividerItemDecoration(DeviceSearchAcrivity.this, DividerItemDecoration.VERTICAL));
    }

    private void initListener() {
        et_search.setHint("请输入车架号、借款人");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleview.setLayoutManager(layoutManager);
        recycleview.setAdapter(searchadapter);
        //添加Android自带的分割线
        recycleview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycleview.setItemAnimator( new DefaultItemAnimator());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_search.setText("");
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                            .hideSoftInputFromWindow(CarSearchActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    // 搜索，进行自己要的操作...
                    if(et_search.getText().toString().trim().equals("")){
                        Toast.makeText(CarSearchActivity.this,"搜索字符不能为空",Toast.LENGTH_SHORT).show();
                    }else{
                        String search_list_last=sp.getString("search_list_car","");
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("search_list_car",et_search.getText().toString().trim()+","+search_list_last);
                        editor.commit();
                        if(!Constant.isNetworkConnected(CarSearchActivity.this)) {
                            //判断网络是否可用
                            Toast.makeText(CarSearchActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
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

    public void queVehicleListForSea(final String trim) {
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        if(ids.equals("")){

        }else{
            jsonObject.put("groupids",ids);
        }
        jsonObject.put("serach",trim);
        jsonObject.put("page",1);
        jsonObject.put("pagesize",100);
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.getCar+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, com.alibaba.fastjson.JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                carListBean= com.alibaba.fastjson.JSONObject.parseObject(jsonObject.toString(),CarListBean.class);
                if(carListBean.isSuccess()){
                    car_list.setVisibility(View.VISIBLE);
                    recycleview.setVisibility(View.GONE);
                    rl_search_history.setVisibility(View.GONE);
                    nowList=carListBean.getList();
                    if(nowList.size()==0){
                        //没有车辆数据
                        Toast.makeText(CarSearchActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
                    }
                    adapter=new CarListAdapter2(CarSearchActivity.this,nowList,trim);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(CarSearchActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    car_list.setLayoutManager(layoutManager);
                    car_list.setAdapter(adapter);
//                adapter.setupRecyclerView(carlistview);
                    //设置增加或删除条目的动画
                    car_list.setItemAnimator( new DefaultItemAnimator());
                    dialog.dialog.dismiss();
                }else{
                    dialog.dialog.dismiss();
                    Toast.makeText(CarSearchActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(CarSearchActivity.this,"加载中...",true,true,false,true);
                dialog.show();

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                dialog.dialog.dismiss();
            }
        });
    }
}
