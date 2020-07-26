package com.lihang.selfmvvm.ui.newmsg;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;

import androidx.annotation.NonNull;

public class NewMsgViewModel extends BaseViewModel<RepositoryImpl> {
    public NewMsgViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 获取未读消息列表返回activity
     */
    public void getNewMsgList() {

    }
}
