package com.example.yzcl.mvp.model.bean;

import com.example.tree.TreeNodeUseid;
import com.example.tree.TreeNodechkDisabled;
import com.zhy.tree.bean.TreeNodeId;
import com.zhy.tree.bean.TreeNodeLabel;
import com.zhy.tree.bean.TreeNodePid;

/**
 * Created by Lenovo on 2018/1/18.
 */

public class NewFileBean {
    @TreeNodeId
    private int id;
    @TreeNodePid
    private int parentid;
    @TreeNodeLabel
    private String name;
    @TreeNodeUseid
    private String userid;
    @TreeNodechkDisabled
    private Boolean chkDisabled;
    public NewFileBean(int id, int parentid, String name, String userid,Boolean chkDisabled) {
        super();
        this.id=id;
        this.parentid=parentid;
        this.name=name;
        this.userid=userid;
        this.chkDisabled=chkDisabled;
    }
}
