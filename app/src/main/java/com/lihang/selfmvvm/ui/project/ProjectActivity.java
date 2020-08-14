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
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

public class ProjectActivity extends BaseActivity<ProjectActivityViewModel, ActivityProjectBinding> {

    private List<ProjectResVo> projectList = new ArrayList<>();
    private CommonAdapter projectAdapter;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_project;
    }

    @Override
    protected void processLogic() {
        initAdapter();
        getWaitPendingProject();
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

    private void getWaitPendingProject() {
        mViewModel.getWaitPendingProject().observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<ProjectResVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<ProjectResVo> data) {
                    projectList.clear();
                    projectList.addAll(data.getList());
                    projectAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    private void getPendingProject() {
        mViewModel.getPendingProject().observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<ProjectResVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<ProjectResVo> data) {
                    projectList.clear();
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
                binding.viewProjectDeclare.setVisibility(View.VISIBLE);
                binding.viewProjectDeclare.setBackgroundColor(R.color.tab_selected);
                binding.viewMyDeclare.setVisibility(View.GONE);
                getWaitPendingProject();
                break;
            case R.id.ll_my_declare:  //已审核
                binding.viewProjectDeclare.setVisibility(View.GONE);
                binding.viewMyDeclare.setVisibility(View.VISIBLE);
                binding.viewMyDeclare.setBackgroundColor(R.color.tab_selected);
                getPendingProject();
                break;
            default:
                break;
        }
    }
}
