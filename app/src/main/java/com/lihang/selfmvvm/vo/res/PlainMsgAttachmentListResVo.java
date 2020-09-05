package com.lihang.selfmvvm.vo.res;

import java.io.Serializable;

public class PlainMsgAttachmentListResVo implements Serializable {
    private int id;
    private int plainMsgId;
    private String name;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlainMsgId() {
        return plainMsgId;
    }

    public void setPlainMsgId(int plainMsgId) {
        this.plainMsgId = plainMsgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
