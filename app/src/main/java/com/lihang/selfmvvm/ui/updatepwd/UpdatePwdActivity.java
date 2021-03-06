package com.lihang.selfmvvm.ui.updatepwd;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityUpdatePwdBinding;
import com.lihang.selfmvvm.utils.AESUtils;
import com.lihang.selfmvvm.utils.ToastUtils;

public class UpdatePwdActivity extends BaseActivity<UpdatePwdActivityViewModel, ActivityUpdatePwdBinding> {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_update_pwd;
    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        binding.btnLogin.setOnClickListener(view -> {
            String pwd = binding.etPassword.getText().toString().trim();
            String newPwd = binding.etNewPassword.getText().toString().trim();
            String douBleNewPwd = binding.etUserPassword.getText().toString().trim();
            if (TextUtils.isEmpty(pwd)) {
                ToastUtils.showToast("密码不能为空");
                return;
            }
            if (TextUtils.isEmpty(newPwd)) {
                ToastUtils.showToast("新密码不能为空");
                return;
            }
            if (TextUtils.isEmpty(douBleNewPwd)) {
                ToastUtils.showToast("确认密码不能为空");
                return;
            }
            if (!newPwd.equals(douBleNewPwd)) {
                ToastUtils.showToast("确保密码一致");
                return;
            }
            UpdatePwdBean updatePwdBean = new UpdatePwdBean();
            updatePwdBean.setPassword(AESUtils.encrypt(pwd));
            updatePwdBean.setNewPassword(AESUtils.encrypt(newPwd));
            mViewModel.updatePwd(updatePwdBean).observe(this, resource -> {
                resource.handler(new OnCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        ToastUtils.showToast("修改成功,请重新登陆");
                        Intent i = new Intent();
                        setResult(1003, i);
                        finish();
                    }
                });
            });
        });
    }

    @Override
    public void onClick(View view) {
    }
}
