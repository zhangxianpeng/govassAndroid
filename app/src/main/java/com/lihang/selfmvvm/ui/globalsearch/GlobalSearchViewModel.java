package com.lihang.selfmvvm.ui.globalsearch;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.SearchValueResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class GlobalSearchViewModel extends BaseViewModel<RepositoryImpl> {
    public GlobalSearchViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 获取搜索结果
     *
     * @param page
     * @param searchValue
     * @return
     */
    public LiveData<Resource<ListBaseResVo<SearchValueResVo>>> getSearchValue(int page, int limit, String searchValue) {
        return getRepository().getSearchValue(page, limit,searchValue);
    }
}
