package com.lihang.selfmvvm.ui.customerservicefeedback;

import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityFeedBackListBinding;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.vo.model.CommunicateMsgVo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * created by zhangxianpeng on 2020/10/26
 * 反馈历史
 */
public class FeedBackListActivity extends BaseActivity<FeedBackListViewModel, ActivityFeedBackListBinding> {

    private List<CommunicateMsgVo> msgList = new ArrayList<>();
    private CommonAdapter msgAdapter;

    /**
     * 默认请求页码
     */
    private int page = 1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_feed_back_list;
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
        msgAdapter = new CommonAdapter<CommunicateMsgVo>(getContext(), R.layout.list_item_feedback, msgList) {

            @Override
            protected void convert(ViewHolder holder, CommunicateMsgVo communicateMsgVo, int position) {
                // TODO: 2021/3/13  需要展示上报人和上报人所属企业
//                String creater = ;
//                holder.setText(R.id.tv_creater, communicateMsgVo.getTitle());
                holder.setText(R.id.tv_title, communicateMsgVo.getContent());
                holder.setText(R.id.tv_creattime, communicateMsgVo.getCreateTime());
                holder.setText(R.id.tv_status, communicateMsgVo.getStatus() == 0 ? "待处理" : "已受理");
                holder.setOnClickListener(R.id.rl_container, (view -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", communicateMsgVo.getId());
                    bundle.putInt("status", communicateMsgVo.getStatus());
                    ActivityUtils.startActivityWithBundle(getContext(), FeedBackActivity.class, bundle);
                }));
            }
        };
        binding.rvFeedback.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvFeedback.setAdapter(msgAdapter);
    }

    private void initData(int page, boolean isClearData) {
        if (CheckPermissionUtils.getInstance().isGovernment()) {
            mViewModel.getFeedBackList(page).observe(this, res -> {
                res.handler(new OnCallback<ListBaseResVo<CommunicateMsgVo>>() {
                    @Override
                    public void onSuccess(ListBaseResVo<CommunicateMsgVo> data) {
                        if (isClearData) {
                            msgList.clear();
                        }
                        msgList.addAll(data.getList());
                        msgAdapter.notifyDataSetChanged();
                    }
                });
            });
        } else {
            mViewModel.getFeedBackListMe(page).observe(this, res -> {
                res.handler(new OnCallback<ListBaseResVo<CommunicateMsgVo>>() {
                    @Override
                    public void onSuccess(ListBaseResVo<CommunicateMsgVo> data) {
                        if (isClearData) {
                            msgList.clear();
                        }
                        msgList.addAll(data.getList());
                        msgAdapter.notifyDataSetChanged();
                    }
                });
            });
        }

    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
        binding.btnAddNewFeedBack.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.btn_add_new_feed_back:
                ActivityUtils.startActivity(this, FeedBackActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(1, true);
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
