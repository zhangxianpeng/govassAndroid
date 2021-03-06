/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: ChildModel
 * Author: zhang
 * Date: 2020/7/11 17:57
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.bean;

/**
 * @ClassName: ChildModel
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/11 17:57
 */
public class ChildModel {
    private String headUrl;
    private String name;
    private String userId;
    private String phone;

    public ChildModel(String headUrl, String name,String userId,String phone) {
        this.headUrl = headUrl;
        this.name = name;
        this.userId = userId;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
