package com.lihang.selfmvvm.ui.senddynamic;

import android.app.Application;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.req.AddDynamicReqVo;
import com.lihang.selfmvvm.vo.res.UploadAttachmentResVo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import okhttp3.MultipartBody;

/**
 * created by zhangxianpeng
 * 发布动态
 */
public class SendDynamicViewModel extends BaseViewModel<RepositoryImpl> {

    public SendDynamicViewModel(@NonNull Application application) {
        super(application);
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

    /**
     * 发布千企动态
     *
     * @param addDynamicReqVo
     * @return
     */
    public LiveData<Resource<String>> saveDynamic(AddDynamicReqVo addDynamicReqVo) {
        return getRepository().saveDynamic(addDynamicReqVo);
    }
}
