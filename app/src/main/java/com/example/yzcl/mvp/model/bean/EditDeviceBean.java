package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/8/9.
 */

public class EditDeviceBean extends BaseResponse {
    private int count;
    private ArrayList<EditDeviceBeanMsg>list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<EditDeviceBeanMsg> getList() {
        return list;
    }

    public void setList(ArrayList<EditDeviceBeanMsg> list) {
        this.list = list;
    }

    public static class EditDeviceBeanMsg{
        private String bindtime;
        private String category;
        private String devie_id;
        private String deviceid;
        private String internalnum;
        private Boolean isadd=false;///是否是添加的
        private String install_part;
        private String devicetypename;//这个和category冲突

        public String getDevicetypename() {
            return devicetypename;
        }

        public void setDevicetypename(String devicetypename) {
            this.devicetypename = devicetypename;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getBindtime() {
            return bindtime;
        }

        public void setBindtime(String bindtime) {
            this.bindtime = bindtime;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDevie_id() {
            return devie_id;
        }

        public void setDevie_id(String devie_id) {
            this.devie_id = devie_id;
        }

        public String getInternalnum() {
            return internalnum;
        }

        public void setInternalnum(String internalnum) {
            this.internalnum = internalnum;
        }

        public Boolean getIsadd() {
            return isadd;
        }

        public void setIsadd(Boolean isadd) {
            this.isadd = isadd;
        }

        public String getInstall_part() {
            return install_part;
        }

        public void setInstall_part(String install_part) {
            this.install_part = install_part;
        }
    }
}
