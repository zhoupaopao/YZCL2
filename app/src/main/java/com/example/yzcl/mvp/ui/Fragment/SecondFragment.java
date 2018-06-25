package com.example.yzcl.mvp.ui.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yzcl.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/12/4.
 */

@SuppressLint("ValidFragment")
public class SecondFragment extends Fragment {
    private TextView text;
    private String TAG="SecondFragment";
    private ArrayList<String>context;
    @SuppressLint("ValidFragment")
    public SecondFragment(ArrayList<String>context){
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_page2,null);
        text=view.findViewById(R.id.text);
        text.setText(context.get(2));
        Log.i(TAG, "onCreateView");
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }
}
