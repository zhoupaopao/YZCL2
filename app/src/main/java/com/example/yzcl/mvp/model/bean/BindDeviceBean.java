package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/8/9.
 */

public class BindDeviceBean extends BaseResponse {
    private int count;
    private ArrayList<BindDeviceBeanMsg>list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<BindDeviceBeanMsg> getList() {
        return list;
    }

    public void setList(ArrayList<BindDeviceBeanMsg> list) {
        this.list = list;
    }

    public static class BindDeviceBeanMsg{
        private String category;
        private String deviceid;
        private String group_name;
        private String internalnum;
        private String valid_end;
        private String loc;

        public String getLoc() {
            return loc;
        }

        public void setLoc(String loc) {
            this.loc = loc;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getInternalnum() {
            return internalnum;
        }

        public void setInternalnum(String internalnum) {
            this.internalnum = internalnum;
        }

        public String getValid_end() {
            return valid_end;
        }

        public void setValid_end(String valid_end) {
            this.valid_end = valid_end;
        }
    }
}
