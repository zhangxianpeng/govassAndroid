package com.lihang.selfmvvm.ui.communicate;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;

import androidx.annotation.NonNull;

public class CommunicateViewModel extends BaseViewModel<RepositoryImpl> {
    public CommunicateViewModel(@NonNull Application application) {
        super(application);
    }

    //getUserMsgList
    public void getUserMsgList() {

    }
}