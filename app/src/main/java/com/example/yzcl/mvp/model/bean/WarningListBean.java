package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/2/6.
 */

public class WarningListBean {
    private String code;
    private String message;
    private int count;
    private boolean success;
    private ArrayList<WarningBean>list;

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

    public ArrayList<WarningBean> getList() {
        return list;
    }

    public void setList(ArrayList<WarningBean> list) {
        this.list = list;
    }

    public class WarningBean{
        private String adcode;// 行政区划代码 ,
        private int alarmtype ;// 报警类型 ,
        private String alarmtypename ;// 报警类型名 ,
        private int bl ;//设备电量 ,
        private String blat;//百度纬度 ,
        private String blng;// 百度经度 ,
        private String car_no ;// 车牌号 ,
        private String carvin ;// 车辆vin号 ,
        private String  city ;// 市 ,
        private String deviceid ;// 设备id ,
        private String devicetype ;// 设备类型 ,
        private String devicetypename ;// 设备类型名 ,
        private String district;//区 ,
        private String  endtime ;//报警结束时间 ,
        private String  fenceid ;// 围栏id ,
        private String glat ;//高德纬度 ,
        private String  glng ;//高德经度 ,
        private String  groupid;//客户id ,
        private String groupname ;// 客户名 ,
        private String  id ;//报警信息编号 ,
        private String internalnum ;// 设备号 ,
        private int ischecked ;//用户是否已经阅读过 0全部 1 已读 2未读 ,
        private String  ischeckedname ;//用户是否已经阅读过 0全部 1 已读 2未读 状态名称 ,
        private int  isend ;// 是否截止 0未截止 1已截止 ,
        private int isquestion ;// 是否有问题的（待确认） ,
        private int issendapp ;// 是否推送到app 0不推送 1 推送 ,
        private int issendsms ;// 是否发送了短信 0未发送 1 已发送 ,
        private int isshow ;// 是否显示 0不显示 1显示 ,
        private String  lat;// gps维度 ,
        private String  lng;// gps经度 ,
        private String  name ;// 借款人名称 ,
        private String  pledgehotpointid ;//二押热点id ,
        private String   province ;// 省份 ,
        private String  remark ;// 报警描述 ,
        private String  speedtime ;//持续时间 ,
        private String  starttime ;// 报警起始时间 ,
        private String street ;//街道 ,
        private String street_number ;// 门牌号 ,
       private int type ;//定位类型。4000是GPS定位，其他都是基站

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public int getAlarmtype() {
            return alarmtype;
        }

        public void setAlarmtype(int alarmtype) {
            this.alarmtype = alarmtype;
        }

        public String getAlarmtypename() {
            return alarmtypename;
        }

        public void setAlarmtypename(String alarmtypename) {
            this.alarmtypename = alarmtypename;
        }

        public int getBl() {
            return bl;
        }

        public void setBl(int bl) {
            this.bl = bl;
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

        public String getCar_no() {
            return car_no;
        }

        public void setCar_no(String car_no) {
            this.car_no = car_no;
        }

        public String getCarvin() {
            return carvin;
        }

        public void setCarvin(String carvin) {
            this.carvin = carvin;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
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

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getFenceid() {
            return fenceid;
        }

        public void setFenceid(String fenceid) {
            this.fenceid = fenceid;
        }

        public String getGlat() {
            return glat;
        }

        public void setGlat(String glat) {
            this.glat = glat;
        }

        public String getGlng() {
            return glng;
        }

        public void setGlng(String glng) {
            this.glng = glng;
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

        public String getInternalnum() {
            return internalnum;
        }

        public void setInternalnum(String internalnum) {
            this.internalnum = internalnum;
        }

        public int getIschecked() {
            return ischecked;
        }

        public void setIschecked(int ischecked) {
            this.ischecked = ischecked;
        }

        public String getIscheckedname() {
            return ischeckedname;
        }

        public void setIscheckedname(String ischeckedname) {
            this.ischeckedname = ischeckedname;
        }

        public int getIsend() {
            return isend;
        }

        public void setIsend(int isend) {
            this.isend = isend;
        }

        public int getIsquestion() {
            return isquestion;
        }

        public void setIsquestion(int isquestion) {
            this.isquestion = isquestion;
        }

        public int getIssendapp() {
            return issendapp;
        }

        public void setIssendapp(int issendapp) {
            this.issendapp = issendapp;
        }

        public int getIssendsms() {
            return issendsms;
        }

        public void setIssendsms(int issendsms) {
            this.issendsms = issendsms;
        }

        public int getIsshow() {
            return isshow;
        }

        public void setIsshow(int isshow) {
            this.isshow = isshow;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPledgehotpointid() {
            return pledgehotpointid;
        }

        public void setPledgehotpointid(String pledgehotpointid) {
            this.pledgehotpointid = pledgehotpointid;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getStreet_number() {
            return street_number;
        }

        public void setStreet_number(String street_number) {
            this.street_number = street_number;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
