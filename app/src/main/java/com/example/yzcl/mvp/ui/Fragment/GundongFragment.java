package com.example.yzcl.mvp.ui.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yzcl.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lenovo on 2017/12/4.
 */

@SuppressLint("ValidFragment")
public class GundongFragment extends Fragment {
    private ArrayList<String>context;
    private HashMap<String,Object>datalist;
    private TextView text1;
    private TextView text2;
    private TextView text3;

    private String TAG="FirstFragment";
    @SuppressLint("ValidFragment")
//    public GundongFragment(ArrayList<String> context){
//        this.context=context;
//    }
    public GundongFragment(HashMap<String,Object> datalist){
        this.datalist=datalist;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view=inflater.inflate(R.layout.fragment_gundong,null);
        text1=view.findViewById(R.id.tv1);
        text2=view.findViewById(R.id.tv2);
        text3=view.findViewById(R.id.tv3);
        text1.setText(datalist.get("a").toString());
        text2.setText(datalist.get("b").toString());
        text3.setText(datalist.get("c").toString());
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
    }
}
