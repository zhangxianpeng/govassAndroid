package com.lihang.selfmvvm.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lihang.selfmvvm.MyApplication;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.common.SystemConst;
import com.lihang.selfmvvm.databinding.ActivityWebBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.vo.res.QuestionNaireItemResVo;

/**
 * 调查问卷界面
 */
public class WebActivity extends BaseActivity<WebViewModel, ActivityWebBinding> {

    /**
     * 区分已填/未填
     */
    private String status = "";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_web;
    }


    /**
     * 政府端已填报
     */
    private int questionnaireRecordId = 0;
    private String titleName = "";


    String content = "";

    @Override
    protected void processLogic() {
        status = getIntent().getStringExtra("status");
        if (CheckPermissionUtils.getInstance().isGovernment()) {  //政府端
            questionnaireRecordId = getIntent().getIntExtra("questionnaireRecordId", 0);
            titleName = getIntent().getStringExtra("questionnaireRecordName");
            binding.tvTitle.setText(titleName);
            mViewModel.getQuestionnairerecordData(questionnaireRecordId).observe(this, res -> {
                res.handler(new OnCallback<QuestionNaireItemResVo>() {
                    @Override
                    public void onSuccess(QuestionNaireItemResVo data) {
                        content = transfer(data.getContent(), questionnaireRecordId, false);
                        initNormalWebView(content);
                    }
                });
            });
        } else {
            QuestionNaireItemResVo questionNaireItemResVo = (QuestionNaireItemResVo) getIntent().getSerializableExtra("enpriceData");
            int recorId = questionNaireItemResVo.getQuestionnaireRecordId();
            Log.e("zhshadas", recorId + "");
            binding.tvTitle.setText(questionNaireItemResVo.getName());
            if (status.equals("0")) {  //未填报
                content = transfer(questionNaireItemResVo.getContent(), recorId, true);
                initNormalWebView(content);
            } else {
                mViewModel.getQuestionnairerecordData(questionNaireItemResVo.getId()).observe(this, res -> {
                    res.handler(new OnCallback<QuestionNaireItemResVo>() {
                        @Override
                        public void onSuccess(QuestionNaireItemResVo data) {
                            content = transfer(data.getContent(), questionNaireItemResVo.getId(), false);
                            initNormalWebView(content);
                        }
                    });
                });
            }
        }
    }

    private void initNormalWebView(String content) {
        WebSettings settings = binding.normalWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
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

    /**
     * 替换 占位符
     *
     * @param content
     * @return
     */
    private String transfer(String content, int id, boolean isWait) {
        if (isWait) {  //未填报
            String s1 = content.replace("#{server}", SystemConst.DEFAULT_SERVER.substring(0, SystemConst.DEFAULT_SERVER.length() - 1));
            String s2 = s1.replace("#{token}", MyApplication.getToken());
            String s3 = s2.replace("#{questionnaireRecordId}", String.valueOf(id));
            return s3;
        } else {  //已填报
            return content.replace("#{server}", SystemConst.DEFAULT_SERVER.substring(0, SystemConst.DEFAULT_SERVER.length() - 1));
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
        if (ButtonClickUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_title_bar_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.normalWebview.canGoBack()) {
            binding.normalWebview.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
