package com.example.yzcl.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzcl.R;
import com.example.yzcl.mvp.ui.CarMonSearchActivity;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/6/27.
 */

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SHViewHolder> {
    private CarMonSearchActivity context;
    private ArrayList<String>list;
    public SearchHistoryAdapter(CarMonSearchActivity context, ArrayList<String> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public SHViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history,parent,false);
        SHViewHolder holder=new SHViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(SHViewHolder holder, final int position) {
        Log.i("onBindViewHolder: ", position+"");
        holder.his_text.setText(list.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,list.size()-position);//通知数据与界面重新绑定
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    class SHViewHolder extends RecyclerView.ViewHolder{
        private ImageView delete;
        private TextView his_text;
        public SHViewHolder(View itemView) {
            super(itemView);
            his_text=itemView.findViewById(R.id.his_text);
            delete=itemView.findViewById(R.id.iv_delete);
        }
    }
}
