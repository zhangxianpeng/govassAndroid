/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: ProjectDeclareViewModel
 * Author: zhang
 * Date: 2020/7/11 22:42
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.ui.projrctdeclare;

import android.app.Application;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.widget.EditText;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;
import com.lihang.selfmvvm.bean.basebean.Resource;
import com.lihang.selfmvvm.vo.req.AddProjectReqVo;
import com.lihang.selfmvvm.vo.res.UploadAttachmentResVo;

import java.util.List;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import okhttp3.MultipartBody;

/**
 * @ClassName: ProjectDeclareViewModel
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/11 22:42
 */
public class ProjectDeclareViewModel extends BaseViewModel<RepositoryImpl> {
    public ProjectDeclareViewModel(@NonNull Application application) {
        super(application);
    }


    /**
     * 设置EditText的hint字体大小
     *
     * @param editText EditText控件
     * @param hintText hint内容
     * @param size     hint字体大小，单位为sp
     */
    public void setEditTextHintWithSize(EditText editText, String hintText, @Dimension int size) {
        if (!TextUtils.isEmpty(hintText)) {
            SpannableString ss = new SpannableString(hintText);
            //设置字体大小 true表示单位是sp
            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);
            ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            editText.setHint(new SpannedString(ss));
        }
    }

    /**
     * 新增项目申报
     *
     * @param addProjectReqVo
     * @return
     */
    public LiveData<Resource<String>> saveProject(AddProjectReqVo addProjectReqVo) {
        return getRepository().saveProject(addProjectReqVo);
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
