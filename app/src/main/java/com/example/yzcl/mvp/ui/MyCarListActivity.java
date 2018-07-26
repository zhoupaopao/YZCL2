package com.example.yzcl.mvp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.adapter.CarListAdapter1;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CarListBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

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
//车辆列表页面
public class MyCarListActivity extends BaseActivity {
    private ImageView back;
    private ImageView search;
    private TextView title;
    private RecyclerView carlistview;
    private ArrayList<String> cc_list;
    private RadioGroup car_status;
    SharedPreferences sp=null;
    CarListBean carListBean;
    ArrayList<CarListBean.CarBean>nowList=new ArrayList<>();
    private String TAG="MyCarListActivity";
    CarListAdapter1 adapter;
    private String ids="";
    private BuildBean dialog;
    private String nowstatus="";//当前页数
    private XRefreshView xRefreshView;
    private int nowPages=1;//当前的页数
//    private String nowstatus="";
//    ArrayList<CarListBean.CarBean>carlist;
//    private RadioButton status_all;
//    private RadioButton status_yuqi;
//    private RadioButton status_zdgz;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycar_list);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        nowPages=1;
        initData(nowstatus,1);
        initListener();
    }

    private void initView() {
        back=findViewById(R.id.back);
        search=findViewById(R.id.add);
        title=findViewById(R.id.title);
        carlistview=findViewById(R.id.carlist);
        car_status=findViewById(R.id.car_status);
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        ids=getIntent().getStringExtra("ids");
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
                        initDataa(nowstatus,1);
//                        adapter.notifyItemRangeChanged(0,5) ;//列表从positionStart位置到itemCount数量的列表项进行数据刷新
//                        adapter.notifyDataSetChanged();//整个数据刷新
                        xRefreshView.stopRefresh();
                    }
                }, 500);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        nowPages++;
                        loadData(nowstatus,nowPages);
                    }
                }, 500);
            }
        });
//        status_all=findViewById(R.id.status_all);
//        status_yuqi=findViewById(R.id.status_yuqi);
//        status_zdgz=findViewById(R.id.status_zdgz);
    }

    private void loadData(String statuss, final int page) {
        //上滑加载更多
        nowPages=page;
        nowstatus=statuss;
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        if(ids.equals("")){

        }else{
            jsonObject.put("groupids",ids);
        }
        if(statuss.equals("")){

        }else{
            jsonObject.put("status",statuss);
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
    private void initData(String statuss,int page) {
        //加了这个可以让已经不能加载的时候能够加载
        xRefreshView.setLoadComplete(false);
        nowPages=page;
        nowstatus=statuss;
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        if(ids.equals("")){

        }else{
            jsonObject.put("groupids",ids);
        }
        if(statuss.equals("")){

        }else{
            jsonObject.put("status",statuss);
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
                adapter=new CarListAdapter1(MyCarListActivity.this,nowList);
                adapter.setCustomLoadMoreView(new XRefreshViewFooter(MyCarListActivity.this));
                LinearLayoutManager layoutManager = new LinearLayoutManager(MyCarListActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                carlistview.setLayoutManager(layoutManager);
                carlistview.setAdapter(adapter);
//                adapter.setupRecyclerView(carlistview);
                //设置增加或删除条目的动画
                carlistview.setItemAnimator( new DefaultItemAnimator());
                dialog.dialog.dismiss();
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog= DialogUIUtils.showLoading(MyCarListActivity.this,"加载中...",true,true,false,true);
                dialog.show();

            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                dialog.dialog.dismiss();
            }
        });
//        cc_list=new ArrayList<>();
//        for(int i=0;i<10;i++){
//            cc_list.add(i+"aa");
//        }
//        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,R.layout.layout_car_list_item,R.id.name,cc_list);
//        carlist.setAdapter(adapter);
    }
    private void initDataa(String statuss,int page) {

        nowPages=page;
        nowstatus=statuss;
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        if(ids.equals("")){

        }else{
            jsonObject.put("groupids",ids);
        }
        if(statuss.equals("")){

        }else{
            jsonObject.put("status",statuss);
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

    private void initListener() {
        search.setImageResource(R.mipmap.search);
        title.setText("车辆列表");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        car_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkid) {
                switch (checkid){
                    case R.id.status_all:
                        //全部
                        Log.i("onCheckedChanged", "all");

                        initData("",1);
                        break;
                    case R.id.status_yuqi:
                        //逾期
                        Log.i("onCheckedChanged", "yuqi");
                        initData("2",1);
                        break;
                    case  R.id.status_zdgz:
                        //重点关注
                        Log.i("onCheckedChanged", "zdgz");
                        initData("3",1);
                        break;
                    default:
                            //其他
                        break;
                }
            }
        });

    }

    private void doNetWork(int i) {
        JSONObject jsonObject=new JSONObject();
        try {
            //0:全部；1：逾期；2：重点关注
            jsonObject.put("status ",i);
            jsonObject.put("pagesize ",100);
            jsonObject.put("page ",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity entity=null;
        try {
            entity=new StringEntity(jsonObject.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String token=sp.getString("Token","");
        AsyncHttpClient client=new AsyncHttpClient();
        client.post(MyCarListActivity.this, Api.getCar + "?token=" + token, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String json = new String(bytes).trim().toString();
                Log.i("getCar", json);
                carListBean = com.alibaba.fastjson.JSONObject.parseObject(json, CarListBean.class);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
}
