package com.lihang.selfmvvm.ui.myenterprises;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.UserInfoVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class EnterprisesDetailViewModel extends BaseViewModel<RepositoryImpl> {
    public EnterprisesDetailViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 获取登录信息
     *
     * @param token
     * @return
     */
    public LiveData<Resource<UserInfoVo>> getUserInfo() {
        return getRepository().getUserInfo();
    }
}