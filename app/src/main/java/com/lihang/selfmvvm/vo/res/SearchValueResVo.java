package com.lihang.selfmvvm.vo.res;

import java.io.Serializable;

/**
 * created by zhangxianpeng
 * 搜索结果
 */
public class SearchValueResVo implements Serializable {
    private String id;
    private String title;
    private int primaryId;
    private String content;
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String
                              id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
