package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/7/3.
 */

public class CarMessageBean extends BaseResponse {
    private CarMessage object;

    public CarMessage getObject() {
        return object;
    }

    public void setObject(CarMessage object) {
        this.object = object;
    }

    public static class CarMessage{
        private String[] carImage;//实际参数是[]
        private String car_brand;
        private String car_no;
        private String car_series;//车系
        private int car_type;
        private boolean car_value;
        private String car_version;
        private String carloantype;
        private String cartodevice;
        private String color;
        private String contactor;
        private String createtime;
        private String creatorid;
        private String engine;
        private String erector;
        private String erector_phone;
        private String id;
        private String imgurls;
        private int is_new_car;
        private int isactive;
        private int iscare;
        private int isgrouped;
        private int isoverdue;
        private int mileage;
        private int online_status;
        private String phone;
        private String remark;
        private String source;
        private String status;
        private String updatetime;
        private String updatorid;
        private int use_prop;
        private String user_id;
        private int used_age;
        private String vin;
        private PledgeCar pledge_car;
        private Pledger pledger;
        private SystemGroup systemgroup;

        public String[] getCarImage() {
            return carImage;
        }

        public void setCarImage(String[] carImage) {
            this.carImage = carImage;
        }

        public String getCar_brand() {
            return car_brand;
        }

        public void setCar_brand(String car_brand) {
            this.car_brand = car_brand;
        }

        public String getCar_no() {
            return car_no;
        }

        public void setCar_no(String car_no) {
            this.car_no = car_no;
        }

        public String getCar_series() {
            return car_series;
        }

        public void setCar_series(String car_series) {
            this.car_series = car_series;
        }

        public int getCar_type() {
            return car_type;
        }

        public void setCar_type(int car_type) {
            this.car_type = car_type;
        }

        public boolean isCar_value() {
            return car_value;
        }

        public void setCar_value(boolean car_value) {
            this.car_value = car_value;
        }

        public String getCar_version() {
            return car_version;
        }

        public void setCar_version(String car_version) {
            this.car_version = car_version;
        }

        public String getCarloantype() {
            return carloantype;
        }

        public void setCarloantype(String carloantype) {
            this.carloantype = carloantype;
        }

        public String getCartodevice() {
            return cartodevice;
        }

        public void setCartodevice(String cartodevice) {
            this.cartodevice = cartodevice;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getContactor() {
            return contactor;
        }

        public void setContactor(String contactor) {
            this.contactor = contactor;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getCreatorid() {
            return creatorid;
        }

        public void setCreatorid(String creatorid) {
            this.creatorid = creatorid;
        }

        public String getEngine() {
            return engine;
        }

        public void setEngine(String engine) {
            this.engine = engine;
        }

        public String getErector() {
            return erector;
        }

        public void setErector(String erector) {
            this.erector = erector;
        }

        public String getErector_phone() {
            return erector_phone;
        }

        public void setErector_phone(String erector_phone) {
            this.erector_phone = erector_phone;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImgurls() {
            return imgurls;
        }

        public void setImgurls(String imgurls) {
            this.imgurls = imgurls;
        }

        public int getIs_new_car() {
            return is_new_car;
        }

        public void setIs_new_car(int is_new_car) {
            this.is_new_car = is_new_car;
        }

        public int getIsactive() {
            return isactive;
        }

        public void setIsactive(int isactive) {
            this.isactive = isactive;
        }

        public int getIscare() {
            return iscare;
        }

        public void setIscare(int iscare) {
            this.iscare = iscare;
        }

        public int getIsgrouped() {
            return isgrouped;
        }

        public void setIsgrouped(int isgrouped) {
            this.isgrouped = isgrouped;
        }

        public int getIsoverdue() {
            return isoverdue;
        }

        public void setIsoverdue(int isoverdue) {
            this.isoverdue = isoverdue;
        }

        public int getMileage() {
            return mileage;
        }

        public void setMileage(int mileage) {
            this.mileage = mileage;
        }

        public int getOnline_status() {
            return online_status;
        }

        public void setOnline_status(int online_status) {
            this.online_status = online_status;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getUpdatorid() {
            return updatorid;
        }

        public void setUpdatorid(String updatorid) {
            this.updatorid = updatorid;
        }

        public int getUse_prop() {
            return use_prop;
        }

        public void setUse_prop(int use_prop) {
            this.use_prop = use_prop;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public int getUsed_age() {
            return used_age;
        }

        public void setUsed_age(int used_age) {
            this.used_age = used_age;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public PledgeCar getPledge_car() {
            return pledge_car;
        }

        public void setPledge_car(PledgeCar pledge_car) {
            this.pledge_car = pledge_car;
        }

        public Pledger getPledger() {
            return pledger;
        }

        public void setPledger(Pledger pledger) {
            this.pledger = pledger;
        }

        public SystemGroup getSystemgroup() {
            return systemgroup;
        }

        public void setSystemgroup(SystemGroup systemgroup) {
            this.systemgroup = systemgroup;
        }

        public static class PledgeCar{
            private int issettled;
            private String carid;
            private String creatorid;
            private String expiretime;
            private String id;
            private String pledgerid;
            private String pledgetime;
            private String settledtime;
            private String updatorid;

            public int getIssettled() {
                return issettled;
            }

            public void setIssettled(int issettled) {
                this.issettled = issettled;
            }

            public String getCarid() {
                return carid;
            }

            public void setCarid(String carid) {
                this.carid = carid;
            }

            public String getCreatorid() {
                return creatorid;
            }

            public void setCreatorid(String creatorid) {
                this.creatorid = creatorid;
            }

            public String getExpiretime() {
                return expiretime;
            }

            public void setExpiretime(String expiretime) {
                this.expiretime = expiretime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPledgerid() {
                return pledgerid;
            }

            public void setPledgerid(String pledgerid) {
                this.pledgerid = pledgerid;
            }

            public String getPledgetime() {
                return pledgetime;
            }

            public void setPledgetime(String pledgetime) {
                this.pledgetime = pledgetime;
            }

            public String getSettledtime() {
                return settledtime;
            }

            public void setSettledtime(String settledtime) {
                this.settledtime = settledtime;
            }

            public String getUpdatorid() {
                return updatorid;
            }

            public void setUpdatorid(String updatorid) {
                this.updatorid = updatorid;
            }
        }
        public static class Pledger{
            private int card_type;
            private int sex;
            private String createtime;
            private String email;
            private String group_id;
            private String id;
            private String idcard;
            private String imgurl;
            private String isactive;
            private String name;
            private String phone;
            private ArrayList<PledgerLoc>pledger_loc;

            public int getCard_type() {
                return card_type;
            }

            public void setCard_type(int card_type) {
                this.card_type = card_type;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getGroup_id() {
                return group_id;
            }

            public void setGroup_id(String group_id) {
                this.group_id = group_id;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIdcard() {
                return idcard;
            }

            public void setIdcard(String idcard) {
                this.idcard = idcard;
            }

            public String getImgurl() {
                return imgurl;
            }

            public void setImgurl(String imgurl) {
                this.imgurl = imgurl;
            }

            public String getIsactive() {
                return isactive;
            }

            public void setIsactive(String isactive) {
                this.isactive = isactive;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public ArrayList<PledgerLoc> getPledger_loc() {
                return pledger_loc;
            }

            public void setPledger_loc(ArrayList<PledgerLoc> pledger_loc) {
                this.pledger_loc = pledger_loc;
            }

            public static class PledgerLoc{
                private int type;
                private String adcode;
                private String address;
                private String city;
                private String createtime;
                private String district;
                private String id;
                private String isactive;
                private String lat;
                private String lng;
                private String maptype;
                private String pledgerid;
                private String province;
                private String updatetime;

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getAdcode() {
                    return adcode;
                }

                public void setAdcode(String adcode) {
                    this.adcode = adcode;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getCity() {
                    return city;
                }

                public void setCity(String city) {
                    this.city = city;
                }

                public String getCreatetime() {
                    return createtime;
                }

                public void setCreatetime(String createtime) {
                    this.createtime = createtime;
                }

                public String getDistrict() {
                    return district;
                }

                public void setDistrict(String district) {
                    this.district = district;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getIsactive() {
                    return isactive;
                }

                public void setIsactive(String isactive) {
                    this.isactive = isactive;
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

                public String getMaptype() {
                    return maptype;
                }

                public void setMaptype(String maptype) {
                    this.maptype = maptype;
                }

                public String getPledgerid() {
                    return pledgerid;
                }

                public void setPledgerid(String pledgerid) {
                    this.pledgerid = pledgerid;
                }

                public String getProvince() {
                    return province;
                }

                public void setProvince(String province) {
                    this.province = province;
                }

                public String getUpdatetime() {
                    return updatetime;
                }

                public void setUpdatetime(String updatetime) {
                    this.updatetime = updatetime;
                }
            }
        }
        public static class SystemGroup{
            private int isactive;
            private int level;
            private int maptype;
            private String city;
            private String contactor;
            private String createtime;
            private String creatorid;
            private String group_dna;
            private String group_name;
            private String id;
            private String  lat;
            private String lng;
            private String parent_id;
            private String phone;
            private String province;
            private String remark;
            private String street;
            private String updatetime;
            private String updatorid;

            public int getIsactive() {
                return isactive;
            }

            public void setIsactive(int isactive) {
                this.isactive = isactive;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public int getMaptype() {
                return maptype;
            }

            public void setMaptype(int maptype) {
                this.maptype = maptype;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getContactor() {
                return contactor;
            }

            public void setContactor(String contactor) {
                this.contactor = contactor;
            }

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getCreatorid() {
                return creatorid;
            }

            public void setCreatorid(String creatorid) {
                this.creatorid = creatorid;
            }

            public String getGroup_dna() {
                return group_dna;
            }

            public void setGroup_dna(String group_dna) {
                this.group_dna = group_dna;
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

            public String getParent_id() {
                return parent_id;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
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

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }

            public String getUpdatorid() {
                return updatorid;
            }

            public void setUpdatorid(String updatorid) {
                this.updatorid = updatorid;
            }
        }


    }
}
