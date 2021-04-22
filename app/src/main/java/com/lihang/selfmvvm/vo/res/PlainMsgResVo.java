package com.lihang.selfmvvm.vo.res;

import java.util.List;

public class PlainMsgResVo {
//    "id": 4,
//            "title": "测试企业用户",
//            "content": "颠三倒四多",
//            "userType": 1,
//            "receiverType": 0,
//            "deleted": 0,
//            "updateUserId": null,
//            "lastUpdateTime": "2020-08-09 19:58:03",
//            "createUserId": 1,
//            "createUser": "admin",
//            "createTime": "2020-08-09 19:57:38",
//            "plainMsgGroupEntityList": null


    private int id;
    private int userType;
    private int receiverType;
    private int updateUserId;
    private int createUserId;
    private int deleted;
    private String title;
    private String content;
    private String lastUpdateTime;
    private String createTime;
    private String createUser;
    private String plainMsgGroupEntityList;
    private List<AttachmentResVo> attachmentList;

    public List<AttachmentResVo> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentResVo> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(int receiverType) {
        this.receiverType = receiverType;
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

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
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

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPlainMsgGroupEntityList() {
        return plainMsgGroupEntityList;
    }

    public void setPlainMsgGroupEntityList(String plainMsgGroupEntityList) {
        this.plainMsgGroupEntityList = plainMsgGroupEntityList;
    }
}
