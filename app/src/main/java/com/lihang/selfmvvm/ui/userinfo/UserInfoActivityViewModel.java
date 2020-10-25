package com.lihang.selfmvvm.ui.userinfo;

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

public class UserInfoActivityViewModel extends BaseViewModel<RepositoryImpl> {
    public UserInfoActivityViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<Resource<UploadSingleResVo>> testUploadFile(String type, File file) {
        return getRepository().uploadSigleFile(type, file);
    }

    public LiveData<Resource<String>> govassLogout(Object token) {
        return getRepository().govassLogout(token);
    }

    public LiveData<Resource<String>> getMsgUnRead() {
        return getRepository().getMsgUnRead();
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
}
