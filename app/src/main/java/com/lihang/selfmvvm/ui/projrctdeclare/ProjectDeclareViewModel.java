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

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.EditText;

import com.lihang.selfmvvm.base.BaseViewModel;
import com.lihang.selfmvvm.base.RepositoryImpl;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;

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


    public void submitDeclare() {

    }

    /**
     * 从本地选择附件上传
     *
     * @param view
     */
    public void selectAttachmentFromLocal(Activity activity, View view) {

    }
}
