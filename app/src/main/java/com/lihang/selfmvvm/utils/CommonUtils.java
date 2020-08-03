package com.lihang.selfmvvm.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.widget.PopupWindow;

import com.lihang.selfmvvm.MyApplication;
import com.lihang.selfmvvm.R;

/**
 * 全局使用工具
 */
public class CommonUtils {

    private volatile static CommonUtils instacne;

    private CommonUtils() {

    }

    public static CommonUtils getInstance() {
        if (instacne == null) {
            synchronized (CommonUtils.class) {
                if (instacne == null) {
                    instacne = new CommonUtils();
                }
            }
        }
        return instacne;
    }

    /**
     * 本地选择文件作为附件
     *
     * @param activity
     */
    public void selectFileFromLocal(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);//打开多个文件
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            activity.startActivityForResult(Intent.createChooser(intent, MyApplication.getContext().getString(R.string.select_file)), 1);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

}
