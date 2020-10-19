package com.lihang.selfmvvm.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.databinding.FragmentPolicyDocumentLibraryBinding;
import com.lihang.selfmvvm.ui.officialdoc.OfficialDocDetailActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.NoticeResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhangxianpeng
 * 政策文件库
 */
public class PolicyDocumentLibraryFragment extends BaseFragment<PolicyDocumentLibraryViewModel, FragmentPolicyDocumentLibraryBinding> {

    private CommonAdapter officialdocAdapter;
    private List<NoticeResVo> officialDocList = new ArrayList<>(); //公告

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
        officialdocAdapter = new CommonAdapter<NoticeResVo>(getContext(), R.layout.list_item_policy_doc, officialDocList) {
            @Override
            protected void convert(ViewHolder holder, NoticeResVo noticeResVo, int position) {
                holder.setText(R.id.tv_title, noticeResVo.getTitle());
                holder.setText(R.id.tv_time, noticeResVo.getCreateTime());
                holder.setText(R.id.tv_ui_flag, getString(R.string.system_announcement));
                holder.setOnClickListener(R.id.rl_container, view -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("flag", "noticelist");
                    bundle.putSerializable("noticeResVo", noticeResVo);
                    ActivityUtils.startActivityWithBundle(getActivity(), OfficialDocDetailActivity.class, bundle);
                });
            }
        };
    }

    private void initData(int page, boolean isClearData) {
        mViewModel.getPublishedNotice(page).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<NoticeResVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<NoticeResVo> data) {
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
