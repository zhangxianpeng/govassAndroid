package com.lihang.selfmvvm.ui.declaredetail;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;

import androidx.annotation.NonNull;

public class DeclareDetailViewModel extends BaseViewModel<RepositoryImpl> {
    public DeclareDetailViewModel(@NonNull Application application) {
        super(application);
    }


    //获取项目申报详情
    public void getDeclareDetail() {

    }
}