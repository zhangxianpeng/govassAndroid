package com.lihang.selfmvvm.ui.project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityProjectBinding;
import com.lihang.selfmvvm.ui.declaredetail.DeclareDetailActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.ProjectResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

public class ProjectActivity extends BaseActivity<ProjectActivityViewModel, ActivityProjectBinding> {

    private List<ProjectResVo> projectList = new ArrayList<>();
    private CommonAdapter projectAdapter;

    /**
     * 默认从第一页开始
     */
    private int page = 1;

    /**
     * 默认是已审核
     */
    private boolean isPending = true;

    /**
     * 是否删除数据
     */
    private boolean isClearData = true;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_project;
    }

    @Override
    protected void processLogic() {
        initFreshLayout();
        initAdapter();
        getPendingProject(page, isPending, true);
    }

    private void initFreshLayout() {
        binding.smartfreshlayout.setOnRefreshListener(this::refresh);
        binding.smartfreshlayout.setOnLoadMoreListener(this::loadMore);
    }

    private void initAdapter() {
        projectAdapter = new CommonAdapter<ProjectResVo>(getContext(), R.layout.goverment_project_list_item, projectList) {
            @Override
            protected void convert(ViewHolder holder, ProjectResVo msgMeResVo, int position) {
                holder.setText(R.id.tv_title, msgMeResVo.getName());
                holder.setText(R.id.tv_time, msgMeResVo.getCreateTime());
                holder.setText(R.id.tv_ui_flag, getString(R.string.project_approval));

                holder.setOnClickListener(R.id.rl_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", msgMeResVo.getId());
                        ActivityUtils.startActivityWithBundle(ProjectActivity.this, DeclareDetailActivity.class, bundle);
                    }
                });
            }
        };
        binding.rvProject.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvProject.setAdapter(projectAdapter);
    }


    /**
     * 获取项目列表（默认为已审核）
     *
     * @param isPending
     */
    private void getPendingProject(int page, boolean isPending, boolean isClearData) {
        mViewModel.getPendingProject(page, isPending).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<ProjectResVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<ProjectResVo> data) {
                    if (isClearData) {
                        projectList.clear();
                    }
                    projectList.addAll(data.getList());
                    projectAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
        binding.llProjectDeclare.setOnClickListener(this::onClick);
        binding.llMyDeclare.setOnClickListener(this::onClick);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.ll_project_declare:  //待审核
                binding.viewProjectDeclare.setVisibility(View.GONE);
                binding.viewMyDeclare.setVisibility(View.VISIBLE);
                binding.viewMyDeclare.setBackgroundColor(R.color.tab_selected);
                isPending = false;
                getPendingProject(1, isPending, true);
                break;
            case R.id.ll_my_declare:  //已审核
                binding.viewProjectDeclare.setVisibility(View.VISIBLE);
                binding.viewProjectDeclare.setBackgroundColor(R.color.tab_selected);
                binding.viewMyDeclare.setVisibility(View.GONE);
                isPending = true;
                getPendingProject(1, isPending, true);
                break;
            default:
                break;
        }
    }

    private void refresh(RefreshLayout refresh) {
        getPendingProject(1, isPending, true);
        binding.smartfreshlayout.finishRefresh();
    }

    private void loadMore(RefreshLayout layout) {
        page += 1;
        getPendingProject(page, isPending, false);
        binding.smartfreshlayout.finishLoadMore();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onResume() {
        super.onResume();
//        if(!isPending) {
//            binding.viewProjectDeclare.setVisibility(View.VISIBLE);
//            binding.viewProjectDeclare.setBackgroundColor(R.color.tab_selected);
//            binding.viewMyDeclare.setVisibility(View.GONE);
//        }
//        getPendingProject(1, isPending, true);
    }
}
