package com.example.yzcl.mvp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.adapter.CarListAdapter2;
import com.example.yzcl.adapter.SettleListAdapter;
import com.example.yzcl.adapter.SettleListSearchAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CarListBean;
import com.example.yzcl.mvp.model.bean.SettleListBean;
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
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/7/30.
 */

public class JieQingSearchActivity extends BaseActivity {
    private ListView carlistview;
    private TextView cancel;
    private ImageView delete;//删除按钮
    private EditText et_search;
    private SharedPreferences sp;
    private String TAG="JieQingSearchActivity";
    private String ids="";
    private BuildBean jq_dia;
    SettleListBean settleListBean;
    ArrayList<SettleListBean.SettleBean>settleBeans;
    SettleListSearchAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jieqing_search);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        carlistview=findViewById(R.id.car_list);
        cancel=findViewById(R.id.cancel);
        delete=findViewById(R.id.delete);
        et_search=findViewById(R.id.et_search);
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
    }

    private void initData() {
        ids=getIntent().getStringExtra("ids");
    }

    private void initListener() {
        et_search.setHint("请输入车架号、借款人");
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
                            .hideSoftInputFromWindow(JieQingSearchActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    // 搜索，进行自己要的操作...
                    if(et_search.getText().toString().trim().equals("")){
                        Toast.makeText(JieQingSearchActivity.this,"搜索字符不能为空",Toast.LENGTH_SHORT).show();
                    }else{
                        if(!Constant.isNetworkConnected(JieQingSearchActivity.this)) {
                            //判断网络是否可用
                            Toast.makeText(JieQingSearchActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                        }else{
                            queJieqingList(et_search.getText().toString().trim());
                        }

                    }

                    return true;
                }
                return false;
            }
        });
    }

    private void queJieqingList(final String trim) {
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        if(ids.equals("")){

        }else{
            jsonObject.put("groupids",ids);
        }
        jsonObject.put("page",1);
        jsonObject.put("pagesize",100);
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.querySettleList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, com.alibaba.fastjson.JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                settleListBean= com.alibaba.fastjson.JSONObject.parseObject(jsonObject.toString(),SettleListBean.class);
                if(settleListBean.isSuccess()){
//                    jq_num.setText("共计结清"+settleListBean.getCount()+"台车辆");
                    settleBeans=settleListBean.getList();
                    adapter= new SettleListSearchAdapter(JieQingSearchActivity.this,settleBeans);
                    carlistview.setAdapter(adapter);
                }else{
                    Toast.makeText(JieQingSearchActivity.this,settleListBean.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onStart() {
                super.onStart();
                jq_dia=DialogUIUtils.showLoading(JieQingSearchActivity.this, "加载中...",false,true,false,false);
                jq_dia.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                DialogUIUtils.dismiss(jq_dia);
            }
        });
    }
    public void qxjq(String id) {
        //执行取消结清操作
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("id",id);
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
        AsyncHttpClient client=new AsyncHttpClient();
        String token=sp.getString("Token","");
        client.post(JieQingSearchActivity.this, Api.cancelSettleById + "?token=" + token, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String json=new String(bytes).trim().toString();
                JSONTokener jsonTokener=new JSONTokener(json);
                try {
                    JSONObject jsonObject1= (JSONObject) jsonTokener.nextValue();
                    if(jsonObject1.getBoolean("success")){
                        Toast.makeText(JieQingSearchActivity.this,jsonObject1.getString("message"),Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(JieQingSearchActivity.this,jsonObject1.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                jq_dia=DialogUIUtils.showLoading(JieQingSearchActivity.this, "加载中...",false,true,false,false);
                jq_dia.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                DialogUIUtils.dismiss(jq_dia);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }


}
