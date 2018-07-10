package com.example.yzcl.mvp.model.bean;

/**
 * Created by Lenovo on 2018/7/10.
 */

public class XfzlBean extends BaseResponse{
    private XfzlObject object;

    public XfzlObject getObject() {
        return object;
    }

    public void setObject(XfzlObject object) {
        this.object = object;
    }

    public static  class XfzlObject{
        private String deviceid;
        private int interval;
        private int type;
        private String wuc;

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public int getInterval() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getWuc() {
            return wuc;
        }

        public void setWuc(String wuc) {
            this.wuc = wuc;
        }
    }
}
