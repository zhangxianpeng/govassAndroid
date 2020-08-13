package com.lihang.selfmvvm.ui.questionnaire;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityQuestionNaireBinding;
import com.lihang.selfmvvm.ui.activity.WebActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.vo.res.QuestionNaireItemResVo;
import com.lihang.selfmvvm.vo.res.QuestionNaireResVo;
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

    private int page = 1;
    /**
     * 0 未填報 1 已填報
     */
    private int status = 0;

    private CommonAdapter commonAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_question_naire;
    }

    @Override
    protected void processLogic() {
        initAdapter();
        getCompleteQuestionList(page, status);
    }

    private void initAdapter() {
        binding.rvProject.setLayoutManager(new LinearLayoutManager(getContext()));
        commonAdapter = new CommonAdapter<QuestionNaireItemResVo>(getContext(), R.layout.goverment_project_list_item, completeList) {

            @Override
            protected void convert(ViewHolder holder, QuestionNaireItemResVo projectBean, int position) {
                holder.setText(R.id.tv_title, completeList.get(position).getName());
                holder.setText(R.id.tv_ui_flag, getString(R.string.questionnaire));
                holder.setText(R.id.tv_time, completeList.get(position).getCreateTime());
                holder.setOnClickListener(R.id.rl_container, (view -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("questionNaireItemResVo", projectBean);
                    ActivityUtils.startActivityWithBundle(getContext(), WebActivity.class, bundle);
                }));
            }
        };
        binding.rvProject.setAdapter(commonAdapter);
    }

    private void getCompleteQuestionList(int page, int status) {
        mViewModel.getQuestiontList(page, status).observe(this, res -> {
            res.handler(new OnCallback<QuestionNaireResVo>() {
                @Override
                public void onSuccess(QuestionNaireResVo data) {
                    completeList.clear();
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
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.rl_tab_complete:
                binding.viewCompleted.setBackgroundColor(getContext().getColor(R.color.tab_selected));
                binding.viewNoCompleted.setBackgroundColor(getContext().getColor(R.color.tab_normal));
                status = 0;
                getCompleteQuestionList(page, status);
                break;
            case R.id.rl_tab_no_complete:
                binding.viewCompleted.setBackgroundColor(getContext().getColor(R.color.tab_normal));
                binding.viewNoCompleted.setBackgroundColor(getContext().getColor(R.color.tab_selected));
                status = 1;
                getCompleteQuestionList(page, status);
                break;
            default:
                break;
        }

    }

    /**
     * 未填报列表
     */
    private void getNoCompleteQuestionList() {
    }
}
