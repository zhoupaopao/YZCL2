package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.yzcl.mvp.ui.mvpactivity.MainActivity;
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
 * 修改密码
 */

public class ChangePwdActivity extends BaseActivity {
    TextView title;
    ImageView back;
    Button sure;
    EditText newpsd;
    EditText oldpsd;
    EditText surepsd;
    SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepwd);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
    }

    private void initView() {
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        title=findViewById(R.id.title);
        back=findViewById(R.id.back);
        sure=findViewById(R.id.sure);
        title.setText("修改密码");
        newpsd=findViewById(R.id.newpsd);
        oldpsd=findViewById(R.id.oldpsd);
        surepsd=findViewById(R.id.surepsd);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newpsd.getText().toString().trim().equals("")||oldpsd.getText().toString().trim().equals("")||surepsd.getText().toString().trim().equals("")){
                    Toast.makeText(ChangePwdActivity.this, "密码不能为空，请输入", Toast.LENGTH_SHORT).show();
                }else if(newpsd.getText().toString().trim().length()<6||oldpsd.getText().toString().trim().length()<6||surepsd.getText().toString().trim().length()<6){
                    Toast.makeText(ChangePwdActivity.this, "密码不能少于6位，请重新输入输入", Toast.LENGTH_SHORT).show();
                }else if(!Constant.isNetworkConnected(ChangePwdActivity.this)) {
                    //判断网络是否可用
                    Toast.makeText(ChangePwdActivity.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                }else if(newpsd.getText().toString().equals(surepsd.getText().toString())){
                    //密码确认
                    RequestParams params=new RequestParams();
                    params.addHeader("Content-Type","application/json");
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("password",oldpsd.getText().toString().trim());
                    jsonObject.put("newpassword",newpsd.getText().toString().trim());
                    params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
                    HttpRequest.post(Api.updateUserPw+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
                        @Override
                        protected void onSuccess(Headers headers, JSONObject jsonObject) {
                            super.onSuccess(headers, jsonObject);
                            if(jsonObject.getBoolean("success")){
                                //成功
                                Log.i("onSuccess", jsonObject.toString());
                                Intent intent= new Intent(ChangePwdActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                Toast.makeText(ChangePwdActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ChangePwdActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(int errorCode, String msg) {
                            super.onFailure(errorCode, msg);

                        }
                    });
                }else{
                    Toast.makeText(ChangePwdActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
