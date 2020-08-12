package com.lihang.selfmvvm.ui.questionnaire;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.QuestionNaireResVo;
import com.lihang.selfmvvm.vo.res.UploadSingleResVo;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class QuestionNaireViewModel extends BaseViewModel<RepositoryImpl> {
    public QuestionNaireViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Resource<QuestionNaireResVo>> getQuestiontList(int page, int status) {
        return getRepository().getQuestiontList(page, status);
    }
}
