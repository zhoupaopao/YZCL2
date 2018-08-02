package com.example.yzcl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tree.Node3;
import com.example.yzcl.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2018/1/19.
 */

public class SimpleTreeeAdapter<T> extends TreeListViewwAdapter<T> {
    /**
     * @param mTree
     * @param context
     * @param datas
     * @param defaultExpandLevel 默认展开几级树
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public SimpleTreeeAdapter(ListView mTree, Context context, List<T> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
    }

    @Override
    public View getConvertView(final Node3 node, final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.tree_list_item,parent,false);
            viewHolder=new ViewHolder();
            ImageView icon=convertView.findViewById(R.id.id_treenode_icon);
            viewHolder.cb=convertView.findViewById(R.id.cb_select_tree);
            if(node.isExpand()){
                //是否展开
                //展开
                icon.setImageResource(R.drawable.tree_ex);
            }else{
                icon.setImageResource(R.drawable.tree_ec);
            }
//            icon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //这里有bug，暂时不能用，包括线上版本
//                    Log.i("expandOrCollapse", position+"");
//                    expandOrCollapse(position);
//                }
//            });
            viewHolder.icon=icon;
            viewHolder.label=convertView
                    .findViewById(R.id.id_treenode_label);
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
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(node, finalViewHolder.cb.isChecked());
            }
        });
        if (node.isChecked()){
            viewHolder.cb.setChecked(true);
        }else {
            viewHolder.cb.setChecked(false);
        }
        //icon的赋值其实是在ifelse里面完成的
        viewHolder.label.setText(node.getName());
        if(node.isChkDisabled()){
            //不能选择
            viewHolder.cb.setEnabled(false);
        }else{
            viewHolder.cb.setEnabled(true);
        }
        final ViewHolder finalViewHolder1 = viewHolder;
//        viewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(node.isChkDisabled()){
//                    finalViewHolder1.cb.setChecked(false);
//                }
//            }
//        });
//        viewHolder.qty.setText(node.getAty()+"/"+node.getTempQty());
        return convertView;
    }
    public final class ViewHolder{
        ImageView icon;//前面的加减号
        TextView label;//具体的text内容
        CheckBox cb;//选择框
    }
    /**
     * 设置多选
     * @param node
     * @param checked
     */
    protected void setChecked(final Node3 node, boolean checked) {
        node.setChecked(checked);
        setChildChecked(node, checked);
//        if(node.getParent()!=null)
//            setNodeParentChecked(node.getParent(), checked);
        notifyDataSetChanged();
    }
    /**
     * 设置是否选中
     * @param node
     * @param checked
     */
    public <T,B>void setChildChecked(Node3 node,boolean checked){
        if(!node.isLeaf()){
            if(node.isChkDisabled()){
                //不能选择
                node.setChecked(false);
            }else{
                node.setChecked(checked);
            }

            for (Node3 childrenNode : node.getChildren()) {
                setChildChecked(childrenNode, checked);
            }
        }else{
            if(node.isChkDisabled()){
                //不能选择
                node.setChecked(false);
            }else{
                node.setChecked(checked);
            }
        }
    }
    private void setNodeParentChecked(Node3 node,boolean checked){
        if(checked){
            node.setChecked(checked);
            if(node.getParent()!=null)
                setNodeParentChecked(node.getParent(), checked);
        }else{
            List<Node3> childrens = node.getChildren();
            boolean isChecked = false;
            for (Node3 children : childrens) {
                if(children.isChecked()){
                    isChecked = true;
                }
            }
            //如果所有自节点都没有被选中 父节点也不选中
//            if(!isChecked){
//                node.setChecked(checked);
//            }
//            if(node.getParent()!=null)
//                setNodeParentChecked(node.getParent(), checked);
        }
    }
    /**
     * 获取排序后所有节点
     * @return
     */
    public List<Node3> getAllNodes(){
        if(mAllNodes == null)
            mAllNodes = new ArrayList<Node3>();
        return mAllNodes;
    }
}
