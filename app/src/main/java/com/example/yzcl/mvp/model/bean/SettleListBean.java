package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/2/5.
 */

public class SettleListBean {
    private String code;
    private String message;
    private int count;
    private boolean success;
    private ArrayList<SettleBean>list;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<SettleBean> getList() {
        return list;
    }

    public void setList(ArrayList<SettleBean> list) {
        this.list = list;
    }

    public static class SettleBean{
        private String car_brand;
        private String car_no;
        private int car_value;
        private String car_version;
        private String groupid;
        private String groupname;
        private String id;
        private int isactive;
        private String pledgeid;
        private String pledgename;
        private String settledtime;
        private String updatename;
        private String updatetime;
        private String updatorid;
        private String vin;

        public String getCar_brand() {
            return car_brand;
        }

        public void setCar_brand(String car_brand) {
            this.car_brand = car_brand;
        }

        public String getCar_no() {
            return car_no;
        }

        public void setCar_no(String car_no) {
            this.car_no = car_no;
        }

        public int getCar_value() {
            return car_value;
        }

        public void setCar_value(int car_value) {
            this.car_value = car_value;
        }

        public String getCar_version() {
            return car_version;
        }

        public void setCar_version(String car_version) {
            this.car_version = car_version;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIsactive() {
            return isactive;
        }

        public void setIsactive(int isactive) {
            this.isactive = isactive;
        }

        public String getPledgeid() {
            return pledgeid;
        }

        public void setPledgeid(String pledgeid) {
            this.pledgeid = pledgeid;
        }

        public String getPledgename() {
            return pledgename;
        }

        public void setPledgename(String pledgename) {
            this.pledgename = pledgename;
        }

        public String getSettledtime() {
            return settledtime;
        }

        public void setSettledtime(String settledtime) {
            this.settledtime = settledtime;
        }

        public String getUpdatename() {
            return updatename;
        }

        public void setUpdatename(String updatename) {
            this.updatename = updatename;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getUpdatorid() {
            return updatorid;
        }

        public void setUpdatorid(String updatorid) {
            this.updatorid = updatorid;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }
    }
}
