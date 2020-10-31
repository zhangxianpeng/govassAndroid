package com.lihang.selfmvvm.ui.customerservicefeedback;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.model.CommunicateMsgVo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * created by zhangxianpeng on 2020/10/26
 * 反馈历史
 */
public class FeedBackListViewModel extends BaseViewModel<RepositoryImpl> {
    public FeedBackListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Resource<ListBaseResVo<CommunicateMsgVo>>> getFeedBackList(int page) {
        return getRepository().getFeedBackList(page);
    }
}
