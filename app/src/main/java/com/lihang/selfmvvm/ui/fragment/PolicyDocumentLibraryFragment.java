package com.lihang.selfmvvm.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.databinding.FragmentPolicyDocumentLibraryBinding;
import com.lihang.selfmvvm.ui.msgdetail.MsgDetailActivity;
import com.lihang.selfmvvm.ui.newmsg.NewMsgActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.OfficialDocResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * created by zhangxianpeng
 * 政策文件库
 */
public class PolicyDocumentLibraryFragment extends BaseFragment<PolicyDocumentLibraryViewModel, FragmentPolicyDocumentLibraryBinding> {

    private CommonAdapter officialdocAdapter;
    private List<OfficialDocResVo> officialDocList = new ArrayList<>(); //公告

    /**
     * 是否请求公告
     */
    private boolean isGetOfficeDoc = false;
    /**
     * 默认请求页码
     */
    private int page = 1;
    /**
     * 是否删除源数据
     */
    private boolean isClearData = true;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_policy_document_library;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initAdapter();
        initData(page, isClearData);
        getUnReadMsgCount();
    }

    private void getUnReadMsgCount() {
        mViewModel.getMsgUnRead().observe(getActivity(), res -> {
            res.handler(new OnCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    binding.badgeView.setVisibility(data.equals("0") ? View.GONE : View.VISIBLE);
                    binding.badgeView.setText(data);
                }

                @Override
                public void onFailure(String msg) {
                }
            });
        });
    }

    @Override
    protected void setListener() {
        initFreshLayout();
        binding.flNewMsg.setOnClickListener(this::onClick);
    }

    private void initFreshLayout() {
        binding.smartfreshlayout.setOnRefreshListener(this::refresh);
        binding.smartfreshlayout.setOnLoadMoreListener(this::loadMore);
    }

    private void initAdapter() {
        officialdocAdapter = new CommonAdapter<OfficialDocResVo>(getContext(), R.layout.list_item_policy_doc, officialDocList) {
            @Override
            protected void convert(ViewHolder holder, OfficialDocResVo noticeResVo, int position) {
                holder.setText(R.id.tv_policy_title, noticeResVo.getTitle());
                holder.setText(R.id.tv_send_time, noticeResVo.getCreateTime());
                holder.setText(R.id.tv_sender, getString(R.string.policy_sender, noticeResVo.getCreateUser()));
                holder.setOnClickListener(R.id.rl_container, view -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("uiflag", "policy");
                    bundle.putSerializable("noticeResVo", noticeResVo);
                    bundle.putInt("id", noticeResVo.getId());
                    ActivityUtils.startActivityWithBundle(getActivity(), MsgDetailActivity.class, bundle);
                });
            }
        };
        binding.rvPolicy.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPolicy.setAdapter(officialdocAdapter);
    }

    private void initData(int page, boolean isClearData) {
        mViewModel.getPolicyList(page).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<OfficialDocResVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<OfficialDocResVo> data) {
                    if (isClearData) {
                        officialDocList.clear();
                    }
                    officialDocList.addAll(data.getList());
                    officialdocAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.fl_new_msg:
                ActivityUtils.startActivity(getContext(), NewMsgActivity.class);
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
