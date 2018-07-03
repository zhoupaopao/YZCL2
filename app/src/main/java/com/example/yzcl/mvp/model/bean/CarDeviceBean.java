package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/7/3.
 */

public class CarDeviceBean extends BaseResponse {
    private int count;
    private ArrayList<DeviceBeans>list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<DeviceBeans> getList() {
        return list;
    }

    public void setList(ArrayList<DeviceBeans> list) {
        this.list = list;
    }

    public static class DeviceBeans{
       private String bindtime;
        private String cardeviceid;
        private String carid;
        private String deviceid;
        private String devicetype;
        private String devicetypename;
        private String install_part;
        private String internalnum;
        private int online_status;
        private String simcard;
        private long valid_end;
        private long valid_from;

        public String getBindtime() {
            return bindtime;
        }

        public void setBindtime(String bindtime) {
            this.bindtime = bindtime;
        }

        public String getCardeviceid() {
            return cardeviceid;
        }

        public void setCardeviceid(String cardeviceid) {
            this.cardeviceid = cardeviceid;
        }

        public String getCarid() {
            return carid;
        }

        public void setCarid(String carid) {
            this.carid = carid;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getDevicetype() {
            return devicetype;
        }

        public void setDevicetype(String devicetype) {
            this.devicetype = devicetype;
        }

        public String getDevicetypename() {
            return devicetypename;
        }

        public void setDevicetypename(String devicetypename) {
            this.devicetypename = devicetypename;
        }

        public String getInstall_part() {
            return install_part;
        }

        public void setInstall_part(String install_part) {
            this.install_part = install_part;
        }

        public String getInternalnum() {
            return internalnum;
        }

        public void setInternalnum(String internalnum) {
            this.internalnum = internalnum;
        }

        public int getOnline_status() {
            return online_status;
        }

        public void setOnline_status(int online_status) {
            this.online_status = online_status;
        }

        public String getSimcard() {
            return simcard;
        }

        public void setSimcard(String simcard) {
            this.simcard = simcard;
        }

        public long getValid_end() {
            return valid_end;
        }

        public void setValid_end(long valid_end) {
            this.valid_end = valid_end;
        }

        public long getValid_from() {
            return valid_from;
        }

        public void setValid_from(long valid_from) {
            this.valid_from = valid_from;
        }
    }
}
