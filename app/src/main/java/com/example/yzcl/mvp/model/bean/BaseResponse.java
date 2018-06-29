package com.example.yzcl.mvp.model.bean;

/**
 * Created by Lenovo on 2017/11/29.
 */

public class BaseResponse {

    //每个接口都会返回的参数
    private boolean success;
    private String code;
    private String message;

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
}
