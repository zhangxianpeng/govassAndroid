package com.lihang.selfmvvm.ui.msgdetail;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;
import com.lihang.selfmvvm.vo.res.PlainMsgAttachmentListResVo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * 消息详情
 */
public class MsgDetailActivityViewModel extends BaseViewModel<RepositoryImpl> {
    public MsgDetailActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Resource<MsgMeResVo>> getMsgDetail(int id) {
        return getRepository().getMsgDetail(id);
    }

    /**
     * 读消息
     *
     * @param id
     * @return
     */
    public LiveData<Resource<MsgMeResVo>> transferReadFlag(int id) {
        return getRepository().transferReadFlag(id);
    }

    /**
     * 获取消息附件列表
     *
     * @param id
     * @return
     */
    public LiveData<Resource<List<PlainMsgAttachmentListResVo>>> getPlainMsgAttachmentList(int id) {
        return getRepository().getPlainMsgAttachmentList(id);
    }
}