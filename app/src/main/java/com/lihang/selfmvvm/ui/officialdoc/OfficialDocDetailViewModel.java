package com.lihang.selfmvvm.ui.officialdoc;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.OfficialDocResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class OfficialDocDetailViewModel extends BaseViewModel<RepositoryImpl> {
    public OfficialDocDetailViewModel(@NonNull Application application) {
        super(application);
    }


    /**
     * 公文详情
     *
     * @return
     */
    public LiveData<Resource<OfficialDocResVo>> getOfficalDocDetail(int id) {
        return getRepository().getOfficalDocDetail(id);
    }
}
