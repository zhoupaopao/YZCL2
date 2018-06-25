package com.example.yzcl.mvp.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.example.yzcl.R;
import com.example.yzcl.adapter.WlglAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.mvp.model.bean.WlglBean;
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
import java.util.List;

/**
 * Created by Lenovo on 2018/1/29.
 */

/**
 * 真正的围栏管理界面
 */
public class EnclosureAllManagerActivity extends BaseActivity {
    private TextView title;
    private TextView add_enclosure;
    private ImageView back;
    private ListView list_enclosure;
    //批量处理
    private LinearLayout batch;
    List<String> arraydata=new ArrayList<>();
    WlglBean wlglBean;
    SharedPreferences sp=null;
    private TextView wl_num;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encloure_all_manager);
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
        wl_num=findViewById(R.id.wl_num);
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        batch=findViewById(R.id.batch);
    }
public void delete(final String id){
//    AlertDialog
    DialogUIUtils.showAlert(EnclosureAllManagerActivity.this, "删除提示", "确定要删除该围栏吗？删除后与该围栏关联的车辆将同步解绑。", "", "", "取消", "确定", false, true, true, new DialogUIListener() {
        @Override
        public void onPositive() {
            //取消
        }

        @Override
        public void onNegative() {
            //确定删除
            AsyncHttpClient client=new AsyncHttpClient();
            RequestParams params=new RequestParams();
            params.put("token",sp.getString("Token",""));
            params.put("id",id);
            params.setContentEncoding("UTF-8");
            client.get(Api.delFence, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try {
                        String json=new String(bytes,"UTF-8").trim().toString();
                        JSONTokener jsonTokener=new JSONTokener(json);
                        JSONObject jsonObject= (JSONObject) jsonTokener.nextValue();
                        if(jsonObject.getBoolean("success")){
                            Toast.makeText(EnclosureAllManagerActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                            initData();
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
        }
    }).show();
}

    public void initData() {
        title.setText("围栏管理");
        add_enclosure.setText("添加围栏");
        for(int i=0;i<50;i++){
            arraydata.add("item"+i);
        }
        final String url_token=sp.getString("Token",null);
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
                    wlglBean= com.alibaba.fastjson.JSONObject.parseObject(json,WlglBean.class);
                    if(wlglBean.isSuccess()){
                        //请求成功
                        wl_num.setText(wlglBean.getCount()+"");
                        WlglAdapter adapter=new WlglAdapter(EnclosureAllManagerActivity.this,wlglBean.getList(),url_token);
                        list_enclosure.setAdapter(adapter);
                    }else{
                        //请求失败
                        Toast.makeText(EnclosureAllManagerActivity.this,wlglBean.getMessage(), Toast.LENGTH_SHORT).show();
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
                intent.setClass(EnclosureAllManagerActivity.this,EnclosureActivity.class);
                startActivity(intent);
            }
        });
        list_enclosure.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(EnclosureAllManagerActivity.this,wlglBean.getList().get(i).getFencename(),Toast.LENGTH_SHORT).show();
            }
        });
        batch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入批量处理页面，可能是弹窗
                Intent intent=new Intent();
                intent.setClass(EnclosureAllManagerActivity.this, EnclosureRadiusManagerActivity.class);
//                intent.putExtra("list",wlglBean.getList());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("onRestart", "onRestart: ");
        initData();
    }
}
