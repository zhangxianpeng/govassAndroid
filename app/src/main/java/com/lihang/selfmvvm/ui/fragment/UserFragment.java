package com.lihang.selfmvvm.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.MyApplication;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.databinding.FragmentUserBinding;
import com.lihang.selfmvvm.ui.customserver.CustomServerActivity;
import com.lihang.selfmvvm.ui.login.GovassLoginActivity;
import com.lihang.selfmvvm.ui.newmsg.NewMsgActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.utils.NoDoubleClickListener;
import com.lihang.selfmvvm.utils.PreferenceUtil;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.TokenReqVo;

import java.io.File;

import static com.lihang.selfmvvm.base.BaseConstant.DEFAULT_FILE_SERVER;
import static com.lihang.selfmvvm.base.BaseConstant.USER_LOGIN_HEAD_URL;
import static com.lihang.selfmvvm.base.BaseConstant.USER_NICK_NAME;
import static com.lihang.selfmvvm.common.SystemConst.DEFAULT_SERVER;

public class UserFragment extends BaseFragment<UserFragmentViewModel, FragmentUserBinding> {
    private String headUrl = "";
    private String realName = "";

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        headUrl = (String) PreferenceUtil.get(USER_LOGIN_HEAD_URL, "");
        realName = (String) PreferenceUtil.get(USER_NICK_NAME, "");
        if (!TextUtils.isEmpty(headUrl))
            Glide.with(this).load(DEFAULT_SERVER + DEFAULT_FILE_SERVER + headUrl).placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher).into(binding.ivHead);
        if (!TextUtils.isEmpty(realName)) binding.tvUserName.setText(realName);

        if (CheckPermissionUtils.getInstance().isGovernment()) {
            binding.projectDeclare.setText(getString(R.string.project_approval));
            binding.myMsg.setText(getString(R.string.my_post));
        } else {
            binding.projectDeclare.setText(getString(R.string.my_declaration));
            binding.myMsg.setText(getString(R.string.my_receive_msg));
        }
    }

    @Override
    protected void setListener() {
        binding.llUserInfo.setOnClickListener(mNoDoubleClickListener);
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
                    break;
                case R.id.ll_my_project:
                    break;
                case R.id.ll_my_article:
                    break;
                case R.id.ll_my_collect:
                    break;
                case R.id.rl_verify:
                    break;
                case R.id.rl_share_down:
                    String filePath = "/storage/emulated/0/Tencent/TIMfile_recv/test.jpg";
                    File file = new File(filePath);
                    mViewModel.testUploadFile("image/jpg", file);
                    break;
                case R.id.rl_contact_us:
                    ActivityUtils.startActivity(getContext(), CustomServerActivity.class);
                    break;
                case R.id.rl_change_account:
                    ActivityUtils.startActivity(getContext(), GovassLoginActivity.class);
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


    private void logout() {
        TokenReqVo token = new TokenReqVo();
        token.setToken(MyApplication.getToken());
        mViewModel.govassLogout(token).observe(this, res -> {
            res.handler(new OnCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    ToastUtils.showToast(data);
                    PreferenceUtil.clear();
                    System.exit(0);
                }
            });
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}