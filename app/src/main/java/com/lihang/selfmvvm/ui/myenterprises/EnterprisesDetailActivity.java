package com.lihang.selfmvvm.ui.myenterprises;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityEnterprisesDetailBinding;
import com.lihang.selfmvvm.ui.bigpicture.BigPictureActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.EnterpriseVo;
import com.lihang.selfmvvm.vo.res.UserInfoVo;

import static com.lihang.selfmvvm.base.BaseConstant.DEFAULT_FILE_SERVER;
import static com.lihang.selfmvvm.common.SystemConst.DEFAULT_SERVER;

public class EnterprisesDetailActivity extends BaseActivity<EnterprisesDetailViewModel, ActivityEnterprisesDetailBinding> {

    private EnterpriseVo enterpriseVo;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_enterprises_detail;
    }

    @Override
    protected void processLogic() {
        enterpriseVo = (EnterpriseVo) getIntent().getSerializableExtra("enterpriseVo");
        if (enterpriseVo == null) {  //个人资料/个人信息
            binding.slEnterprises.setVisibility(View.GONE);
            binding.slUserInfo.setVisibility(View.VISIBLE);
            binding.tvMsg.setText(getContext().getString(R.string.personal_data));
            mViewModel.getUserInfo().observe(this, res -> {
                res.handler(new OnCallback<UserInfoVo>() {
                    @Override
                    public void onSuccess(UserInfoVo data) {
                        Glide.with(EnterprisesDetailActivity.this).load(DEFAULT_SERVER + DEFAULT_FILE_SERVER + data.getHeadUrl()).placeholder(R.mipmap.default_tx_img)
                                .error(R.mipmap.default_tx_img).into(binding.ivUserHead);
                        binding.tvUserEmail.setText(data.getEmail());
                        binding.tvUserPhone.setText(data.getMobile());
                        binding.tvUserName.setText(data.getUsername());
                        binding.tvUserRealName.setText(data.getRealname());
                    }
                });
            });

        } else {  //我的企业
            binding.tvMsg.setText(getContext().getString(R.string.my_business));
            binding.slEnterprises.setVisibility(View.VISIBLE);
            binding.slUserInfo.setVisibility(View.GONE);
            binding.tvEnterAddress.setText(enterpriseVo.getAddress());
            Glide.with(this).load(DEFAULT_SERVER + DEFAULT_FILE_SERVER + enterpriseVo.getBusinessLicenseImg()).placeholder(R.mipmap.default_img)
                    .error(R.mipmap.default_img).into(binding.ivBusinessLicenseImg);
            binding.tvBusinessTerm.setText(enterpriseVo.getBusinessTerm());
            binding.tvType.setText(enterpriseVo.getBusinessType());
            binding.tvEnterpriseCode.setText(enterpriseVo.getEnterpriseCode());
            binding.tvEnterpriseName.setText(enterpriseVo.getEnterpriseName());
            binding.tvLegalRepresentative.setText(enterpriseVo.getLegalRepresentative());
            binding.tvRegisteredCapital.setText(enterpriseVo.getRegisteredCapital());
            binding.tvSetupDate.setText(enterpriseVo.getSetUpDate());
            binding.tvBusinessScope.setText(enterpriseVo.getBusinessScope());
        }

    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
        binding.ivBusinessLicenseImg.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.iv_businessLicense_img:
                Bundle bundle = new Bundle();
                bundle.putString("imgUrl", enterpriseVo.getBusinessLicenseImg());
                ActivityUtils.startActivityWithBundle(this, BigPictureActivity.class, bundle);
                break;
            default:
                break;
        }
    }
}
