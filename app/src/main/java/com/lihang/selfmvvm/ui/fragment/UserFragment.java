package com.lihang.selfmvvm.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.databinding.FragmentUserBinding;
import com.lihang.selfmvvm.ui.TestActivity;
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_user_info:
                ActivityUtils.startActivity(getContext(), TestActivity.class);
                break;
            default:
                break;
        }
    }
}