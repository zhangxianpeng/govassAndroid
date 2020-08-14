package com.lihang.selfmvvm.ui.officialdoc;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.OfficialDocResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class OfficialDocListViewModel extends BaseViewModel<RepositoryImpl> {
    public OfficialDocListViewModel(@NonNull Application application) {
        super(application);
    }


    /**
     * 获取 我的公文 列表
     *
     * @return
     */
    public LiveData<Resource<ListBaseResVo<OfficialDocResVo>>> getOfficalDoc() {
        return getRepository().getOfficalDoc();
    }
}