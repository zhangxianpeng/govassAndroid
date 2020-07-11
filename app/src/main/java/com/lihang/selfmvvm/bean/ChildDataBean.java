/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: ChildDataBean
 * Author: zhang
 * Date: 2020/7/11 17:57
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.bean;

import java.io.Serializable;

/**
 * @ClassName: ChildDataBean
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/11 17:57
 */
public class ChildDataBean implements Serializable {
    private String headUrl;
    private String nickName;

    public ChildDataBean(String headUrl, String nickName) {
        this.headUrl = headUrl;
        this.nickName = nickName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
