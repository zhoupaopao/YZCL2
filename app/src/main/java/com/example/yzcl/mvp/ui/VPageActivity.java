package com.example.yzcl.mvp.ui;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.adapter.FragmentVPAdapter;
import com.example.yzcl.mvp.ui.Fragment.FirstFragment;
import com.example.yzcl.mvp.ui.Fragment.SecondFragment;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/12/4.
 */

public class VPageActivity extends FragmentActivity {
    private ViewPager vp;
    private TextView page1;
    private TextView page2;
    private TextView page3;
    private TextView page4;
    private TextView page5;
    private ArrayList<TextView> pagelist;
    //fragment数组
    private ArrayList<Fragment>fs;
    //尝试fragment传值
    private ArrayList<String>data;
    FragmentVPAdapter adapter;
    HorizontalScrollView horizontalScrollViewMM;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpage);
        //初始化布局
        initView();
        //初始化数据
        initData();
        //初始化监听
        initListener();

    }

    private void initView() {
        vp=findViewById(R.id.vpage);
        page1=findViewById(R.id.page1);
        page2=findViewById(R.id.page2);
        page3=findViewById(R.id.page3);
        page4=findViewById(R.id.page4);
        page5=findViewById(R.id.page5);
        horizontalScrollViewMM=findViewById(R.id.horizontalScrollViewMM);
        data=new ArrayList<String>();
        for(int i=0;i<3;i++){
            data.add(i+"");
        }
        pagelist=new ArrayList<TextView>();
        pagelist.add(page1);
        pagelist.add(page2);
        pagelist.add(page3);
        pagelist.add(page4);
        pagelist.add(page5);
        fs=new ArrayList<Fragment>();
        fs.add(new FirstFragment(data));
        fs.add(new SecondFragment(data));
        fs.add(new FirstFragment(data));
        fs.add(new SecondFragment(data));
        fs.add(new FirstFragment(data));
    }

    private void initData() {
        FragmentManager fm=getSupportFragmentManager();
//        adapter=new MypageAdapter(fm);
        adapter=new FragmentVPAdapter(fm,fs);
        vp.setAdapter(adapter);
    }

    private void initListener() {
        for(int i=0;i<5;i++){
            final int finalI = i;
            pagelist.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movetobutton(finalI);
                    vp.setCurrentItem(finalI);
                    adapter.notifyDataSetChanged();
                    if(finalI ==1|| finalI ==2){
                        horizontalScrollViewMM.arrowScroll(View.FOCUS_LEFT);
                        Log.i("pos", "012");
                    }else if(finalI==3||finalI==4){
                        Log.i("pos", "34");
                        horizontalScrollViewMM.arrowScroll(View.FOCUS_RIGHT);
                    }
                }
            });
        }

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("onPageScrolled", position+"|"+positionOffset+"|"+positionOffsetPixels);
                    movetobutton(position);
                    if(position==1||position==2){
                        horizontalScrollViewMM.arrowScroll(View.FOCUS_LEFT);
                        Log.i("pos", "012");
                    }else if(position==3||position==4){
                        Log.i("pos", "34");
                        horizontalScrollViewMM.arrowScroll(View.FOCUS_RIGHT);
                    }
                    //之前有的，但是这边加上会报错，暂时注释
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("onPageSelected", position+" ");
                //可以用在数据刷新上面
                //将数据获取写在onresume上面
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("onPageScrollStateChange", state+" ");
            }
        });
    }

    private void movetobutton(int position) {
        //滑动viewpager的时候，对应头上的button变颜色
        for(int i=0;i<5;i++){
            if(i==position){
                //是这个按钮
                pagelist.get(i).setTextColor(0xff0000ff);
            }else{
                //不是
                pagelist.get(i).setTextColor(0xff000000);
            }
        }
    }


    class MypageAdapter extends FragmentPagerAdapter{

        public MypageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fs.get(position);
        }

        @Override
        public int getCount() {
            return fs.size();
        }

        //对于第一个fragment无效
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

        }
//        public void setFragments(ArrayList fragments) {
//            if(this.fragments != null){
//                FragmentTransaction ft = fm.beginTransaction();
//                for(Fragment f:this.fragments){
//                    ft.remove(f);
//                }
//                ft.commit();
//                ft=null;
//                fm.executePendingTransactions();
//            }
//            this.fragments = fragments;
//            notifyDataSetChanged();
//        }
////        就能完美的解决fragmentpageadapter数据刷新。
    }
}
