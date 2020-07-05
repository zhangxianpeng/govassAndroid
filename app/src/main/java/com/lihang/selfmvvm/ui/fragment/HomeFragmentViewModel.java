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

import androidx.annotation.NonNull;

/**
 * @ClassName: HomeFragmentViewModel
 * @Description:  HomeFragment 中的数据操作
 * 需要加上 public 权限修饰符
 * @Author: zhang
 * @Date: 2020/7/4 13:23
 */
public class HomeFragmentViewModel extends BaseViewModel<RepositoryImpl> {
    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    //getBanner  获取公告轮播
    //getProjectList 获取项目列表
    //getHomeMenu  获取菜单按钮
}
