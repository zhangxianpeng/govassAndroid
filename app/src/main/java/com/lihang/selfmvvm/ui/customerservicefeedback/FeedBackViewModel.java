package com.lihang.selfmvvm.ui.customerservicefeedback;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.model.CommunicateMsgVo;
import com.lihang.selfmvvm.vo.req.AddFeedBackReqVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * created by zhangxianpeng
 */
public class FeedBackViewModel extends BaseViewModel<RepositoryImpl> {
    public FeedBackViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * saveFeedBack
     *
     * @param addFeedBackReqVo
     * @return
     */
    public LiveData<Resource<String>> saveFeedBack(AddFeedBackReqVo addFeedBackReqVo) {
        return getRepository().saveFeedBack(addFeedBackReqVo);
    }

    /**
     * 获取会话内容
     *
     * @param id
     * @return
     */
    public LiveData<Resource<CommunicateMsgVo>> getFeedBackInfo(int id) {
        return getRepository().getFeedBackInfo(id);
    }
}
