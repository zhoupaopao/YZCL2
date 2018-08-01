package com.example.yzcl.mvp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.adapter.CarDeviceListAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.DeviceListBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by 13126 on 2018/7/31.
 */
//真实的设备列表
public class RealDeviceListActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener{
    private ImageView back;
    private TextView AddCar;
    private TextView title;
    RecyclerView recyclerView;
    ArrayList<String>arrayList;
    CarDeviceListAdapter adapter;
    private AppBarLayout appBarLayout;
    private int dev_status=10;//设备在离线状态
    private XRefreshView xRefreshView;
    private String ids="";
    private String TAG="RealDeviceListActivity";
    private SharedPreferences sp;
    ArrayList<DeviceListBean.DeviceLLBean>deviceLLBeans;
    private BuildBean dialog;
    private RelativeLayout choose_customer;
    private TextView groupname;//账户名字
    private RadioGroup car_status;
    private int nowPages=1;//当前的页数
    private ImageView search;
    private RadioButton status_zdgz,status_yuqi,status_all;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_carlist);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData(dev_status,1);
        tongji(0,1);
        tongji(1,1);
        initListener();
    }

    private void initView() {
        status_zdgz=findViewById(R.id.status_zdgz);
        status_yuqi=findViewById(R.id.status_yuqi);
        status_all=findViewById(R.id.status_all);
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        title=findViewById(R.id.title);
        back=findViewById(R.id.back);
        AddCar=findViewById(R.id.add);
        search=findViewById(R.id.search);
        choose_customer=findViewById(R.id.choose_customer);
        deviceLLBeans=new ArrayList<>();
        car_status=findViewById(R.id.car_status);
        recyclerView=findViewById(R.id.recyclerView);
        arrayList=new ArrayList<>();
        groupname=findViewById(R.id.groupname);
        groupname.setText("全部用户");
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.contentView);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        xRefreshView=findViewById(R.id.xrefreshview);

        xRefreshView.setPullLoadEnable(true);
        //设置刷新完成以后，headerview固定的时间
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setMoveForHorizontal(true);
        //是否自动加载更新
        xRefreshView.setAutoLoadMore(true);
        xRefreshView.enablePullUpWhenLoadCompleted(true);
//        xRefreshView.setCustomFooterView(new XRefreshViewFooter(this));
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        initDataa(nowstatus,1);
//                        adapter.notifyItemRangeChanged(0,5) ;//列表从positionStart位置到itemCount数量的列表项进行数据刷新
//                        adapter.notifyDataSetChanged();//整个数据刷新
                        initDataa(dev_status,1);
                        xRefreshView.stopRefresh();

                    }
                }, 500);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        nowPages++;
                        loadmore(dev_status,nowPages);

                    }
                }, 1500);
            }
        });
    }
    private void tongji(final int dev_status, int page) {
        nowPages=page;
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("page",page);
        jsonObject.put("pagesize",1);
        if(ids.equals("")){

        }else{
            jsonObject.put("groupids",ids);
        }
        if(dev_status==0){
            //代表离线
            jsonObject.put("onlineState",dev_status);
        }else if(dev_status==1){
            //在线
            jsonObject.put("onlineState",dev_status);
        }else if(dev_status==2){
            //未定位
            jsonObject.put("onlineState",dev_status);
        }else{
            //全部
        }
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queryDeviceList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                DeviceListBean deviceListBean=JSONObject.parseObject(jsonObject.toString(),DeviceListBean.class);
                if(dev_status==0){
                    status_zdgz.setText("离线("+deviceListBean.getCount()+")");
                }else if(dev_status==1){
                    status_yuqi.setText("在线("+deviceListBean.getCount()+")");
                }else{
                    status_all.setText("全部("+deviceListBean.getCount()+")");
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
    }
    private void initData(final int dev_status, int page) {
        xRefreshView.setLoadComplete(false);
        nowPages=page;
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("page",page);
        jsonObject.put("pagesize",10);
        if(ids.equals("")){

        }else{
            jsonObject.put("groupids",ids);
        }
        if(dev_status==0){
            //代表离线
            jsonObject.put("onlineState",dev_status);
        }else if(dev_status==1){
            //在线
            jsonObject.put("onlineState",dev_status);
        }else if(dev_status==2){
            //未定位
            jsonObject.put("onlineState",dev_status);
        }else{
            //全部
        }
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queryDeviceList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                DeviceListBean deviceListBean=JSONObject.parseObject(jsonObject.toString(),DeviceListBean.class);
                if(dev_status==0){
                    status_zdgz.setText("离线("+deviceListBean.getCount()+")");
                }else if(dev_status==1){
                    status_yuqi.setText("在线("+deviceListBean.getCount()+")");
                }else{
                    status_all.setText("全部("+deviceListBean.getCount()+")");
                }
                deviceLLBeans=deviceListBean.getList();
                adapter=new CarDeviceListAdapter(RealDeviceListActivity.this,deviceLLBeans);
                adapter.setCustomLoadMoreView(new XRefreshViewFooter(RealDeviceListActivity.this));
                LinearLayoutManager layoutManager = new LinearLayoutManager(RealDeviceListActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                //设置增加或删除条目的动画
                recyclerView.setItemAnimator( new DefaultItemAnimator());
                if(deviceLLBeans.size()<10){
                    //说明没了
                    xRefreshView.setLoadComplete(true);

                }else{
                    xRefreshView.setLoadComplete(false);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        dialog.dialog.dismiss();
                    }
                }, 1500);
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(RealDeviceListActivity.this,"加载中...",true,true,false,true);
                dialog.show();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
    }
    private void loadmore(int dev_status,int page) {
        nowPages=page;
        //加载更多使用
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("page",page);
        jsonObject.put("pagesize",10);
        if(ids.equals("")){

        }else{
            jsonObject.put("groupids",ids);
        }
        if(dev_status==0){
            //代表离线
            jsonObject.put("onlineState",dev_status);
        }else if(dev_status==1){
            //在线
            jsonObject.put("onlineState",dev_status);
        }else if(dev_status==2){
            //未定位
            jsonObject.put("onlineState",dev_status);
        }else{
            //全部
        }
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queryDeviceList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                DeviceListBean deviceListBean=JSONObject.parseObject(jsonObject.toString(),DeviceListBean.class);
//                deviceLLBeans=deviceListBean.getList();
                if(deviceLLBeans.size()==0){
                    //没有更多数据的情况
                    xRefreshView.setLoadComplete(true);
                }else{
                    deviceLLBeans.addAll(deviceListBean.getList());
                    xRefreshView.stopLoadMore(true);//true代表加载成功，false代表失败
                }

            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
    }
    private void initDataa(int dev_status,int page) {
        nowPages=page;
        //下拉刷新使用
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("page",page);
        jsonObject.put("pagesize",10);
        if(ids.equals("")){

        }else{
            jsonObject.put("groupids",ids);
        }
        if(dev_status==0){
            //代表离线
            jsonObject.put("onlineState",dev_status);
        }else if(dev_status==1){
            //在线
            jsonObject.put("onlineState",dev_status);
        }else if(dev_status==2){
            //未定位
            jsonObject.put("onlineState",dev_status);
        }else{
            //全部
        }
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queryDeviceList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                DeviceListBean deviceListBean=JSONObject.parseObject(jsonObject.toString(),DeviceListBean.class);
                deviceLLBeans=deviceListBean.getList();
                adapter=new CarDeviceListAdapter(RealDeviceListActivity.this,deviceLLBeans);
                adapter.setCustomLoadMoreView(new XRefreshViewFooter(RealDeviceListActivity.this));
                LinearLayoutManager layoutManager = new LinearLayoutManager(RealDeviceListActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                //设置增加或删除条目的动画
                recyclerView.setItemAnimator( new DefaultItemAnimator());

                if(deviceLLBeans.size()>=10){
                    //加载显示
                    xRefreshView.setLoadComplete(false);
                }
            }

            @Override
            public void onStart() {
                super.onStart();

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
    }

    private void initListener() {
        title.setText("设备列表");
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
                intent.setClass(RealDeviceListActivity.this,AddCarActivity.class);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RealDeviceListActivity.this,DeviceSearchAcrivity.class);
                intent.putExtra("ids",ids);
                startActivity(intent);
            }
        });
        choose_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RealDeviceListActivity.this,CustomerChooseActivity.class);
                startActivityForResult(intent,1);
            }
        });
        car_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkid) {
                switch (checkid){
                    case R.id.status_all:
                        //全部
                        Log.i("onCheckedChanged", "all");
                        dev_status=10;
                        initData(10,1);
                        break;
                    case R.id.status_yuqi:
                        //在线
                        dev_status=1;
                        Log.i("onCheckedChanged", "zaixian");
                        initData(1,1);
                        break;
                    case  R.id.status_zdgz:
                        //离线
                        dev_status=0;
                        Log.i("onCheckedChanged", "离线");
                        initData(0,1);
                        break;
                    default:
                        //其他
                        break;
                }
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            xRefreshView.setEnabled(true);
        } else {
            xRefreshView.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
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
                //重新请求数据，根据当前的在离线和ids
                initData(dev_status,1);
                tongji(0,1);
                tongji(1,1);
                tongji(10,1);
                break;
        }
    }
}
