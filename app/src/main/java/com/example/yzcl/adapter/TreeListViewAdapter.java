package com.example.yzcl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;


import com.example.tree.Node;
import com.example.tree.TreeHelper;

import java.util.List;

/**
 * Created by Lenovo on 2017/11/30.
 */

public abstract class TreeListViewAdapter<T>extends BaseAdapter {
    protected Context mContext;
    protected List<Node> mNodes;
    protected LayoutInflater mInflater;
    protected List<Node> mAllNodes;
    private OnTreeNodeClickListener onTreeNodeClickListener;


    public interface OnTreeNodeClickListener{
        void onClick(Node node,int position);
    }
    public void setOnTreeNodeClickListener(OnTreeNodeClickListener onTreeNodeClickListener){
        this.onTreeNodeClickListener = onTreeNodeClickListener;
    }

    /**
     *
     * @param mTree
     * @param context
     * @param datas
     * @param defaultExpandLevel
     *            默认展开几级树
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public TreeListViewAdapter(ListView mTree, Context context, List<T> datas,
                               int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException
    {
        mContext = context;
        mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
        mNodes = TreeHelper.filterVisibleNode(mAllNodes);
        mInflater = LayoutInflater.from(context);
        mTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //先不写
                if (onTreeNodeClickListener != null)
                {
                    onTreeNodeClickListener.onClick(mNodes.get(i),
                            i);
                }
            }
        });
    }

    /**
     * 相应ListView的点击事件
     * 前面那个按钮的点击事件
     *
     * @param position
     */
    public void expandOrCollapse(int position)
    {
        Node n = mNodes.get(position);

        if (n != null)// 排除传入参数错误异常
        {
            if (!n.isLeaf())
            {
                n.setExpand(!n.isExpand());
                mNodes = TreeHelper.filterVisibleNode(mAllNodes);
                notifyDataSetChanged();// 刷新视图
            }
        }
    }
    @Override
    public int getCount() {
        return mNodes.size();
    }

    @Override
    public Object getItem(int i) {
       return mNodes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Node node = mNodes.get(i);
        Log.i("node", node.toString());
        convertView = getConvertView( node , i , convertView , viewGroup);
        Log.i("node", "12");
        int le=node.getLevel() * 30;
        convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
        return convertView ;
    }

    public abstract View getConvertView(Node node , int position, View convertView,
                                        ViewGroup parent);
}
