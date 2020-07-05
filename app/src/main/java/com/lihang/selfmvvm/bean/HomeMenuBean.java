/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: HomeMenuBean
 * Author: zhang
 * Date: 2020/7/4 13:40
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.bean;

import java.io.Serializable;

/**
 * @ClassName: HomeMenuBean
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/4 13:40
 */
public class HomeMenuBean implements Serializable {
    private int imageUrl;
    private String title;

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
