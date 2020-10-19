package com.lihang.selfmvvm.ui.questionnaire;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityQuestionNaireOfGovermentBinding;
import com.lihang.selfmvvm.ui.web.WebActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.EnterpriseVo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.QuestionNaireItemResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * created byz zhangxianpeng
 * 调查问卷（政府新增显示界面）
 */
public class QuestionNaireOfGovermentActivity extends BaseActivity<QuestionNaireOfGovermentViewModel, ActivityQuestionNaireOfGovermentBinding> {

    /**
     * 企業列表
     */
    private List<EnterpriseVo> enterpriseVoList = new ArrayList<>();

    /**
     * 默认页数
     */
    private int page = 1;

    /**
     * 0 未填報 1 已填報  "" 全部
     */
    private String status = "0";

    private CommonAdapter commonAdapter;

    private int recordId = 0;

    private QuestionNaireItemResVo questionNaireResVo;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_question_naire_of_goverment;
    }

    @Override
    protected void processLogic() {
        questionNaireResVo = (QuestionNaireItemResVo) getIntent().getSerializableExtra("questionNaireItemResVo");
        recordId = questionNaireResVo.getId();
        initView();
        initFreshLayout();
        initAdapter();
        getEnpriceListFromRecordId(page, "0", recordId, true);
    }

    private void initView() {
        binding.tvTitle.setText(questionNaireResVo.getName());
    }

    private void initFreshLayout() {
        binding.smartfreshlayout.setOnRefreshListener(this::refresh);
        binding.smartfreshlayout.setOnLoadMoreListener(this::loadMore);
    }

    private void initAdapter() {
        binding.rvEnterprise.setLayoutManager(new LinearLayoutManager(getContext()));
        commonAdapter = new CommonAdapter<EnterpriseVo>(getContext(), R.layout.goverment_project_list_item, enterpriseVoList) {

            @Override
            protected void convert(ViewHolder holder, EnterpriseVo projectBean, int position) {
                holder.setText(R.id.tv_title, projectBean.getEnterpriseName());
                if (status.equals("0")) {
                    holder.setText(R.id.tv_ui_flag, getString(R.string.not_filled_in));
                } else {
                    holder.setText(R.id.tv_ui_flag, getString(R.string.completed));
                    holder.setOnClickListener(R.id.rl_container, (view -> {
                        Bundle bundle = new Bundle();
                        bundle.putInt("questionnaireRecordId", projectBean.getId());
                        bundle.putString("questionnaireRecordName", questionNaireResVo.getName());
                        bundle.putString("status", status);
                        ActivityUtils.startActivityWithBundle(getContext(), WebActivity.class, bundle);
                    }));
                }
                holder.setText(R.id.tv_time, projectBean.getCreateTime());
            }
        };
        binding.rvEnterprise.setAdapter(commonAdapter);
    }

    /**
     * 默认获取全部问卷列表（政府）
     * 默认获取未填报问卷列表 （企业）
     *
     * @param page
     * @param isClearDataSource
     */
    private void getEnpriceListFromRecordId(int page, String status, int recorId, boolean isClearDataSource) {
        mViewModel.getEnpriceList(page, status, recorId).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<EnterpriseVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<EnterpriseVo> data) {
                    if (isClearDataSource) {
                        enterpriseVoList.clear();
                    }
                    enterpriseVoList.addAll(data.getList());
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
                getEnpriceListFromRecordId(page, status, recordId, true);
                break;
            case R.id.rl_tab_no_complete:
                binding.viewCompleted.setBackgroundColor(getContext().getColor(R.color.tab_normal));
                binding.viewNoCompleted.setBackgroundColor(getContext().getColor(R.color.tab_selected));
                status = "1";
                getEnpriceListFromRecordId(page, status, recordId, true);
                break;
            default:
                break;
        }

    }

    private void refresh(RefreshLayout refresh) {
        getEnpriceListFromRecordId(1, status, recordId, true);
        binding.smartfreshlayout.finishRefresh();
    }

    private void loadMore(RefreshLayout layout) {
        page += 1;
        getEnpriceListFromRecordId(page, status, recordId, false);
        binding.smartfreshlayout.finishLoadMore();
    }
}
