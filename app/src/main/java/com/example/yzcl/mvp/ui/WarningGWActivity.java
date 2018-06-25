package com.example.yzcl.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.Fragment.GwListFragment;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2018/2/7.
 */

public class WarningGWActivity extends BaseActivity {
    private TextView date_text;
    private ImageView date_img;
    private ImageView back;
    private ImageView search;
    private ImageView shaixuan;
    private TextView title;
    private TabLayout mTabTl;
    private ViewPager vp;
    //存放tab里面的头
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;
    private String alarmtype="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_gw);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        Intent intent=getIntent();
        alarmtype=intent.getStringExtra("alarmtype");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        date_text=findViewById(R.id.date_text);
        date_img=findViewById(R.id.date_img);
        title=findViewById(R.id.title);
        back=findViewById(R.id.back);
        shaixuan=findViewById(R.id.shaixuan);
        search=findViewById(R.id.search);
        mTabTl=findViewById(R.id.tl_tab);
        vp=findViewById(R.id.vp_content);
        tabFragments=new ArrayList<>();

        Log.i("alarmtype", alarmtype);
    }

    private void initData() {
        if(alarmtype.equals("gw")){
            //高危报警
            tabIndicators=new ArrayList<>();
            tabIndicators.add("全部(50)");
            tabIndicators.add("拆除(20)");
            tabIndicators.add("屏蔽(30)");
            tabFragments.add(new GwListFragment("1"));
            tabFragments.add(new GwListFragment("2"));
            tabFragments.add(new GwListFragment("3"));
            contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
            vp.setAdapter(contentAdapter);
        }else if(alarmtype.equals("sb")){
            Log.i("alarmtype", "sb ");
            //设备报警
            tabIndicators=new ArrayList<>();
            tabIndicators.add("全部(50)");
            tabIndicators.add("断电(20)");
            tabIndicators.add("光感异常(30)");
            tabIndicators.add("子母分离(30)");
            tabIndicators.add("伪基站报警(30)");
            tabIndicators.add("无线电量低(30)");
            tabFragments.add(new GwListFragment("4"));
            tabFragments.add(new GwListFragment("5"));
            tabFragments.add(new GwListFragment("6"));
            tabFragments.add(new GwListFragment("7"));
            tabFragments.add(new GwListFragment("8"));
            tabFragments.add(new GwListFragment("9"));
            contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
            vp.setAdapter(contentAdapter);
        }

    }

    private void initListener() {
        if(alarmtype.equals("gw")){
            title.setText("高危报警");
            mTabTl.setTabMode(TabLayout.MODE_FIXED);
            mTabTl.setTabTextColors(ContextCompat.getColor(this, R.color.car_message_text), ContextCompat.getColor(this, R.color.title_color));
            mTabTl.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.title_color));
            ViewCompat.setElevation(mTabTl, 10);
            mTabTl.setupWithViewPager(vp);
        }else if(alarmtype.equals("sb")){
            title.setText("设备报警");
            mTabTl.setTabMode(TabLayout.MODE_SCROLLABLE);
            mTabTl.setTabTextColors(ContextCompat.getColor(this, R.color.car_message_text), ContextCompat.getColor(this, R.color.title_color));
            mTabTl.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.title_color));
            ViewCompat.setElevation(mTabTl, 10);
            mTabTl.setupWithViewPager(vp);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }

    }
}
