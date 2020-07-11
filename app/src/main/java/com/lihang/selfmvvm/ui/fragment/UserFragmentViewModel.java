/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: UserFragmentViewModel
 * Author: zhang
 * Date: 2020/7/11 17:20
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
 * @ClassName: UserFragmentViewModel
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/11 17:20
 */
public class UserFragmentViewModel extends BaseViewModel<RepositoryImpl> {
    public UserFragmentViewModel(@NonNull Application application) {
        super(application);
    }


    //getUserInfo
    public void getUserInfo() {

    }
}
