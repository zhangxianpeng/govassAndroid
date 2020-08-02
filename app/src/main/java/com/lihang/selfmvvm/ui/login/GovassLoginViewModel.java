package com.lihang.selfmvvm.ui.login;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.req.LoginReqVo;
import com.lihang.selfmvvm.vo.res.LoginDataVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class GovassLoginViewModel extends BaseViewModel<RepositoryImpl> {
    public GovassLoginViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<Resource<LoginDataVo>> govassLogin(LoginReqVo loginReqVo) {
        return getRepository().govassLogin(loginReqVo);
    }
}