package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/1/30.
 */

public class WlglRadiusBean {
    private int count;
    private String code;
    private String message;
    private boolean success;
    private ArrayList<EnclosureBean>list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<EnclosureBean> getList() {
        return list;
    }

    public void setList(ArrayList<EnclosureBean> list) {
        this.list = list;
    }

    public static class EnclosureBean{
        private String id;
        private String fencename;
        private String parametersets;
        //围栏形状
        private int fencearea;
        //围栏类型
        private int fencetype;
        //是否在线
        private int isactive;
        //是否选中
        private boolean cb;

        public boolean isCb() {
            return cb;
        }

        public void setCb(boolean cb) {
            this.cb = cb;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFencename() {
            return fencename;
        }

        public void setFencename(String fencename) {
            this.fencename = fencename;
        }

        public String getParametersets() {
            return parametersets;
        }

        public void setParametersets(String parametersets) {
            this.parametersets = parametersets;
        }

        public int getFencearea() {
            return fencearea;
        }

        public void setFencearea(int fencearea) {
            this.fencearea = fencearea;
        }

        public int getFencetype() {
            return fencetype;
        }

        public void setFencetype(int fencetype) {
            this.fencetype = fencetype;
        }

        public int getIsactive() {
            return isactive;
        }

        public void setIsactive(int isactive) {
            this.isactive = isactive;
        }
    }
}
