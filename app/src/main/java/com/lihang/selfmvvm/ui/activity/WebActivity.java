package com.lihang.selfmvvm.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lihang.selfmvvm.MyApplication;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.base.NormalViewModel;
import com.lihang.selfmvvm.common.SystemConst;
import com.lihang.selfmvvm.databinding.ActivityWebBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.QuestionNaireItemResVo;

/**
 * 调查问卷界面
 */
public class WebActivity extends BaseActivity<NormalViewModel, ActivityWebBinding> {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_web;
    }

    private QuestionNaireItemResVo questionNaireItemResVo;

    @Override
    protected void processLogic() {
        questionNaireItemResVo = (QuestionNaireItemResVo) getIntent().getSerializableExtra("questionNaireItemResVo");
        int id = questionNaireItemResVo.getId();
        String title = questionNaireItemResVo.getName();
        String content = transfer(questionNaireItemResVo.getContent(), id);
        binding.tvTitle.setText(title);
        initNormalWebView(content);
    }

    private void initNormalWebView(String content) {
        WebSettings settings = binding.normalWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        binding.normalWebview.setWebViewClient(new MyWebViewClient(this));
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
    private String transfer(String content, int id) {
        String s1 = content.replace("#{server}", SystemConst.DEFAULT_SERVER.substring(0, SystemConst.DEFAULT_SERVER.length() - 1));
        String s2 = s1.replace("#{token}", MyApplication.getToken());
        String s3 = s2.replace("#{questionnaireId}", String.valueOf(id));
        return s3;
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
