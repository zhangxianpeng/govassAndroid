package com.lihang.selfmvvm.ui.register;

import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityRegisterBinding;
import com.lihang.selfmvvm.ui.login.GovassLoginActivity;
import com.lihang.selfmvvm.utils.AESUtils;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.RegisterReqVo;

/**
 * created by zhangxianpeng on 2020/11/16
 * 注册界面重构
 */
public class RegisterActivity extends BaseActivity<RegisterStepTwoViewModel, ActivityRegisterBinding> {

    //默认用户类型就是法人
    private int enterpriseUserType = 0;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        binding.btnRegister.setOnClickListener(this::onClick);
        binding.rgSex.setOnCheckedChangeListener(new MyRadioButtonListener());
    }

    class MyRadioButtonListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_faren:
                    enterpriseUserType = 0;
                    break;
                case R.id.rb_other:
                    enterpriseUserType = 1;
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        int viewId = view.getId();
        switch (viewId) {
            case R.id.btn_register:
                if (TextUtils.isEmpty(binding.etUserName.getText().toString().trim()) ||
                        TextUtils.isEmpty(binding.etName.getText().toString().trim()) ||
                        TextUtils.isEmpty(binding.etPhone.getText().toString().trim()) ||
                        TextUtils.isEmpty(binding.etEmail.getText().toString().trim()) ||
                        TextUtils.isEmpty(binding.etVerifycode.getText().toString().trim()) ||
                        TextUtils.isEmpty(binding.etPassword.getText().toString().trim()) ||
                        TextUtils.isEmpty(binding.etPasswordAgain.getText().toString().trim())) {
                    ToastUtils.showToast("请输入必填信息后重试");
                    return;
                }

                if (!(binding.etPassword.getText().toString().trim()).equals((binding.etPasswordAgain.getText().toString().trim()))) {
                    ToastUtils.showToast("请输入相同的密码");
                    return;
                }

                RegisterReqVo registerReqVo = new RegisterReqVo();
                registerReqVo.setEnterpriseUserType(enterpriseUserType);
                registerReqVo.setEmail(binding.etEmail.getText().toString().trim());
                registerReqVo.setIdentityCard(binding.etVerifycode.getText().toString().trim());
                registerReqVo.setMobile(binding.etPhone.getText().toString().trim());
                registerReqVo.setPassword(AESUtils.encrypt(binding.etPassword.getText().toString().trim()));
                registerReqVo.setRealname(binding.etName.getText().toString().trim());
                registerReqVo.setUsername(binding.etUserName.getText().toString().trim());

                mViewModel.register(registerReqVo).observe(this, res -> {
                    res.handler(new OnCallback<String>() {
                        @Override
                        public void onSuccess(String data) {
                            ToastUtils.showToast("注册成功");
                            ActivityUtils.startActivity(RegisterActivity.this, GovassLoginActivity.class);
                            finish();
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
}
