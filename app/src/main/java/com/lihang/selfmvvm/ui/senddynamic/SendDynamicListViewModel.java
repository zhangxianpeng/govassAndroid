package com.lihang.selfmvvm.ui.senddynamic;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.DynamicVo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * created by zhangxianpeng on 2020/10/26
 */
public class SendDynamicListViewModel extends BaseViewModel<RepositoryImpl> {
    public SendDynamicListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Resource<ListBaseResVo<DynamicVo>>> getDynamicHistoryList(int page) {
        return getRepository().getDynamicHistoryList(page);
    }
}
