package com.example.yzcl.mvp.model.bean;

import java.util.List;

/**
 * Created by 狄飞 on 2016/8/13.
 */
public class CarBrandBean extends BaseResponse{

    private int count;
    private List<BrandListBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<BrandListBean> getList() {
        return list;
    }

    public void setList(List<BrandListBean> list) {
        this.list = list;
    }

    public static class BrandListBean {
        private String id;
        private String name;
        private String initial;

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

        public String getInitial() {
            return initial;
        }

        public void setInitial(String initial) {
            this.initial = initial;
        }
    }
}
