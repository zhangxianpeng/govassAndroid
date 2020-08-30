package com.lihang.selfmvvm.ui.myenterprises;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.EnterpriseVo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class MyEnterprisesListViewModel extends BaseViewModel<RepositoryImpl> {
    public MyEnterprisesListViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 我的企业列表
     *
     * @param page
     * @return
     */
    public LiveData<Resource<ListBaseResVo<EnterpriseVo>>> getEnterpriseList(int page) {
        return getRepository().getEnterpriseList(page);
    }
}
