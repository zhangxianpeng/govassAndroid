package com.lihang.selfmvvm.ui.register;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class RegisterStepOneViewModel extends BaseViewModel<RepositoryImpl> {
    public RegisterStepOneViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 上传营业执照
     * 图片可以拍照、也可以从本地选择
     * @param type
     * @param key
     * @param file
     * @return
     */
    public LiveData<Resource<String>> uploadBusinesslicense(String type, String key, File file) {
        return getRepository().upLoadPic(type, key, file);
    }

}
