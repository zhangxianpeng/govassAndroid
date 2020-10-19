package com.lihang.selfmvvm.ui.web;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.QuestionNaireItemResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class WebViewModel extends BaseViewModel<RepositoryImpl> {
    public WebViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<Resource<QuestionNaireItemResVo>> getQuestionnairerecordData(int id) {
        return getRepository().getQuestionnairerecordData(id);
    }

}
