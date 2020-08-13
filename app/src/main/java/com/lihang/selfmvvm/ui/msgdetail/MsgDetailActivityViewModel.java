package com.lihang.selfmvvm.ui.msgdetail;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * 消息详情
 */
public class MsgDetailActivityViewModel extends BaseViewModel<RepositoryImpl> {
    public MsgDetailActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Resource<MsgMeResVo>> getMsgDetail(int id) {
        return getRepository().getMsgDetail(id);
    }

    public LiveData<Resource<String>> transferReadFlag(int id) {
        return getRepository().transferReadFlag(id);
    }
}