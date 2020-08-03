/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: BottomNavigationViewModel
 * Author: zhang
 * Date: 2020/7/11 9:21
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.ui.main;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.NormalViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.ImageDataInfo;
import com.lihang.selfmvvm.vo.res.UserInfoVo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * @ClassName: BottomNavigationViewModel
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/11 9:21
 */
public class BottomNavigationViewModel extends BaseViewModel<RepositoryImpl> {
    public BottomNavigationViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    public LiveData<Resource<UserInfoVo>> getUserInfo(String token) {
        return getRepository().getUserInfo(token);
    }
}
