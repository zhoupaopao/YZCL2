package com.example.yzcl.mvp.ui.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.example.yzcl.R;
import com.example.yzcl.adapter.GwListAdapter;
import com.example.yzcl.adapter.SbListAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.mvp.model.bean.GwListBean;
import com.example.yzcl.mvp.model.bean.WarningListBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Lenovo on 2018/2/7.
 */

@SuppressLint("ValidFragment")
public class GwListFragment extends Fragment {
    String type="";
    SharedPreferences sp=null;
    ListView warning_list;
    GwListBean gwListBean;
    WarningListBean warningListBean;
    @SuppressLint("ValidFragment")
    public GwListFragment(String content){
        this.type=content;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.layout_list_warning, null);
        warning_list=contentView.findViewById(R.id.warning_list);
        //获取这个fragment所依靠的那个activity
        sp=getActivity().getSharedPreferences("YZCL",Context.MODE_PRIVATE);
        doNetWork();

        return contentView;
    }

    private void doNetWork() {
        AsyncHttpClient client=new AsyncHttpClient();
        JSONObject jsonObject=new JSONObject();
        if(type=="1"){
            try {
                jsonObject.put("alarmtype","-1");
                //不加pagesize了
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(type=="2"){
            try {
                //拆除
                jsonObject.put("alarmtype","1");
                //不加pagesize了
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(type=="3"){
            try {
                //屏蔽
                jsonObject.put("alarmtype","2");
                //不加pagesize了
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(type=="4"){
            try {
                //屏蔽
                jsonObject.put("alarmtype","-1");
                jsonObject.put("devicetype","0");

                //不加pagesize了
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(type=="5"){

        }else if(type=="6"){

        }else if(type=="7"){

        }else if(type=="8"){

        }else if(type=="9"){

        }
        StringEntity entity=null;
        try {
            entity=new StringEntity(jsonObject.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String token=sp.getString("Token","");
        if(type=="1"||type=="2"||type=="3"){
            client.post(getActivity(), Api.queryHighAlarmList+"?token="+token, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String json=new String(bytes).trim().toString();
                    gwListBean= com.alibaba.fastjson.JSONObject.parseObject(json,GwListBean.class);
                    GwListAdapter adapter=new GwListAdapter(GwListFragment.this.getActivity(),gwListBean.getList());
                    warning_list.setAdapter(adapter);
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                }
            });
        }else if(type=="4"||type=="5"||type=="6"||type=="7"||type=="8"||type=="9"){
            Log.i("json", "123");
            client.post(getActivity(), Api.getAlarmInfo+"?token="+token, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String json=new String(bytes).trim().toString();
                    Log.i("json", json);
                    warningListBean= com.alibaba.fastjson.JSONObject.parseObject(json,WarningListBean.class);
                    SbListAdapter adapter=new SbListAdapter(GwListFragment.this.getActivity(),warningListBean.getList());
                    warning_list.setAdapter(adapter);
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                }
            });
        }


    }
}
