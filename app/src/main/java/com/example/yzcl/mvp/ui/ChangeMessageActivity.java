package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

                }

            }
        });

    }
}
