package com.lihang.selfmvvm.ui.customserver;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.CsDataInfoVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class CustomServerViewModel extends BaseViewModel<RepositoryImpl> {
    public CustomServerViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<Resource<CsDataInfoVo>> getCustomerService(String token) {
        return getRepository().getCustomerService(token);
    }
}
