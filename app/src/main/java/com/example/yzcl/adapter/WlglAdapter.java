package com.example.yzcl.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.example.yzcl.R;
import com.example.yzcl.content.Api;
import com.example.yzcl.mvp.model.bean.WlglBean;
import com.example.yzcl.mvp.ui.EnclosureAllManagerActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Lenovo on 2018/2/1.
 */

public class WlglAdapter extends BaseAdapter {
private ArrayList<WlglBean.EnclosureBean>datalist;
//private Context context;
private EnclosureAllManagerActivity context;
private LayoutInflater mInflater;
//private SharedPreferences sp=null;
    private String token;
    public WlglAdapter(EnclosureAllManagerActivity context,ArrayList<WlglBean.EnclosureBean>datalist,String token){
        this.context= context;
        this.datalist=datalist;
        this.token=token;
        mInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View contentView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        //初始化
        if(contentView==null){
            contentView=mInflater.inflate(R.layout.layout_wlgl_list,null);
            viewHolder=new ViewHolder();
            //围栏关联的车辆数量
            viewHolder.contect_car_num=contentView.findViewById(R.id.contect_car_num);
            //围栏名字
            viewHolder.wlgl_name=contentView.findViewById(R.id.wlgl_name);
            //围栏日期
            viewHolder.wlgl_date=contentView.findViewById(R.id.wlgl_date);
            //进围栏布局（图标+字）判断是否显示
            viewHolder.jin_ll=contentView.findViewById(R.id.jin_ll);
            //出围栏布局（图标+字）判断是否显示
            viewHolder.chu_ll=contentView.findViewById(R.id.chu_ll);
            //进图标，判断显示图标是什么
            viewHolder.jin_img=contentView.findViewById(R.id.jin_img);
            //出图标，判断显示图标是什么
            viewHolder.chu_img=contentView.findViewById(R.id.chu_img);
            //围栏关联按钮，需要点击事件
            viewHolder.wlgl_line=contentView.findViewById(R.id.wlgl_line);
            //围栏删除按钮，需要点击事件
            viewHolder.delete=contentView.findViewById(R.id.delete);
            contentView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) contentView.getTag();
        }
        final WlglBean.EnclosureBean enclosureBean=datalist.get(i);
        //开始赋值，处理事件
        viewHolder.wlgl_name.setText(enclosureBean.getFencename());
        // 围栏形状(1:圆形;2:多边形;3:区域围栏)
//        enclosureBean.getFencearea();
        //围栏类型 0:出围栏,1:进围栏 2进出围栏 ,
        //按钮的亮暗代表围栏是否开启
        switch (enclosureBean.getFencetype()){
            case 0:
                //进围栏布局取消
                viewHolder.jin_ll.setVisibility(View.GONE);
                viewHolder.chu_ll.setVisibility(View.VISIBLE);
                //判断是否有效，1：有效，0：无效，通过按钮 亮暗显示,这里不用管进围栏
                if(enclosureBean.getIsactive()==1){
                    //有效
                    viewHolder.chu_img.setImageResource(R.mipmap.u5920);
                }else{
                    //无效
                    viewHolder.chu_img.setImageResource(R.mipmap.u5979);
                }
                break;
            case 1:
                //出围栏布局取消
                viewHolder.chu_ll.setVisibility(View.GONE);
                viewHolder.jin_ll.setVisibility(View.VISIBLE);
                if(enclosureBean.getIsactive()==1){
                    //有效
                    viewHolder.jin_img.setImageResource(R.mipmap.u5920);
                }else{
                    //无效
                    viewHolder.jin_img.setImageResource(R.mipmap.u5979);
                }
                break;
            case 2:
                viewHolder.jin_ll.setVisibility(View.VISIBLE);
                viewHolder.chu_ll.setVisibility(View.VISIBLE);
                //都显示
                if(enclosureBean.getIsactive()==1){
                    //有效
                    viewHolder.jin_img.setImageResource(R.mipmap.u5920);
                    viewHolder.chu_img.setImageResource(R.mipmap.u5920);
                }else{
                    //无效
                    viewHolder.jin_img.setImageResource(R.mipmap.u5979);
                    viewHolder.chu_img.setImageResource(R.mipmap.u5979);
                }
                break;
        }
        viewHolder.wlgl_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"关联车辆"+enclosureBean.getId(),Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //能够调用activity里面的方法
                context.delete(enclosureBean.getId());


//                Toast.makeText(context,"删除围栏"+enclosureBean.getFencename(),Toast.LENGTH_SHORT).show();
            }
        });
        return contentView;
    }
    public  static class ViewHolder{
        private TextView wlgl_name;
        private TextView wlgl_date;
        private TextView contect_car_num;
        private LinearLayout jin_ll;
        private LinearLayout chu_ll;
        private ImageView jin_img;
        private ImageView chu_img;
        private ImageView wlgl_line;
        private ImageView delete;
    }
}
