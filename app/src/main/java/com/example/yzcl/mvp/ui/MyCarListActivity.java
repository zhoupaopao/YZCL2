package com.example.yzcl.mvp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

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
    private String TAG="MyCarListActivity";
    CarListAdapter1 adapter;
    private String ids="";
    private BuildBean dialog;
    private String nowstatus="";
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
        initData(nowstatus);
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
//        status_all=findViewById(R.id.status_all);
//        status_yuqi=findViewById(R.id.status_yuqi);
//        status_zdgz=findViewById(R.id.status_zdgz);
    }

    private void initData(String statuss) {
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
                adapter=new CarListAdapter1(MyCarListActivity.this,carListBean.getList());
                LinearLayoutManager layoutManager = new LinearLayoutManager(MyCarListActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                carlistview.setLayoutManager(layoutManager);
                carlistview.setAdapter(adapter);
                adapter.setupRecyclerView(carlistview);
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

                        initData("");
                        break;
                    case R.id.status_yuqi:
                        //逾期
                        Log.i("onCheckedChanged", "yuqi");
                        initData("2");
                        break;
                    case  R.id.status_zdgz:
                        //重点关注
                        Log.i("onCheckedChanged", "zdgz");
                        initData("3");
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
