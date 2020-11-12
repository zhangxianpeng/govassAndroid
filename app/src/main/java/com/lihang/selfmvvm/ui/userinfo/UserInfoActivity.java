package com.lihang.selfmvvm.ui.userinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.MyApplication;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.customview.iosdialog.NewIOSAlertDialog;
import com.lihang.selfmvvm.databinding.ActivityUserInfoBinding;
import com.lihang.selfmvvm.ui.customserver.CustomServerActivity;
import com.lihang.selfmvvm.ui.login.GovassLoginActivity;
import com.lihang.selfmvvm.ui.mydeclare.MyDeclareActivity;
import com.lihang.selfmvvm.ui.myenterprises.EnterprisesDetailActivity;
import com.lihang.selfmvvm.ui.newmsg.NewMsgActivity;
import com.lihang.selfmvvm.ui.officialdoc.OfficialDocListActivity;
import com.lihang.selfmvvm.ui.project.ProjectActivity;
import com.lihang.selfmvvm.ui.share.ShareActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.utils.NoDoubleClickListener;
import com.lihang.selfmvvm.utils.PackageUtils;
import com.lihang.selfmvvm.utils.PreferenceUtil;
import com.lihang.selfmvvm.vo.req.TokenReqVo;

import static com.lihang.selfmvvm.base.BaseConstant.DEFAULT_FILE_SERVER;
import static com.lihang.selfmvvm.base.BaseConstant.USER_LOGIN_HEAD_URL;
import static com.lihang.selfmvvm.base.BaseConstant.USER_LOGIN_TOKEN;
import static com.lihang.selfmvvm.base.BaseConstant.USER_NICK_NAME;
import static com.lihang.selfmvvm.common.SystemConst.DEFAULT_SERVER;

/**
 * created by zhangxianpeng
 * 个人中心
 */
public class UserInfoActivity extends BaseActivity<UserInfoActivityViewModel, ActivityUserInfoBinding> {
    private String headUrl = "";
    private String realName = "";
    private NewIOSAlertDialog changeAccountDialog;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void processLogic() {
        binding.tvVersion.setText("V" + PackageUtils.getVersionName(getContext()));
        headUrl = (String) PreferenceUtil.get(USER_LOGIN_HEAD_URL, "");
        realName = (String) PreferenceUtil.get(USER_NICK_NAME, "");
        if (!TextUtils.isEmpty(headUrl))
            Glide.with(this).load(DEFAULT_SERVER + DEFAULT_FILE_SERVER + headUrl).placeholder(R.mipmap.default_tx_img)
                    .error(R.mipmap.default_tx_img).into(binding.ivHead);
        if (!TextUtils.isEmpty(realName)) binding.tvUserName.setText(realName);

        if (CheckPermissionUtils.getInstance().isGovernment()) {
            binding.projectDeclare.setText(getString(R.string.project_approval));
            binding.myMsg.setText(getString(R.string.my_post));
        } else {
            binding.projectDeclare.setText(getString(R.string.my_declaration));
            binding.myMsg.setText(getString(R.string.my_receive_msg));
        }

        getUnReadMsgCount();
    }

    private void getUnReadMsgCount() {
        mViewModel.getMsgUnRead().observe(this, res -> {
            res.handler(new OnCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    if (!TextUtils.isEmpty(data) && !data.equals("0")) {
                        binding.badgeView.setVisibility(View.VISIBLE);
                        binding.badgeView.setText(data);
                    }
                }
            });
        });
    }

    @Override
    protected void setListener() {
        binding.llUserInfo.setOnClickListener(mNoDoubleClickListener);
        binding.rlUserInfo.setOnClickListener(mNoDoubleClickListener);
        binding.llMyProject.setOnClickListener(mNoDoubleClickListener);
        binding.llMyArticle.setOnClickListener(mNoDoubleClickListener);
        binding.llMyCollect.setOnClickListener(mNoDoubleClickListener);
        binding.rlVerify.setOnClickListener(mNoDoubleClickListener);
        binding.rlShareDown.setOnClickListener(mNoDoubleClickListener);
        binding.rlContactUs.setOnClickListener(mNoDoubleClickListener);
        binding.rlChangeAccount.setOnClickListener(mNoDoubleClickListener);
        binding.rlExit.setOnClickListener(mNoDoubleClickListener);
        binding.flNewMsg.setOnClickListener(mNoDoubleClickListener);
    }

    private NoDoubleClickListener mNoDoubleClickListener = new NoDoubleClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            switch (view.getId()) {
                case R.id.ll_user_info:
                case R.id.rl_user_info:
                    ActivityUtils.startActivity(getContext(), EnterprisesDetailActivity.class);
                    break;
                case R.id.ll_my_project:
                    if (CheckPermissionUtils.getInstance().isGovernment()) {
                        ActivityUtils.startActivity(getContext(), ProjectActivity.class);
                    } else {
                        ActivityUtils.startActivity(getContext(), MyDeclareActivity.class);
                    }
                    break;
                case R.id.ll_my_article:
                    Bundle bundle = new Bundle();
                    bundle.putString("uiFlag", "myArticles");
                    ActivityUtils.startActivityWithBundle(getContext(), OfficialDocListActivity.class, bundle);
                    break;
                case R.id.ll_my_collect:
                    break;
                case R.id.rl_verify:  //认证中心
                    break;
                case R.id.rl_share_down:  //推广下载
                    ActivityUtils.startActivity(getContext(), ShareActivity.class);
                    break;
                case R.id.rl_contact_us:
                    ActivityUtils.startActivity(getContext(), CustomServerActivity.class);
                    break;
                case R.id.rl_change_account:
                    changeAccout();
                    break;
                case R.id.rl_exit:
                    logout();
                    break;
                case R.id.fl_new_msg:
                    ActivityUtils.startActivity(getContext(), NewMsgActivity.class);
                    break;
                default:
                    break;
            }
        }
    };

    private void changeAccout() {
        changeAccountDialog = new NewIOSAlertDialog(getContext()).builder();
        changeAccountDialog.setGone().setTitle("提示").setMsg("是否确定切换账号").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(UserInfoActivity.this, GovassLoginActivity.class);
                finish();
            }
        }).show();
    }

    private void logout() {
        changeAccountDialog = new NewIOSAlertDialog(getContext()).builder();
        changeAccountDialog.setGone().setTitle("提示").setMsg("是否确定退出系统").setNegativeButton("取消", null)
                .setPositiveButton("确定", view -> logoutAndClearToken()).show();
    }

    private void logoutAndClearToken() {
        TokenReqVo token = new TokenReqVo();
        token.setToken(MyApplication.getToken());
        mViewModel.govassLogout(token).observe(this, res -> {
            res.handler(new OnCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    PreferenceUtil.remove(USER_LOGIN_TOKEN);
                    setResult(3);
                    finish();
                }
            });
        });
    }

    @Override
    public void onClick(View view) {

    }
}
