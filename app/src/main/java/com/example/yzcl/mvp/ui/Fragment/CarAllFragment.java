package com.example.yzcl.mvp.ui.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.CarListBean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/7/26.
 */

@SuppressLint("ValidFragment")
public class CarAllFragment extends Fragment {
    private ArrayList<CarListBean.CarBean>context;
    private RecyclerView recyclerview;
    private String TAG="FirstFragment";
    @SuppressLint("ValidFragment")
    public CarAllFragment(ArrayList<CarListBean.CarBean> context){
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
//        View view=inflater.inflate(R.layout.fragment_page1,null);
//        recyclerview=view.findViewById(R.id.recyclerview);
//        donetWork();
//        return view;
    }

    private void donetWork() {

    }
}
