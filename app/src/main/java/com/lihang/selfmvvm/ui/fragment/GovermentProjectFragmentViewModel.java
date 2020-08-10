/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: GovermentProjectFragmentViewModel
 * Author: zhang
 * Date: 2020/7/18 1:00
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
 * @ClassName: GovermentProjectFragmentViewModel
 * @Description: java类作用描述 政府端
 * @Author: zhang
 * @Date: 2020/7/18 1:00
 */
public class GovermentProjectFragmentViewModel extends BaseViewModel<RepositoryImpl> {
    public GovermentProjectFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    //getProjectList
    public void getProjectList() {

    }
}
