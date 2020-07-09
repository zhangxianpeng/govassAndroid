/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: ProjectBean
 * Author: zhang
 * Date: 2020/7/9 21:02
 * Description: 企业项目 item  对应实体
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.bean;

import java.io.Serializable;

/**
 * @ClassName: ProjectBean
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/9 21:02
 */
public class ProjectBean implements Serializable {

    private int imagePath;
    private String projectTitle;
    private String projectTime;

    public int getImagePath() {
        return imagePath;
    }

    public void setImagePath(int imagePath) {
        this.imagePath = imagePath;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectTime() {
        return projectTime;
    }

    public void setProjectTime(String projectTime) {
        this.projectTime = projectTime;
    }
}
