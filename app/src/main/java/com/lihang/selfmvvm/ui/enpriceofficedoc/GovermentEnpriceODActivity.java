package com.lihang.selfmvvm.ui.enpriceofficedoc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityGovermentEnpriceOdBinding;
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
 * created by zhangxianpeng
 * 企业公告界面（政府端）
 */
public class GovermentEnpriceODActivity extends BaseActivity<EnpriceODViewModel, ActivityGovermentEnpriceOdBinding> {

    /**
     * 企業公告列表
     */
    private List<EnpriceOdVo> enterpriseVoList = new ArrayList<>();

    /**
     * 默认页数
     */
    private int page = 1;

    /**
     * 默认tab点击左边
     */
    private boolean isClickLeft = true;

    private CommonAdapter commonAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_goverment_enprice_od;
    }

    @Override
    protected void processLogic() {
        initFreshLayout();
        initAdapter();
        getEnterpriseNoticeList(1, true);
    }

    /**
     * 获取已发布的企业公告列表
     */
    private void getEnterpriseNoticeList(int page, boolean isClearSourceData) {
        mViewModel.getEnterpriseNoticeList(page).observe(this, res -> {
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

    private void getEnterpriseNoticeAuditList(int page, boolean isClearSourceData) {
        mViewModel.getEnterpriseNoticeAuditList(page).observe(this, res -> {
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
                    ActivityUtils.startActivityWithBundle(GovermentEnpriceODActivity.this,OdDetailActivity.class,bundle);
                });
            }
        };
        binding.rvEnterpriseOd.setAdapter(commonAdapter);
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
        binding.rlTabYifabu.setOnClickListener(this::onClick);
        binding.rlTabDaishenhe.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.rl_tab_yifabu:
                isClickLeft = true;
                updateTabBtnView(binding.viewYifabu, binding.viewDaishenhe, true);
                getEnterpriseNoticeList(1, true);
                break;
            case R.id.rl_tab_daishenhe:
                isClickLeft = false;
                updateTabBtnView(binding.viewYifabu, binding.viewDaishenhe, false);
                getEnterpriseNoticeAuditList(1, true);
                break;
            default:
                break;
        }
    }

    /**
     * 更新状态
     *
     * @param viewYifabu
     * @param viewDaishenhe
     * @param isClickLeftBtn
     */
    @SuppressLint("NewApi")
    private void updateTabBtnView(View viewYifabu, View viewDaishenhe, boolean isClickLeftBtn) {
        if (isClickLeftBtn) {
            viewDaishenhe.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewYifabu.setBackgroundColor(getContext().getColor(R.color.tab_selected));
        } else {
            viewDaishenhe.setBackgroundColor(getContext().getColor(R.color.tab_selected));
            viewYifabu.setBackgroundColor(getContext().getColor(R.color.tab_normal));
        }
    }

    private void refresh(RefreshLayout refresh) {
        if (isClickLeft) {
            getEnterpriseNoticeList(1, true);
        } else {
            getEnterpriseNoticeAuditList(1, true);
        }
        binding.smartfreshlayout.finishRefresh();
    }

    private void loadMore(RefreshLayout layout) {
        page += 1;
        if (isClickLeft) {
            getEnterpriseNoticeList(page, false);
        } else {
            getEnterpriseNoticeAuditList(page, false);
        }
        binding.smartfreshlayout.finishLoadMore();
    }
}
