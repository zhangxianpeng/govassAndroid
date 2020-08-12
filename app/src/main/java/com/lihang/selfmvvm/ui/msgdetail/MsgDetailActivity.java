package com.lihang.selfmvvm.ui.msgdetail;

import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityMsgDetailBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;

public class MsgDetailActivity extends BaseActivity<MsgDetailActivityViewModel, ActivityMsgDetailBinding> {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_msg_detail;
    }

    @Override
    protected void processLogic() {
        int id = getIntent().getIntExtra("id", 0);
        mViewModel.getMsgDetail(id).observe(this, res -> {
            res.handler(new OnCallback<MsgMeResVo>() {
                @Override
                public void onSuccess(MsgMeResVo data) {
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
        if (ButtonClickUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }
}
