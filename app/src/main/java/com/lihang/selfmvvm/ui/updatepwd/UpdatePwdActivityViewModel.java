package com.lihang.selfmvvm.ui.updatepwd;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.res.UploadAttachmentResVo;
import com.lihang.selfmvvm.vo.res.UploadSingleResVo;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import okhttp3.MultipartBody;

public class UpdatePwdActivityViewModel extends BaseViewModel<RepositoryImpl> {
    public UpdatePwdActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Resource<String>> updatePwd(UpdatePwdBean bean) {
        return getRepository().updatePwd(bean);
    }
}
