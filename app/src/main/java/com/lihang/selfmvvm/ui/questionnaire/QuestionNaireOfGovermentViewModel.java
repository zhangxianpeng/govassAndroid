package com.lihang.selfmvvm.ui.questionnaire;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.EnterpriseVo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.QuestionNaireResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class QuestionNaireOfGovermentViewModel extends BaseViewModel<RepositoryImpl> {
    public QuestionNaireOfGovermentViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 根据问卷id获取问卷详情
     *
     * @param page
     * @param status
     * @param questionnaireRecordId
     * @return
     */
    public LiveData<Resource<ListBaseResVo<EnterpriseVo>>> getEnpriceList(int page, String status, int questionnaireRecordId) {
        return getRepository().getEnpriceList(page, status, questionnaireRecordId);
    }


}
