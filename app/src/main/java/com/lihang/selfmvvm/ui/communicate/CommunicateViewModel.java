package com.lihang.selfmvvm.ui.communicate;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;
import com.lihang.selfmvvm.vo.res.PlainMsgResVo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class CommunicateViewModel extends BaseViewModel<RepositoryImpl> {
    public CommunicateViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<Resource<ListBaseResVo<PlainMsgResVo>>> getPlainMsgList() {
        return getRepository().getPlainMsgList(0);
    }

    public LiveData<Resource<String>> deletePlainMsgList(List<Integer> idList) {
        return getRepository().deletePlainMsgList(idList);
    }
}