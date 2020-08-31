package com.lihang.selfmvvm.ui.project;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.ProjectResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class ProjectActivityViewModel extends BaseViewModel<RepositoryImpl> {
    public ProjectActivityViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 已审核项目/待审核
     * 政府
     *
     * @return
     */
    public LiveData<Resource<ListBaseResVo<ProjectResVo>>> getPendingProject(int page, boolean isPending) {
        return isPending ? getRepository().getPendingProject(page) : getRepository().getWaitPendingProject(page);
    }
}
