package com.lihang.selfmvvm.vo.res;

/**
 * 分组成员实体
 */
public class MemberDetailResVo {
//     "userId": 1,
//             "username": "admin",
//             "realname": "admin",
//             "headUrl": "9954a728-56bd-4ff9-a981-2b7e0de710c0.jpg",
//             "password": "9ec9750e709431dad22365cabc5c625482e574c74adaebba7dd02f1129e4ce1d",
//             "salt": "YzcmCZNvbXocrsz9dm8e",
//             "userType": 0,
//             "email": "root@renren.io",
//             "mobile": "13612345678",
//             "status": 1,
//             "deleted": 0,
//             "roleIdList": null,
//             "groupIdList": null,
//             "updateUserId": 1,
//             "createUserId": 1,
//             "lastUpdateTime": "2020-06-29 16:25:21",
//             "createTime": "2016-11-11 11:11:11"

    private int userId;
    private String username;
    private String realname;
    private String headUrl;
    private String password;
    private String salt;
    private int userType;
    private String email;
    private String mobile;
    private int status;
    private int deleted;
    private String roleIdList;
    private String groupIdList;
    private int updateUserId;
    private int createUserId;
    private String lastUpdateTime;
    private String createTime;

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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(String roleIdList) {
        this.roleIdList = roleIdList;
    }

    public String getGroupIdList() {
        return groupIdList;
    }

    public void setGroupIdList(String groupIdList) {
        this.groupIdList = groupIdList;
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
