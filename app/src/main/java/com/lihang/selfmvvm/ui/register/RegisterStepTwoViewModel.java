package com.lihang.selfmvvm.ui.register;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.req.LoginReqVo;
import com.lihang.selfmvvm.vo.req.RegisterReqVo;
import com.lihang.selfmvvm.vo.res.LoginDataVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class RegisterStepTwoViewModel extends BaseViewModel<RepositoryImpl> {
    public RegisterStepTwoViewModel(@NonNull Application application) {
        super(application);
    }


    /**
     * 提交注册请求
     * @param registerReqVo
     * @return
     */
    public LiveData<Resource<String>> register(RegisterReqVo registerReqVo) {
        return getRepository().register(registerReqVo);
    }
}
