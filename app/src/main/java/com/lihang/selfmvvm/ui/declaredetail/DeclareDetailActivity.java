package com.lihang.selfmvvm.ui.declaredetail;

import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityDeclareDetailBinding;

/**
 * 申报详情
 */
public class DeclareDetailActivity extends BaseActivity<DeclareDetailViewModel, ActivityDeclareDetailBinding> {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_declare_detail;
    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        binding.ibtnTitleBarBack.setOnClickListener(this::onClick);
        binding.btnAdopt.setOnClickListener(this::onClick);
        binding.btnNotadopt.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_title_bar_back:
                finish();
                break;
            case R.id.btn_adopt:
                binding.btnNotadopt.setBackground(getContext().getDrawable(R.mipmap.ic_moren));
                binding.btnAdopt.setBackground(getContext().getDrawable(R.mipmap.ic_pass));
//                binding.btnAdopt.setTextColor();
                break;
            case R.id.btn_notadopt:
                binding.btnNotadopt.setBackground(getContext().getDrawable(R.mipmap.ic_fail));
                binding.btnAdopt.setBackground(getContext().getDrawable(R.mipmap.ic_moren));
                break;
            default:
                break;
        }

    }
}