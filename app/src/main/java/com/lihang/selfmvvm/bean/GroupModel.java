/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: GroupDataBean
 * Author: zhang
 * Date: 2020/7/11 17:55
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.bean;

/**
 * @ClassName: GroupDataBean
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/11 17:55
 */
public class GroupModel {
    private String title;
    private String online;

    public GroupModel(String title, String online) {
        this.title = title;
        this.online = online;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }
}
