package com.lihang.selfmvvm.ui.fragment;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.bean.basebean.ResponModel;
import com.lihang.selfmvvm.vo.req.IdReqVo;
import com.lihang.selfmvvm.vo.res.DynamicVo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * created by zhangxianpeng on 20201017
 * 千企动态
 */
public class DynamicFragmentViewModel extends BaseViewModel<RepositoryImpl> {
    public DynamicFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Resource<ListBaseResVo<DynamicVo>>> getDynamicList(int page, int contentType) {
        return getRepository().getDynamicList(page, contentType);
    }

    public LiveData<Resource<ResponModel<String>>> likeDynamic(IdReqVo idReqVo) {
        return getRepository().likeDynamic(idReqVo);
    }
}
