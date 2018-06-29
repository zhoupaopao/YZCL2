package com.example.yzcl.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.yzcl.mvp.model.bean.carDetailGPSBeans;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lenovo on 2018/6/29.
 */

public class DeviceMsPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private FragmentManager fm;
    private ArrayList<carDetailGPSBeans.carDetailGPSBean>al;
    public DeviceMsPagerAdapter(FragmentManager fm, ArrayList<Fragment> fs, ArrayList<carDetailGPSBeans.carDetailGPSBean>al) {
        super(fm);
        this.fm = fm;
        this.fragments = fs;
        this.al = al;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
