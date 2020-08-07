package com.lihang.selfmvvm.common;

import java.util.HashMap;

public class HEADS {
    /**
     * 添加头部信息
     */
    public static HashMap<String, String> login(String token) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        return map;
    }
}
