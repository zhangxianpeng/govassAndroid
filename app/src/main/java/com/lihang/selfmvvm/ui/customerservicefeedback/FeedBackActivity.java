package com.lihang.selfmvvm.ui.customerservicefeedback;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.adapter.CommunicateMsgAdapter;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityFeedBackBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.model.CommunicateMsgvO;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * created by zhangxianpeng
 * 客服反馈
 */
public class FeedBackActivity extends BaseActivity<FeedBackViewModel, ActivityFeedBackBinding> implements TextWatcher {

    private List<CommunicateMsgvO> msgList = new ArrayList<>();
    private CommunicateMsgAdapter msgAdapter;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void processLogic() {
        initAdapter();
        initData();
    }

    private void initData() {
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvCommunicate.setLayoutManager(layoutManager);
        msgAdapter = new CommunicateMsgAdapter(this, msgList);
        binding.rvCommunicate.setAdapter(msgAdapter);
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
        binding.etInput.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        updateSendBtn();
    }

    private void updateSendBtn() {
        String input = binding.etInput.getText().toString().trim();
        binding.btnSend.setEnabled(TextUtils.isEmpty(input) ? false : true);
    }
}
