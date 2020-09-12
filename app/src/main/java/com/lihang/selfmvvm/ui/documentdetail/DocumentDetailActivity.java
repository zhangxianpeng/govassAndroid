package com.lihang.selfmvvm.ui.documentdetail;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lihang.selfmvvm.R;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 公文详情
 */
public class DocumentDetailActivity extends AppCompatActivity {

    private String filePath = "";
    private TbsReaderView tbsReaderView;
    private RelativeLayout rlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_detail);
        filePath = "/storage/emulated/0/Tencent/TIMfile_recv/offer-聘用意向函-张现鹏.docx";
        Log.e("FilePreviewActivity", filePath);

        tbsReaderView = new TbsReaderView(this, readerCallback);
        rlRoot = findViewById(R.id.rl_root);
        rlRoot.addView(tbsReaderView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        openFile();
    }

    private void openFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
        }
        Bundle bundle = new Bundle();
        bundle.putString("filePath", filePath);
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
        boolean result = tbsReaderView.preOpen(parseFormat(parseName(filePath)), false);
        if (result) {
            tbsReaderView.openFile(bundle);
        }
    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    private String parseName(String url) {
        String fileName = null;
        try {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        } finally {
            if (TextUtils.isEmpty(fileName)) {
                fileName = String.valueOf(System.currentTimeMillis());
            }
        }
        return fileName;
    }

    TbsReaderView.ReaderCallback readerCallback = new TbsReaderView.ReaderCallback() {
        @Override
        public void onCallBackAction(Integer integer, Object o, Object o1) {

        }
    };


    @Override
    protected void onDestroy() {
        tbsReaderView.onStop();
        super.onDestroy();
    }
}
