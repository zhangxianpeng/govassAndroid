package com.lihang.selfmvvm.ui.enpriceofficedoc;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.req.AddOdReqVo;
import com.lihang.selfmvvm.vo.req.PlainMsgReqVo;
import com.lihang.selfmvvm.vo.res.EnpriceOdVo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * 企业公獒viewmodel
 * 政府企业公告界面、公告详情界面公用
 */
public class EnpriceODViewModel extends BaseViewModel<RepositoryImpl> {
    public EnpriceODViewModel(@NonNull Application application) {
        super(application);
    }

    //获取已发布企业公告（政府端）
    public LiveData<Resource<ListBaseResVo<EnpriceOdVo>>> getEnterpriseNoticeList(int page) {
        return getRepository().getEnterpriseNoticeList(page, 4);
    }

    //获取待审核企业公告（政府端）
    public LiveData<Resource<ListBaseResVo<EnpriceOdVo>>> getEnterpriseNoticeAuditList(int page) {
        return getRepository().getEnterpriseNoticeAuditList(page, 1);
    }

    //我的企业公告列表（企业端）
    public LiveData<Resource<ListBaseResVo<EnpriceOdVo>>> getEnterpriseNoticeListComment(int page, int status) {
        return getRepository().getEnterpriseNoticeListComment(page, status);
    }

    //发布公告
    public LiveData<Resource<String>> saveOd(AddOdReqVo addOdReqVo) {
        return getRepository().saveOd(addOdReqVo);
    }
}
