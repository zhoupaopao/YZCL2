package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/6/28.
 */

public class carDetailGPSBeans extends BaseResponse{
    private int count;
    private ArrayList<carDetailGPSBean>list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<carDetailGPSBean> getList() {
        return list;
    }

    public void setList(ArrayList<carDetailGPSBean> list) {
        this.list = list;
    }

    public static class carDetailGPSBean{
        private String car_id;
        private String devie_id;
        private String group_name;
        private String internalnum;
        private String type_name;
        private String online_status;
        private String status;
        private String pledge_name;
        private String vin;
        private String car_no;
        private String install_part;
        private String bindtime;
        private String category;
        private deviceGPSBean dgm;

        public String getCar_id() {
            return car_id;
        }

        public void setCar_id(String car_id) {
            this.car_id = car_id;
        }

        public String getDevie_id() {
            return devie_id;
        }

        public void setDevie_id(String devie_id) {
            this.devie_id = devie_id;
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

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public String getOnline_status() {
            return online_status;
        }

        public void setOnline_status(String online_status) {
            this.online_status = online_status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPledge_name() {
            return pledge_name;
        }

        public void setPledge_name(String pledge_name) {
            this.pledge_name = pledge_name;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public String getCar_no() {
            return car_no;
        }

        public void setCar_no(String car_no) {
            this.car_no = car_no;
        }

        public String getInstall_part() {
            return install_part;
        }

        public void setInstall_part(String install_part) {
            this.install_part = install_part;
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

        public deviceGPSBean getDgm() {
            return dgm;
        }

        public void setDgm(deviceGPSBean dgm) {
            this.dgm = dgm;
        }

        public static class deviceGPSBean{
            private String deviceId;
            private String lat;
            private String lng;
            private String blat;
            private String blng;
            private String postion;
            private String time;
            private String stime;
            private String status;
            private String bl;
            private boolean analysis;
            private String type;
            private String alarm;

            public String getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
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

            public String getPostion() {
                return postion;
            }

            public void setPostion(String postion) {
                this.postion = postion;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getStime() {
                return stime;
            }

            public void setStime(String stime) {
                this.stime = stime;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getBl() {
                return bl;
            }

            public void setBl(String bl) {
                this.bl = bl;
            }

            public boolean isAnalysis() {
                return analysis;
            }

            public void setAnalysis(boolean analysis) {
                this.analysis = analysis;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getAlarm() {
                return alarm;
            }

            public void setAlarm(String alarm) {
                this.alarm = alarm;
            }
        }

    }
}
