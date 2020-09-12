package com.lihang.selfmvvm.ui.enpriceofficedoc;

import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityEnpriceOdBinding;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.EnpriceOdVo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 企业公告界面（企业端）
 */
public class EnpriceODActivity extends BaseActivity<EnpriceODViewModel, ActivityEnpriceOdBinding> {

    /**
     * 企業公告列表
     */
    private List<EnpriceOdVo> enterpriseVoList = new ArrayList<>();

    /**
     * 默认页数
     */
    private int page = 1;

    private CommonAdapter commonAdapter;

    /**
     * 默认的status
     */
    private int status = 4;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_enprice_od;
    }

    @Override
    protected void processLogic() {
        initFreshLayout();
        initAdapter();
        getEnterpriseNoticeList(1, true, status);
    }

    private void getEnterpriseNoticeList(int page, boolean isClearSourceData, int status) {
        mViewModel.getEnterpriseNoticeListComment(page, status).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<EnpriceOdVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<EnpriceOdVo> data) {
                    if (isClearSourceData) {
                        enterpriseVoList.clear();
                    }
                    enterpriseVoList.addAll(data.getList());
                    if (commonAdapter != null) commonAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    private void initFreshLayout() {
        binding.smartfreshlayout.setOnRefreshListener(this::refresh);
        binding.smartfreshlayout.setOnLoadMoreListener(this::loadMore);
    }

    private void initAdapter() {
        binding.rvEnterpriseOd.setLayoutManager(new LinearLayoutManager(getContext()));
        commonAdapter = new CommonAdapter<EnpriceOdVo>(getContext(), R.layout.goverment_project_list_item, enterpriseVoList) {

            @Override
            protected void convert(ViewHolder holder, EnpriceOdVo enpriceOdVo, int position) {
                holder.setText(R.id.tv_title, enpriceOdVo.getTitle());
                holder.setText(R.id.tv_time, enpriceOdVo.getCreateTime());
                holder.setText(R.id.tv_ui_flag, enpriceOdVo.getEnterpriseName());
                holder.setOnClickListener(R.id.rl_container, view -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("enpriceOdVo",enpriceOdVo);
                    ActivityUtils.startActivityWithBundle(EnpriceODActivity.this,OdDetailActivity.class,bundle);
                });
            }
        };
        binding.rvEnterpriseOd.setAdapter(commonAdapter);
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
        binding.tvMyOd.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.tv_my_od:
                ActivityUtils.startActivity(this, MyEnterprisesOdListActivity.class);
                break;
            default:
                break;
        }
    }

    private void refresh(RefreshLayout refresh) {
        getEnterpriseNoticeList(1, true, status);
        binding.smartfreshlayout.finishRefresh();
    }

    private void loadMore(RefreshLayout layout) {
        page += 1;
        getEnterpriseNoticeList(page, false, status);
        binding.smartfreshlayout.finishLoadMore();
    }
}
