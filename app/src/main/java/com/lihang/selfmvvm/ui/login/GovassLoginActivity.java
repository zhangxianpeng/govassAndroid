package com.lihang.selfmvvm.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.MyApplication;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.common.SystemConst;
import com.lihang.selfmvvm.customview.iosdialog.NewIOSAlertDialog;
import com.lihang.selfmvvm.databinding.ActivityGovassLoginactivityBinding;
import com.lihang.selfmvvm.ui.main.BottonNavigationActivity;
import com.lihang.selfmvvm.ui.register.RegisterActivity;
import com.lihang.selfmvvm.utils.AESUtils;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.NoDoubleClickListener;
import com.lihang.selfmvvm.utils.PreferenceUtil;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.LoginReqVo;
import com.lihang.selfmvvm.vo.res.CsDataInfoVo;
import com.lihang.selfmvvm.vo.res.LoginDataVo;
import com.lihang.selfmvvm.vo.res.UserInfoVo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.lihang.selfmvvm.base.BaseConstant.USER_LOGIN_TOKEN;

/**
 * created by zhangxianpeng
 * 登录界面
 */
public class GovassLoginActivity extends BaseActivity<GovassLoginViewModel, ActivityGovassLoginactivityBinding> implements TextWatcher {
    private String uuid = "";
    private String userName = "";
    private String passwprd = "";
    private String verifyCode = "";

    //已登录用户数据
    private List<UserInfoVo> loginUserList = new ArrayList<>();
    private List<String> loginUserStrList = new ArrayList<>();

    //已登录用户列表
    private static final String LOGIN_USER_LIST = "loginUserList";
    private NewIOSAlertDialog myDialog;

    private boolean isShowPassword = false;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_govass_loginactivity;
    }

    @Override
    protected void processLogic() {
        initVerifyCode();
        initLoginUserList();
    }

    private void initVerifyCode() {
        uuid = UUID.randomUUID().toString();
        String verifyCodeImgUrl = SystemConst.DEFAULT_SERVER + SystemConst.CAPTCHA + "?uuid=" + uuid;
        Glide.with(this).load(verifyCodeImgUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(binding.ivVerifycode);
    }

    private void initLoginUserList() {
        loginUserList = PreferenceUtil.getDataList(LOGIN_USER_LIST);
        if (loginUserList.size() > 0) {
            binding.ivDown.setVisibility(View.VISIBLE);
            loginUserStrList = change2StringList(loginUserList);
        } else {
            binding.ivDown.setVisibility(View.GONE);
        }
    }

    private List<String> change2StringList(List<UserInfoVo> loginUserList) {
        List<String> newList = new ArrayList<>();
        for (UserInfoVo userInfoVo : loginUserList) {
            newList.add(userInfoVo.getUsername());
        }
        return newList;
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
        binding.ivDown.setOnClickListener(mNoDoubleClickListener);
    }

    /**
     * 展示已登录的用户列表
     */
    private void showListPopulWindow() {
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, loginUserStrList));
        listPopupWindow.setAnchorView(binding.etPhone);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener((adapterView, view, i, l) -> {
            binding.etPhone.setText(loginUserStrList.get(i));
            binding.etUserPassword.setText(getPwd(loginUserStrList.get(i)));
            updateSwitchView();
            listPopupWindow.dismiss();
        });
        listPopupWindow.show();
    }

    private void updateSwitchView() {
        if (!TextUtils.isEmpty(getStringByUI(binding.etUserPassword))) {
            binding.switchRememberPwd.setChecked(true);
        } else {
            binding.switchRememberPwd.setChecked(false);
        }
    }

    private String getPwd(String userName) {
        String pwd = "";
        for (UserInfoVo userInfoVo : loginUserList) {
            if (userName.equals(userInfoVo.getUsername())) {
                pwd = userInfoVo.getPassword();
            }
        }
        return pwd;
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
                    ActivityUtils.startActivity(getContext(), RegisterActivity.class);
                    finish();
                    break;
                case R.id.iv_clear_input_pwd:
                    if(!isShowPassword) {
                        isShowPassword = true;
                        binding.ivClearInputPwd.setImageResource(R.mipmap.icon_password_visible);
                        binding.etUserPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }else {
                        isShowPassword = false;
                        binding.ivClearInputPwd.setImageResource(R.mipmap.icon_password_invisible);
                        binding.etUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    break;
                case R.id.tv_forget_pwd:
                    mViewModel.getCustomerService(MyApplication.getToken()).observe(GovassLoginActivity.this, res -> {
                        res.handler(new OnCallback<CsDataInfoVo>() {
                            @Override
                            public void onSuccess(CsDataInfoVo data) {
                                showDialogContactCs();
                            }
                        });
                    });
                    break;
                case R.id.iv_down:
                    showListPopulWindow();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 联系客服
     */
    private void showDialogContactCs() {
        String phone = "892731274";
        myDialog = new NewIOSAlertDialog(getContext()).builder();
        myDialog.setGone().setMsg("需重置密码，请联系系统管理员，联系方式：" + phone).setNegativeButton("取消", null).setPositiveButton("拨打", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                startActivity(dialIntent);
            }
        }).show();
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
                            saveLoginUser(userName, passwprd);
                            String token = data.getToken();
                            PreferenceUtil.put(USER_LOGIN_TOKEN, token);
                            ToastUtils.showToast("登录成功");
                            ActivityUtils.startActivity(getContext(), BottonNavigationActivity.class);
                            finish();
                        }
                    });
                });
    }

    /**
     * 保存已登陆的账号密码
     *
     * @param userName
     * @param passwprd
     */
    private void saveLoginUser(String userName, String passwprd) {
        loginUserList = PreferenceUtil.getDataList(LOGIN_USER_LIST);
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUsername(userName);
        if (loginUserList.size() < 1) {   //原来没数据就直接加
            if (binding.switchRememberPwd.isChecked()) {
                userInfoVo.setPassword(passwprd);
            } else {
                userInfoVo.setPassword("");
            }
            loginUserList.add(userInfoVo);
            PreferenceUtil.setDataList(LOGIN_USER_LIST, loginUserList);
        } else {   //原来有数据先过滤再加
            if (!checkIsExistence(userName, loginUserList)) {
                if (binding.switchRememberPwd.isChecked()) {
                    userInfoVo.setPassword(passwprd);
                } else {
                    userInfoVo.setPassword("");
                }
                loginUserList.add(userInfoVo);
                PreferenceUtil.setDataList(LOGIN_USER_LIST, loginUserList);
            } else {
                if (binding.switchRememberPwd.isChecked()) {
                    userInfoVo.setPassword(passwprd);
                } else {
                    userInfoVo.setPassword("");
                }
                loginUserList.remove(getRepeatPosition(userName, loginUserList));
                loginUserList.add(userInfoVo);
                PreferenceUtil.setDataList(LOGIN_USER_LIST, loginUserList);
            }
        }
    }

    /**
     * 过滤已保存的账号
     *
     * @param userName
     * @param loginUserList
     * @return
     */
    private boolean checkIsExistence(String userName, List<UserInfoVo> loginUserList) {
        boolean isReapeat = false;
        for (UserInfoVo userInfoVo : loginUserList) {
            if (userInfoVo.getUsername().equals(userName)) isReapeat = true;
        }
        return isReapeat;
    }

    private int getRepeatPosition(String userName, List<UserInfoVo> loginUserList) {
        int position = 0;
        for (int i = 0; i < loginUserList.size(); i++) {
            if (loginUserList.get(i).getUsername().equals(userName)) position = i;
        }
        return position;
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