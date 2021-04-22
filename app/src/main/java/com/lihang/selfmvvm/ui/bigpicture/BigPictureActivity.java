package com.lihang.selfmvvm.ui.bigpicture;

import android.os.Environment;
import android.view.View;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityBigPictureBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.LogUtils;
import com.lihang.selfmvvm.utils.ToastUtils;

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
 * created by zhangxianpeng
 * 查看大图
 */
public class BigPictureActivity extends BaseActivity<BigPictureViewModel, ActivityBigPictureBinding> {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_big_picture;
    }

    @Override
    protected void processLogic() {
        String imgUrl = getIntent().getStringExtra("imgUrl");
        String fileName = getIntent().getStringExtra("fileName");
        binding.tvMsg.setText(fileName);
        Glide.with(this).load(DEFAULT_SERVER + DEFAULT_FILE_SERVER + imgUrl)
                .placeholder(R.mipmap.default_img)
                .error(R.mipmap.default_img).into(binding.ivBigPicture);

        binding.ivDownload.setOnClickListener(view -> downLoadFile(fileName, DEFAULT_SERVER + DEFAULT_FILE_SERVER + imgUrl));
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
    }

    private void downLoadFile(String fileName, String url) {
        String filePath = Environment.getExternalStorageDirectory() + File.separator + "download";
        //先判断此文件是否已经下载过
        if (checkFileIsExist(fileName, filePath)) {
            ToastUtils.showToast("附件保存到：" + filePath + "/" + fileName);
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
                public void onResponse(Call call, Response response) {
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
                        runOnUiThread(() -> {
                            ToastUtils.showToast("附件保存到：" + savePath + "/" + fileName);
                        });
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
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }
}
