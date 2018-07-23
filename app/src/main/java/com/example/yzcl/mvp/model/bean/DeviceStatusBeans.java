package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/7/23.
 */

public class DeviceStatusBeans extends BaseResponse {
    private int count;
    private ArrayList<DeviceStatus>list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<DeviceStatus> getList() {
        return list;
    }

    public void setList(ArrayList<DeviceStatus> list) {
        this.list = list;
    }

    public static class DeviceStatus{
        private String deviceType;
        private int bindCounts;
        private int jzcounts;
        private int lxcounts;
        private int unbindCounts;
        private int xscounts;

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public int getBindCounts() {
            return bindCounts;
        }

        public void setBindCounts(int bindCounts) {
            this.bindCounts = bindCounts;
        }

        public int getJzcounts() {
            return jzcounts;
        }

        public void setJzcounts(int jzcounts) {
            this.jzcounts = jzcounts;
        }

        public int getLxcounts() {
            return lxcounts;
        }

        public void setLxcounts(int lxcounts) {
            this.lxcounts = lxcounts;
        }

        public int getUnbindCounts() {
            return unbindCounts;
        }

        public void setUnbindCounts(int unbindCounts) {
            this.unbindCounts = unbindCounts;
        }

        public int getXscounts() {
            return xscounts;
        }

        public void setXscounts(int xscounts) {
            this.xscounts = xscounts;
        }
    }

}
