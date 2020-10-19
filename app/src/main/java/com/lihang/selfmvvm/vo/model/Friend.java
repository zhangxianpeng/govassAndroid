package com.lihang.selfmvvm.vo.model;

import android.widget.ImageView;

import java.util.List;

/**
 * created by zhangxianpeng on 20201017
 */
public class Friend {
    private int headIv;
    private String name;
    private String createTime;
    private String content;
    private List<Integer> imageList;
    private int likeCount;

    public int getHeadIv() {
        return headIv;
    }

    public void setHeadIv(int headIv) {
        this.headIv = headIv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Integer> getImageList() {
        return imageList;
    }

    public void setImageList(List<Integer> imageList) {
        this.imageList = imageList;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
