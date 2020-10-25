package com.lihang.selfmvvm.ui.fragment;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.OfficialDocResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * created by zhangxianpeng
 * 政策文件库
 */
public class PolicyDocumentLibraryViewModel extends BaseViewModel<RepositoryImpl> {
    public PolicyDocumentLibraryViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Resource<ListBaseResVo<OfficialDocResVo>>> getPolicyList(int page) {
        return getRepository().getPolicyList(page);
    }
}
