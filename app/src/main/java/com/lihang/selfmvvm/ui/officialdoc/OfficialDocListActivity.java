package com.lihang.selfmvvm.ui.officialdoc;

import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityOfficialDocListBinding;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.OfficialDocResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

public class OfficialDocListActivity extends BaseActivity<OfficialDocListViewModel, ActivityOfficialDocListBinding> {

    private CommonAdapter officialdocAdapter;
    private List<OfficialDocResVo> officialDocList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_official_doc_list;
    }

    @Override
    protected void processLogic() {
        initFreshLayout();
        initAdapter();
        initData();
    }

    private void initFreshLayout() {
        binding.smartfreshlayout.setOnRefreshListener(this::refresh);
        binding.smartfreshlayout.setOnLoadMoreListener(this::loadMore);
    }

    private void initAdapter() {
        officialdocAdapter = new CommonAdapter<OfficialDocResVo>(getContext(), R.layout.goverment_project_list_item, officialDocList) {

            @Override
            protected void convert(ViewHolder holder, OfficialDocResVo msgMeResVo, int position) {
                holder.setText(R.id.tv_title, msgMeResVo.getTitle());
                holder.setText(R.id.tv_time, msgMeResVo.getCreateTime());
                holder.setText(R.id.tv_ui_flag, getString(R.string.project_approval));

                holder.setOnClickListener(R.id.rl_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", msgMeResVo.getId());
                        ActivityUtils.startActivityWithBundle(OfficialDocListActivity.this, OfficialDocDetailActivity.class, bundle);
                    }
                });
            }
        };
        binding.rvOfficialdoc.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvOfficialdoc.setAdapter(officialdocAdapter);
    }

    private void initData() {
        mViewModel.getOfficalDoc().observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<OfficialDocResVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<OfficialDocResVo> data) {
                    officialDocList.clear();
                    officialDocList.addAll(data.getList());
                    officialdocAdapter.notifyDataSetChanged();
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
        initData();
        binding.smartfreshlayout.finishRefresh();
    }

    private void loadMore(RefreshLayout layout) {
        initData();
        binding.smartfreshlayout.finishLoadMore();
    }
}
