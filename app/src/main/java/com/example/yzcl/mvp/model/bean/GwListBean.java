package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/2/8.
 */

public class GwListBean {
    private String code;
    private String message;
    private int count;
    private boolean success;
    private ArrayList<GwBean> list;

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

    public ArrayList<GwBean> getList() {
        return list;
    }

    public void setList(ArrayList<GwBean> list) {
        this.list = list;
    }

    public class GwBean{
       private String endtime;
       private String groupid;
       private String groupname;
       private String id;
       private String pledgeid;
       private String pledgename;
       private String speedtime;
       private String starttime;
       private String status;
       private String statusname;
       private int type;
       private String typename;
       private String vin;

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
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

        public String getSpeedtime() {
            return speedtime;
        }

        public void setSpeedtime(String speedtime) {
            this.speedtime = speedtime;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatusname() {
            return statusname;
        }

        public void setStatusname(String statusname) {
            this.statusname = statusname;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }
    }
}
