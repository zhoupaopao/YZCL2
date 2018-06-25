package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.example.yzcl.mvp.ui.mvpactivity.HomePage;
import com.example.yzcl.utils.StatusBarUtil;
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

/**
 * Created by Lenovo on 2017/12/15.
 */

public class RiskWarningActivity extends BaseActivity {
    ImageView back;
    TextView title;
    private RelativeLayout choose_customer;
    SharedPreferences sp=null;
    TextView gwbj_num;
    TextView lxbj_num;
    TextView sbbj_num;
    TextView dzbj_num;
    LinearLayout ll_gw;
    LinearLayout ll_lx;
    LinearLayout ll_sb;
    LinearLayout ll_wl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riskwarning);
        ImmersionBar.with(this)
                .statusBarColor(R.color.jb_head_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initData() {
        AsyncHttpClient client=new AsyncHttpClient();
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("","");
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
        client.post(RiskWarningActivity.this, Api.queAlarmStati + "?token=" + token, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String json=new String(bytes).trim().toString();
                Log.i("queVehicleAbnormalStati", json);
                JSONTokener jsonTokener=new JSONTokener(json);
                try {
                    JSONObject jsonObject1= (JSONObject) jsonTokener.nextValue();
                    gwbj_num.setText(jsonObject1.getJSONObject("object").getString("unPowerCounts"));
                    lxbj_num.setText(jsonObject1.getJSONObject("object").getString("offlineCounts"));
                    sbbj_num.setText(jsonObject1.getJSONObject("object").getString("lightCounts"));
                    dzbj_num.setText(jsonObject1.getJSONObject("object").getString("fenceCounts"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i("queVehicleAbnormalStati", "failure");
            }
        });
    }

    private void initListener() {
        choose_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RiskWarningActivity.this,CustomerChooseActivity.class);
                startActivity(intent);
            }
        });
        ll_gw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RiskWarningActivity.this,WarningGWActivity.class);
                intent.putExtra("alarmtype","gw");
                startActivity(intent);
            }
        });
        ll_sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RiskWarningActivity.this,WarningGWActivity.class);
                intent.putExtra("alarmtype","sb");
                startActivity(intent);
            }
        });
    }

    private void initView() {
        back=findViewById(R.id.back);
        choose_customer=findViewById(R.id.choose_customer);
        title=findViewById(R.id.title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("风控预警");
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        gwbj_num=findViewById(R.id.gwbj_num);
        lxbj_num=findViewById(R.id.lxbj_num);
        sbbj_num=findViewById(R.id.sbbj_num);
        dzbj_num=findViewById(R.id.dzwlbj_num);
        ll_gw=findViewById(R.id.ll_gw);
        ll_lx=findViewById(R.id.ll_lx);
        ll_sb=findViewById(R.id.ll_sb);
        ll_wl=findViewById(R.id.ll_wl);
    }
}
