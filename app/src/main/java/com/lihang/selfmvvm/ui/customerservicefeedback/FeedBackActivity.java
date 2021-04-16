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
import com.lihang.selfmvvm.utils.LogUtils;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.model.CommunicateMsgVo;
import com.lihang.selfmvvm.vo.req.AddFeedBackReqVo;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * created by zhangxianpeng
 * 客服反馈
 */
public class FeedBackActivity extends BaseActivity<FeedBackViewModel, ActivityFeedBackBinding> implements TextWatcher {

    private List<CommunicateMsgVo> msgList = new ArrayList<>();
    private CommunicateMsgAdapter msgAdapter;

    private AddFeedBackReqVo addFeedBackReqVo;
    //会话id
    private int id = -1;
    //状态
    private int status;

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
        id = getIntent().getIntExtra("id", -1);
        status = getIntent().getIntExtra("status", -1);
        if (id != -1) getCommunicateInfo(id);
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
        binding.btnSend.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.btn_send:
                sendFeedBack();
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
        if (status == 1) {
            binding.btnSend.setEnabled(false);
        }
    }

    private void sendFeedBack() {
        addFeedBackReqVo = new AddFeedBackReqVo();
        addFeedBackReqVo.setContent(binding.etInput.getText().toString().trim());
        addFeedBackReqVo.setTitle("app无标题输入框");
        mViewModel.saveFeedBack(addFeedBackReqVo).observe(this, res -> {
            res.handler(new OnCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    //请求info
                    ToastUtils.showToast("消息反馈成功，请持续关注");
                    finish();
                }
            });
        });
    }

    private void getCommunicateInfo(int id) {
        mViewModel.getFeedBackInfo(id).observe(this, res -> {
            res.handler(new OnCallback<CommunicateMsgVo>() {
                @Override
                public void onSuccess(CommunicateMsgVo data) {
                    msgList.clear();
                    CommunicateMsgVo sendData = new CommunicateMsgVo();
                    String userName = data.getUsername();
                    String answer = data.getAnswer();
                    String enterpriseName = data.getEnterpriseName();
                    String result = !TextUtils.isEmpty(enterpriseName) ? (userName + "-" + enterpriseName) : userName;
                    sendData.setSend(true);
                    if (data != null) {
                        sendData.setContent(data.getContent());
                        sendData.setUsername(result);
                        sendData.setAttachmentList(data.getAttachmentList());
                    }
                    msgList.add(sendData);

                    CommunicateMsgVo receiveData = new CommunicateMsgVo();
                    receiveData.setSend(false);
                    if (data != null) {
                        receiveData.setAnswer(answer);
                        receiveData.setAttachmentList(data.getAttachmentList());
                    }
                    msgList.add(receiveData);

                    if (msgAdapter != null) msgAdapter.notifyDataSetChanged();
                }
            });
        });
    }
}
