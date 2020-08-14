package com.lihang.selfmvvm.ui.project;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.ProjectResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class ProjectActivityViewModel extends BaseViewModel<RepositoryImpl> {
    public ProjectActivityViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 待审核项目
     * 区别政府/企业
     *
     * @return
     */
    public LiveData<Resource<ListBaseResVo<ProjectResVo>>> getWaitPendingProject() {
        return CheckPermissionUtils.getInstance().isGovernment() ? getRepository().getWaitPendingProject() : getRepository().getListMePending();
    }

    /**
     * 已审核项目
     * 区别政府/企业
     *
     * @return
     */
    public LiveData<Resource<ListBaseResVo<ProjectResVo>>> getPendingProject() {
        return CheckPermissionUtils.getInstance().isGovernment() ? getRepository().getPendingProject() : getRepository().getListMeHandled();
    }


}
