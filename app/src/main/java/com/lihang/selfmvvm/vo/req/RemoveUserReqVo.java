package com.lihang.selfmvvm.vo.req;

import java.util.List;

public class RemoveUserReqVo {
    private int groupId;
    private List<Integer> userIdList;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<Integer> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<Integer> userIdList) {
        this.userIdList = userIdList;
    }
}
