package com.lihang.selfmvvm.ui.mailist;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.req.PlainMsgReqVo;
import com.lihang.selfmvvm.vo.req.RemoveUserReqVo;
import com.lihang.selfmvvm.vo.res.GroupResVo;
import com.lihang.selfmvvm.vo.res.MemberDetailResVo;
import com.lihang.selfmvvm.vo.res.UploadAttachmentResVo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import okhttp3.MultipartBody;

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

    /**
     * 获取分组
     *
     * @param type
     * @return
     */
    public LiveData<Resource<GroupResVo>> getGroupList(int type) {
        return getRepository().getGuoupList(type);
    }

    /**
     * 多文件上传
     *
     * @param parts
     * @return
     */
    public LiveData<Resource<List<UploadAttachmentResVo>>> uploadMultyFile(List<MultipartBody.Part> parts) {
        return getRepository().uploadMultyFile(parts);
    }


    /**
     * 发布群组消息
     *
     * @param plainMsgReqVo
     * @return
     */
    public LiveData<Resource<String>> saveGroupPlainMsg(PlainMsgReqVo plainMsgReqVo) {
        return getRepository().savePlainMsg(plainMsgReqVo);
    }


    /**
     * 全部政府用户
     * @return
     */
    public LiveData<Resource<List<MemberDetailResVo>>> getAllGovernment() {
        return getRepository().getAllGovernment();
    }

    /**
     * 获取全部企业用户
     *
     * @return
     */
    public LiveData<Resource<List<MemberDetailResVo>>> getAllEnterprise() {
        return getRepository().getAllEnterprise();
    }
}
