package com.example.tree;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2018/1/19.
 */

public class Node3 {
    private int id;
    /**
     * 根节点pId为0
     */
    private int pId = 0;
    private String name;
    private String useid;

    /**
     * 当前的级别
     */
    private int level;
    /**
     * 是否展开
     */
    private boolean isExpand = false;

    /**
     * 是否选中
     */
    private boolean isChecked = false;
    private int icon;

    /**
     * 下一级的子Node
     */
    private List<Node3> children = new ArrayList<Node3>();

    /**
     * 父Node
     */
    private Node3 parent;

    public Node3()
    {
    }

    public Node3(int id, int pId, String name,  String useid)
    {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.useid =useid;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUseid() {
        return useid;
    }

    public void setUseid(String useid) {
        this.useid = useid;
    }
    /**
     * 是否为跟节点
     *
     * @return
     */
    public boolean isRoot()
    {
        return parent == null;
    }

    /**
     * 判断父节点是否展开
     *
     * @return
     */
    public boolean isParentExpand()
    {
        if (parent == null)
            return false;
        return parent.isExpand();
    }
    /**
     * 是否是叶子界点
     *
     * @return
     */
    public boolean isLeaf()
    {
        return children.size() == 0;
    }
    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (!isExpand)
        {

            Log.e("TAG", name + " , " + "shousuo ");
            for (Node3 node : children)
            {
                node.setExpand(isExpand);
            }
        }
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public List<Node3> getChildren() {
        return children;
    }

    public void setChildren(List<Node3> children) {
        this.children = children;
    }

    public Node3 getParent() {
        return parent;
    }

    public void setParent(Node3 parent) {
        this.parent = parent;
    }
}
