package com.lihang.selfmvvm.vo.res;

import java.util.List;

/**
 * 分组详情
 */
public class GroupDetailsResVo {

    //   "id": 1,
    //            "name": "默认分组～",
    //            "type": 0,
    //            "remark": "mmmmmm",
    //            "updateUserId": 1,
    //            "lastUpdateTime": "2020-07-14 23:43:47",
    //            "createUserId": 1,
    //            "createTime": "2020-07-14 23:43:13",
    //            "userList": null

    private int id;
    private String name;
    private int type;
    private String remark;
    private int updateUserId;
    private String lastUpdateTime;
    private int createUserId;
    private String createTime;
    private List<MemberDetailResVo> userList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public List<MemberDetailResVo> getUserList() {
        return userList;
    }

    public void setUserList(List<MemberDetailResVo> userList) {
        this.userList = userList;
    }
}
