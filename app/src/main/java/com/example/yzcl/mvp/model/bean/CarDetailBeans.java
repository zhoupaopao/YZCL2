package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/6/28.
 */

public class CarDetailBeans extends BaseResponse{
    private int count;
    private ArrayList<CarDetailBean>list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<CarDetailBean> getList() {
        return list;
    }

    public void setList(ArrayList<CarDetailBean> list) {
        this.list = list;
    }

    public static class CarDetailBean{
        private String iscare;
        private String isoverdue;
        private String sign_status;
        private String status;
        private String search;
        private String deviceid;
        private String carid;
        private String vin;
        private String name;
        private int type;
        private String category;
        private String groupids;
        private String page;
        private int ct;
        private String pagesize;
        private String online_status;
        private String custom_team_id;
        private ArrayList<DeviceBean>device;

        public String getIscare() {
            return iscare;
        }

        public void setIscare(String iscare) {
            this.iscare = iscare;
        }

        public String getIsoverdue() {
            return isoverdue;
        }

        public void setIsoverdue(String isoverdue) {
            this.isoverdue = isoverdue;
        }

        public String getSign_status() {
            return sign_status;
        }

        public void setSign_status(String sign_status) {
            this.sign_status = sign_status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSearch() {
            return search;
        }

        public void setSearch(String search) {
            this.search = search;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getCarid() {
            return carid;
        }

        public void setCarid(String carid) {
            this.carid = carid;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getGroupids() {
            return groupids;
        }

        public void setGroupids(String groupids) {
            this.groupids = groupids;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public int getCt() {
            return ct;
        }

        public void setCt(int ct) {
            this.ct = ct;
        }

        public String getPagesize() {
            return pagesize;
        }

        public void setPagesize(String pagesize) {
            this.pagesize = pagesize;
        }

        public String getOnline_status() {
            return online_status;
        }

        public void setOnline_status(String online_status) {
            this.online_status = online_status;
        }

        public String getCustom_team_id() {
            return custom_team_id;
        }

        public void setCustom_team_id(String custom_team_id) {
            this.custom_team_id = custom_team_id;
        }

        public ArrayList<DeviceBean> getDevice() {
            return device;
        }

        public void setDevice(ArrayList<DeviceBean> device) {
            this.device = device;
        }

        public static class DeviceBean{
            private String id;
            private String internalnum;
            private String type_name;
            private String online_status;
            private String status;
            private String category;

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

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }
        }
    }
}
