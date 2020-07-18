package com.lihang.selfmvvm.ui.senddocument;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;

import androidx.annotation.NonNull;

public class SendDocumentViewModel extends BaseViewModel<RepositoryImpl> {
    public SendDocumentViewModel(@NonNull Application application) {
        super(application);
    }

    //获取已发公文列表
    public void getMyDeclareList() {

    }
}

