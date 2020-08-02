package com.lihang.selfmvvm.vo.res;

public class CsDataInfoVo {
    //      "id": 7,
    //        "phone": "13689898989",
    //        "email": "2323@qq.com",
    //        "address": "232",
    //        "remark": "232",
    //        "defaultFlag": 1,
    //        "updateUserId": null,
    //        "lastUpdateTime": null,
    //        "createUserId": 1,
    //        "createTime": "2020-07-28 17:09:21"
    private int id;
    private String phone;
    private String email;
    private String address;
    private String remark;
    private int defaultFlag;
    private int updateUserId;
    private String lastUpdateTime;
    private int createUserId;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(int defaultFlag) {
        this.defaultFlag = defaultFlag;
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
}
