package com.example.yzcl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.example.yzcl.R;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/6/8.
 */

public class EasyRecycleAdapter extends BaseRecyclerAdapter<EasyRecycleAdapter.ESViewHolder> {
    ArrayList<String>list;
    Context context;
    public EasyRecycleAdapter(Context context, ArrayList<String>list){
        this.list=list;
        this.context=context;
    }
    public void Additem(ArrayList<String>aa){
//        list.clear();
        list.addAll(aa);
        notifyDataSetChanged();
    }

    @Override
    public ESViewHolder getViewHolder(View view) {
        return new ESViewHolder(view);
    }

    @Override
    public ESViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_recycle_view,parent,false);
        ESViewHolder viewHolder=new ESViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ESViewHolder holder, int position, boolean isItem) {
        holder.tv1.setText(list.get(position));
    }




    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    class ESViewHolder extends RecyclerView.ViewHolder{
        TextView tv1;
        public ESViewHolder(View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.tv);
        }
    }
}
