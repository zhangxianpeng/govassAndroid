package com.lihang.selfmvvm.ui.login;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.common.SystemConst;
import com.lihang.selfmvvm.databinding.ActivityGovassLoginactivityBinding;
import com.lihang.selfmvvm.ui.register.RegisterStepOneActivity;
import com.lihang.selfmvvm.utils.AESUtils;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.LogUtils;
import com.lihang.selfmvvm.utils.NoDoubleClickListener;
import com.lihang.selfmvvm.utils.PreferenceUtil;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.LoginReqVo;
import com.lihang.selfmvvm.vo.res.LoginDataVo;

import java.util.UUID;

import static com.lihang.selfmvvm.base.BaseConstant.USER_LOGIN_TOKEN;

/**
 * 登录界面
 */
public class GovassLoginActivity extends BaseActivity<GovassLoginViewModel, ActivityGovassLoginactivityBinding> implements TextWatcher {
    private static final String TAG = "GovassLoginActivity";
    private String uuid = "";
    private String userName = "";
    private String passwprd = "";
    private String verifyCode = "";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_govass_loginactivity;
    }

    @Override
    protected void processLogic() {
        initVerifyCode();
    }

    private void initVerifyCode() {
        uuid = UUID.randomUUID().toString();
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

        binding.etPhone.addTextChangedListener(this);
        binding.etUserPassword.addTextChangedListener(this);
        binding.etVerifycode.addTextChangedListener(this);
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
                    doLogin();
                    break;
                case R.id.tv_register:
                    ActivityUtils.startActivity(getContext(), RegisterStepOneActivity.class);
                    break;
                default:
                    break;
            }
        }
    };


    private void doLogin() {
        LoginReqVo loginReqVo = new LoginReqVo();
        loginReqVo.setUuid(uuid);
        loginReqVo.setPassword(AESUtils.encrypt(passwprd));
        loginReqVo.setUsername(userName);
        loginReqVo.setCaptcha(verifyCode);
        mViewModel.govassLogin(loginReqVo)
                .observe(this, resource -> {
                    resource.handler(new OnCallback<LoginDataVo>() {
                        @Override
                        public void onSuccess(LoginDataVo data) {
                                String token = data.getToken();
                                PreferenceUtil.put(USER_LOGIN_TOKEN, token);
                                ToastUtils.showToast("登录成功，保存token到本地");
                        }

                        @Override
                        public void onFailure(String msg) {
                            LogUtils.e("HTTP","onFailure==="+msg);
                        }

                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                        }
                    });
                });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        userName = getStringByUI(binding.etPhone);
        passwprd = getStringByUI(binding.etUserPassword);
        verifyCode = getStringByUI(binding.etVerifycode);

        if (TextUtils.isEmpty(userName)) {
            ToastUtils.showToast("用户名不能为空~");
            return;
        }

        if (TextUtils.isEmpty(passwprd)) {
            ToastUtils.showToast("密码不能为空");
            return;
        }

        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtils.showToast("验证码不能为空");
            return;
        }
        binding.btnLogin.setEnabled(true);

    }
}