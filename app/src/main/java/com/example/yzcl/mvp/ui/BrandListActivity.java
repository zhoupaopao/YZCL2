package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.adapter.SortAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.model.bean.CarBrandBean;
import com.example.yzcl.mvp.presenter.view.SideBar;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * Created by Lenovo on 2018/3/19.
 */

public class BrandListActivity extends Activity {
    private SideBar sideBar;
    private TextView dialog;
    private ListView sortListView;
    private SharedPreferences sp = null;
    private BuildBean loading;
    String token;
    private TextView title;
    ImageView back;
    private SortAdapter adapter;
    ArrayList<String> indexString = new ArrayList<>();
    ArrayList<CarBrandBean.BrandListBean> brand_list=new ArrayList<>();
    Boolean isfirpage=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brandlist);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sp = getSharedPreferences("YZCL", MODE_PRIVATE);
        token = sp.getString("Token", "");
        back= (ImageView) findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title);
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        title.setText("选择品牌");
        sideBar.setTextView(dialog);
        initDatas("0");
        initEvents();
        setAdapter();
    }

    private void initDatas(String idd ) {

        RequestParams params=new RequestParams();
        params.addHeader("Content-Type","application/json");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("id",idd+"");
        params.setRequestBody(MediaType.parse("application/json"),jsonObject.toString());
        HttpRequest.post(Api.getCarBand+"?token="+sp.getString(Constant.Token,""), params, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i("jsonObject", jsonObject.toString());
                CarBrandBean carBrandBean = JSONObject.parseObject(jsonObject.toString(), CarBrandBean.class);
                brand_list = (ArrayList<CarBrandBean.BrandListBean>) carBrandBean.getList();
//                brandlist.add("选择品牌");
                for (int i = 0; i < brand_list.size(); i++) {
                    CarBrandBean.BrandListBean brandListBean = brand_list.get(i);
                    String shou=brand_list.get(i).getInitial();
                    if (shou.toUpperCase().matches("[A-Z]")) {
                        if (!indexString.contains(shou)) {
                            indexString.add(shou);
                            Log.i("onSuccess", shou);
                        }
                    }

                }
                Log.i("onsuccess", indexString.toString());
                sideBar.setIndexText(indexString);

                adapter = new SortAdapter(BrandListActivity.this, brand_list);
                sortListView.setAdapter(adapter);
                loading.dialog.dismiss();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }

            @Override
            public void onStart() {
                super.onStart();
                loading= DialogUIUtils.showLoading(BrandListActivity.this,"加载中...",true,true,false,true);
                loading.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }
    private void initEvents() {
//设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position );
                }
            }
        });

        //ListView的点击事件
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                mTvTitle.setText(((CarBrandBean.BrandListBean) adapter.getItem(position - 1)).getBrand_name());
//                Toast.makeText(getApplication(), ((CarBrandBean.BrandListBean) adapter.getItem(position)).getBrand_name(), Toast.LENGTH_SHORT).show();
                //重新请求列表，先判断是那个页面了
                if(isfirpage){
                    Log.i("onItemClick: ", ((CarBrandBean.BrandListBean) adapter.getItem(position)).getName()+"|"+((CarBrandBean.BrandListBean)adapter.getItem(position)).getId());
                    initDatas(((CarBrandBean.BrandListBean)adapter.getItem(position)).getId());
                    sideBar.setVisibility(View.GONE);
                    isfirpage=false;
                }else{
                    Intent intent=new Intent();
                    intent.putExtra("brand_name",((CarBrandBean.BrandListBean) adapter.getItem(position)).getName()); //将计算的值回传回去
                    intent.putExtra("brand_id", ((CarBrandBean.BrandListBean) adapter.getItem(position)).getId()); //将计算的值回传回去
                    //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                    Log.i("onItemClick: ", ((CarBrandBean.BrandListBean) adapter.getItem(position)).getName());
                    setResult(3, intent);

                    finish();
                }



            }
        });
    }

    private void setAdapter() {

        Log.i("setAdapter", indexString.toString());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isfirpage){
                    finish();
                }else{
                    initDatas("0");
                    sideBar.setVisibility(View.VISIBLE);
                    isfirpage=true;
                }

            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(isfirpage){
                finish();
            }else{
                initDatas("0");
                sideBar.setVisibility(View.VISIBLE);
                isfirpage=true;
            }
        }
        return true;
    }
}
