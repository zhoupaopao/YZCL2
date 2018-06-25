package com.example.yzcl.mvp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzcl.R;
import com.example.yzcl.adapter.WlglRadiusAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.mvp.model.bean.WlglRadiusBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/1/29.
 */

/**
 * 真正的围栏管理界面
 */
public class EnclosureRadiusManagerActivity extends BaseActivity {
    private TextView title;
    private TextView add_enclosure;
    private ImageView back;
    private ListView list_enclosure;
    WlglRadiusBean wlglBean;
    SharedPreferences sp=null;
    CheckBox radio_all;
    WlglRadiusAdapter adapter;
    ArrayList<WlglRadiusBean.EnclosureBean> list;
    private TextView finish;
    boolean radio_all_ck=false;
    private TextView plkq;
    private TextView plgb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encloure_radius_manager);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();

    }

    private void initView() {
        title=findViewById(R.id.title);
        back=findViewById(R.id.back);
        add_enclosure=findViewById(R.id.textview2);
        list_enclosure=findViewById(R.id.list_encloure);
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        radio_all=findViewById(R.id.radio_all);
        finish=findViewById(R.id.finish);
        plkq=findViewById(R.id.plkq);
        plgb=findViewById(R.id.plgz);
    }

    private void initData() {
        title.setText("围栏管理");
        add_enclosure.setText("添加围栏");
        String url_token=sp.getString("Token",null);
        Log.i("url_token", url_token);
        Log.i("json", "312");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fencename","");
            jsonObject.put("pagesize",100);
            jsonObject.put("page",1);
//            jsonObject.put("token",sp.getString("token",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonObject.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client=new AsyncHttpClient();
        //设置contenttype的请求方式
        client.post(this, "http://118.178.227.126:20200//fence/v1/getPageFence?token="+url_token, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String json=new String(bytes,"UTF-8").trim().toString();
                    wlglBean= com.alibaba.fastjson.JSONObject.parseObject(json,WlglRadiusBean.class);
                    list=wlglBean.getList();
//                    for(int j=0;j<list.size();j++){
//                        list.get(j).setCb(true);
//                    }
                    if(wlglBean.isSuccess()){
                        //请求成功

                         adapter=new WlglRadiusAdapter(EnclosureRadiusManagerActivity.this,list, new AllCheckListener() {
                             @Override
                             public void onCheckedChanged(boolean b) {
                                 radio_all.setChecked(b);
                             }
                         });
                        list_enclosure.setAdapter(adapter);
                    }else{
                        //请求失败
                        Toast.makeText(EnclosureRadiusManagerActivity.this,wlglBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.i("json", json);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    String json=new String(bytes,"UTF-8").trim().toString();
                    Log.i("json1", json);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        add_enclosure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(EnclosureRadiusManagerActivity.this,EnclosureActivity.class);
                startActivity(intent);
            }
        });
        radio_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radio_all_ck){
                    //代表全选
                    Log.i("onCheckedChanged", "2 ");
                    for(int i=0;i<list.size();i++){
                        list.get(i).setCb(false);
                    }
                    adapter.notifyDataSetChanged();
                }else{

                    Log.i("onCheckedChanged", "1 ");
                    radio_all_ck=true;
                    for(int i=0;i<list.size();i++){
                        list.get(i).setCb(true);
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        });
//        radio_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//
//
//                }else{
//                    Log.i("onCheckedChanged", "2 ");
//                    for(int i=0;i<list.size();i++){
//                        list.get(i).setCb(false);
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list=adapter.getDatalist();

            }
        });
        plgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasfenceid=false;
                list=adapter.getDatalist();
                String fenceids="";
                AsyncHttpClient client=new AsyncHttpClient();
                RequestParams params=new RequestParams();
                params.put("token",sp.getString("Token",""));
                for(int i=0;i<list.size();i++){
                    if(list.get(i).isCb()){
                        //被选中的
                        hasfenceid=true;
                        fenceids=fenceids+","+list.get(i).getId();
                    }
                }
                if(hasfenceid){
                    //有至少一个围栏id
                    params.put("fenceids",fenceids);
                    params.setContentEncoding("UTF-8");
                    client.get(Api.batchClose, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            try {
                                String json=new String(bytes,"UTF-8").trim().toString();
                                JSONTokener jsonTokener=new JSONTokener(json);
                                JSONObject ob= (JSONObject) jsonTokener.nextValue();
                                if(ob.getBoolean("success")){

                                    Toast.makeText(EnclosureRadiusManagerActivity.this,"关闭成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(EnclosureRadiusManagerActivity.this,"关闭失败",Toast.LENGTH_SHORT).show();
                                }

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                        }
                    });

                }else{
                    //没有围栏id
                    Toast.makeText(EnclosureRadiusManagerActivity.this,"请至少选择一个围栏",Toast.LENGTH_SHORT).show();
                }

            }
        });
        plkq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasfenceid=false;
                list=adapter.getDatalist();
                String fenceids="";
                AsyncHttpClient client=new AsyncHttpClient();
                RequestParams params=new RequestParams();
                params.put("token",sp.getString("Token",""));
                for(int i=0;i<list.size();i++){
                    if(list.get(i).isCb()){
                        //被选中的
                        hasfenceid=true;
                        fenceids=fenceids+","+list.get(i).getId();
                    }
                }
                if(hasfenceid){
                    //有至少一个围栏id
                    params.put("fenceids",fenceids);
                    params.setContentEncoding("UTF-8");
                    client.get(Api.batchOpen, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            try {
                                String json=new String(bytes,"UTF-8").trim().toString();
                                JSONTokener jsonTokener=new JSONTokener(json);
                                JSONObject ob= (JSONObject) jsonTokener.nextValue();
                                if(ob.getBoolean("success")){

                                   Toast.makeText(EnclosureRadiusManagerActivity.this,"开启成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(EnclosureRadiusManagerActivity.this,"开启失败",Toast.LENGTH_SHORT).show();
                                }

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                        }
                    });

                }else{
                    //没有围栏id
                    Toast.makeText(EnclosureRadiusManagerActivity.this,"请至少选择一个围栏",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public interface AllCheckListener {
        void onCheckedChanged(boolean b);
    }
}
