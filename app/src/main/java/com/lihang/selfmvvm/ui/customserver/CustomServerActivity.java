package com.lihang.selfmvvm.ui.customserver;

import android.view.View;

import com.lihang.selfmvvm.MyApplication;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityCustomServerBinding;
import com.lihang.selfmvvm.vo.res.CsDataInfoVo;

public class CustomServerActivity extends BaseActivity<CustomServerViewModel, ActivityCustomServerBinding> {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_custom_server;
    }

    @Override
    protected void processLogic() {
        mViewModel.getCustomerService(MyApplication.getToken()).observe(this, res -> {
            res.handler(new OnCallback<CsDataInfoVo>() {
                @Override
                public void onSuccess(CsDataInfoVo data) {
                    binding.tvPhone.setText(getString(R.string.phoneNumber) + "  " + data.getPhone());
                    binding.tvEmail.setText(getString(R.string.email) + "  " + data.getEmail());
                    binding.tvAddress.setText(getString(R.string.address) + "  " + data.getAddress());
                }
            });
        });
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }
}
