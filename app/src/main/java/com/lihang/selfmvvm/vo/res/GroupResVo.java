package com.lihang.selfmvvm.vo.res;

import java.util.List;

/**
 * 分组列表
 */
public class GroupResVo {
//    "totalCount": 2,
//            "pageSize": 10,
//            "totalPage": 1,
//            "currPage": 1,

    private int totalCount;
    private int pageSize;
    private int totalPage;
    private int currPage;
    private List<GroupDetailsResVo> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public List<GroupDetailsResVo> getList() {
        return list;
    }

    public void setList(List<GroupDetailsResVo> list) {
        this.list = list;
    }
}
