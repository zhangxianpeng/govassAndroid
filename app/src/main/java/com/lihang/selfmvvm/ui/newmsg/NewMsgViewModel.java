package com.lihang.selfmvvm.ui.newmsg;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class NewMsgViewModel extends BaseViewModel<RepositoryImpl> {
    public NewMsgViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<Resource<ListBaseResVo<MsgMeResVo>>> getMsgMeList() {
        return getRepository().getMsgMeList();
    }
}
