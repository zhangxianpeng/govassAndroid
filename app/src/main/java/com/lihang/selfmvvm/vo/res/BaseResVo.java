package com.lihang.selfmvvm.vo.res;

import java.io.Serializable;

public class BaseResVo implements Serializable {
    private String msg;
    private String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
