package com.example.yzcl.mvp.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.yzcl.R;
import com.example.yzcl.adapter.GundongAdater;
import com.example.yzcl.mvp.presenter.AnimationUtil;
import com.example.yzcl.mvp.ui.Fragment.FirstFragment;
import com.example.yzcl.mvp.ui.Fragment.GundongFragment;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lenovo on 2018/1/12.
 */

/**
 * 展示viewpager侧滑无限滚动
 * 同时动态隐藏显示
 */
public class GunDongActivity extends FragmentActivity {
    ViewPager viewPager;
    ArrayList<HashMap<String,Object>>datalist;
    HashMap<String,Object>hashMap;
    ArrayList<Fragment>fs;
    private int currentPosition = 0;
    private Button show;
    private Button hide;
    private RelativeLayout rl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gundong);
        initView();
        initData();
        initListener();
//        List<Integer> list = new ArrayList<>();
//        list.add(R.mipmap.u34);
//        list.add(R.mipmap.u34);
//        list.add(R.mipmap.u34);
//        list.add(R.mipmap.u34);
//        list.add(R.mipmap.u34);
//        MyAdapter adapter = new MyAdapter(this, list);
//        viewPager.setAdapter(adapter);
//        viewPager.setPageMargin(60);
//        // pageCount设置红缓存的页面数
//        viewPager.setOffscreenPageLimit(2);
    }

    private void initListener() {
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                rl.setVisibility(View.VISIBLE);
//// 向右边移入
//                rl.setAnimation(AnimationUtils.makeInAnimation(GunDongActivity.this, true));
                rl.setVisibility(View.VISIBLE);
                rl.setAnimation(AnimationUtil.moveToViewLocation());
            }
        });
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                rl.setVisibility(View.GONE);
//// 向左边移出
//                rl.setAnimation(AnimationUtils.makeOutAnimation(GunDongActivity.this, false));
                rl.setVisibility(View.GONE);
                rl.setAnimation(AnimationUtil.moveToViewBottom());
            }
        });
    }

    private void initData() {
        //模拟数据
        //这里想模拟数组
        for(int i=0;i<4;i++){
            //需要在这边定义hashmap，不然数组里面全是最后一组数据
            hashMap=new HashMap<>();
            hashMap.put("a",i);
            hashMap.put("b",i);
            hashMap.put("c",i);
            datalist.add(hashMap);
            Log.i("hashMap", hashMap.toString());
            Log.i("datalist", datalist.toString());
        }

        Log.i("datalist", datalist.toString());
        fs=new ArrayList<Fragment>();
        fs.add(new GundongFragment(datalist.get(3)));
        fs.add(new GundongFragment(datalist.get(0)));
        fs.add(new GundongFragment(datalist.get(1)));
        fs.add(new GundongFragment(datalist.get(2)));
        fs.add(new GundongFragment(datalist.get(3)));
        fs.add(new GundongFragment(datalist.get(0)));
        FragmentManager fm=getSupportFragmentManager();
        GundongAdater adapter=new GundongAdater(fm,fs,datalist);
        viewPager.setAdapter(adapter);
        viewPager.setPageMargin(60);
        // pageCount设置红缓存的页面数
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //        若viewpager滑动未停止，直接返回
                if (state != ViewPager.SCROLL_STATE_IDLE) return;
//        若当前为第一张，设置页面为倒数第二张
                if (currentPosition == 0) {
                    viewPager.setCurrentItem(fs.size()-2,false);
                } else if (currentPosition == fs.size()-1) {
//        若当前为倒数第一张，设置页面为第二张
                    viewPager.setCurrentItem(1,false);
                }
            }
        });
        viewPager.setCurrentItem(1,false);

    }

    private void initView() {
        //--初始化viewpager
        show=findViewById(R.id.show);
        hide=findViewById(R.id.hide);
        rl=findViewById(R.id.rl);
        viewPager = (ViewPager) findViewById(R.id.fl_vp);
        datalist=new ArrayList<>();


    }

//--适配器
        public class MyAdapter extends PagerAdapter {
            private List<Integer> list;
            private Context context;

            public MyAdapter(Context context, List<Integer> list) {
                this.context = context;
                this.list = list;
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView iv = new ImageView(context);
                iv.setImageResource(list.get(position));
                container.addView(iv);
                return iv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }


}

}
