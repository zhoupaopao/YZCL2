package com.example.yzcl.mvp.model.biz;


import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;

public interface IUserBiz {

   public void login(RequestParams params, JsonHttpRequestCallback jsonHttpRequestCallback);
}
