package com.lihang.selfmvvm.ui.questionnaire;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.vo.res.QuestionNaireResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class QuestionNaireViewModel extends BaseViewModel<RepositoryImpl> {
    public QuestionNaireViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 调查问卷（政府/企业）
     *
     * @param page
     * @param status
     * @return
     */
    public LiveData<Resource<QuestionNaireResVo>> getQuestiontList(int page, int status) {
        return CheckPermissionUtils.getInstance().isGovernment() ? getRepository().getQuestiontList(page, status) : getRepository().getEnQuestiontList(page, status);
    }


}
