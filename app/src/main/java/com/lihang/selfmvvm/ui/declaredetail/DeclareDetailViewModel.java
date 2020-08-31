package com.lihang.selfmvvm.ui.declaredetail;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.req.AuditReqVo;
import com.lihang.selfmvvm.vo.res.ProjectResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class DeclareDetailViewModel extends BaseViewModel<RepositoryImpl> {
    public DeclareDetailViewModel(@NonNull Application application) {
        super(application);
    }


    /**
     * 项目详情
     *
     * @param id
     * @return
     */
    public LiveData<Resource<ProjectResVo>> getProjectDetail(int id) {
        return getRepository().getProjectDetail(id);
    }

    /**
     * 通过
     *
     * @param auditReqVo
     * @return
     */
    public LiveData<Resource<String>> pass(AuditReqVo auditReqVo) {
        return getRepository().pass(auditReqVo);
    }

    /**
     * 不通过
     *
     * @param auditReqVo
     * @return
     */
    public LiveData<Resource<String>> noPass(AuditReqVo auditReqVo) {
        return getRepository().noPass(auditReqVo);
    }

}