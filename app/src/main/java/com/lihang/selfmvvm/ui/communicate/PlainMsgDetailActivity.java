package com.lihang.selfmvvm.ui.communicate;

import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityPlainMsgDetailBinding;
import com.lihang.selfmvvm.vo.res.PlainMsgResVo;

/**
 * created by zhangxianpeng
 * 普通消息
 */
public class PlainMsgDetailActivity extends BaseActivity<PlainMsgDetailViewModel, ActivityPlainMsgDetailBinding> {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_plain_msg_detail;
    }

    @Override
    protected void processLogic() {
        int id = getIntent().getIntExtra("id", 0);
        mViewModel.getPlainMsgDetail(id).observe(this, res -> {
            res.handler(new OnCallback<PlainMsgResVo>() {
                @Override
                public void onSuccess(PlainMsgResVo data) {
                    binding.tvContent.setText(data.getContent());
                    binding.tvMsg.setText(data.getTitle());
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
