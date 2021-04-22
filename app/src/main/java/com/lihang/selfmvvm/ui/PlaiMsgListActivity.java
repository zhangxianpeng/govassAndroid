package com.lihang.selfmvvm.ui;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityPlaiMsgListBinding;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;
import com.lihang.selfmvvm.vo.res.PlainMsgResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

public class PlaiMsgListActivity extends BaseActivity<CommonApiViewModel, ActivityPlaiMsgListBinding> {
    private List<PlainMsgResVo> msgList = new ArrayList<>();
    private CommonAdapter msgAdapter;
    private int page = 1;
    private boolean isClearData = true;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_plai_msg_list;
    }

    @Override
    protected void processLogic() {
        initView();
        initAdapter();
        initData(page, isClearData);
    }

    private void initView() {
        binding.ivTitleBarBack.setOnClickListener(view -> finish());
        binding.tvTitle.setText("政府端发送消息列表");
        binding.smartfreshlayout.setOnRefreshListener(this::refresh);
        binding.smartfreshlayout.setOnLoadMoreListener(this::loadMore);
    }

    private void refresh(RefreshLayout refresh) {
        initData(1, true);
        binding.smartfreshlayout.finishRefresh();
    }

    private void loadMore(RefreshLayout layout) {
        page += 1;
        initData(page, false);
        binding.smartfreshlayout.finishLoadMore();
    }

    private void initAdapter() {
        msgAdapter = new CommonAdapter<PlainMsgResVo>(this, R.layout.adapter_plainmsg_item, msgList) {

            @Override
            protected void convert(ViewHolder holder, PlainMsgResVo msgMeResVo, int position) {
                String plainMsg = msgMeResVo.getTitle();
                String createUser = msgMeResVo.getCreateUser();
                String createTime = msgMeResVo.getCreateTime();
                int plainMsgId = msgMeResVo.getId();
                holder.setText(R.id.tv_plainmsg_title, TextUtils.isEmpty(plainMsg) ? "" : plainMsg);
                holder.setText(R.id.tv_sender, TextUtils.isEmpty(createUser) ? "" : "发送者：" + createUser);
                holder.setText(R.id.tv_send_time, TextUtils.isEmpty(createTime) ? "" : createTime);

                holder.setOnClickListener(R.id.ll_container, (view -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("plainMsgId", plainMsgId);
                    ActivityUtils.startActivityWithBundle(PlaiMsgListActivity.this, PlainMsgDetailInfoActivity.class, bundle);
                }));
            }
        };
        binding.rvMsg.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMsg.setAdapter(msgAdapter);
    }

    private void initData(int page, boolean isClearData) {
        mViewModel.getPlainMsgList(page).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<PlainMsgResVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<PlainMsgResVo> data) {
                    if (isClearData) {
                        msgList.clear();
                    }
                    msgList.addAll(data.getList());
                    msgAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    @Override
    protected void setListener() {
    }

    @Override
    public void onClick(View view) {
    }
}
