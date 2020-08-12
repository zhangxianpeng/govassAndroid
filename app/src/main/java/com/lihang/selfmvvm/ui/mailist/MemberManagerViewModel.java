package com.lihang.selfmvvm.ui.mailist;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.req.RemoveUserReqVo;
import com.lihang.selfmvvm.vo.res.MemberDetailResVo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class MemberManagerViewModel extends BaseViewModel<RepositoryImpl> {
    public MemberManagerViewModel(@NonNull Application application) {
        super(application);
    }


    /**
     * 根据分组id获取分组下所有成员信息（政府）
     *
     * @param groupId
     * @return
     */
    public LiveData<Resource<List<MemberDetailResVo>>> getGovernmentFromId(int groupId) {
        return getRepository().getGovernmentFromId(groupId);
    }

    /**
     * 根据分组id获取分组下所有成员信息（企业）
     *
     * @param groupId
     * @return
     */
    public LiveData<Resource<List<MemberDetailResVo>>> getEnterpriseFromId(int groupId) {
        return getRepository().getEnterpriseFromId(groupId);
    }

    /**
     * 从当前分组移除用户
     *
     * @param removeUserReqVo
     * @return
     */
    public LiveData<Resource<String>> removeUser(RemoveUserReqVo removeUserReqVo) {
        return getRepository().removeUser(removeUserReqVo);
    }

}
