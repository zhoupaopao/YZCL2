package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CarMessageBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2017/12/13.
 */

/**
 * 修改个人信息
 */

public class ChangeMessageActivity extends BaseActivity {
    TextView cancel;//取消
    TextView sure;//确定
    TextView title;//标题
    EditText String_text;//文本填写
    ImageView delete;//文本删除按钮
    TextView prompt;//提示
    JSONObject jsonObject;//需要提交的参数
    private SharedPreferences sp;
    String nowid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_message);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
    }

    private void initView() {
        Intent intent=getIntent();
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        cancel=findViewById(R.id.textview1);
        sure=findViewById(R.id.textview2);
        title=findViewById(R.id.title);
        delete=findViewById(R.id.delete);
        String_text=findViewById(R.id.string_text);
        prompt=findViewById(R.id.prompt);
        jsonObject= (JSONObject) JSONObject.parse(intent.getStringExtra("json_param"));
        nowid=intent.getStringExtra("nowid");
        title.setText(intent.getStringExtra("title"));
        String_text.setText(intent.getStringExtra("StringText"));
        String_text.setSelection(intent.getStringExtra("StringText").length());
        if(nowid.equals("2")){
            //设置手机号最大位数
            String_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            //设置只能为数字
            String_text.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        if(nowid.equals("1")){
            String_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            //设置只能为数字
//            String_text.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        if(intent.getStringExtra("prompt")==""){
            prompt.setVisibility(View.INVISIBLE);
        }else{
            prompt.setVisibility(View.VISIBLE);
            prompt.setText(intent.getStringExtra("prompt"));
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String_text.setText("");
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //需要提交数据

                if(String_text.getText().toString().trim().equals("")){
                    //是空
                    Toast.makeText(ChangeMessageActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }else if(nowid.equals("2")&&String_text.getText().toString().trim().length()!=11){
                    Toast.makeText(ChangeMessageActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                }else if(nowid.equals("1")&&String_text.getText().toString().trim().length()<2){
                    Toast.makeText(ChangeMessageActivity.this,"请输入正确的姓名",Toast.LENGTH_SHORT).show();
                }
                else if(nowid.equals("3")){
                    //是邮箱的话判断邮箱格式是不是符合
                    if(!Constant.isNetworkConnected(ChangeMessageActivity.this)) {
                        //判断网络是否可用
                        Toast.makeText(ChangeMessageActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                    }else{
                        if(isEmail(String_text.getText().toString().trim())){
                            //格式正确
                            RequestParams params=new RequestParams();
                            params.addHeader("Content-Type","application/json");
                            jsonObject.put("email",String_text.getText().toString().trim());
                            params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
                            HttpRequest.post(Api.updateUser+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
                                @Override
                                protected void onSuccess(Headers headers, JSONObject jsonObject) {
                                    super.onSuccess(headers, jsonObject);
                                    if(jsonObject.getBoolean("success")){
                                        Toast.makeText(ChangeMessageActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Toast.makeText(ChangeMessageActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(int errorCode, String msg) {
                                    super.onFailure(errorCode, msg);

                                }
                            });
                        }else{
                            Toast.makeText(ChangeMessageActivity.this,"邮箱格式不正确，请重新输入",Toast.LENGTH_SHORT).show();
                        }
                    }


                }else{
                    if(!Constant.isNetworkConnected(ChangeMessageActivity.this)) {
                        //判断网络是否可用
                        Toast.makeText(ChangeMessageActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                    }else{
                    RequestParams params=new RequestParams();
                    params.addHeader("Content-Type","application/json");
                    if(nowid.equals("1")){
                        //姓名
                        jsonObject.put("name",String_text.getText().toString().trim());
                    }else if(nowid.equals("2")){
                        //手机号
                        jsonObject.put("phone",String_text.getText().toString().trim());
                    }else if(nowid.equals("3")){
                        //邮箱
                        jsonObject.put("email",String_text.getText().toString().trim());
                    }

                    params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
                    HttpRequest.post(Api.updateUser+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
                        @Override
                        protected void onSuccess(Headers headers, JSONObject jsonObject) {
                            super.onSuccess(headers, jsonObject);
                            if(jsonObject.getBoolean("success")){
                                Toast.makeText(ChangeMessageActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(ChangeMessageActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(int errorCode, String msg) {
                            super.onFailure(errorCode, msg);

                        }
                    });

                }}

            }
        });

    }
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }
//    private boolean checkEmail(String s) {
//        //判断email格式是否正确
//        if(s.matches("\\p{Alpha}\\w{2,15}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}"))
//        {
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }

}
