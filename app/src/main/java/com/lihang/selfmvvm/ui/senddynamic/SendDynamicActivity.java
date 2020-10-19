package com.lihang.selfmvvm.ui.senddynamic;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivitySendDynamicBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.AttachmentResVo;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhangxianpeng
 * 发布动态
 */
public class SendDynamicActivity extends BaseActivity<SendDynamicViewModel, ActivitySendDynamicBinding> implements TextWatcher {

    //是否纯文字
    private boolean isOnlyWord = false;

    private List<AttachmentResVo> attachmentResVoList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_send_dynamic;
    }

    @Override
    protected void processLogic() {
        isOnlyWord = true;
        changeView(isOnlyWord);
    }

    private void changeView(boolean isOnlyWord) {
    }

    @Override
    protected void setListener() {
        binding.tvCancel.setOnClickListener(this::onClick);
        binding.btnSend.setOnClickListener(this::onClick);
        binding.etDynamic.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.tv_cancel:
                onBackPressed();
                break;
            case R.id.btn_send:
                sendDynamic();
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        updateSendBtn();
    }

    private void updateSendBtn() {
        String dynamicContent = binding.etDynamic.getText().toString().trim();
        if (!TextUtils.isEmpty(dynamicContent)) {
            binding.btnSend.setEnabled(true);
        }
    }

    private void sendDynamic() {
        
    }
}
