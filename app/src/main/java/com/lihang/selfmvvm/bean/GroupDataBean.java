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

import java.io.Serializable;

/**
 * @ClassName: GroupDataBean
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/11 17:55
 */
public class GroupDataBean implements Serializable {
    private String name;
    private String num;

    public GroupDataBean(String name) {
        this.name = name;
    }

    public GroupDataBean(String name, String num) {
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
