package com.lihang.selfmvvm.ui.register;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;

import androidx.annotation.NonNull;

public class RegisterStepTwoViewModel extends BaseViewModel<RepositoryImpl> {
    public RegisterStepTwoViewModel(@NonNull Application application) {
        super(application);
    }

    //提交注册请求
    public void register() {

    }
}
