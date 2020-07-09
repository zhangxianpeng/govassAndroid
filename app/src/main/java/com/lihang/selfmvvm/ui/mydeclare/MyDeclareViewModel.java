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

import androidx.annotation.NonNull;

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

    //getMyDeclareList
    public void getMyDeclareList() {

    }
}
