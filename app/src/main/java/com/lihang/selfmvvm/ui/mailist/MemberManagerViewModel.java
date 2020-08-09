package com.lihang.selfmvvm.ui.mailist;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.GroupDetailsResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class MemberManagerViewModel extends BaseViewModel<RepositoryImpl> {
    public MemberManagerViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 根据Id获取分组详情
     * @param groupId
     * @return
     */
    public LiveData<Resource<GroupDetailsResVo>> getGroupAllUser(int groupId) {
        return getRepository().getGroupAllUser(groupId);
    }
}
