/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: FilePreviewViewModel
 * Author: zhang
 * Date: 2020/7/12 15:47
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.ui.filepreview;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * @ClassName: FilePreviewViewModel
 * @Author: zhangxianpeng
 * @Date: 2020/7/12 15:47
 */
public class FilePreviewViewModel extends BaseViewModel<RepositoryImpl> {
    public FilePreviewViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 文件下载至 content://downloads/my_downloads
     *
     * @param fileName
     * @param filePath
     * @return
     */
    public LiveData<Resource<File>> downFile(String fileName, String filePath) {
        return getRepository().downFile("content://downloads/my_downloads", fileName, filePath);
    }
}