package com.example.yzcl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.tree.Node;
import com.example.tree.Node3;
import com.example.tree.TreeHelper3;

import java.util.List;

/**
 * Created by Lenovo on 2018/1/19.
 */

public abstract class TreeListViewwAdapter<T> extends BaseAdapter {

    protected Context mContext;
    /**
     * 存储所有可见的Node
     */
    protected List<Node3> mNodes;
    protected LayoutInflater mInflater;
    /**
     * 存储所有的Node
     */
    protected List<Node3> mAllNodes;

    /**
     * 点击的回调接口
     */
    private OnTreeNodeClickListener onTreeNodeClickListener;

    public interface OnTreeNodeClickListener
    {
        void onClick(Node3 node, int position);
    }

    public void setOnTreeNodeClickListener(
            OnTreeNodeClickListener onTreeNodeClickListener)
    {
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
    public TreeListViewwAdapter(ListView mTree, Context context, List<T> datas,
                               int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException
    {
        mContext = context;
        /**
         * 对所有的Node进行排序
         */
        mAllNodes = TreeHelper3.getSortedNodes(datas, defaultExpandLevel);
        /**
         * 过滤出可见的Node
         */
        mNodes = TreeHelper3.filterVisibleNode(mAllNodes);
        mInflater = LayoutInflater.from(context);

        /**
         * 设置节点点击时，可以展开以及关闭；并且将ItemClick事件继续往外公布
         */
        mTree.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                Log.i("expandOrCollapse1", position+"");
                //这个是展开列表,由于图标点击有bug,先采用这种全部点击
//                expandOrCollapse(position);

                if (onTreeNodeClickListener != null)
                {
                    onTreeNodeClickListener.onClick(mNodes.get(position),
                            position);
                }
            }

        });

    }
    /**
     * 相应ListView的点击事件 展开或关闭某节点
     *
     * @param position
     */
    public void expandOrCollapse(int position)
    {
        Node3 n = mNodes.get(position);

        if (n != null)// 排除传入参数错误异常
        {
            if (!n.isLeaf())
            {
                n.setExpand(!n.isExpand());
                mNodes = TreeHelper3.filterVisibleNode(mAllNodes);
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Node3 node = mNodes.get(i);
        Log.i("node", node.toString());
        view = getConvertView( node , i , view , viewGroup);
//        Log.i("expandOrCollapse2", i+"");
        RelativeLayout myView = (RelativeLayout) view;
        //父布局下的imageview
        ImageView iv = (ImageView) myView.getChildAt(0);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandOrCollapse(i);
            }
        });

        view.setPadding(node.getLevel() * 30, 3, 3, 3);
        return view ;
    }
    public abstract View getConvertView(Node3 node , int position, View convertView,
                                        ViewGroup parent);
}
