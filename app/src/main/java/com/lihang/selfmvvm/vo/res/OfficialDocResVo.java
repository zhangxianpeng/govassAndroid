package com.lihang.selfmvvm.vo.res;

public class OfficialDocResVo {
//     "id": 14,
//             "title": "GGGGGGGGGG",
//             "content": "<p>3434343</p>",
//             "publishType": 1,
//             "status": 0,
//             "auditorId": null,
//             "auditor": null,
//             "auditOpinion": null,
//             "deleted": 0,
//             "updateUserId": null,
//             "lastUpdateTime": null,
//             "createUserId": 1,
//             "createUser": "admin",
//             "createTime": "2020-08-05 02:14:39",
//             "attachmentList": null,
//             "officialDocumentLabelEntityList": null,
//             "officialDocumentEnterpriseEntityList": null

    private int id;
    private int publishType;
    private int status;
    private int auditorId;
    private int deleted;
    private int updateUserId;
    private String title;
    private String content;
    private String auditor;
    private String auditOpinion;
    private String lastUpdateTime;
    private String createUser;
    private String createTime;
    private String attachmentList;
    private String officialDocumentLabelEntityList;
    private String officialDocumentEnterpriseEntityList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPublishType() {
        return publishType;
    }

    public void setPublishType(int publishType) {
        this.publishType = publishType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(int auditorId) {
        this.auditorId = auditorId;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
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

    public String getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(String attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getOfficialDocumentLabelEntityList() {
        return officialDocumentLabelEntityList;
    }

    public void setOfficialDocumentLabelEntityList(String officialDocumentLabelEntityList) {
        this.officialDocumentLabelEntityList = officialDocumentLabelEntityList;
    }

    public String getOfficialDocumentEnterpriseEntityList() {
        return officialDocumentEnterpriseEntityList;
    }

    public void setOfficialDocumentEnterpriseEntityList(String officialDocumentEnterpriseEntityList) {
        this.officialDocumentEnterpriseEntityList = officialDocumentEnterpriseEntityList;
    }
}
