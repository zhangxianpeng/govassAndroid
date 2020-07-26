package com.lihang.selfmvvm.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.databinding.FragmentUserBinding;
import com.lihang.selfmvvm.ui.TestActivity;
import com.lihang.selfmvvm.ui.newmsg.NewMsgActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;

public class UserFragment extends BaseFragment<UserFragmentViewModel, FragmentUserBinding> {

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {
        binding.llUserInfo.setOnClickListener(this::onClick);
        binding.llMyProject.setOnClickListener(this::onClick);
        binding.llMyArticle.setOnClickListener(this::onClick);
        binding.llMyCollect.setOnClickListener(this::onClick);
        binding.rlVerify.setOnClickListener(this::onClick);
        binding.rlShareDown.setOnClickListener(this::onClick);
        binding.rlContactUs.setOnClickListener(this::onClick);
        binding.rlChangeAccount.setOnClickListener(this::onClick);
        binding.rlExit.setOnClickListener(this::onClick);
        binding.flNewMsg.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
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
                break;
            case R.id.rl_contact_us:
                break;
            case R.id.rl_change_account:
                break;
            case R.id.rl_exit:
                ActivityUtils.startActivity(getContext(), TestActivity.class);
                break;
            case R.id.fl_new_msg:
                ActivityUtils.startActivity(getContext(), NewMsgActivity.class);
                break;
            default:
                break;
        }
    }
}