/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: MsgFragmentViewModel
 * Author: zhang
 * Date: 2020/7/11 17:21
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
import com.lihang.selfmvvm.vo.res.ImageDataInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * @ClassName: MsgFragmentViewModel
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/11 17:21
 */
public class MsgFragmentViewModel extends BaseViewModel<RepositoryImpl> {
    public MsgFragmentViewModel(@NonNull Application application) {
        super(application);
    }


    //getUserInfo
    public void getMsg() {

    }

    public LiveData<Resource<List<ImageDataInfo>>> getGovassBannerList(String token) {
        return getRepository().getGovassBannerList(token);
    }


}