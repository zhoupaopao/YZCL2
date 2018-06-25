package com.example.yzcl.mvp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.baseactivity.BaseActivity;
import com.gyf.barlibrary.ImmersionBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/12/20.
 */

public class CarManagerFragmentActivity extends BaseActivity {
    RecyclerView list;
    List<String> ls;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_carmanager);
        ImmersionBar.with(this)
                .statusBarColor(R.color.title_color)
                .init();
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new ContentAdapter());

    }

    private void initData() {

    }

    private void initView() {
        list=findViewById(R.id.carlist);
        ls=new ArrayList<>();
    }
    private class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder>{

        @Override
        public ContentAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContentHolder(LayoutInflater.from(CarManagerFragmentActivity.this).inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(ContentAdapter.ContentHolder holder, int position) {
            holder.itemTv.setText("Item "+new DecimalFormat("00").format(position));
        }

        @Override
        public int getItemCount() {
            return 100;
        }

        class ContentHolder extends RecyclerView.ViewHolder{

            private TextView itemTv;

            public ContentHolder(View itemView) {
                super(itemView);
                itemTv = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }

    }
}
