package com.lihang.selfmvvm.vo.res;

import java.util.List;

public class ProjectResVo {
    //    address: "广西壮族自治区南宁市高新区中关村1"
//    amount: 5600
//    auditOpinion: "通过"
//    contact: "13687895989"
//    createTime: "2020-07-20 10:30:32"
//    createUserId: 16
//    deleted: 0
//    description: "项目描述1"
//    enterpriseId: 6
//    enterpriseName: "广西天正钢结构有限公司"
//    id: 4
//    lastUpdateTime: "2020-08-14 11:33:23"
//    linkman: "张三1"
//    name: "测试项目1"
//    projectAttachmentEntityList: null
//    status: 0
//    type: "互联网项目1"
//    updateUserId: 1
    private int amount;
    private int createUserId;
    private int deleted;
    private int enterpriseId;
    private int id;
    private int status;
    private int updateUserId;
    private String address;
    private String auditOpinion;
    private String contact;
    private String createTime;
    private String description;
    private String enterpriseName;
    private String lastUpdateTime;
    private String linkman;
    private String name;
    private List<AttachmentResVo> projectAttachmentEntityList;
    private String type;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(int updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AttachmentResVo> getProjectAttachmentEntityList() {
        return projectAttachmentEntityList;
    }

    public void setProjectAttachmentEntityList(List<AttachmentResVo> projectAttachmentEntityList) {
        this.projectAttachmentEntityList = projectAttachmentEntityList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
