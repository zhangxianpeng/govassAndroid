package com.lihang.selfmvvm.ui.enpriceofficedoc;

import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityOdDetailBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.EnpriceOdVo;

/**
 * created by zhangxianpeng
 * 企业公告详情界面
 */
public class OdDetailActivity extends BaseActivity<EnpriceODViewModel, ActivityOdDetailBinding> {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_od_detail;
    }

    @Override
    protected void processLogic() {
        EnpriceOdVo enpriceOdVo = (EnpriceOdVo) getIntent().getSerializableExtra("enpriceOdVo");
        binding.tvOdTitle.setText(enpriceOdVo.getTitle());
        binding.tvContent.setText(enpriceOdVo.getContent());
        binding.tvEnpriceName.setText(enpriceOdVo.getEnterpriseName());
        binding.tvCreattime.setText(enpriceOdVo.getCreateTime());
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }
}
