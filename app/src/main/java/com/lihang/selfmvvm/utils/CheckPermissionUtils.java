/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: CheckPermissionUtils
 * Author: zhang
 * Date: 2020/7/7 21:18
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.utils;

/**
 * @ClassName: CheckPermissionUtils
 * @Description: 检测登录角色
 * @Author: zhang
 * @Date: 2020/7/7 21:18
 */
public class CheckPermissionUtils {
    private volatile static CheckPermissionUtils instacne;
    public boolean isGovernment;

    private CheckPermissionUtils() {
    }

    public static CheckPermissionUtils getInstance() {
        if (instacne == null) {
            synchronized (CheckPermissionUtils.class) {
                if (instacne == null) {
                    instacne = new CheckPermissionUtils();
                }
            }
        }
        return instacne;
    }

    public void setGovernment(boolean government) {
        isGovernment = government;
    }

    public boolean isGovernment() {
        return isGovernment;
    }
}
