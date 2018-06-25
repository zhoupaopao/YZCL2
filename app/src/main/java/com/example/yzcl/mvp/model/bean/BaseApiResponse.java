package com.example.yzcl.mvp.model.bean;

/**
 * Created by Lenovo on 2017/11/29.
 */

public class BaseApiResponse {

    //每个接口都会返回的参数
    private int Result;
    private String ErrorMsg="";

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

}
