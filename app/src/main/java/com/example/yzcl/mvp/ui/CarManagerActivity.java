package com.example.yzcl.mvp.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.example.yzcl.mvp.model.bean.DeviceBean;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 2017/11/30.
 */

/**
 * 用来显示车辆管理分导页面
 */
public class CarManagerActivity extends BaseActivity {
    private XRefreshView refreshView;
    private ListView mylist;
    private ProgressDialog progressDialog;
    private DeviceBean deviceBean;
    private int flag=0;//标识当前选中的是 全部0  在线1 离线2
    private TextView allnum;
    private TextView numalnum;
    private TextView yuqinum;
    private TextView add;
    private ImageView back;
    private ImageView search;
    private TextView title;
    private HashMap<String,DeviceBean.CarListBean> map;
    private ArrayList<DeviceBean.CarListBean> carlist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manager);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();

    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(CarManagerActivity.this,AddCarActivity.class);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        allnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=0;
                initSearList(flag);
                notifyAdapterDataChange();//数据变化
            }
        });
        numalnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=1;
                initSearList(flag);
                notifyAdapterDataChange();
            }
        });
        yuqinum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=2;
                initSearList(flag);
                notifyAdapterDataChange();
            }
        });
    }

    private void initData() {
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("queryString","");
        params.put("userID","3ab403f00d324cb9807dfbb7d8bd63ee");
        params.put("onLineType",0);
        params.put("isBindCar",0);
        params.put("page",1);
        params.put("pageSize",50);
        params.put("sortName","");
        params.put("asc","");
        params.put("showChild",true);
        params.put("tokenString","JdefLeelKBb3tmhj+Thwc8UDX9OiXS8LL/6J9OKHhA6PLmh2IetJlKcUMahLaUbp/wueOVl9KgDzKgRKT6ZDZQXsu5fV2tuymVhkCHgzN4E/A3gXLFuILeYlbK0KJPju8CssGwH6Leu03aumIXHfoat5Bm8US21ePuKg+vUUcEU=");
        params.setContentEncoding("UTF-8");
        client.post(Api.Jsonp_GetDevicePositionByUserID, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String json=new String(bytes,"UTF-8").trim();
                    //将接口中的数据获取到
                    deviceBean= JSONObject.parseObject(json,DeviceBean.class);
                    //初始化数据
                    initCarlist();
                    //将不同的数据放入不同的分组
                    initSearList(flag);
                    //数据有变化，刷新适配器
                    notifyAdapterDataChange();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
    private void initCarlist() {
        allnum.setText("全部（"+deviceBean.getAllCount()+"）");
        numalnum.setText("正常（"+deviceBean.getOnLineCount()+"）");
        yuqinum.setText("逾期（"+deviceBean.getOffLineCount()+"）");

    }
    private void initSearList(int i) {
        switch (i){
            case 0:
                map=new HashMap<String,DeviceBean.CarListBean>();
                carlist=new ArrayList<DeviceBean.CarListBean>();
                for(int j=0;j<deviceBean.getDataList().size();j++){
                    carlist.add(deviceBean.getDataList().get(j));
                }
                break;
            case 1:
                map=new HashMap<String,DeviceBean.CarListBean>();
                carlist=new ArrayList<DeviceBean.CarListBean>();
                carlist.clear();
                for(int j=0;j<deviceBean.getDataList().size();j++){
                    if(deviceBean.getDataList().get(j).isIsOnline()==true){

                        carlist.add(deviceBean.getDataList().get(j));
                    }

                }
                break;
            case 2:
                map=new HashMap<String,DeviceBean.CarListBean>();
                carlist=new ArrayList<DeviceBean.CarListBean>();
                carlist.clear();
                for(int j=0;j<deviceBean.getDataList().size();j++){
                    if(deviceBean.getDataList().get(j).isIsOnline()==false){

                        carlist.add(deviceBean.getDataList().get(j));
                    }

                }
                break;
        }
    }

    private void notifyAdapterDataChange() {
        CarListAdapter adapter=new CarListAdapter(CarManagerActivity.this,carlist);
        mylist.setAdapter(adapter);
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        refreshView=findViewById(R.id.custom_view1);
        mylist=findViewById(R.id.mylistview);
        progressDialog=new ProgressDialog(this);
        allnum=findViewById(R.id.allnum);
        numalnum=findViewById(R.id.numalnum);
        yuqinum=findViewById(R.id.yuqinumm);
        back=findViewById(R.id.back);
        title=findViewById(R.id.title);
        search=findViewById(R.id.search);
        add=findViewById(R.id.add);
        title.setText("车辆管理");
    }
    public class CarListAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        private ArrayList<DeviceBean.CarListBean>data=null;
//        private CarManagerActivity carManagerActivity;
        public CarListAdapter(CarManagerActivity carManagerActivity, ArrayList<DeviceBean.CarListBean> carl) {
//            this.carManagerActivity=carManagerActivity;
            this.mInflater = LayoutInflater.from(carManagerActivity);
            data=carl;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder=null;
            if(viewHolder==null){
                viewHolder=new ViewHolder();
                view=mInflater.inflate(R.layout.item_carlist_ceshi,null);
                viewHolder.name=view.findViewById(R.id.name);
                viewHolder.address=view.findViewById(R.id.address);
                viewHolder.carnum=view.findViewById(R.id.carnum);
                viewHolder.show=view.findViewById(R.id.show);
                view.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            viewHolder.name.setText(data.get(i).getCarNumber());
            viewHolder.address.setText(data.get(i).getAddress());
            viewHolder.carnum.setText(data.get(i).getCarInfoID());
            viewHolder.show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(CarManagerActivity.this,data.get(i).getDeviceID().toString(),Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }
    }
    public class ViewHolder{
        public TextView name;
        public TextView carnum;
        public TextView address;
        public Button show;
    }
}
