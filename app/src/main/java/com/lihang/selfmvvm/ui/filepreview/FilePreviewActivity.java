package com.lihang.selfmvvm.ui.filepreview;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityFilePreviewBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.lihang.selfmvvm.base.BaseConstant.DEFAULT_FILE_SERVER;
import static com.lihang.selfmvvm.common.SystemConst.DEFAULT_SERVER;

/**
 * 文件预览
 * created by zhangxianpeng
 */
public class FilePreviewActivity extends BaseActivity<FilePreviewViewModel, ActivityFilePreviewBinding> {

    /**
     * 文件路徑
     */
    private String filePath;
    /**
     * 文件名
     */
    private String fileName;
    private TbsReaderView tbsReaderView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_file_preview;
    }

    @Override
    protected void processLogic() {
        initView();
    }

    private void initView() {
        filePath = getIntent().getExtras().getString("fileUrl");
        fileName = getIntent().getExtras().getString("fileName");
        binding.tvFileName.setText(fileName);

        tbsReaderView = new TbsReaderView(this, readerCallback);
        binding.rlRoot.addView(tbsReaderView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        downLoadFile(fileName, DEFAULT_SERVER + DEFAULT_FILE_SERVER + filePath);
    }

    /**
     * @param fileName
     * @param url
     */
    private void downLoadFile(String fileName, String url) {
        String filePath = Environment.getExternalStorageDirectory() + File.separator + "download";
        //先判断此文件是否已经下载过
        if (checkFileIsExist(fileName, filePath)) {
            openFile(fileName + "/" + filePath);
            return;
        } else {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    // 储存下载文件的目录
                    String savePath = isExistDir(filePath);
                    try {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        File file = new File(savePath, fileName);
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            // 下载中
//                        listener.onDownloading(progress);
                        }
                        fos.flush();
                        // 下载完成
//                    listener.onDownloadSuccess();
                        //下载完成后发送消息
                        sendDownCompleteMsg(fileName, savePath);
                    } catch (Exception e) {
//                    listener.onDownloadFailed();
                        ToastUtils.showToast("下载文件失败，无法预览");
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {
                        }
                    }
                }
            });
        }
    }

    private boolean checkFileIsExist(String fileName, String filePath) {
        boolean isExist = false;
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (files == null) {
            isExist = false;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            s.add(files[i].getAbsolutePath());
        }

        for (String name : s) {
            if (name.equals(fileName)) isExist = true;
            break;
        }

        return isExist;
    }

    private void sendDownCompleteMsg(String fileName, String filePath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.pbDownload.setVisibility(View.GONE);
                openFile(filePath + "/" + fileName);
            }
        });
    }

    /**
     * 判断文件夹是否存在
     *
     * @param filePath
     * @return
     */
    private String isExistDir(String filePath) {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), filePath);
        if (!downloadFile.mkdirs()) {
            try {
                downloadFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    @Override
    protected void setListener() {
        binding.ibtnTitleBarBack.setOnClickListener(this::onClick);
    }

    private void openFile(String filePath) {
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

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.ibtn_title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }
}