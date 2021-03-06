/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: MsgFragmentViewModel
 * Author: zhang
 * Date: 2020/7/11 17:21
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.ui.fragment;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.req.AddGroupReqVo;
import com.lihang.selfmvvm.vo.req.PlainMsgReqVo;
import com.lihang.selfmvvm.vo.req.RemoveUserReqVo;
import com.lihang.selfmvvm.vo.res.GroupResVo;
import com.lihang.selfmvvm.vo.res.MemberDetailResVo;
import com.lihang.selfmvvm.vo.res.UploadAttachmentResVo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import okhttp3.MultipartBody;

/**
 * @ClassName: MsgFragmentViewModel
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/11 17:21
 */
public class MsgFragmentViewModel extends BaseViewModel<RepositoryImpl> {
    public MsgFragmentViewModel(@NonNull Application application) {
        super(application);
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
     * 获取全部政府用户
     *
     * @return
     */
    public LiveData<Resource<List<MemberDetailResVo>>> getAllGovernment() {
        return getRepository().getAllGovernment();
    }

    public LiveData<Resource<List<MemberDetailResVo>>> searchGovernmentUser(String username) {
        return getRepository().searchGovernmentUser(username);
    }

    public LiveData<Resource<List<MemberDetailResVo>>> searchEnterpriseUser(String username) {
        return getRepository().searchEnterpriseUser(username);
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
     * 获取全部企业用户
     *
     * @return
     */
    public LiveData<Resource<List<MemberDetailResVo>>> getAllEnterprise() {
        return getRepository().getAllEnterprise();
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
     * 查询分组名是否重复
     *
     * @param type
     * @param groupName
     * @return
     */
    public LiveData<Resource<String>> checkGroupNameRepeat(int type, String groupName) {
        return getRepository().checkGroupNameRepeat(type, groupName);
    }

    /**
     * 保存分组
     *
     * @param addGroupReqVo
     * @return
     */
    public LiveData<Resource<String>> saveGroup(AddGroupReqVo addGroupReqVo) {
        return getRepository().saveGroup(addGroupReqVo);
    }

    /**
     * 更新分组名
     *
     * @param groupId
     * @param type
     * @param groupName
     * @return
     */
    public LiveData<Resource<String>> updateGroupName(int groupId, int type, String groupName) {
        return getRepository().updateGroupName(groupId, type, groupName);
    }

    /**
     * 删除分组
     *
     * @param groupIds
     * @return
     */
    public LiveData<Resource<String>> deleteGroup(List<Integer> groupIds) {
        return getRepository().deleteGroup(groupIds);
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
     * 多文件上传
     *
     * @param parts
     * @return
     */
    public LiveData<Resource<List<UploadAttachmentResVo>>> uploadMultyFile(List<MultipartBody.Part> parts) {
        return getRepository().uploadMultyFile(parts);
    }


    /**
     * 发布消息
     *
     * @param plainMsgReqVo
     * @return
     */
    public LiveData<Resource<String>> savePlainMsg(PlainMsgReqVo plainMsgReqVo) {
        return getRepository().savePlainMsg(plainMsgReqVo);
    }
}