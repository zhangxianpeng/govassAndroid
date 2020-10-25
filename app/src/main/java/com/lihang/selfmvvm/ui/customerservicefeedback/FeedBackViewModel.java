package com.lihang.selfmvvm.ui.customerservicefeedback;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;

import androidx.annotation.NonNull;

/**
 * created by zhangxianpeng
 */
public class FeedBackViewModel extends BaseViewModel<RepositoryImpl> {
    public FeedBackViewModel(@NonNull Application application) {
        super(application);
    }
}
