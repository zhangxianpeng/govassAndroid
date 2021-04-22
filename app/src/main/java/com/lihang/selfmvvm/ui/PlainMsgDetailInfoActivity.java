package com.lihang.selfmvvm.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityPlainMsgDetailInfoBinding;
import com.lihang.selfmvvm.ui.bigpicture.BigPictureActivity;
import com.lihang.selfmvvm.ui.filepreview.FilePreviewActivity;
import com.lihang.selfmvvm.ui.msgdetail.MsgDetailActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.vo.res.AttachmentResVo;
import com.lihang.selfmvvm.vo.res.PlainMsgAttachmentListResVo;
import com.lihang.selfmvvm.vo.res.PlainMsgResVo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

public class PlainMsgDetailInfoActivity extends BaseActivity<CommonApiViewModel, ActivityPlainMsgDetailInfoBinding> {
    private CommonAdapter attachmentAdapter;
    private List<AttachmentResVo> attachmentList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_plain_msg_detail_info;
    }

    @Override
    protected void processLogic() {
        binding.ivTitleBarBack.setOnClickListener(view -> finish());
        initAttachmentAdapter();
        int plainMsgId = getIntent().getIntExtra("plainMsgId", -1);
        mViewModel.getPlainMsgDetailInfo(plainMsgId).observe(this, res -> {
            res.handler(new OnCallback<PlainMsgResVo>() {
                @Override
                public void onSuccess(PlainMsgResVo data) {
                    String title = data.getTitle();
                    binding.tvTitle.setText(title);
                    String content = data.getContent();
                    loadHtmlContent(content);
                    List<AttachmentResVo> attachmentResVos = data.getAttachmentList();
                    if (attachmentResVos != null && attachmentResVos.size() > 0) {
                        binding.rvAttachment.setVisibility(View.VISIBLE);
                        attachmentList.clear();
                        attachmentList.addAll(attachmentResVos);
                        if (attachmentAdapter != null) {
                            attachmentAdapter.notifyDataSetChanged();
                        }
                    } else {
                        binding.rvAttachment.setVisibility(View.GONE);
                    }
                }
            });
        });
    }

    private void initAttachmentAdapter() {
        attachmentAdapter = new CommonAdapter<AttachmentResVo>(this, R.layout.rv_attachment_item, attachmentList) {
            @Override
            protected void convert(ViewHolder holder, AttachmentResVo plainMsgAttachmentListResVo, int position) {
                holder.setText(R.id.tv_project_title, plainMsgAttachmentListResVo.getName());
                holder.setOnClickListener(R.id.rl_container, (view -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("plainMsgAttachmentUrl", plainMsgAttachmentListResVo.getUrl());
                    //图片文件跳转到图片预览界面
                    if (plainMsgAttachmentListResVo.getName().endsWith("PNG") ||
                            plainMsgAttachmentListResVo.getName().endsWith("JPG") ||
                            plainMsgAttachmentListResVo.getName().endsWith("JEPG") ||
                            plainMsgAttachmentListResVo.getName().endsWith("png") ||
                            plainMsgAttachmentListResVo.getName().endsWith("jpg") ||
                            plainMsgAttachmentListResVo.getName().endsWith("jepg")) {
                        bundle.putString("fileName", plainMsgAttachmentListResVo.getName());
                        bundle.putString("imgUrl", plainMsgAttachmentListResVo.getUrl());
                        ActivityUtils.startActivityWithBundle(getContext(), BigPictureActivity.class, bundle);
                    } else {
                        bundle.putString("fileUrl", plainMsgAttachmentListResVo.getUrl());
                        bundle.putString("fileName", plainMsgAttachmentListResVo.getName());
                        ActivityUtils.startActivityWithBundle(getContext(), FilePreviewActivity.class, bundle);
                    }
                }));
            }
        };
        binding.rvAttachment.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAttachment.setAdapter(attachmentAdapter);
    }

    @Override
    protected void setListener() {
    }

    @Override
    public void onClick(View view) {
    }

    private void loadHtmlContent(String content) {
        WebSettings settings = binding.normalWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true); // 支持缩放
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (dm.densityDpi > 240) {
            settings.setDefaultFontSize(40); //可以取1-72之间的任意值，默认16
        }
        binding.normalWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        binding.normalWebview.setWebViewClient(new MyWebViewClient(this));
        binding.normalWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        binding.normalWebview.loadData(StringEscapeUtils.unescapeHtml4(content), "text/html;charset=utf-8", "utf-8");
    }

    static class MyWebViewClient extends WebViewClient {
        private Activity activity;

        public MyWebViewClient(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
}
