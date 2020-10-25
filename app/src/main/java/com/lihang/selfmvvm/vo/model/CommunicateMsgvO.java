package com.lihang.selfmvvm.vo.model;

/**
 * created by zhangxianpeng
 */
public class CommunicateMsgvO {

    //接受
    public static final int TYPE_RECEIVED = 0;
    //发送
    public static final int TYPE_SENT = 1;
    private String headImg;
    private String content;
    private int type;

    public CommunicateMsgvO(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
