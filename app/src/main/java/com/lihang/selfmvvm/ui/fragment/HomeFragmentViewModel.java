/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: HomeFragmentViewModel
 * Author: zhang
 * Date: 2020/7/4 13:23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.ui.fragment;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.ImageDataInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * @ClassName: HomeFragmentViewModel
 * @Description: HomeFragment 中的数据操作
 * 需要加上 public 权限修饰符
 * @Author: zhang
 * @Date: 2020/7/4 13:23
 */
public class HomeFragmentViewModel extends BaseViewModel<RepositoryImpl> {
    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 获取公告轮播
     * @param token
     * @return
     */
    public LiveData<Resource<List<ImageDataInfo>>> getGovassBannerList(String token) {
        return getRepository().getGovassBannerList(token);
    }
}
