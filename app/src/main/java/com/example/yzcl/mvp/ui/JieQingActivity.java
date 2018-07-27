package com.example.yzcl.mvp.ui;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.adapter.SettleListAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/2/4.
 */

public class JieQingActivity extends BaseActivity {
    private ImageView back;
    private ImageView search;
    private TextView title;
    private ListView carlistview;
    SharedPreferences sp=null;
    private TextView jq_date;
    private TextView jq_num;
    ImageView rili;
    private TextView sure;
    private TextView cancel;
    private DatePicker dp;
    private String ids="";
    private String starttime="";
    private String endtime="";
    BuildBean dia;
    BuildBean jq_dia;
    SettleListBean settleListBean;
    ArrayList<SettleListBean.SettleBean>settleBeans;
    SettleListAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jieqing_list);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        back=findViewById(R.id.back);
        search=findViewById(R.id.add);
        title=findViewById(R.id.title);
        carlistview=findViewById(R.id.carlist);
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        jq_date=findViewById(R.id.jq_date);
        jq_num=findViewById(R.id.jq_num);
        rili=findViewById(R.id.rili);
    }

    private void initData() {
        ids=getIntent().getStringExtra("ids");
        Calendar calendar=Calendar.getInstance();
        int init_month=calendar.get(Calendar.MONTH)+1;
        jq_date.setText(calendar.get(Calendar.YEAR)+"年"+init_month+"月");
        int days=getDayOfMonth(calendar.get(Calendar.YEAR),init_month);
        starttime=calendar.get(Calendar.YEAR)+"-"+init_month+"-"+01;
        endtime=calendar.get(Calendar.YEAR)+"-"+init_month+"-"+days;
        //舒适化结清列表
        InitList();
    }
    //获取某年某月有多少天
    public int getDayOfMonth(int year,int month){
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0); //输入类型为int类型
        return c.get(Calendar.DAY_OF_MONTH);
    }
    private void InitList() {
        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        if(ids.equals("")){

        }else{
            jsonObject.put("groupids",ids);
        }
        jsonObject.put("starttime",starttime);
        jsonObject.put("endtime",endtime);
        jsonObject.put("page",1);
        jsonObject.put("pagesize",100);
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.querySettleList+"?token="+sp.getString(Constant.Token,""),params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, com.alibaba.fastjson.JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                settleListBean= com.alibaba.fastjson.JSONObject.parseObject(jsonObject.toString(),SettleListBean.class);
                if(settleListBean.isSuccess()){
                    jq_num.setText("共计结清"+settleListBean.getCount()+"台车辆");
                    settleBeans=settleListBean.getList();
                    adapter= new SettleListAdapter(JieQingActivity.this,settleBeans);
                    carlistview.setAdapter(adapter);
                }else{
                    Toast.makeText(JieQingActivity.this,settleListBean.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onStart() {
                super.onStart();
                jq_dia=DialogUIUtils.showLoading(JieQingActivity.this, "加载中...",false,true,false,false);
                jq_dia.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                DialogUIUtils.dismiss(jq_dia);
            }
        });

    }
    private void initListener() {
        search.setImageResource(R.mipmap.search);
        search.setVisibility(View.GONE);
        title.setText("已结清列表");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View rootViewB = View.inflate(JieQingActivity.this, R.layout.date_choice_bottom, null);
                //为了能够单独隐藏这个弹出框
                dia=DialogUIUtils.showCustomBottomAlert(JieQingActivity.this, rootViewB);
                dia.show();
                sure=rootViewB.findViewById(R.id.sure);
                cancel=rootViewB.findViewById(R.id.cancel);
                dp=rootViewB.findViewById(R.id.date_bottom);
//                ((ViewGroup)dp.getChildAt(0)).getChildAt(0))
                Calendar calendar=Calendar.getInstance();
//                dp.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                dp.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                        //这里时间改变不需要进行操作
                    }
                });
                hideDay(dp);
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int date_month=dp.getMonth()+1;
                        //隐藏
                        DialogUIUtils.dismiss(dia);
                        jq_date.setText(dp.getYear()+"年"+date_month+"月");
                        int days=getDayOfMonth(dp.getYear(),date_month);
                        starttime=dp.getYear()+"-"+date_month+"-"+01;
                        endtime=dp.getYear()+"-"+date_month+"-"+days;
                        Log.i("onClick: ", endtime);
                        InitList();
//                        Toast.makeText(JieQingActivity.this,dp.getYear()+"-"+date_month,Toast.LENGTH_SHORT).show();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogUIUtils.dismiss(dia);
                    }
                });
            }
        });
    }
    private void hideDay(DatePicker mDatePicker) {
        //可以用来隐藏日，普通的datepicker是显示年月日的，这里只需要年月
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = mDatePicker.findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(mDatePicker);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        client.post(JieQingActivity.this, Api.cancelSettleById + "?token=" + token, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String json=new String(bytes).trim().toString();
                JSONTokener jsonTokener=new JSONTokener(json);
                try {
                    JSONObject jsonObject1= (JSONObject) jsonTokener.nextValue();
                    if(jsonObject1.getBoolean("success")){
                        Toast.makeText(JieQingActivity.this,jsonObject1.getString("message"),Toast.LENGTH_SHORT).show();
                        InitList();
                    }else{
                        Toast.makeText(JieQingActivity.this,jsonObject1.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                jq_dia=DialogUIUtils.showLoading(JieQingActivity.this, "加载中...",false,true,false,false);
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
