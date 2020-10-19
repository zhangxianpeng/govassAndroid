package com.lihang.selfmvvm.ui.senddynamic;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;

import androidx.annotation.NonNull;

/**
 * created by zhangxianpeng
 * 发布动态
 */
public class SendDynamicViewModel extends BaseViewModel<RepositoryImpl> {

    public SendDynamicViewModel(@NonNull Application application) {
        super(application);
    }


}
