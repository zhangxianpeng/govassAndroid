package com.lihang.selfmvvm.ui.myenterprises;

import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityMyEnterprisesListBinding;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.EnterpriseVo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * created by zhangxianpeng
 * 我的企业列表
 */
public class MyEnterprisesListActivity extends BaseActivity<MyEnterprisesListViewModel, ActivityMyEnterprisesListBinding> {

    private List<EnterpriseVo> enterpriseVoList = new ArrayList<>();
    private CommonAdapter enterpriseAdapter;

    /**
     * 默认请求页码
     */
    private int page = 1;
    /**
     * 是否清除源数据
     */
    private boolean isClearData = true;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_enterprises_list;
    }

    @Override
    protected void processLogic() {
        initFreshLayout();
        initAdapter();
        getEnterpriseList(page, true);
    }

    private void initFreshLayout() {
        binding.smartfreshlayout.setOnRefreshListener(this::refresh);
        binding.smartfreshlayout.setOnLoadMoreListener(this::loadMore);
    }

    private void initAdapter() {
        enterpriseAdapter = new CommonAdapter<EnterpriseVo>(getContext(), R.layout.goverment_project_list_item, enterpriseVoList) {

            @Override
            protected void convert(ViewHolder holder, EnterpriseVo enterpriseVo, int position) {
                holder.setText(R.id.tv_title, enterpriseVo.getEnterpriseName());
                holder.setText(R.id.tv_time, enterpriseVo.getCreateTime());
                holder.setText(R.id.tv_ui_flag, enterpriseVo.getLegalRepresentative());

                holder.setOnClickListener(R.id.rl_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("enterpriseVo", enterpriseVo);
                        ActivityUtils.startActivityWithBundle(MyEnterprisesListActivity.this, EnterprisesDetailActivity.class, bundle);
                    }
                });
            }
        };
        binding.rvMyEnterprise.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMyEnterprise.setAdapter(enterpriseAdapter);
    }

    private void getEnterpriseList(int page, boolean isClearData) {
        mViewModel.getEnterpriseList(page).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<EnterpriseVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<EnterpriseVo> data) {
                    if (isClearData) {
                        enterpriseVoList.clear();
                    }
                    enterpriseVoList.addAll(data.getList());
                    enterpriseAdapter.notifyDataSetChanged();
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
        if (ButtonClickUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void refresh(RefreshLayout refresh) {
        getEnterpriseList(1, true);
        binding.smartfreshlayout.finishRefresh();
    }

    private void loadMore(RefreshLayout layout) {
        page += 1;
        getEnterpriseList(page, false);
        binding.smartfreshlayout.finishLoadMore();
    }
}
