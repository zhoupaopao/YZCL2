package com.example.yzcl.mvp.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.adapter.CarDeviceListAdapter;
import com.example.yzcl.adapter.DeviceListAdapter;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.example.yzcl.utils.MyListView;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/7/30.
 */

public class DeviceListActivity extends BaseActivity{
    //设备列表界面
    private ImageView back;
    private ImageView search;
    private TextView title;
    private ArrayList<String>list;
    private MyListView device_list;
    Toolbar toolbar;
    AppBarLayout abl_title;
    TextView tv_title;
    CarDeviceListAdapter adapter;
    TranslateAnimation mShowAction;
    TranslateAnimation mHiddenAction;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }


    private void initView() {
        toolbar=findViewById(R.id.tl_title);
        back=findViewById(R.id.back);
        search=findViewById(R.id.add);
        title=findViewById(R.id.title);
        tv_title=findViewById(R.id.tv_title);
        device_list=findViewById(R.id.device_list);
        abl_title=findViewById(R.id.abl_title);
        list=new ArrayList<>();
    }

    private void initData() {
        for(int i=0;i<20;i++){
            list.add("第"+i+"条");
        }
    }

    private void initListener() {
        search.setImageResource(R.mipmap.search);
        adapter= new CarDeviceListAdapter(DeviceListActivity.this,list);
        device_list.setAdapter(adapter);
        abl_title.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {
                    toolbar.setVisibility(View.GONE);
                    //展开状态
                    Log.i("onStateChanged: ", "2: ");
                    tv_title.setVisibility(View.GONE);
                    tv_title.animate()
                            .alpha(0f)
                            .setDuration(500)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    tv_title.setVisibility(View.GONE);
                                }
                            });
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    Log.i("onStateChanged: ", "3: ");
                    toolbar.setVisibility(View.VISIBLE);
                    tv_title.setVisibility(View.VISIBLE);
                    tv_title.animate()
                            .alpha(1f)
                            .setDuration(500)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    tv_title.setVisibility(View.VISIBLE);
                                }
                            });
                }else {
                    //中间状态
                    Log.i("onStateChanged: ", "1: ");

                }
            }
        });
//        setListViewHeightBasedOnChildren(device_list);
    }

    public abstract static class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
        public enum State {
            EXPANDED,
            COLLAPSED,
            IDLE
        }

        private State mCurrentState = State.IDLE;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (i == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED);
                }
                mCurrentState = State.EXPANDED;
            } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED);
                }
                mCurrentState = State.COLLAPSED;
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE);
                }
                mCurrentState = State.IDLE;
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
    }
}
