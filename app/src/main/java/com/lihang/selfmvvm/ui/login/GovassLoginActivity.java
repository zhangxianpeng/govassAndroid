package com.lihang.selfmvvm.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.MyApplication;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.common.SystemConst;
import com.lihang.selfmvvm.databinding.ActivityGovassLoginactivityBinding;
import com.lihang.selfmvvm.ui.main.BottonNavigationActivity;
import com.lihang.selfmvvm.ui.register.RegisterStepOneActivity;
import com.lihang.selfmvvm.utils.AESUtils;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.NoDoubleClickListener;
import com.lihang.selfmvvm.utils.PreferenceUtil;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.LoginReqVo;
import com.lihang.selfmvvm.vo.res.CsDataInfoVo;
import com.lihang.selfmvvm.vo.res.LoginDataVo;

import java.util.UUID;

import cn.yhq.dialog.core.DialogBuilder;

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
    private static final int RESPONSE_CODE = 10002;

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
        String verifyCodeImgUrl = SystemConst.DEFAULT_SERVER + SystemConst.CAPTCHA + "?uuid=" + uuid;
        Glide.with(this).load(verifyCodeImgUrl).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(binding.ivVerifycode);
    }

    @Override
    protected void setListener() {
        binding.ivVerifycode.setOnClickListener(mNoDoubleClickListener);
        binding.btnLogin.setOnClickListener(mNoDoubleClickListener);
        binding.tvRegister.setOnClickListener(mNoDoubleClickListener);
        binding.ivClearInputPwd.setOnClickListener(mNoDoubleClickListener);
        binding.etPhone.addTextChangedListener(this);
        binding.etUserPassword.addTextChangedListener(this);
        binding.etVerifycode.addTextChangedListener(this);
        binding.tvForgetPwd.setOnClickListener(mNoDoubleClickListener);
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
                    finish();
                    break;
                case R.id.iv_clear_input_pwd:
                    passwprd = "";
                    binding.etUserPassword.setText("");
                    break;
                case R.id.tv_forget_pwd:
                    mViewModel.getCustomerService(MyApplication.getToken()).observe(GovassLoginActivity.this, res -> {
                        res.handler(new OnCallback<CsDataInfoVo>() {
                            @Override
                            public void onSuccess(CsDataInfoVo data) {
                                showDialogContactCs(data.getPhone());
                            }
                        });
                    });
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 联系客服
     *
     * @param phone
     */
    private void showDialogContactCs(String phone) {
        DialogBuilder.alertDialog(this).setMessage("如需找回密码，请及时联系： " + phone)
                .setOnPositiveButtonClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                        startActivity(dialIntent);
                    }
                }).create().show();
    }

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
                            ToastUtils.showToast("登录成功");
                            ActivityUtils.startActivity(getContext(), BottonNavigationActivity.class);
                            finish();
                        }

                        @Override
                        public void onFailure(String msg) {
                            super.onFailure(msg);
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

        updateBtn();
        updateClearBtn();
    }

    private void updateBtn() {
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(passwprd) && !TextUtils.isEmpty(verifyCode)) {
            binding.btnLogin.setEnabled(true);
        } else {
            binding.btnLogin.setEnabled(false);
        }
    }

    private void updateClearBtn() {
        if (!TextUtils.isEmpty(passwprd)) {
            binding.ivClearInputPwd.setVisibility(View.VISIBLE);
        } else {
            binding.ivClearInputPwd.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

}