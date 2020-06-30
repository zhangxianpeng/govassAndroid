package com.lihang.selfmvvm.base;

import android.app.Application;

import androidx.annotation.NonNull;

/**
 * 不需要用ViewModel的,请用此类代替
 */
public class NormalViewModel extends BaseViewModel {
    public NormalViewModel(@NonNull Application application) {
        super(application);
    }
}
