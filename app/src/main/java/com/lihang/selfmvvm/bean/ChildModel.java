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
    private String name;
    private String sig;

    public ChildModel(String name, String sig) {
        this.name = name;
        this.sig = sig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }
}
