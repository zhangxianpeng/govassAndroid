package com.lihang.selfmvvm.ui.officialdoc;

import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityOfficialDocDetailBinding;
import com.lihang.selfmvvm.vo.res.OfficialDocResVo;

public class OfficialDocDetailActivity extends BaseActivity<OfficialDocDetailViewModel, ActivityOfficialDocDetailBinding> {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_official_doc_detail;
    }

    @Override
    protected void processLogic() {
        int id = getIntent().getIntExtra("id", 0);
        mViewModel.getOfficalDocDetail(id).observe(this, res -> {
            res.handler(new OnCallback<OfficialDocResVo>() {
                @Override
                public void onSuccess(OfficialDocResVo data) {
                    binding.tvTitle.setText(data.getTitle());
                    binding.tvCreatTime.setText(getString(R.string.createTime) + data.getCreateTime());
                    binding.tvContent.setText(data.getContent());
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
