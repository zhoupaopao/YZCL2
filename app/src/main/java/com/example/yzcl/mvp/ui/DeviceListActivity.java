package com.example.yzcl.mvp.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.adapter.CarDeviceListAdapter;
import com.example.yzcl.adapter.DeviceListAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.DeviceListBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.example.yzcl.mvp.ui.mvpactivity.HomePage;
import com.example.yzcl.mvp.ui.mvpactivity.MainActivity;
import com.example.yzcl.utils.MyListView;
import com.gyf.barlibrary.ImmersionBar;


import java.util.ArrayList;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/7/30.
 */

public class DeviceListActivity extends BaseActivity{
    //设备列表界面
    private ImageView back;
    private ImageView search;
    private TextView title;
    private ArrayList<String>list;
    private MyListView device_list;
    Toolbar toolbar;
    DeviceListBean deviceListBean;
    ArrayList<DeviceListBean.DeviceLLBean>deviceLLBeans;
    AppBarLayout abl_title;
    TextView tv_title;
    CarDeviceListAdapter adapter;
    TranslateAnimation mShowAction;
    TranslateAnimation mHiddenAction;
    private String ids="";
    private String TAG="DeviceListActivity";
    private SharedPreferences sp;
    private RadioGroup online;
    private RadioGroup binding;
    private int dev_status=0;//设备在离线状态
    private int dev_bind_status=0;//设备绑车状态
    private BuildBean dialog;
//    private XRefreshView xRefreshView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData(dev_status,dev_bind_status);
        initListener();
    }


    private void initView() {
        ids=getIntent().getStringExtra("ids");
        online=findViewById(R.id.online);
        deviceLLBeans=new ArrayList<>();
        binding=findViewById(R.id.binding);
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        toolbar=findViewById(R.id.tl_title);
        back=findViewById(R.id.back);
        search=findViewById(R.id.add);
        title=findViewById(R.id.title);
        tv_title=findViewById(R.id.tv_title);
        device_list=findViewById(R.id.device_list);
        abl_title=findViewById(R.id.abl_title);
        list=new ArrayList<>();
        //sadsad
//        xRefreshView=findViewById(R.id.xrefreshview);
//
//        xRefreshView.setPullLoadEnable(true);
//        //设置刷新完成以后，headerview固定的时间
//        xRefreshView.setPinnedTime(1000);
//        xRefreshView.setMoveForHorizontal(true);
//        //是否自动加载更新
//        xRefreshView.setAutoLoadMore(true);
//        xRefreshView.enablePullUpWhenLoadCompleted(true);
////        xRefreshView.setCustomFooterView(new XRefreshViewFooter(this));
//        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
//
//            @Override
//            public void onRefresh(boolean isPullDown) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                        initDataa(nowstatus,1);
////                        adapter.notifyItemRangeChanged(0,5) ;//列表从positionStart位置到itemCount数量的列表项进行数据刷新
////                        adapter.notifyDataSetChanged();//整个数据刷新
//                        xRefreshView.stopRefresh();
//                    }
//                }, 500);
//            }
//
//            @Override
//            public void onLoadMore(boolean isSilence) {
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
////                        nowPages++;
////                        loadData(nowstatus,nowPages);
//                    }
//                }, 500);
//            }
//        });
    }

    private void initData(int dev_status,int dev_bind_status) {
        Log.i(TAG, "initData: 123");
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("page",1);
        jsonObject.put("pagesize",10);
        if(ids.equals("")){

        }else{
            jsonObject.put("groupids",ids);
        }
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.queryDeviceList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                DeviceListBean deviceListBean=JSONObject.parseObject(jsonObject.toString(),DeviceListBean.class);
                deviceLLBeans=deviceListBean.getList();
                adapter=new CarDeviceListAdapter(DeviceListActivity.this,deviceLLBeans);
                LinearLayoutManager layoutManager = new LinearLayoutManager(DeviceListActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                device_list.setLayoutManager(layoutManager);
                device_list.setNestedScrollingEnabled(false);
                device_list.setAdapter(adapter);
                //设置增加或删除条目的动画
                device_list.setItemAnimator( new DefaultItemAnimator());
                dialog.dialog.dismiss();
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(DeviceListActivity.this,"加载中...",true,true,false,true);
                dialog.show();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });
        for(int i=0;i<20;i++){
            list.add("第"+i+"条");
        }
    }

    private void initListener() {
        title.setText("设备列表");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        search.setImageResource(R.mipmap.search);
//        adapter= new CarDeviceListAdapter(DeviceListActivity.this,list);
//        device_list.setAdapter(adapter);
        abl_title.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {
                    toolbar.setVisibility(View.GONE);
                    //展开状态
                    Log.i("onStateChanged: ", "2: ");
                    tv_title.setVisibility(View.GONE);
                    tv_title.animate()
                            .alpha(0f)
                            .setDuration(500)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    tv_title.setVisibility(View.GONE);
                                }
                            });
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    Log.i("onStateChanged: ", "3: ");
                    toolbar.setVisibility(View.VISIBLE);
                    tv_title.setVisibility(View.VISIBLE);
                    String ttile="";//用于显示头部栏
                    if(dev_status==0){
                        //全部
                        ttile="全部";
                    }else if(dev_status==1){
                        //在线
                        ttile="在线";
                    }else if(dev_status==2){
                        //离线
                        ttile="离线";
                    }
                    if(dev_bind_status==0){
                        //全部
                        ttile=ttile+"-全部";
                    }else if(dev_bind_status==1){
                        //已绑车
                        ttile=ttile+"-已绑车";
                    }else if(dev_bind_status==2){
                        //未绑车
                        ttile=ttile+"-未绑车";
                    }else if(dev_bind_status==3){
                        //已过期
                        ttile=ttile+"-已过期";
                    }
                    tv_title.setText(ttile);
                    tv_title.animate()
                            .alpha(1f)
                            .setDuration(500)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    tv_title.setVisibility(View.VISIBLE);
                                }
                            });
                }else {
                    //中间状态
                    Log.i("onStateChanged: ", "1: ");

                }
            }
        });
        binding.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkid) {
                switch (checkid){
                    case R.id.binding_all:
                        //全部
                        dev_bind_status=0;
                        initData(dev_status,0);
                        break;
                    case R.id.binding_have:
                        //已绑车
                        dev_bind_status=1;
                        initData(dev_status,1);
                        break;
                    case  R.id.binding_none:
                        //未绑车
                        dev_bind_status=2;
                        initData(dev_status,2);
                        break;
                    case  R.id.binding_over:
                        //已过期
                        dev_bind_status=3;
                        initData(dev_status,3);
                        break;
                    default:
                        //其他
                        break;
                }
            }
        });
        online.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkid) {
                switch (checkid){
                    case R.id.online_all:
                        //全部
                        Log.i("onCheckedChanged", "all");
                        dev_status=0;
                        initData(0,dev_bind_status);
                        break;
                    case R.id.online_is:
                        //在线
                        dev_status=1;
                        Log.i("onCheckedChanged", "yuqi");
                        initData(1,dev_bind_status);
                        break;
                    case  R.id.online_none:
                        //离线
                        dev_status=2;
                        Log.i("onCheckedChanged", "zdgz");
                        initData(2,dev_bind_status);
                        break;
                    default:
                        //其他
                        break;
                }
            }
        });
//        setListViewHeightBasedOnChildren(device_list);
    }

    public abstract static class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
        public enum State {
            EXPANDED,
            COLLAPSED,
            IDLE
        }

        private State mCurrentState = State.IDLE;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (i == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED);
                }
                mCurrentState = State.EXPANDED;
            } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED);
                }
                mCurrentState = State.COLLAPSED;
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE);
                }
                mCurrentState = State.IDLE;
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
    }
}
