/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: UserFragmentViewModel
 * Author: zhang
 * Date: 2020/7/11 17:20
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.ui.fragment;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.bean.basebean.ResponModel;
import com.lihang.selfmvvm.vo.res.UploadSingleResVo;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * @ClassName: UserFragmentViewModel
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/11 17:20
 */
public class UserFragmentViewModel extends BaseViewModel<RepositoryImpl> {
    public UserFragmentViewModel(@NonNull Application application) {
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

}
