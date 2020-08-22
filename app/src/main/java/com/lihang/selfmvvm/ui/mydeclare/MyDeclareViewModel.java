/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: MyDeclareViewModel
 * Author: zhang
 * Date: 2020/7/9 22:02
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.ui.mydeclare;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.ProjectResVo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * @ClassName: MyDeclareViewModel
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/9 22:02
 */
public class MyDeclareViewModel extends BaseViewModel<RepositoryImpl> {
    public MyDeclareViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 已审核项目
     * 企业
     *
     * @return
     */
    public LiveData<Resource<ListBaseResVo<ProjectResVo>>> getListMeHandled(int page) {
        return getRepository().getListMeHandled(page);
    }

    /**
     * 待审核项目
     * 企业
     *
     * @return
     */
    public LiveData<Resource<ListBaseResVo<ProjectResVo>>> getListMePending(int page) {
        return getRepository().getListMePending(page);
    }


}
