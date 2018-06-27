package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/6/27.
 */

public class CarMonSearchListBean {
    private int count;
    private boolean success;
    private String code;
    private String message;
    private ArrayList<CarSearchBean>list;

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

    public ArrayList<CarSearchBean> getList() {
        return list;
    }

    public void setList(ArrayList<CarSearchBean> list) {
        this.list = list;
    }

    public static class CarSearchBean{
        private String id;
        private String vin;
        private String name;
        private String custom_team_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCustom_team_id() {
        return custom_team_id;
    }

    public void setCustom_team_id(String custom_team_id) {
        this.custom_team_id = custom_team_id;
    }
}
}
