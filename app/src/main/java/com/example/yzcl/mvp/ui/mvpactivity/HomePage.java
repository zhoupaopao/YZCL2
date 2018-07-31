package com.example.yzcl.mvp.ui.mvpactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.map.MapView;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.example.yzcl.R;
import com.example.yzcl.adapter.GridViewAdapter;
import com.example.yzcl.content.Api;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.presenter.GlideImageLoader;
import com.example.yzcl.mvp.ui.CarAddressActivity;
import com.example.yzcl.mvp.ui.CarManagerActivity;
import com.example.yzcl.mvp.ui.CarManagerFragmentActivity;
import com.example.yzcl.mvp.ui.CarManagerRealActivity;
import com.example.yzcl.mvp.ui.CustomerManagementActivity;
import com.example.yzcl.mvp.ui.CustomerManagerActivity;
import com.example.yzcl.mvp.ui.EnclosureActivity;
import com.example.yzcl.mvp.ui.EnclosureAllManagerActivity;
import com.example.yzcl.mvp.ui.EnclosureManagerActivity;
import com.example.yzcl.mvp.ui.GunDongActivity;
import com.example.yzcl.mvp.ui.LowerAccountActivity;
import com.example.yzcl.mvp.ui.PersonMessageActivity;
import com.example.yzcl.mvp.ui.RiskWarningActivity;
import com.example.yzcl.mvp.ui.VPageActivity;
import com.example.yzcl.mvp.ui.VehicleMonitoringActivity;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.example.yzcl.utils.HorizonService;
import com.gyf.barlibrary.ImmersionBar;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;

/**
 * Created by Lenovo on 2017/11/28.
 */
//页面的主页面
public class HomePage extends BaseActivity implements OnBannerListener{
    Banner banner;
    GridView grid;
    TextView title;
    private MapView bmapview;
    ImageView head;
    private SimpleAdapter sim_adapter;
    private List<Map<String,Object>>data_list;
    SharedPreferences sp=null;
    GridViewAdapter gridViewAdapter;
    private ArrayList<String>iconname;
    private ArrayList<Boolean>needshow;
    private long mExitTime=0;
    Intent intent;
    private String TAG="HomePage";
    private BuildBean jq_dia;
    private int[] icon = { R.mipmap.group1, R.mipmap.group2,
            R.mipmap.group3,R.mipmap.group4, R.mipmap.group5,
            R.mipmap.group5, R.mipmap.group7,R.mipmap.group8};
    private Intent intent1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_home);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        banner=findViewById(R.id.banner);
        title=findViewById(R.id.title);
        bmapview=findViewById(R.id.bmap);
        head=findViewById(R.id.iamgeView2);
        title.setText("首页");
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(HomePage.this, PersonMessageActivity.class);
                startActivity(intent);
                //设置页面切换的方向，其中的XY可以更换，duration代表时间
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });
        sp=getSharedPreferences("YZCL",MODE_PRIVATE);
        grid=findViewById(R.id.grid);
        List<Integer>images=new ArrayList<>();
//        images.add(R.mipmap.banner);
        images.add(R.mipmap.banner);
        banner.setImages(images)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .start();
//        banner.setImages(MyApplication.images)
//                .setImageLoader(new GlideImageLoader())
//                .setOnBannerListener(this)
//                .start();
        data_list=new ArrayList<Map<String,Object>>();
        iconname=new ArrayList<>();
        needshow=new ArrayList<>();

        //获取权限
        if(!Constant.isNetworkConnected(HomePage.this)) {
            //判断网络是否可用
            Toast.makeText(HomePage.this, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
        }else{
            achieveJurisdiction();
        }
        intent1 = new Intent(HomePage.this,HorizonService.class);
        startService(intent1);

    }

    private void achieveJurisdiction() {
        RequestParams params=new RequestParams();
        params.addFormDataPart("token",sp.getString(Constant.Token,""));
        HttpRequest.get(Api.getUserGeneralInfo,params,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(Headers headers, JSONObject jsonObject) {
                super.onSuccess(headers, jsonObject);
                Log.i(TAG, "onSuccess: "+jsonObject.toString());
                if(jsonObject.getBoolean("success")){
                    //请求成功
                    //获取权限
                    HashMap<String,Boolean>hashMap=new HashMap<>();
                    hashMap.put("qx_jk",false);
                    hashMap.put("qx_fk",false);
                    hashMap.put("qx_clgl",false);
                    hashMap.put("qx_xdzx",false);
                    hashMap.put("qx_wlgl",false);
                    hashMap.put("qx_zlsz",false);
                    hashMap.put("qx_khgl",false);
                    String list_Jurisdiction=jsonObject.getJSONObject("object").getString("rightstring");
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("list_Jurisdiction",list_Jurisdiction);
                    editor.commit();
                    String[]list_jur=list_Jurisdiction.split(",");
                    for(int i=0;i<list_jur.length;i++){
                        if (list_jur[i].equals("218")){
                            //有车辆监控权限
                            hashMap.put("qx_jk",true);
                        }
                    }
//                    String []from={"image","text","coverimg"};
//                    int [] to = {R.id.image1,R.id.text1,R.id.coverimg};
                    gridViewAdapter=new GridViewAdapter(HomePage.this,getData(hashMap));
//                    sim_adapter=new SimpleAdapter(HomePage.this,getData(0),R.layout.item_gridview,from,to);
                    grid.setAdapter(gridViewAdapter);
//                    grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                    switch (i){
//                                        case 0:
//                                            //尝试使用viewpager（现）
//                                            //风控预警（实际）
////                        Toast.makeText(HomePage.this,R.string.car_veh,Toast.LENGTH_SHORT).show();
//                                            intent=new Intent();
////                        intent.setClass(HomePage.this, GunDongActivity.class);
//                                            intent.setClass(HomePage.this, VehicleMonitoringActivity.class);
//                                            startActivity(intent);
//                                            break;
//                                        case 1:
//                                            //车辆监控（实际）
////                        intent=new Intent();
////                        //风控预警（实际）
////                        intent.setClass(HomePage.this, RiskWarningActivity.class);
////                        startActivity(intent);
//                                            Toast.makeText(HomePage.this,"敬请期待",Toast.LENGTH_SHORT).show();
//                                            break;
//                                        case 2:
//                                            //车辆管理列表
//                         intent=new Intent();
////                        //2.0的车辆管理
//////                        intent.setClass(HomePage.this, CarManagerActivity.class);
//                        intent.setClass(HomePage.this, CarManagerRealActivity.class);
//                        startActivity(intent);
////                                            Toast.makeText(HomePage.this,"敬请期待",Toast.LENGTH_SHORT).show();
//                                            break;
//                                        case 3:
//                                            //围栏功能（现）
//                                            //下单中心（实际）
////                        intent=new Intent();
////                        intent.setClass(HomePage.this,VPageActivity .class);
////                        startActivity(intent);
//                                            Toast.makeText(HomePage.this,"敬请期待",Toast.LENGTH_SHORT).show();
//                                            break;
//                                        case 4:
//                                            //围栏管理（实际）
////                        intent=new Intent();
//////                        intent.setClass(HomePage.this,EnclosureManagerActivity.class);
////                        intent.setClass(HomePage.this,EnclosureAllManagerActivity.class);
////                        startActivity(intent);
//
//                                            Toast.makeText(HomePage.this,"敬请期待",Toast.LENGTH_SHORT).show();
//
//                                            break;
//                                        case 5:
//                                            //指令设置（实际）
//                                            //账号管理，有展开
//                                            Toast.makeText(HomePage.this,"敬请期待",Toast.LENGTH_SHORT).show();
////                         intent=new Intent();
////                        intent.setClass(HomePage.this, CustomerManagementActivity.class);
////                        startActivity(intent);
//                                            break;
//                                        case 6:
//                                            //客户管理
////                        intent=new Intent();
//////                        intent.setClass(HomePage.this, VPageActivity.class);
//////                        intent.setClass(HomePage.this, LowerAccountActivity.class);
////                        intent.setClass(HomePage.this, CustomerManagerActivity.class);
////                        startActivity(intent);
//                                            Toast.makeText(HomePage.this,"敬请期待",Toast.LENGTH_SHORT).show();
//                                            break;
//                                        case 7:
//                                            //更多
////                        intent=new Intent();
//////                        intent.setClass(HomePage.this, CarManagerFragmentActivity.class);
////                                                intent.setClass(HomePage.this, GunDongActivity.class);
////                        startActivity(intent);
//                                            Toast.makeText(HomePage.this,"敬请期待",Toast.LENGTH_SHORT).show();
//                                            break;
//
//                                    }
//                                }
//                            });




                }else{
                    Toast.makeText(HomePage.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                }
                DialogUIUtils.dismiss(jq_dia);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }

            @Override
            public void onStart() {
                super.onStart();
                jq_dia = DialogUIUtils.showLoading(HomePage.this, "加载中...", false, true, false, false);
                jq_dia.show();
            }
        });
    }

//    private void Handle_Jurisdiction(String[] list_j) {
//        //循环判断有没有这个对应的参数
//        //有设置为true，没有就false
//
//    }

    private void setStatusBarUpperAPI21() {
        Window window = getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }

    @Override
    public void OnBannerClick(int position) {
//        Toast.makeText(getApplicationContext(),"你点击了："+position,Toast.LENGTH_SHORT).show();
    }

    public List<Map<String,Object>> getData(HashMap<String,Boolean> qx_jk) {
        //模仿返回数据有新的
//        boolean needshow=true;
        if(qx_jk.get("qx_jk")){
            iconname.add("车辆监控");
            needshow.add(true);
        }else{
            iconname.add("车辆监控");
            needshow.add(false);
        }
        iconname.add("风控预警");
        needshow.add(false);
        iconname.add("车辆管理");
        needshow.add(true);
        iconname.add("车辆列表");
        needshow.add(true);
        for(int i=0;i<iconname.size();i++){
            if(needshow.get(i)){
                Map<String,Object>map=new HashMap<String,Object>();
                map.put("image",icon[i]);
                map.put("text",iconname.get(i));
                map.put("coverimg","");
                map.put("needshow",needshow.get(i));
                data_list.add(map);
            }else{
                continue;
            }

        }
        return data_list;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis()-mExitTime>2000)){ //如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();// 更新mExitTime
            }else{
                System.exit(0);
            }
        }
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bmapview.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bmapview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bmapview.onResume();
    }
}
