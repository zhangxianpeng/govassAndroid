package com.lihang.selfmvvm.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityTestBinding;
import com.lihang.selfmvvm.ui.filepreview.FilePreviewActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;

import java.io.File;

/**
 * 测试 打开 doc pdf
 */
public class TestActivity extends BaseActivity<TestViewModel, ActivityTestBinding> {

    private String docPath = "file:///assets/a.doc";
    private String pdfPath = "file:///assets/b.pdf";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_test;
    }

    @Override
    protected void processLogic() {
//        getFilePath();
    }

    /**
     * 获取本地文件的路径 并传 到 预览界面中
     */
    private void getFilePath() {
        String path = "file" + File.separator;
        Log.e("zhangxp===>", path);
    }

    @Override
    protected void setListener() {
        binding.btnOpenDoc.setOnClickListener(this::onClick);
        binding.btnOpenPdf.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.btn_open_doc:
                bundle.putString("path", docPath);
                break;
            case R.id.btn_open_pdf:
                bundle.putString("path", pdfPath);
                break;
            default:
                break;
        }
        ActivityUtils.startActivityWithBundle(getContext(), FilePreviewActivity.class, bundle);
    }
}
