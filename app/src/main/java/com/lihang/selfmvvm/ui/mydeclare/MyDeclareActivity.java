package com.lihang.selfmvvm.ui.mydeclare;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityMyDeclareBinding;
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

/**
 * 我的申报（企業端）
 */
public class MyDeclareActivity extends BaseActivity<MyDeclareViewModel, ActivityMyDeclareBinding> {

    private List<ProjectResVo> projectList = new ArrayList<>();
    private CommonAdapter projectAdapter;

    private static final String TAG = MyDeclareActivity.class.getSimpleName();

    /**
     * 默认从第一页开始
     */
    private int page = 1;

    private boolean isHaveDeclare = true;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_declare;
    }

    @Override
    protected void processLogic() {
        initFreshLayout();
        initAdapter();
        getHaveDeclareProject(page,true);
    }

    private void getHaveDeclareProject(int page, boolean isClearDataSource) {
        mViewModel.getListMeHandled(page).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<ProjectResVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<ProjectResVo> data) {
                    if(isClearDataSource) {
                        projectList.clear();
                    }
                    projectList.addAll(data.getList());
                    projectAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    private void getWaitDeclareProject(int page, boolean isClearDataSource) {
        mViewModel.getListMePending(page).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<ProjectResVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<ProjectResVo> data) {
                    if(isClearDataSource) {
                        projectList.clear();
                    }
                    projectList.addAll(data.getList());
                    projectAdapter.notifyDataSetChanged();
                }
            });
        });
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
                holder.setText(R.id.tv_ui_flag, getString(R.string.project_application));

                holder.setOnClickListener(R.id.rl_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", msgMeResVo.getId());
                        ActivityUtils.startActivityWithBundle(MyDeclareActivity.this, DeclareDetailActivity.class, bundle);
                    }
                });
            }
        };
        binding.lvMyDeclare.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.lvMyDeclare.setAdapter(projectAdapter);
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
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.ll_project_declare:  //已审核
                binding.viewProjectDeclare.setVisibility(View.VISIBLE);
                binding.viewProjectDeclare.setBackgroundColor(R.color.tab_selected);
                binding.viewMyDeclare.setVisibility(View.GONE);
                isHaveDeclare = true;
                getHaveDeclareProject(1,false);
                break;
            case R.id.ll_my_declare:  //待审核
                binding.viewProjectDeclare.setVisibility(View.GONE);
                binding.viewMyDeclare.setVisibility(View.VISIBLE);
                binding.viewMyDeclare.setBackgroundColor(R.color.tab_selected);
                isHaveDeclare = false;
                getWaitDeclareProject(1,false);
                break;
            default:
                break;
        }
    }

    private void refresh(RefreshLayout refresh) {
        if (isHaveDeclare) {
            getHaveDeclareProject(page, true);
        } else {
            getWaitDeclareProject(page, true);
        }
        binding.smartfreshlayout.finishRefresh();
    }

    private void loadMore(RefreshLayout layout) {
        page += 1;
        if (isHaveDeclare) {
            getHaveDeclareProject(page, false);
        } else {
            getWaitDeclareProject(page, false);
        }
        binding.smartfreshlayout.finishLoadMore();
    }
}