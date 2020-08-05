package com.lihang.selfmvvm.ui.register;

import android.text.TextUtils;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityRegisterStepTwoBinding;
import com.lihang.selfmvvm.utils.AESUtils;
import com.lihang.selfmvvm.utils.NoDoubleClickListener;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.RegisterReqVo;

public class RegisterStepTwoActivity extends BaseActivity<RegisterStepTwoViewModel, ActivityRegisterStepTwoBinding> {

    private RegisterReqVo registerReqVo;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register_step_two;
    }

    @Override
    protected void processLogic() {
        registerReqVo = (RegisterReqVo) getIntent().getExtras().getSerializable("registerReqVo");
    }

    @Override
    protected void setListener() {
        binding.tvPreviewStep.setOnClickListener(mNoDoubleClickListener);
        binding.tvComplete.setOnClickListener(mNoDoubleClickListener);
        binding.ivHead.setOnClickListener(mNoDoubleClickListener);
        binding.tvLogin.setOnClickListener(mNoDoubleClickListener);
    }

    private NoDoubleClickListener mNoDoubleClickListener = new NoDoubleClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            switch (view.getId()) {
                case R.id.tv_preview_step:
                    finish();
                    break;
                case R.id.tv_complete:
                    doRegister();
                    break;
                case R.id.iv_head:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {

    }

    private void doRegister() {
        if (TextUtils.isEmpty(getStringByUI(binding.etUserName))) {
            ToastUtils.showToast(getContext().getString(R.string.message_must));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etPhone))) {
            ToastUtils.showToast(getContext().getString(R.string.message_must));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etMail))) {
            ToastUtils.showToast(getContext().getString(R.string.message_must));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etIdCard))) {
            ToastUtils.showToast(getContext().getString(R.string.message_must));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etPassword))) {
            ToastUtils.showToast(getContext().getString(R.string.message_must));
            return;
        }


        registerReqVo.setUsername(getStringByUI(binding.etUserName));
        registerReqVo.setMobile(getStringByUI(binding.etPhone));
        registerReqVo.setEmail(getStringByUI(binding.etMail));
        registerReqVo.setIdentityCard(getStringByUI(binding.etIdCard));
        registerReqVo.setPassword(AESUtils.encrypt(getStringByUI(binding.etPassword)));

        mViewModel.register(registerReqVo).observe(this,res-> {
            res.handler(new OnCallback<String>() {
                @Override
                public void onSuccess(String data) {

                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                }

                @Override
                public void onFailure(String msg) {
                    super.onFailure(msg);
                }
            });
        });

    }
}
