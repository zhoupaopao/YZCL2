package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/2/3.
 */

public class CarListBean {
    private int count;
    private String code;
    private String message;
    private boolean success;
    private ArrayList<CarBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<CarBean> getList() {
        return list;
    }

    public void setList(ArrayList<CarBean> list) {
        this.list = list;
    }

    public static class CarBean{
        private String id;
        private String vin;
        //品牌
        private String car_brand;
        //车系
        private String car_series;
        //车型
        private String car_version;
        private String createtime; // 创建时间
        private int  devicecount ;// 设备数量
        private String  groupid;//归属客户id
        private String groupname ;// 归属客户名
        private int  iscare ;// 是否重点关注
        private int  isoverdue ;// 是否逾期
        private String  orderid ;// 工单id
        private String  pledgeid;// 借款人id
        private String  pledgename;// 借款人名称
        private String  status;// 标记状态
        private int  used_age ;//使用年限(年)
        private String car_address;
        private String car_no;
        private String sign_status;//报警类型


        public String getCar_address() {
            return car_address;
        }

        public void setCar_address(String car_address) {
            this.car_address = car_address;
        }

        public String getCar_no() {
            return car_no;
        }

        public void setCar_no(String car_no) {
            this.car_no = car_no;
        }

        public String getSign_status() {
            return sign_status;
        }

        public void setSign_status(String sign_status) {
            this.sign_status = sign_status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public String getCar_brand() {
            return car_brand;
        }

        public void setCar_brand(String car_brand) {
            this.car_brand = car_brand;
        }

        public String getCar_series() {
            return car_series;
        }

        public void setCar_series(String car_series) {
            this.car_series = car_series;
        }

        public String getCar_version() {
            return car_version;
        }

        public void setCar_version(String car_version) {
            this.car_version = car_version;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getDevicecount() {
            return devicecount;
        }

        public void setDevicecount(int devicecount) {
            this.devicecount = devicecount;
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

        public int getIscare() {
            return iscare;
        }

        public void setIscare(int iscare) {
            this.iscare = iscare;
        }

        public int getIsoverdue() {
            return isoverdue;
        }

        public void setIsoverdue(int isoverdue) {
            this.isoverdue = isoverdue;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getUsed_age() {
            return used_age;
        }

        public void setUsed_age(int used_age) {
            this.used_age = used_age;
        }
    }
}
