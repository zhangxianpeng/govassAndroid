package com.lihang.selfmvvm.ui.activity;

import android.util.Log;
import android.view.View;

import com.lihang.selfmvvm.MyApplication;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.base.NormalViewModel;
import com.lihang.selfmvvm.common.SystemConst;
import com.lihang.selfmvvm.databinding.ActivityWebBinding;
import com.lihang.selfmvvm.utils.LogUtils;
import com.lihang.selfmvvm.vo.res.QuestionNaireItemResVo;


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
        String content = transfer(questionNaireItemResVo.getContent(), id);
        System.out.print(content);
        binding.webViewX5.loadUrl(content);
        binding.leoTitleBar.bar_left_btn.setOnClickListener(this);
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
        binding.webViewX5.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        binding.webViewX5.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.webViewX5.destroy();
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bar_left_btn:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.webViewX5.canGoBack()) {
            binding.webViewX5.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
