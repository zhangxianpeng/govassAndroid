package com.lihang.selfmvvm.bean.basebean;

import java.io.Serializable;

/**
 * 这个类是泛型类，可根据后端的返回字段修改
 */
public class ResponModel<T> implements Serializable {
    public static final int RESULT_SUCCESS = 0;

    private T data;
    private int code;
    private String msg;


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess(){
        return RESULT_SUCCESS == code;
    }
}
