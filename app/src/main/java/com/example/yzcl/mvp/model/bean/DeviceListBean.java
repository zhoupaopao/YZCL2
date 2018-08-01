package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/7/31.
 */

public class DeviceListBean extends BaseResponse {
    private int count;
    private ArrayList<DeviceLLBean>list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<DeviceLLBean> getList() {
        return list;
    }

    public void setList(ArrayList<DeviceLLBean> list) {
        this.list = list;
    }

    public static class DeviceLLBean{
        private String alaram;
        private String bindtime;
        private String bl;
        private String createtime;
        private String devicetype;
        private String gpsStates;
        private String group_name;
        private String id;
        private String internalnum;
        private String isactive;
        private String onlineStateStr;
        private String postion;
        private String simcard;
        private String lastLocTime;
        private String pledgerName;
        private String vin;
        private String blat;
        private String blng;
        private String valid_from;
        private String valid_end;

        public String getValid_from() {
            return valid_from;
        }

        public void setValid_from(String valid_from) {
            this.valid_from = valid_from;
        }

        public String getValid_end() {
            return valid_end;
        }

        public void setValid_end(String valid_end) {
            this.valid_end = valid_end;
        }

        public String getLastLocTime() {
            return lastLocTime;
        }

        public void setLastLocTime(String lastLocTime) {
            this.lastLocTime = lastLocTime;
        }

        public String getPledgerName() {
            return pledgerName;
        }

        public void setPledgerName(String pledgerName) {
            this.pledgerName = pledgerName;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public String getBlat() {
            return blat;
        }

        public void setBlat(String blat) {
            this.blat = blat;
        }

        public String getBlng() {
            return blng;
        }

        public void setBlng(String blng) {
            this.blng = blng;
        }

        public String getAlaram() {
            return alaram;
        }

        public void setAlaram(String alaram) {
            this.alaram = alaram;
        }

        public String getBindtime() {
            return bindtime;
        }

        public void setBindtime(String bindtime) {
            this.bindtime = bindtime;
        }

        public String getBl() {
            return bl;
        }

        public void setBl(String bl) {
            this.bl = bl;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getDevicetype() {
            return devicetype;
        }

        public void setDevicetype(String devicetype) {
            this.devicetype = devicetype;
        }

        public String getGpsStates() {
            return gpsStates;
        }

        public void setGpsStates(String gpsStates) {
            this.gpsStates = gpsStates;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInternalnum() {
            return internalnum;
        }

        public void setInternalnum(String internalnum) {
            this.internalnum = internalnum;
        }

        public String getIsactive() {
            return isactive;
        }

        public void setIsactive(String isactive) {
            this.isactive = isactive;
        }

        public String getOnlineStateStr() {
            return onlineStateStr;
        }

        public void setOnlineStateStr(String onlineStateStr) {
            this.onlineStateStr = onlineStateStr;
        }

        public String getPostion() {
            return postion;
        }

        public void setPostion(String postion) {
            this.postion = postion;
        }

        public String getSimcard() {
            return simcard;
        }

        public void setSimcard(String simcard) {
            this.simcard = simcard;
        }
    }
}
