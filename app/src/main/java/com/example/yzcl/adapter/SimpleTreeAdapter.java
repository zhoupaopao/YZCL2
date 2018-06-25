package com.example.yzcl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tree.Node;
import com.example.yzcl.R;
import com.example.yzcl.mvp.model.bean.FileBean;

import java.util.List;

/**
 * Created by Lenovo on 2017/11/30.
 */

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {
    public SimpleTreeAdapter(ListView mTree, Context context,
                             List<T> datas, int defaultExpandLevel)throws IllegalArgumentException,IllegalAccessException{
        super(mTree, context, datas, defaultExpandLevel);
    }

    @Override
    public View getConvertView(Node node, final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){

            convertView=mInflater.inflate(R.layout.tree_list_item,parent,false);
            viewHolder=new ViewHolder();
            ImageView icon=convertView.findViewById(R.id.id_treenode_icon);
            if(node.isExpand()){
                //是否展开
                //展开
                icon.setImageResource(R.drawable.tree_ex);
            }else{
                icon.setImageResource(R.drawable.tree_ec);
            }
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandOrCollapse(position);
                }
            });
            viewHolder.icon=icon;
            viewHolder.label=convertView
                    .findViewById(R.id.id_treenode_label);
            viewHolder.qty=(TextView)convertView.findViewById(R.id.textView47);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
            if(node.isExpand()){
                viewHolder.icon.setImageResource(R.drawable.tree_ex);
            }else{
                viewHolder.icon.setImageResource(R.drawable.tree_ec);
            }

        }
        if (node.getIcon() == -1)
        {
            viewHolder.icon.setVisibility(View.INVISIBLE);
        } else
        {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }
        //icon的赋值其实是在ifelse里面完成的
        viewHolder.label.setText(node.getName());
        viewHolder.qty.setText(node.getAty()+"/"+node.getTempQty());
        return convertView;
    }

    private final class ViewHolder
    {
        ImageView icon;//前面的加减号
        TextView label;//具体的text内容
        TextView qty;//0/1
    }
}
