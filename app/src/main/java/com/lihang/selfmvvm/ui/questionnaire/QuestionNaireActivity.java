package com.lihang.selfmvvm.ui.questionnaire;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityQuestionNaireBinding;
import com.lihang.selfmvvm.ui.activity.WebActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.QuestionNaireItemResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

public class QuestionNaireActivity extends BaseActivity<QuestionNaireViewModel, ActivityQuestionNaireBinding> {

    /**
     * 已填报列表
     */
    private List<QuestionNaireItemResVo> completeList = new ArrayList<>();

    /**
     * 默认页数
     */
    private int page = 1;

    /**
     * 0 未填報 1 已填報  "" 全部
     */
    private String status = "0";


    private CommonAdapter commonAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_question_naire;
    }

    @Override
    protected void processLogic() {
        initFreshLayout();
        initAdapter();
        initView();
    }

    private void initView() {
        if (CheckPermissionUtils.getInstance().isGovernment()) {
            binding.llTab.setVisibility(View.GONE);
            getCompleteQuestionList(page, "", true);
        } else {
            binding.llTab.setVisibility(View.VISIBLE);
            getCompleteQuestionList(page, "0", true);
        }
    }

    private void initFreshLayout() {
        binding.smartfreshlayout.setOnRefreshListener(this::refresh);
        binding.smartfreshlayout.setOnLoadMoreListener(this::loadMore);
    }

    private void initAdapter() {
        binding.rvProject.setLayoutManager(new LinearLayoutManager(getContext()));
        commonAdapter = new CommonAdapter<QuestionNaireItemResVo>(getContext(), R.layout.goverment_project_list_item, completeList) {

            @Override
            protected void convert(ViewHolder holder, QuestionNaireItemResVo projectBean, int position) {
                holder.setText(R.id.tv_title, projectBean.getName());
                holder.setText(R.id.tv_ui_flag, getString(R.string.questionnaire));
                holder.setText(R.id.tv_time, projectBean.getCreate_time());
                if (CheckPermissionUtils.getInstance().isGovernment()) {
                    holder.setOnClickListener(R.id.rl_container, (view -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("questionNaireItemResVo", projectBean);
                        ActivityUtils.startActivityWithBundle(getContext(), QuestionNaireOfGovermentActivity.class, bundle);
                    }));
                } else {
                    holder.setOnClickListener(R.id.rl_container, (view -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("enpriceData", projectBean);
                        bundle.putString("status", status);
                        ActivityUtils.startActivityWithBundle(getContext(), WebActivity.class, bundle);
                    }));
                }

            }
        };
        binding.rvProject.setAdapter(commonAdapter);
    }

    /**
     * 默认获取全部问卷列表（政府）
     * 默认获取未填报问卷列表 （企业）
     *
     * @param page
     * @param isClearDataSource
     */
    private void getCompleteQuestionList(int page, String status, boolean isClearDataSource) {
        mViewModel.getQuestiontList(page, status).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<QuestionNaireItemResVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<QuestionNaireItemResVo> data) {
                    if (isClearDataSource) {
                        completeList.clear();
                    }
                    completeList.addAll(data.getList());
                    commonAdapter.notifyDataSetChanged();
                }
            });
        });

    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
        binding.rlTabComplete.setOnClickListener(this::onClick);
        binding.rlTabNoComplete.setOnClickListener(this::onClick);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.rl_tab_complete:
                binding.viewCompleted.setBackgroundColor(getContext().getColor(R.color.tab_selected));
                binding.viewNoCompleted.setBackgroundColor(getContext().getColor(R.color.tab_normal));
                status = "0";
                getCompleteQuestionList(page, status, true);
                break;
            case R.id.rl_tab_no_complete:
                binding.viewCompleted.setBackgroundColor(getContext().getColor(R.color.tab_normal));
                binding.viewNoCompleted.setBackgroundColor(getContext().getColor(R.color.tab_selected));
                status = "1";
                getCompleteQuestionList(page, status, true);
                break;
            default:
                break;
        }

    }

    private void refresh(RefreshLayout refresh) {
        getCompleteQuestionList(1, status, true);
        binding.smartfreshlayout.finishRefresh();
    }

    private void loadMore(RefreshLayout layout) {
        page += 1;
        getCompleteQuestionList(page, status, false);
        binding.smartfreshlayout.finishLoadMore();
    }
}
