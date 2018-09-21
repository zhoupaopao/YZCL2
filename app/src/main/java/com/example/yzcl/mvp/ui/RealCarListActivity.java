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
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.adapter.CarDeviceListAdapter;
import com.example.yzcl.adapter.CarListAdapter1;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CarListBean;
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
 * Created by Lenovo on 2018/8/1.
 */
//真实的车辆列表
public class RealCarListActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener{
    private ImageView back;
    private TextView AddCar;
    private TextView title;
    RecyclerView recyclerView;
    ArrayList<String> arrayList;
    CarListAdapter1 adapter;
    private AppBarLayout appBarLayout;
    private int dev_status=0;//设备在离线状态
    private XRefreshView xRefreshView;
    private String ids="";
    private String TAG="RealDeviceListActivity";
    private SharedPreferences sp;
//    ArrayList<DeviceListBean.DeviceLLBean>deviceLLBeans;
    private BuildBean dialog;
    private RelativeLayout choose_customer;
    private TextView groupname;//账户名字
    private RadioGroup car_status;
    private int nowPages=1;//当前的页数
    private RadioButton status_all;
    private RadioButton status_yuqi;
    private RadioButton status_zdgz;
    private ImageView search;
    CarListBean carListBean;
    ArrayList<CarListBean.CarBean>nowList=new ArrayList<>();
    private boolean canaddcar=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_carlist);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();

                initView();
        checkqx();
//        initData(dev_status,1);
        initListener();

    }
    private void initView() {
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        title=findViewById(R.id.title);
        search=findViewById(R.id.search);
        back=findViewById(R.id.back);
        AddCar=findViewById(R.id.add);
        AddCar.setVisibility(View.GONE);
        car_status=findViewById(R.id.car_status);
        choose_customer=findViewById(R.id.choose_customer);
//        deviceLLBeans=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        arrayList=new ArrayList<>();
        groupname=findViewById(R.id.groupname);
        status_all=findViewById(R.id.status_all);
        status_yuqi=findViewById(R.id.status_yuqi);
        status_zdgz=findViewById(R.id.status_zdgz);
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
                        loadData(dev_status,nowPages);

                    }
                }, 500);
            }
        });
    }
    private void initDataa(int statuss,int page) {

        nowPages=page;
        dev_status=statuss;
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        if(ids.equals("")){

        }else{
            jsonObject.put("groupids",ids);
        }
        if(statuss==2){
            //逾期
            jsonObject.put("status",statuss+"");
        }else if(statuss==3){
            //重点关注
            jsonObject.put("status",statuss+"");
        }else{
            //全部
        }
        jsonObject.put("page",1);
        jsonObject.put("pagesize",10);
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.getCar+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, com.alibaba.fastjson.JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                carListBean= com.alibaba.fastjson.JSONObject.parseObject(jsonObject.toString(),CarListBean.class);
                nowList=carListBean.getList();
//                adapter=new CarListAdapter1(MyCarListActivity.this,nowList);
                adapter.changedata(nowList);
                if(nowList.size()>=10){
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
//        cc_list=new ArrayList<>();
//        for(int i=0;i<10;i++){
//            cc_list.add(i+"aa");
//        }
//        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,R.layout.layout_car_list_item,R.id.name,cc_list);
//        carlist.setAdapter(adapter);
    }
    private void loadData(int statuss, final int page) {
        //上滑加载更多
        Log.i(TAG+"loadData: ", page+"/"+statuss);
        nowPages=page;
        dev_status=statuss;
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        if(ids.equals("")){

        }else{
            jsonObject.put("groupids",ids);
        }
        if(statuss==2){
            //逾期
            jsonObject.put("status",statuss+"");
        }else if(statuss==3){
            //重点关注
            jsonObject.put("status",statuss+"");
        }else{
            //全部

        }
        jsonObject.put("page",page);
        jsonObject.put("pagesize",10);
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.getCar+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, com.alibaba.fastjson.JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                carListBean= com.alibaba.fastjson.JSONObject.parseObject(jsonObject.toString(),CarListBean.class);
                if(carListBean.getList().size()==0){
                    //没有更多数据的情况
                    xRefreshView.setLoadComplete(true);
                }else{
                    nowList.addAll(carListBean.getList());
                    Log.i(TAG, page-1+"onSuccess: ");
//                    adapter.notifyItemRangeInserted((page-1)*10, nowList.size()-1);//批量插入。从起始位置到结束位置
                    // 刷新完成必须调用此方法停止加载
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
    public void achievetj() {
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        if(ids.equals("")){

        }else{
            jsonObject.put("groupids",ids);
        }
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.getRedBind+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, com.alibaba.fastjson.JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                if(jsonObject.getBoolean("success")){
                    com.alibaba.fastjson.JSONObject jsonObject1=jsonObject.getJSONObject("object");
                    int normal=jsonObject1.getInteger("bindCar")-jsonObject1.getInteger("overdueCar")-jsonObject1.getInteger("careCar");
                    status_all.setText("全部("+jsonObject1.getInteger("bindCar")+")");
                    status_yuqi.setText("逾期("+jsonObject1.getInteger("overdueCar")+")");
                    status_zdgz.setText("重点关注("+jsonObject1.getInteger("careCar")+")");
                }else{
                    Toast.makeText(RealCarListActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(RealCarListActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void initData(int dev_status1, int page) {
        xRefreshView.setLoadComplete(false);
        dev_status=dev_status1;
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
            //代表所以
        }else if(dev_status==1){
            //在线
            jsonObject.put("status",dev_status+"");
        }else if(dev_status==2){
            //离线
            jsonObject.put("status",dev_status+"");
        }else if(dev_status==3){
            jsonObject.put("status",dev_status+"");
        }
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        final int finalDev_status = dev_status;
        HttpRequest.post(Api.getCar+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, jsonObject.toString());
                carListBean= com.alibaba.fastjson.JSONObject.parseObject(jsonObject.toString(),CarListBean.class);
                if(carListBean.isSuccess()){
                    nowList=carListBean.getList();

                    adapter=new CarListAdapter1(RealCarListActivity.this,nowList, finalDev_status);
                    adapter.setCustomLoadMoreView(new XRefreshViewFooter(RealCarListActivity.this));
                    LinearLayoutManager layoutManager = new LinearLayoutManager(RealCarListActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
//                adapter.setupRecyclerView(carlistview);
                    //设置增加或删除条目的动画
                    recyclerView.setItemAnimator( new DefaultItemAnimator());
                    //获取统计
                    if(nowList.size()<10){
                        //说明没了
                        xRefreshView.setLoadComplete(true);

                    }else{
                        xRefreshView.setLoadComplete(false);
                    }
                    achievetj();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            dialog.dialog.dismiss();
                        }
                    }, 1500);

                }else{
                    Toast.makeText(RealCarListActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    dialog.dialog.dismiss();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                Log.i(TAG, "dialog.show() ");
                dialog= DialogUIUtils.showLoading(RealCarListActivity.this,"加载中...",true,true,false,true);
                dialog.show();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });


//        adapter=new EasyRecycleAdapter(this,arrayList);


    }

    private void initListener() {
        title.setText("车辆列表");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        AddCar.setText("新增");
//        AddCar.setVisibility(View.GONE);
        AddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RealCarListActivity.this,AddCarActivity.class);
                startActivity(intent);
            }
        });
        choose_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RealCarListActivity.this,CustomerChooseActivity.class);
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

                        initData(10,1);
                        break;
                    case R.id.status_yuqi:
                        //逾期
                        Log.i("onCheckedChanged", "yuqi");
                        initData(2,1);
                        break;
                    case  R.id.status_zdgz:
                        //重点关注
                        Log.i("onCheckedChanged", "zdgz");
                        initData(3,1);
                        break;
                    default:
                        //其他
                        break;
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RealCarListActivity.this,CarSearchActivity.class);
                intent.putExtra("ids",ids);
                startActivity(intent);
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
    private void checkqx() {
        String list_Jurisdiction=sp.getString("list_Jurisdiction","");
        String[]list_jur=list_Jurisdiction.split(",");
        for(int i=0;i<list_jur.length;i++){
            if (list_jur[i].equals("176")){
                //新增
                AddCar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initView();
        initData(dev_status,1);
//        initListener();
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
                Log.i(TAG, names);
                groupname.setText(names.substring(0,names.length()-1));
                //重新请求数据，根据当前的在离线和ids
//                initData(dev_status,1);
                break;
        }
    }

}
