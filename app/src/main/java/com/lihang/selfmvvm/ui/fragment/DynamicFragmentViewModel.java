package com.lihang.selfmvvm.ui.fragment;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;

import androidx.annotation.NonNull;

/**
 * created by zhangxianpeng on 20201017
 * 千企动态
 */
public class DynamicFragmentViewModel extends BaseViewModel<RepositoryImpl> {
    public DynamicFragmentViewModel(@NonNull Application application) {
        super(application);
    }
}
