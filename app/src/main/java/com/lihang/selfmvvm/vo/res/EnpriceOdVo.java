package com.lihang.selfmvvm.vo.res;

import java.io.Serializable;

/**
 * 企业公告返回VO
 */
public class EnpriceOdVo implements Serializable {

//    id	:	11
//    enterpriseId	:	1
//    enterpriseName	:	广西震铄木业有限公司
//    title	:	信心
//    contentType	:	0
//    content	:	桂林米粉修改2失败修改重新提交审核
//    status	:	1
//    auditorId	:	1
//    auditOpinion	:	尺度太大
//    deleted	:	0
//    updateUserId	:	1
//    lastUpdateTime	:	2020-09-06 23:26:38
//    createUserId	:	11
//    createTime	:	2020-09-06 23:11:06


    private int id;
    private int enterpriseId;
    private String enterpriseName;
    private String title;
    private int contentType;
    private String content;
    private int status;
    private int auditorId;
    private int deleted;
    private int updateUserId;
    private int createUserId;
    private String auditOpinion;
    private String lastUpdateTime;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
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
}
