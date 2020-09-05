package com.lihang.selfmvvm.vo.res;

import java.io.Serializable;

public class MsgMeResVo implements Serializable {
//    "id": 20,
//            "userId": 1,
//            "username": "admin",
//            "title": "3434",
//            "content": "34343",
//            "type": 1,
//            "primaryId": 19,
//            "readFlag": 1,
//            "readTime": "2020-08-09 22:27:11",
//            "updateUserId": null,
//            "lastUpdateTime": "2020-08-09 22:27:10",
//            "createUserId": 1,
//            "createUser": "admin",
//            "createTime": "2020-08-09 20:25:18"

    private int id;
    private int userId;
    private String username;
    private String title;
    private String content;
    private String readTime;
    private String lastUpdateTime;
    private String createUser;
    private String createTime;
    private int type;
    private int primaryId;
    private int readFlag;
    private int updateUserId;
    private int createUserId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }

    public int getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(int readFlag) {
        this.readFlag = readFlag;
    }

    public int getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(int updateUserId) {
        this.updateUserId = updateUserId;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }
}
