package com.example.yzcl.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzcl.R;
import com.example.yzcl.content.Constant;
import com.example.yzcl.mvp.ui.CarSearchActivity;
import com.example.yzcl.mvp.ui.DeviceSearchAcrivity;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/6/27.
 */

public class SearchHistoryDeviceCarAdapter extends RecyclerView.Adapter<SearchHistoryDeviceCarAdapter.SHViewHolder> {
    private CarSearchActivity context;
    private ArrayList<String>list;
    private SharedPreferences sp;
    public SearchHistoryDeviceCarAdapter(CarSearchActivity context, ArrayList<String> list){
        this.context=context;
        this.list=list;
        sp=context.getSharedPreferences("YZCL", Context.MODE_PRIVATE);
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
                Log.i("delete", "onClick: ");
                list.remove(position);
                SharedPreferences.Editor editor=sp.edit();
                if(list.size()==0){
                    editor.remove("search_list");
                    Log.i("putString", "onClick:1 ");
                }else{
                    Log.i("putString", "onClick: ");
                    String search_list="";
                    for(int i=list.size()-1;i>=0;i--){
                        search_list=list.get(i)+","+search_list;
                    }
                    editor.putString("search_list",search_list);
                }
                editor.commit();


                notifyItemRemoved(position);
                notifyItemRangeChanged(position,list.size()-position);//通知数据与界面重新绑定
            }
        });
        holder.rl_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Constant.isNetworkConnected(context)) {
                    //判断网络是否可用
                    Toast.makeText(context, "当前网络不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                }else{
                    context.queVehicleListForSea(list.get(position));
                    Log.i("onClick: ", list.get(position));
                }
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
        private RelativeLayout rl_line;
        public SHViewHolder(View itemView) {
            super(itemView);
            his_text=itemView.findViewById(R.id.his_text);
            delete=itemView.findViewById(R.id.iv_delete);
            rl_line=itemView.findViewById(R.id.rl_line);
        }
    }
}
