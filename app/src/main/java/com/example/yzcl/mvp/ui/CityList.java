package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

/**
 * Created by Lenovo on 2017/12/8.
 */

public class CityList extends Activity {
    private ListView mTree;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_city);
        initView();

    }

    private void initView() {
        progressDialog=new ProgressDialog(this);
        mTree=findViewById(R.id.ac_listview);
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("TokenString","JdefLeelKBb3tmhj+Thwc8UDX9OiXS8LL/6J9OKHhA6PLmh2IetJlKcUMahLaUbp/wueOVl9KgDzKgRKT6ZDZQXsu5fV2tuymVhkCHgzN4E/A3gXLFuILeYlbK0KJPju8CssGwH6Leu03aumIXHfoat5Bm8US21ePuKg+vUUcEU=");
        params.setContentEncoding("UTF-8");
        client.post(Api.GET_PROVINCE_CITY, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String json=new String(bytes,"UTF-8").trim();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                progressDialog.setMessage("正在获取信息");
                progressDialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (progressDialog.isShowing() && progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
}
