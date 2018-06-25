package com.example.yzcl.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lenovo on 2018/1/15.
 */

public class GundongAdater extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private FragmentManager fm;

    private ArrayList<HashMap<String,Object>>al;
    public GundongAdater(FragmentManager fm, ArrayList<Fragment> fs, ArrayList<HashMap<String,Object>>al) {
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
