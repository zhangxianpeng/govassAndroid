package com.lihang.selfmvvm.vo.res;

public class VersionVo {
//    "id": 5,
//            "device": 0,
//            "appVersion": "1.0.4",
//            "forceFlag": 1,
//            "changeLog": "测试更新版本功能",
//            "url": "https://www.pgyer.com/0DXB",
//            "createUserId": 1,
//            "createUser": "admin",
//            "createTime": "2020-08-29 09:24:57"

    private int id;
    private int device;
    private int forceFlag;
    private int createUserId;
    private String appVersion;
    private String changeLog;
    private String url;
    private String createUser;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public int getForceFlag() {
        return forceFlag;
    }

    public void setForceFlag(int forceFlag) {
        this.forceFlag = forceFlag;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
