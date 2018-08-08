package com.example.yzcl.mvp.model.bean;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2018/3/16.
 */

public class CityBean extends BaseResponse{
    private int count;
    private ArrayList<NewCityBean>list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<NewCityBean> getList() {
        return list;
    }

    public void setList(ArrayList<NewCityBean> list) {
        this.list = list;
    }

    public static class NewCityBean{
        private String id;
        private String name;
        private String code;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}
