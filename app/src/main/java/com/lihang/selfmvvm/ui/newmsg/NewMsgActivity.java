package com.lihang.selfmvvm.ui.newmsg;

import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityNewMsgBinding;
import com.lihang.selfmvvm.ui.msgdetail.MsgDetailActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

public class NewMsgActivity extends BaseActivity<NewMsgViewModel, ActivityNewMsgBinding> {
    private static final String TAG = NewMsgActivity.class.getSimpleName();

    private List<MsgMeResVo> msgList = new ArrayList<>();
    private CommonAdapter msgAdapter;

    /**
     * 默认请求页码
     */
    private int page = 1;
    /**
     * 默认删除元数据
     */
    private boolean isClearData = true;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_new_msg;
    }

    @Override
    protected void processLogic() {
        initFreshLayout();
        initAdapter();
        initData(page, true);
    }

    private void initFreshLayout() {
        binding.smartfreshlayout.setOnRefreshListener(this::refresh);
        binding.smartfreshlayout.setOnLoadMoreListener(this::loadMore);
    }

    private void initAdapter() {
        msgAdapter = new CommonAdapter<MsgMeResVo>(getContext(), R.layout.new_msg_list_item, msgList) {

            @Override
            protected void convert(ViewHolder holder, MsgMeResVo msgMeResVo, int position) {

                if (msgMeResVo.getReadFlag() == 1) {  // 0 未读  1 已读
                    holder.setVisible(R.id.tv_declare_status, false);
                    holder.setText(R.id.tv_declare_result, msgMeResVo.getTitle());
                } else {
                    holder.setVisible(R.id.tv_declare_status, true);
                    holder.setBackgroundRes(R.id.tv_declare_status, R.drawable.circle_shape_fail);
                    holder.setText(R.id.tv_declare_result, msgMeResVo.getTitle());
                }
                holder.setText(R.id.tv_declare_time, msgMeResVo.getCreateTime());
                holder.setText(R.id.tv_declare_msg, msgMeResVo.getContent());

                holder.setOnClickListener(R.id.ll_container, (view -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", msgMeResVo.getId());
                    bundle.putInt("readFlag", msgMeResVo.getReadFlag());
                    ActivityUtils.startActivityWithBundle(getContext(), MsgDetailActivity.class, bundle);
                }));
            }
        };
        binding.rvNewMsg.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvNewMsg.setAdapter(msgAdapter);
    }

    private void initData(int page, boolean isClearData) {
        mViewModel.getMsgMeList(page).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<MsgMeResVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<MsgMeResVo> data) {
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

    private void refresh(RefreshLayout refresh) {
        initData(1, true);
        binding.smartfreshlayout.finishRefresh();
    }

    private void loadMore(RefreshLayout layout) {
        page += 1;
        initData(page, false);
        binding.smartfreshlayout.finishLoadMore();
    }
}