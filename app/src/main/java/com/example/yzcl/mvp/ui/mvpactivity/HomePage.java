package com.example.yzcl.mvp.ui.mvpactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
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

import com.example.yzcl.R;
import com.example.yzcl.mvp.presenter.GlideImageLoader;
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
import com.gyf.barlibrary.ImmersionBar;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 2017/11/28.
 */
//页面的主页面
public class HomePage extends BaseActivity implements OnBannerListener{
    Banner banner;
    GridView grid;
    TextView title;
    ImageView head;
    private SimpleAdapter sim_adapter;
    private List<Map<String,Object>>data_list;
    SharedPreferences sp=null;
    private String[]iconname;
    private long mExitTime=0;
    Intent intent;
    private int[] icon = { R.mipmap.group1, R.mipmap.group2,
            R.mipmap.group3,R.mipmap.group4, R.mipmap.group5,
            R.mipmap.group5, R.mipmap.group7,R.mipmap.group8};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_home);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        banner=findViewById(R.id.banner);
        title=findViewById(R.id.title);
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
        images.add(R.mipmap.u34);
        images.add(R.mipmap.u36);
        banner.setImages(images)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .start();
//        banner.setImages(MyApplication.images)
//                .setImageLoader(new GlideImageLoader())
//                .setOnBannerListener(this)
//                .start();
        data_list=new ArrayList<Map<String,Object>>();
        iconname=getResources().getStringArray(R.array.grid_icon_text);
        getData();

        String []from={"image","text","coverimg"};
        int [] to = {R.id.image1,R.id.text1,R.id.coverimg};
        sim_adapter=new SimpleAdapter(this,data_list,R.layout.item_gridview,from,to);
        grid.setAdapter(sim_adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        //尝试使用viewpager（现）
                        //风控预警（实际）
                        Toast.makeText(HomePage.this,R.string.car_veh,Toast.LENGTH_SHORT).show();
                        intent=new Intent();
//                        intent.setClass(HomePage.this, GunDongActivity.class);
                        intent.setClass(HomePage.this, VehicleMonitoringActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        //车辆监控（实际）
                        intent=new Intent();
                        //风控预警（实际）
                        intent.setClass(HomePage.this, RiskWarningActivity.class);
                        startActivity(intent);
                        Toast.makeText(HomePage.this,"风控预警",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        //车辆管理列表
                         intent=new Intent();
                        //2.0的车辆管理
//                        intent.setClass(HomePage.this, CarManagerActivity.class);
                        intent.setClass(HomePage.this, CarManagerRealActivity.class);
                        startActivity(intent);
                        Toast.makeText(HomePage.this,"下单中心",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        //围栏功能（现）
                        //下单中心（实际）
                        intent=new Intent();
                        intent.setClass(HomePage.this,VPageActivity .class);
                        startActivity(intent);
                        break;
                    case 4:
                        //围栏管理（实际）
                        intent=new Intent();
//                        intent.setClass(HomePage.this,EnclosureManagerActivity.class);
                        intent.setClass(HomePage.this,EnclosureAllManagerActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        //指令设置（实际）
                        //账号管理，有展开
                        Toast.makeText(HomePage.this,"车辆管理",Toast.LENGTH_SHORT).show();
                         intent=new Intent();
                        intent.setClass(HomePage.this, CustomerManagementActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        //客户管理
                        intent=new Intent();
//                        intent.setClass(HomePage.this, VPageActivity.class);
//                        intent.setClass(HomePage.this, LowerAccountActivity.class);
                        intent.setClass(HomePage.this, CustomerManagerActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        //更多
                        intent=new Intent();
//                        intent.setClass(HomePage.this, CarManagerFragmentActivity.class);
                                                intent.setClass(HomePage.this, GunDongActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });
    }
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
        Toast.makeText(getApplicationContext(),"你点击了："+position,Toast.LENGTH_SHORT).show();
    }

    public List<Map<String,Object>> getData() {
        //模仿返回数据有新的
        boolean needshow=true;
        for(int i=0;i<iconname.length;i++){
            Map<String,Object>map=new HashMap<String,Object>();
            map.put("image",icon[i]);
            map.put("text",iconname[i]);
            if(i==1&&needshow){
                map.put("coverimg",R.mipmap.u114);
            }else{
                map.put("coverimg","");
            }
            data_list.add(map);
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
}
