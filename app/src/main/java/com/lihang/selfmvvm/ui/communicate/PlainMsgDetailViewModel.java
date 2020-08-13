package com.lihang.selfmvvm.ui.communicate;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.PlainMsgResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class PlainMsgDetailViewModel extends BaseViewModel<RepositoryImpl> {
    public PlainMsgDetailViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<Resource<PlainMsgResVo>> getPlainMsgDetail(int id) {
        return getRepository().getPlainMsgDetail(id);
    }
}