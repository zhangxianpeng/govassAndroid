package com.lihang.selfmvvm.ui;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;
import com.lihang.selfmvvm.vo.res.PlainMsgResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class CommonApiViewModel extends BaseViewModel<RepositoryImpl> {
    public CommonApiViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Resource<ListBaseResVo<PlainMsgResVo>>> getPlainMsgList(int page) {
        return getRepository().getPlainMsgList(page);
    }

    public LiveData<Resource<PlainMsgResVo>> getPlainMsgDetailInfo(int plainMsgId) {
        return getRepository().getPlainMsgDetail(plainMsgId);
    }
}