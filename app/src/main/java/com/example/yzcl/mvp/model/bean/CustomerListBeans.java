package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/7/27.
 */

public class CustomerListBeans extends BaseResponse {
    private int count;
    private ArrayList<CustomerList>list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<CustomerList> getList() {
        return list;
    }

    public void setList(ArrayList<CustomerList> list) {
        this.list = list;
    }

    public static class CustomerList{
        private String id;
        private String group_name;
        private String parent_id;
        private Boolean ischec=false;
        private Boolean chkDisabled;

        public Boolean getChkDisabled() {
            return chkDisabled;
        }

        public void setChkDisabled(Boolean chkDisabled) {
            this.chkDisabled = chkDisabled;
        }

        public Boolean getIschec() {
            return ischec;
        }

        public void setIschec(Boolean ischec) {
            this.ischec = ischec;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }
    }
}
