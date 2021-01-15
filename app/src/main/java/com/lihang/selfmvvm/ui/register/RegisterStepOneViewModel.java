package com.lihang.selfmvvm.ui.register;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.bean.basebean.ResponModel;
import com.lihang.selfmvvm.vo.req.FillEnterpriseReqVo;
import com.lihang.selfmvvm.vo.res.EnterpriseVo;
import com.lihang.selfmvvm.vo.res.UploadSingleResVo;
import com.lihang.selfmvvm.vo.res.UserInfoVo;

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
     * @param file
     * @return
     */
    public LiveData<Resource<UploadSingleResVo>> uploadBusinesslicense(String type, File file) {
        return getRepository().uploadSigleFile(type, file);
    }

    //获取用户信息
    public LiveData<Resource<UserInfoVo>> getUserInfo() {
        return getRepository().getUserInfo();
    }

    //完善企业信息
    public LiveData<Resource<String>> fillEnterpriseInfo(FillEnterpriseReqVo enterpriseVo) {
        return getRepository().fillEnterpriseInfo(enterpriseVo);
    }
}
