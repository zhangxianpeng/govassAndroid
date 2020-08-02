package com.lihang.selfmvvm.ui.register;

import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityRegisterStepOneBinding;
import com.lihang.selfmvvm.ui.login.GovassLoginActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.NoDoubleClickListener;


public class RegisterStepOneActivity extends BaseActivity<RegisterStepOneViewModel, ActivityRegisterStepOneBinding> {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register_step_one;
    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        binding.ibtnTitleBarBack.setOnClickListener(mNoDoubleClickListener);
        binding.tvNextStep.setOnClickListener(mNoDoubleClickListener);
        binding.ivHead.setOnClickListener(mNoDoubleClickListener);
        binding.tvLogin.setOnClickListener(mNoDoubleClickListener);
        binding.flAddCompanyFile.setOnClickListener(mNoDoubleClickListener);

    }

    private NoDoubleClickListener mNoDoubleClickListener = new NoDoubleClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            switch (view.getId()) {
                case R.id.ibtn_title_bar_back:
                    finish();
                    break;
                case R.id.tv_next_step:
                    gotoNextStep();
                    break;
                case R.id.iv_head:
                    break;
                case R.id.tv_login:
                    ActivityUtils.startActivity(RegisterStepOneActivity.this, GovassLoginActivity.class);
                    break;
                case R.id.fl_add_company_file:
                    showPicDialog();
                    break;
                default:
                    break;
            }
        }
    };

    private void gotoNextStep() {
        //TODO
    }

    private void showPicDialog() {
        //TODO
    }

    @Override
    public void onClick(View view) {

    }
}
