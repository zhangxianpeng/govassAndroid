package com.lihang.selfmvvm.vo.res;

import java.io.Serializable;
import java.util.List;

public class NoticeResVo implements Serializable {
// "id": 1,
//         "title": "343434",
//         "imageUrl": "22a4d2d2-f1c5-4d7e-849e-d2dd9e4000a7.jpg",
//         "contentType": 0,
//         "content": "<p>43434343434454545344343<img src=\"http://localhost:8080/govass/file-server/91118c25-f5e6-4062-ac98-a0045ab3b303.jpg\" title=\"乔丹.jpg\" alt=\"乔丹.jpg\" style=\"width:100%\"/></p>",
//         "status": 0,
//         "auditOpinion": null,
//         "deleted": 0,
//         "updateUserId": null,
//         "lastUpdateTime": "2020-07-03 02:09:59",
//         "createUserId": 1,
//         "createTime": "2020-07-03 01:38:18",
//         "noticeAttachmentEntityList": null

    private int id;
    private int contentType;   //0 富文本  1 链接
    private int status;
    private int deleted;
    private int createUserId;
    private int updateUserId;

    private String title;
    private String imageUrl;
    private String content;
    private String auditOpinion;
    private String lastUpdateTime;
    private String createTime;
    private List<AttachmentResVo> noticeAttachmentEntityList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public int getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(int updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
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

    public List<AttachmentResVo> getNoticeAttachmentEntityList() {
        return noticeAttachmentEntityList;
    }

    public void setNoticeAttachmentEntityList(List<AttachmentResVo> noticeAttachmentEntityList) {
        this.noticeAttachmentEntityList = noticeAttachmentEntityList;
    }
}
