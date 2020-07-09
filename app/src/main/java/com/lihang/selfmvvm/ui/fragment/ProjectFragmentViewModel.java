/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: ProjectFragmentViewModel
 * Author: zhang
 * Date: 2020/7/9 20:57
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
 * @ClassName: ProjectFragmentViewModel
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/9 20:57
 */
public class ProjectFragmentViewModel extends BaseViewModel<RepositoryImpl> {
    public ProjectFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    //getProjectList
    public void getProjectList() {

    }
}
