package com.lihang.selfmvvm.ui.questionnaire;

import android.os.Build;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.bean.ProjectBean;
import com.lihang.selfmvvm.databinding.ActivityQuestionNaireBinding;
import com.lihang.selfmvvm.ui.activity.WebActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

public class QuestionNaireActivity extends BaseActivity<QuestionNaireViewModel, ActivityQuestionNaireBinding> {

    /**
     * 已填报列表
     */
    private ArrayList<ProjectBean> completeList = new ArrayList<>();
    /**
     * 未填报列表
     */
    private ArrayList<ProjectBean> noCompleteList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_question_naire;
    }

    @Override
    protected void processLogic() {
        getCompleteQuestionList();
    }

    private void getCompleteQuestionList() {
        ProjectBean testBean1 = new ProjectBean();
        testBean1.setProjectTitle("2020年南宁服务机构能力提升研修班项目（第八期）");
        testBean1.setUiFlag(getContext().getString(R.string.questionnaire));
        testBean1.setProjectTime("2020年6月17日");
        completeList.add(testBean1);

        ProjectBean testBean2 = new ProjectBean();
        testBean2.setProjectTitle("2020年创客南宁大赛项目");
        testBean2.setUiFlag(getContext().getString(R.string.questionnaire));
        testBean2.setProjectTime("2020年6月17日");
        completeList.add(testBean2);

        ProjectBean testBean3 = new ProjectBean();
        testBean3.setProjectTitle("高考加油！为芊芊学子助力！");
        testBean3.setUiFlag(getContext().getString(R.string.questionnaire));
        testBean3.setProjectTime("2020年6月17日");
        completeList.add(testBean3);

        ProjectBean testBean4 = new ProjectBean();
        testBean4.setProjectTitle("在变革中求发展 在发展中求突破项目");
        testBean4.setUiFlag(getContext().getString(R.string.questionnaire));
        testBean4.setProjectTime("2020年6月17日");
        completeList.add(testBean4);

        ProjectBean testBean5 = new ProjectBean();
        testBean5.setProjectTitle("关于疫情下外贸营销的困难与机遇");
        testBean5.setUiFlag(getContext().getString(R.string.questionnaire));
        testBean5.setProjectTime("2020年6月17日");
        completeList.add(testBean5);

        ProjectBean testBean6 = new ProjectBean();
        testBean6.setProjectTitle("直播营销“带货南宁”项目");
        testBean6.setUiFlag(getContext().getString(R.string.questionnaire));
        testBean6.setProjectTime("2020年6月17日");
        completeList.add(testBean6);

        binding.rvProject.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvProject.setAdapter(new CommonAdapter<ProjectBean>(getContext(), R.layout.goverment_project_list_item, completeList) {

            @Override
            protected void convert(ViewHolder holder, ProjectBean projectBean, int position) {
                holder.setText(R.id.tv_title, completeList.get(position).getProjectTitle());
                holder.setText(R.id.tv_ui_flag, completeList.get(position).getUiFlag());
                holder.setText(R.id.tv_time, completeList.get(position).getProjectTime());
                holder.setOnClickListener(R.id.rl_container, (view -> {
                    ActivityUtils.startActivity(getContext(), WebActivity.class);
                }));
            }
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
                getCompleteQuestionList();
                break;
            case R.id.rl_tab_no_complete:
                binding.viewCompleted.setBackgroundColor(getContext().getColor(R.color.tab_normal));
                binding.viewNoCompleted.setBackgroundColor(getContext().getColor(R.color.tab_selected));
                getNoCompleteQuestionList();
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
