package com.lihang.selfmvvm.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.bean.ProjectBean;
import com.lihang.selfmvvm.databinding.FragmentGovermentProjectBinding;
import com.lihang.selfmvvm.ui.declaredetail.DeclareDetailActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;


public class GovermentProjectFragment extends BaseFragment<GovermentProjectFragmentViewModel, FragmentGovermentProjectBinding> {

    /**
     * 待审批列表
     */
    private ArrayList<ProjectBean> waitApproveprojectList = new ArrayList<>();
    /**
     * 已审批列表
     */
    private ArrayList<ProjectBean> haveApprovedprojectList = new ArrayList<>();
    /**
     * 默认选中待审批tab
     */
    private boolean isWaitApprove = true;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_goverment_project;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        getProjectList();
    }

    private void getProjectList() {
        ProjectBean testBean1 = new ProjectBean();
        testBean1.setProjectTitle("2020年南宁服务机构能力提升研修班项目（第八期）");
        testBean1.setProjectTime("2020年6月17日");
        waitApproveprojectList.add(testBean1);

        ProjectBean testBean2 = new ProjectBean();
        testBean2.setProjectTitle("2020年创客南宁大赛项目");
        testBean2.setProjectTime("2020年6月17日");
        waitApproveprojectList.add(testBean2);

        ProjectBean testBean3 = new ProjectBean();
        testBean3.setProjectTitle("高考加油！为芊芊学子助力！");
        testBean3.setProjectTime("2020年6月17日");
        waitApproveprojectList.add(testBean3);

        ProjectBean testBean4 = new ProjectBean();
        testBean4.setProjectTitle("在变革中求发展 在发展中求突破项目");
        testBean4.setProjectTime("2020年6月17日");
        waitApproveprojectList.add(testBean4);

        ProjectBean testBean5 = new ProjectBean();
        testBean5.setProjectTitle("关于疫情下外贸营销的困难与机遇");
        testBean5.setProjectTime("2020年6月17日");
        waitApproveprojectList.add(testBean5);

        ProjectBean testBean6 = new ProjectBean();
        testBean6.setProjectTitle("直播营销“带货南宁”项目");
        testBean6.setProjectTime("2020年6月17日");
        waitApproveprojectList.add(testBean6);

        binding.rvProject.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvProject.setAdapter(new CommonAdapter<ProjectBean>(getContext(), R.layout.goverment_project_list_item, waitApproveprojectList) {

            @Override
            protected void convert(ViewHolder holder, ProjectBean projectBean, int position) {
                holder.setText(R.id.tv_title, waitApproveprojectList.get(position).getProjectTitle());
                holder.setText(R.id.tv_time, waitApproveprojectList.get(position).getProjectTime());
                holder.setOnClickListener(R.id.rl_container, (view -> {
                    ActivityUtils.startActivity(getContext(), DeclareDetailActivity.class);
                }));
            }
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {

    }
}