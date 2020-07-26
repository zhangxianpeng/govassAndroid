package com.lihang.selfmvvm.ui.filepreview;

import android.os.Bundle;
import android.util.Log;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.customview.filereaderview.FileReaderView;
import com.tencent.smtt.sdk.TbsReaderView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 文件预览
 */
public class FilePreviewActivity extends AppCompatActivity {

    private String filePath = "";
    private FileReaderView mDocumentReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_preview);
        filePath = getIntent().getExtras().getString("path");
        Log.e("FilePreviewActivity", filePath);
        mDocumentReaderView = findViewById(R.id.fileReaderView);
        mDocumentReaderView.show(filePath);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDocumentReaderView != null) {
            mDocumentReaderView.stop();
        }
    }
}