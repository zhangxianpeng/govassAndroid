package com.lihang.selfmvvm.ui.msgdetail;

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
import com.lihang.selfmvvm.databinding.ActivityMsgDetailBinding;
import com.lihang.selfmvvm.ui.bigpicture.BigPictureActivity;
import com.lihang.selfmvvm.ui.filepreview.FilePreviewActivity;
import com.lihang.selfmvvm.ui.officialdoc.OfficialDocDetailActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.AttachmentResVo;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;
import com.lihang.selfmvvm.vo.res.OfficialDocResVo;
import com.lihang.selfmvvm.vo.res.PlainMsgAttachmentListResVo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * created by zhangxianpeng
 * 消息详情界面
 */
public class MsgDetailActivity extends BaseActivity<MsgDetailActivityViewModel, ActivityMsgDetailBinding> {

    private List<PlainMsgAttachmentListResVo> attachmentList = new ArrayList<>();
    private CommonAdapter attachmentAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_msg_detail;
    }

    @Override
    protected void processLogic() {
        initAdapter();

        String uiflag = getIntent().getStringExtra("uiflag");

        if (uiflag.equals("newmsg")) {  //新的消息
            binding.tvContent.setVisibility(View.VISIBLE);
            binding.normalWebview.setVisibility(View.GONE);
            MsgMeResVo msgMeResVo = (MsgMeResVo) getIntent().getSerializableExtra("msgMeResVo");
            int readFlag = msgMeResVo.getReadFlag();
            int id = msgMeResVo.getId();
            String title = msgMeResVo.getTitle();
            binding.tvTitle.setText(title);
            getPlainMsgAttachmentList(msgMeResVo.getPrimaryId());
            getPlainMsdDetail(id);
            transferReadFlag(readFlag, id);
        } else if (uiflag.equals("policy")) {  //政策文件
            binding.tvContent.setVisibility(View.GONE);
            binding.normalWebview.setVisibility(View.VISIBLE);
            OfficialDocResVo pilocyResVo = (OfficialDocResVo) getIntent().getSerializableExtra("noticeResVo");
            binding.tvTitle.setText(pilocyResVo.getTitle());
            loadHtml(StringEscapeUtils.unescapeHtml4(pilocyResVo.getContent()));
            int id = getIntent().getIntExtra("id", -1);
            mViewModel.getPolicyInfo(id).observe(this, res -> {
                res.handler(new OnCallback<OfficialDocResVo>() {
                    @Override
                    public void onSuccess(OfficialDocResVo data) {
                        List<AttachmentResVo> attachmentResVos = data.getAttachmentList();
                        if (attachmentResVos != null && attachmentResVos.size() > 0) {
                            binding.cardView.setVisibility(View.VISIBLE);
                            attachmentList.clear();
                            attachmentList.addAll(transfer(attachmentResVos));
                            if (attachmentAdapter != null) attachmentAdapter.notifyDataSetChanged();
                        }
                    }
                });
            });
        }
    }

    private void loadHtml(String content) {
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
        binding.normalWebview.loadData(content, "text/html;charset=utf-8", "utf-8");
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

    private List<PlainMsgAttachmentListResVo> transfer(List<AttachmentResVo> data) {
        List<PlainMsgAttachmentListResVo> resultList = new ArrayList<>();
        for (AttachmentResVo attachmentResVo : data) {
            PlainMsgAttachmentListResVo plainMsgAttachmentListResVo = new PlainMsgAttachmentListResVo();
            plainMsgAttachmentListResVo.setName(attachmentResVo.getName());
            plainMsgAttachmentListResVo.setUrl(attachmentResVo.getUrl());
            resultList.add(plainMsgAttachmentListResVo);
        }
        return resultList;
    }

    private void initAdapter() {
        attachmentAdapter = new CommonAdapter<PlainMsgAttachmentListResVo>(getContext(), R.layout.rv_attachment_item, attachmentList) {
            @Override
            protected void convert(ViewHolder holder, PlainMsgAttachmentListResVo plainMsgAttachmentListResVo, int position) {
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
        binding.rvPlainMsgAttachment.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPlainMsgAttachment.setAdapter(attachmentAdapter);
    }

    private void getPlainMsgAttachmentList(int primaryId) {
        mViewModel.getPlainMsgAttachmentList(primaryId).observe(this, res -> {
            res.handler(new OnCallback<List<PlainMsgAttachmentListResVo>>() {
                @Override
                public void onSuccess(List<PlainMsgAttachmentListResVo> data) {
                    if (data != null && data.size() > 0) {
                        binding.cardView.setVisibility(View.VISIBLE);
                        attachmentList.clear();
                        attachmentList.addAll(data);
                        if (attachmentAdapter != null) attachmentAdapter.notifyDataSetChanged();
                    }
                }
            });
        });
    }

    private void getPlainMsdDetail(int id) {
        mViewModel.getMsgDetail(id).observe(this, res -> {
            res.handler(new OnCallback<MsgMeResVo>() {
                @Override
                public void onSuccess(MsgMeResVo data) {
                    binding.tvContent.setText(data.getContent());
                }
            });
        });
    }

    private void transferReadFlag(int readFlag, int id) {
        if (readFlag != 1) {
            mViewModel.transferReadFlag(id).observe(this, res -> {
                res.handler(new OnCallback<MsgMeResVo>() {
                    @Override
                    public void onSuccess(MsgMeResVo data) {
                    }
                });
            });
        }
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }
}
