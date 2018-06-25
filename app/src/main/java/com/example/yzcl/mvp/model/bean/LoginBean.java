package com.example.yzcl.mvp.model.bean;

import java.util.List;

/**
 * Created by Lenovo on 2017/12/11.
 */

public class LoginBean {

       /* object: {
            token: "462ed1d89b264c52a42c2d0be86d6d40",
                    userid: "a4d472ec184449fc97c7a43732430987",
                    groupid: null,
                    vehicleid: null
        },
        message: "",
                code: "",
            success: true*/

    private String message;
    private String code;
    private boolean success;
    private PersonMsg object;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public PersonMsg getObject() {
        return object;
    }

    public void setObject(PersonMsg object) {
        this.object = object;
    }



    public class PersonMsg {
        private String token;
        private String userid;
        private String key;
        private String image;
        private String errorcode;
        private String errorMessage;
        private String vehicleid;
        private String lastlogintime;
        private String roleid;
        private String groupname;
        private String groupid;
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getErrorcode() {
            return errorcode;
        }

        public void setErrorcode(String errorcode) {
            this.errorcode = errorcode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getVehicleid() {
            return vehicleid;
        }

        public void setVehicleid(String vehicleid) {
            this.vehicleid = vehicleid;
        }

        public String getLastlogintime() {
            return lastlogintime;
        }

        public void setLastlogintime(String lastlogintime) {
            this.lastlogintime = lastlogintime;
        }

        public String getRoleid() {
            return roleid;
        }

        public void setRoleid(String roleid) {
            this.roleid = roleid;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }
    }
}
