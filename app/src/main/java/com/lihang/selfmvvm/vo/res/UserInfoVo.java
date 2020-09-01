package com.lihang.selfmvvm.vo.res;

public class UserInfoVo {

    //"userId": 11,
    //        "username": "李四",
    //        "realname": "李四",
    //        "headUrl": "c38219b6-b922-40da-a5e0-5035e374f9fb.jpg",
    //        "password": null,
    //        "salt": null,
    //        "userType": 1,
    //        "email": "dssd@163.com",
    //        "mobile": "13644444444",
    //        "status": 1,
    //        "deleted": 0,
    //        "updateUserId": 1,
    //        "createUserId": 1,
    //        "lastUpdateTime": "2020-07-23 22:49:48",
    //        "createTime": "2020-07-02 01:17:07",
    //        "enterpriseId": 1,
    //        "identityCard": "121212121212121212",
    //        "enterpriseUserType": 1,
    //        "authStatus": 1

    /**
     * 用户id
     */
    private int userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 头像
     */
    private String headUrl;
    /**
     * 密码
     */
    private String password;
    private String salt;
    /**
     * 用户类型   0  政府   1 企业
     */
    private int userType;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 账户状态  0 禁用  1 正常
     */
    private int status;
    /**
     * 是否删除
     */
    private int deleted;
    /**
     * 更新id
     */
    private int updateUserId;
    /**
     * 创建id
     */
    private int createUserId;
    /**
     * 最新更新时间
     */
    private String lastUpdateTime;
    /**
     * 账户创建时间
     */
    private String createTime;
    /**
     * 企业id
     */
    private int enterpriseId;
    /**
     * 身份证
     */
    private String identityCard;
    /**
     * 企业类型
     */
    private int enterpriseUserType;
    /**
     * 授权状态  0 未认证  1 已认证  2 认证失败
     */
    private int authStatus;

    private EnterpriseVo enterpriseEntity;

    public EnterpriseVo getEnterpriseEntity() {
        return enterpriseEntity;
    }

    public void setEnterpriseEntity(EnterpriseVo enterpriseEntity) {
        this.enterpriseEntity = enterpriseEntity;
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

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public int getEnterpriseUserType() {
        return enterpriseUserType;
    }

    public void setEnterpriseUserType(int enterpriseUserType) {
        this.enterpriseUserType = enterpriseUserType;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }
}
