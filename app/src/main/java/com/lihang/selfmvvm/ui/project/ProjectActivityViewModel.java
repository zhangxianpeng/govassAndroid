package com.lihang.selfmvvm.ui.project;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.OfficialDocResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class ProjectActivityViewModel extends BaseViewModel<RepositoryImpl> {
    public ProjectActivityViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 获取待审核列表
     *
     * @return
     */
    public LiveData<Resource<ListBaseResVo<OfficialDocResVo>>> getWaitPendingOfficalDoc() {
        return getRepository().getWaitPendingOfficalDoc();
    }

    /**
     * 获取已处理列表
     *
     * @return
     */
    public LiveData<Resource<ListBaseResVo<OfficialDocResVo>>> getOfficalDoc() {
        return getRepository().getOfficalDoc();
    }
}
