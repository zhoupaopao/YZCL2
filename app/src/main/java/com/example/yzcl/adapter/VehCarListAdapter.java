package com.example.yzcl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

/**
 * Created by Lenovo on 2018/8/2.
 */

public class VehCarListAdapter extends BaseRecyclerAdapter<VehCarListAdapter.ViewHolder> {

    @Override
    public ViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, boolean isItem) {

    }

    @Override
    public int getAdapterItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
