package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;

/**
 * Created by Lenovo on 2017/12/12.
 */

public class AccountMessageActivity extends BaseActivity {
    ImageView back;
    TextView title;
    RelativeLayout name;
    RelativeLayout mobile;
    RelativeLayout email;
    RelativeLayout changepwd;
    SharedPreferences sp;
    BuildBean dia;
    TextView login_name;
    TextView user_name;
    TextView phone;
    TextView tv_email;
    TextView group_name;
    String list_Jurisdiction;//权限设置
    private String TAG="AccountMessageActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_message);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
//        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Constant.isNetworkConnected(AccountMessageActivity.this)) {
            //判断网络是否可用
            Toast.makeText(AccountMessageActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
        }else{
            initData();
        }

    }

    private void initData() {
        RequestParams params=new RequestParams();
        params.addFormDataPart("token",sp.getString(Constant.Token,""));
        params.addFormDataPart("id",sp.getString(Constant.userid,""));
        HttpRequest.get(Api.getDetailUser,params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, "onSuccess: "+jsonObject.toString());
                if(jsonObject.getBoolean("success")){
                    //请求成功
                    JSONObject object=jsonObject.getJSONObject("object");
                    login_name.setText(object.getString("user_account"));
                    user_name.setText(object.getString("name"));
                    phone.setText(object.getString("phone"));
                    tv_email.setText(object.getString("email"));
                    group_name.setText(object.getJSONObject("systemgroup").getString("group_name"));
                }else{
                    Toast.makeText(AccountMessageActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                }
                DialogUIUtils.dismiss(dia);
            }

            @Override
            public void onStart() {
                super.onStart();
                dia= DialogUIUtils.showLoading(AccountMessageActivity.this, "加载中...", false, true, false, false);
                dia.show();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(AccountMessageActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        name=findViewById(R.id.changename);
        mobile=findViewById(R.id.mobile);
        email=findViewById(R.id.email);
        changepwd=findViewById(R.id.changepsd);
        back=findViewById(R.id.back);
        title=findViewById(R.id.title);
        title.setText("账户信息");
        login_name=findViewById(R.id.login_name);
        user_name=findViewById(R.id.user_name);
        phone=findViewById(R.id.phone);
        tv_email=findViewById(R.id.tv_email);
        group_name=findViewById(R.id.group_name);
        list_Jurisdiction=sp.getString("list_Jurisdiction","");
        String[]list_jur=list_Jurisdiction.split(",");
        for(int i=0;i<list_jur.length;i++){
            if (list_jur[i].equals("146")){
                changepwd.setVisibility(View.VISIBLE);
                break;
            }
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //不同按钮对应不同数据，不同请求接口
        //设置姓名
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("id",sp.getString(Constant.userid,""));
                jsonObject.put("phone",phone.getText().toString().trim());
                jsonObject.put("email",tv_email.getText().toString().trim());
                Intent intent=new Intent();
                intent.putExtra("title","设置姓名");
                intent.putExtra("nowid","1");
                intent.putExtra("StringText",user_name.getText().toString().trim());
                intent.putExtra("json_param",jsonObject.toString());
                intent.putExtra("prompt","输入2~10个汉字的中文姓名");
                intent.setClass(AccountMessageActivity.this,ChangeMessageActivity.class);
                startActivity(intent);
            }
        });
        //设置手机号
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("id",sp.getString(Constant.userid,""));
                jsonObject.put("name",user_name.getText().toString().trim());
                jsonObject.put("email",tv_email.getText().toString().trim());
                Intent intent=new Intent();
                intent.putExtra("title","设置手机号");
                intent.putExtra("nowid","2");
                intent.putExtra("StringText",phone.getText().toString().trim());
                intent.putExtra("json_param",jsonObject.toString());
                intent.putExtra("prompt","");
                intent.setClass(AccountMessageActivity.this,ChangeMessageActivity.class);
                startActivity(intent);
            }
        });
        //设置邮箱
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("id",sp.getString(Constant.userid,""));
                jsonObject.put("name",user_name.getText().toString().trim());
                jsonObject.put("phone",phone.getText().toString().trim());
                Intent intent=new Intent();
                intent.putExtra("title","设置邮箱");
                intent.putExtra("nowid","3");
                intent.putExtra("StringText",tv_email.getText().toString().trim());
                intent.putExtra("json_param",jsonObject.toString());
                intent.putExtra("prompt","");
                intent.setClass(AccountMessageActivity.this,ChangeMessageActivity.class);
                startActivity(intent);
            }
        });
        //修改密码
        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(AccountMessageActivity.this,ChangePwdActivity.class);
                startActivity(intent);
            }
        });
    }
}
