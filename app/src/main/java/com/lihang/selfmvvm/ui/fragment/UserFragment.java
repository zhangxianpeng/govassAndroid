package com.lihang.selfmvvm.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.MyApplication;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.databinding.FragmentUserBinding;
import com.lihang.selfmvvm.ui.customserver.CustomServerActivity;
import com.lihang.selfmvvm.ui.login.GovassLoginActivity;
import com.lihang.selfmvvm.ui.mydeclare.MyDeclareActivity;
import com.lihang.selfmvvm.ui.newmsg.NewMsgActivity;
import com.lihang.selfmvvm.ui.officialdoc.OfficialDocListActivity;
import com.lihang.selfmvvm.ui.project.ProjectActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.utils.NoDoubleClickListener;
import com.lihang.selfmvvm.utils.PreferenceUtil;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.TokenReqVo;
import com.lihang.selfmvvm.vo.res.UploadAttachmentResVo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
        mViewModel.getMsgUnRead().observe(getActivity(), res -> {
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
                    if (CheckPermissionUtils.getInstance().isGovernment()) {
                        ActivityUtils.startActivity(getContext(), ProjectActivity.class);
                    } else {
                        ActivityUtils.startActivity(getContext(), MyDeclareActivity.class);
                    }
                    break;
                case R.id.ll_my_article:
                    ActivityUtils.startActivity(getContext(), OfficialDocListActivity.class);
                    break;
                case R.id.ll_my_collect:
                    break;
                case R.id.rl_verify:
                    break;
                case R.id.rl_share_down:
                    String filePath = "/storage/emulated/0/Tencent/TIMfile_recv/test.jpg";
                    File file = new File(filePath);
                    List<File> fileList = new ArrayList<>();
                    fileList.add(file);
                    doMultyUploadFile(fileList);
                    break;
                case R.id.rl_contact_us:
                    ActivityUtils.startActivity(getContext(), CustomServerActivity.class);
                    break;
                case R.id.rl_change_account:
                    logout(1);
                    break;
                case R.id.rl_exit:
                    logout(2);
                    break;
                case R.id.fl_new_msg:
                    ActivityUtils.startActivity(getContext(), NewMsgActivity.class);
                    break;
                default:
                    break;
            }
        }
    };

    private void doMultyUploadFile(List<File> fileList) {
        List<MultipartBody.Part> parts
                = new ArrayList<>();
        for (File file : fileList) {
            RequestBody body = MultipartBody.create(MultipartBody.FORM, file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), body);
            parts.add(part);
        }

        mViewModel.uploadMultyFile(parts).observe(this, res -> {
            res.handler(new OnCallback<List<UploadAttachmentResVo>>() {
                @Override
                public void onSuccess(List<UploadAttachmentResVo> data) {
                    ToastUtils.showToast(data.get(0).getFileName());
                }
            });
        });
    }


    private void logout(int flag) {
        TokenReqVo token = new TokenReqVo();
        token.setToken(MyApplication.getToken());
        mViewModel.govassLogout(token).observe(this, res -> {
            res.handler(new OnCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    PreferenceUtil.clear();
                    switch (flag) {
                        case 1:   //切换账号
                            ActivityUtils.startActivity(getContext(), GovassLoginActivity.class);
                            getActivity().finish();
                            break;
                        case 2:  //退出
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(0);
                            break;
                        default:
                            break;
                    }
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