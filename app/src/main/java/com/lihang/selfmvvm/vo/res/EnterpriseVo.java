package com.lihang.selfmvvm.vo.res;

import java.io.Serializable;

public class EnterpriseVo implements Serializable {
//     "id": 1,
//             "enterpriseName": "广西震铄木业有限公司",
//             "enterpriseCode": "9145012679130400XL",
//             "legalRepresentative": "何晓宁",
//             "businessType": "有限责任公司",
//             "businessScope": "木片、原木、单板、细木工板、胶合板、集成材、木装饰线条加工销售",
//             "registeredCapital": 6700.00,
//             "setUpDate": "2006-07-12 00:00:00",
//             "businessTerm": "长期",
//             "address": "广西南宁宾阳县芦圩镇新模村委201号",
//             "businessLicenseImg": "c38219b6-b922-40da-a5e0-5035e374f9fb.jpg",
//             "authStatus": 1,
//             "deleted": 0,
//             "updateUserId": 1,
//             "lastUpdateTime": "2020-07-01 01:02:48",
//             "createUserId": 1,
//             "createTime": "2020-07-01 00:00:06",
//             "labelIdList": null

    private int id;
    private int questionnaireRecordId;
    private String enterpriseName;
    private String enterpriseCode;
    private String legalRepresentative;
    private String businessType;
    private String businessScope;
    private String registeredCapital;
    private String setUpDate;
    private String businessTerm;
    private String address;
    private String businessLicenseImg;
    private int authStatus;
    private int deleted;
    private int updateUserId;
    private String lastUpdateTime;
    private int createUserId;
    private String createTime;
    private String labelIdList;

    public int getQuestionnaireRecordId() {
        return questionnaireRecordId;
    }

    public void setQuestionnaireRecordId(int questionnaireRecordId) {
        this.questionnaireRecordId = questionnaireRecordId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getSetUpDate() {
        return setUpDate;
    }

    public void setSetUpDate(String setUpDate) {
        this.setUpDate = setUpDate;
    }

    public String getBusinessTerm() {
        return businessTerm;
    }

    public void setBusinessTerm(String businessTerm) {
        this.businessTerm = businessTerm;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessLicenseImg() {
        return businessLicenseImg;
    }

    public void setBusinessLicenseImg(String businessLicenseImg) {
        this.businessLicenseImg = businessLicenseImg;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
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

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLabelIdList() {
        return labelIdList;
    }

    public void setLabelIdList(String labelIdList) {
        this.labelIdList = labelIdList;
    }
}
