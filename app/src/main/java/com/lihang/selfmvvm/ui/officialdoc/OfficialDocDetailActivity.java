package com.lihang.selfmvvm.ui.officialdoc;

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
import com.lihang.selfmvvm.databinding.ActivityOfficialDocDetailBinding;
import com.lihang.selfmvvm.ui.bigpicture.BigPictureActivity;
import com.lihang.selfmvvm.ui.filepreview.FilePreviewActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.AttachmentResVo;
import com.lihang.selfmvvm.vo.res.ImageDataInfo;
import com.lihang.selfmvvm.vo.res.NoticeResVo;
import com.lihang.selfmvvm.vo.res.OfficialDocResVo;
import com.lihang.selfmvvm.vo.res.SearchValueResVo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by zhangxianpeng
 * 公告详情
 */
public class OfficialDocDetailActivity extends BaseActivity<OfficialDocDetailViewModel, ActivityOfficialDocDetailBinding> {

    private CommonAdapter attachmentAdapter;
    private List<AttachmentResVo> attachmentList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_official_doc_detail;
    }

    @Override
    protected void processLogic() {
        initAttachmentAdapter();
        String flag = getIntent().getStringExtra("flag");
        NoticeResVo noticeResVo = (NoticeResVo) getIntent().getSerializableExtra("noticeResVo");  //公告
        ImageDataInfo imageDataInfo = (ImageDataInfo) getIntent().getSerializableExtra("imageDataInfo");   //轮播图
        OfficialDocResVo officialDocResVo = (OfficialDocResVo) getIntent().getSerializableExtra("officialDocResVo");  //发文、收文
        SearchValueResVo searchValueResVo = (SearchValueResVo) getIntent().getSerializableExtra("searchValueResVo");  //全局搜索
        if (flag.equals("homebanner")) {  //首页banner
            binding.tvTitle.setText(imageDataInfo.getTitle());
            if (imageDataInfo.getContentType() == 0) {
                initNormalWebView(imageDataInfo.getContent());
            } else {  //传地址
                loadHtml(imageDataInfo.getContent());
            }
        } else if (flag.equals("noticelist")) {  //公告
            binding.tvTitle.setText(noticeResVo.getTitle());
            if (noticeResVo.getContentType() == 0) {
                initNormalWebView(noticeResVo.getContent());
            } else {
                loadHtml(noticeResVo.getContent());
            }
        } else if (flag.equals("articallist")) {
            binding.tvTitle.setText(officialDocResVo.getTitle());
            initNormalWebView(officialDocResVo.getContent());
            getAttachmentList(officialDocResVo.getId());
        } else if (flag.equals("commonSearch")) { //全局搜索
            binding.tvTitle.setText(searchValueResVo.getTitle());
            initNormalWebView(searchValueResVo.getContent());
        }
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

    private void getAttachmentList(Integer id) {
        mViewModel.getOfficalDocDetail(id).observe(this,res-> {
            res.handler(new OnCallback<OfficialDocResVo>() {
                @Override
                public void onSuccess(OfficialDocResVo data) {
                    if(data == null) {
                        return;
                    }
                    if(!data.getAttachmentList().isEmpty()) {
                        binding.rvAttachment.setVisibility(View.VISIBLE);
                        attachmentList.clear();
                        attachmentList.addAll(data.getAttachmentList());
                        if(attachmentAdapter!=null) {
                            attachmentAdapter.notifyDataSetChanged();
                        }
                    } else {
                        binding.rvAttachment.setVisibility(View.GONE);
                    }
                }
            });
        });
    }

    private void loadHtml(String content) {
        WebSettings settings = binding.normalWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        binding.normalWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        binding.normalWebview.setWebViewClient(new MyWebViewClient(this));
        binding.normalWebview.loadUrl(content);
    }

    private void initNormalWebView(String content) {
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
                if (newProgress == 100) {
                    binding.pbWebview.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    binding.pbWebview.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    binding.pbWebview.setProgress(newProgress);//设置进度值
                }
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

    @Override
    protected void onResume() {
        super.onResume();
        binding.normalWebview.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        binding.normalWebview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.normalWebview.destroy();
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.iv_title_bar_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.normalWebview.canGoBack()) {
            if (binding.normalWebview.copyBackForwardList().getSize() == 2) {
                finish();
            } else {
                binding.normalWebview.goBack();
            }
        } else {
            super.onBackPressed();
        }
    }
}
