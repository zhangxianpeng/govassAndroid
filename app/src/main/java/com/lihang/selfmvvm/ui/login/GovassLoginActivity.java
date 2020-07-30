package com.lihang.selfmvvm.ui.login;

import android.view.View;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.common.SystemConst;
import com.lihang.selfmvvm.databinding.ActivityGovassLoginactivityBinding;
import com.lihang.selfmvvm.ui.register.RegisterStepOneActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.CodeUtils;
import com.lihang.selfmvvm.utils.LogUtils;
import com.lihang.selfmvvm.utils.NoDoubleClickListener;

import java.util.UUID;

/**
 * 登录界面
 */
public class GovassLoginActivity extends BaseActivity<GovassLoginViewModel, ActivityGovassLoginactivityBinding> {
    private static final String TAG = "GovassLoginActivity";
    private CodeUtils codeUtils;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_govass_loginactivity;
    }

    @Override
    protected void processLogic() {
        initVerifyCode();
    }

    private void initVerifyCode() {
        String uuid = UUID.randomUUID().toString();
        LogUtils.d("zhangxianpenguuid===", uuid);
        String verifyCodeImgUrl = SystemConst.DEFAULT_SERVER + SystemConst.CAPTCHA + "?uuid=" + uuid;
        Glide.with(this).load(verifyCodeImgUrl).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(binding.ivVerifycode);
    }

    @Override
    protected void setListener() {
        binding.ivVerifycode.setOnClickListener(mNoDoubleClickListener);
        binding.btnLogin.setOnClickListener(mNoDoubleClickListener);
        binding.tvRegister.setOnClickListener(mNoDoubleClickListener);
    }


    private NoDoubleClickListener mNoDoubleClickListener = new NoDoubleClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            switch (view.getId()) {
                case R.id.iv_verifycode:
                    initVerifyCode();
                    break;
                case R.id.btn_login:
                    //登录

                    break;
                case R.id.tv_register:
                    ActivityUtils.startActivity(getContext(), RegisterStepOneActivity.class);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {

    }
}