package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2018/1/17.
 */

public class CustomerManagerBean {
    private String message;
    private String code;
    private boolean success;
    private treeBean object;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public treeBean getObject() {
        return object;
    }

    public void setObject(treeBean object) {
        this.object = object;
    }

    public static class treeBean{
        private ArrayList<customerBean> tree;

        public ArrayList<customerBean> getTree() {
            return tree;
        }

        public void setTree(ArrayList<customerBean> tree) {
            this.tree = tree;
        }

        public static class customerBean{
            private String id;
            private String group_name;
            private String parent_id;
            private String group_dna;
            private String contactor;
            private String phone;
            private int level;
            private String province;
            private String city;
            private String street;
            private String lat;
            private String lng;
            private int maptype;
            private String createtime;
            private String creatorid;
            private String updatorid;
            private String updatetime;
            private String remark;
            private int isactive;
            private boolean chkDisabled;
            private String status;
            private ArrayList<customerBean> childCustomerModel;

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

            public String getGroup_dna() {
                return group_dna;
            }

            public void setGroup_dna(String group_dna) {
                this.group_dna = group_dna;
            }

            public String getContactor() {
                return contactor;
            }

            public void setContactor(String contactor) {
                this.contactor = contactor;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public int getMaptype() {
                return maptype;
            }

            public void setMaptype(int maptype) {
                this.maptype = maptype;
            }

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getCreatorid() {
                return creatorid;
            }

            public void setCreatorid(String creatorid) {
                this.creatorid = creatorid;
            }

            public String getUpdatorid() {
                return updatorid;
            }

            public void setUpdatorid(String updatorid) {
                this.updatorid = updatorid;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getIsactive() {
                return isactive;
            }

            public void setIsactive(int isactive) {
                this.isactive = isactive;
            }

            public boolean isChkDisabled() {
                return chkDisabled;
            }

            public void setChkDisabled(boolean chkDisabled) {
                this.chkDisabled = chkDisabled;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public ArrayList<customerBean> getChildCustomerModel() {
                return childCustomerModel;
            }

            public void setChildCustomerModel(ArrayList<customerBean> childCustomerModel) {
                this.childCustomerModel = childCustomerModel;
            }
        }
    }
}
