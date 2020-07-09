package com.lihang.selfmvvm.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.lihang.nbadapter.BaseAdapter;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.bean.ProjectBean;
import com.lihang.selfmvvm.databinding.FragmentProjectBinding;
import com.lihang.selfmvvm.ui.activity.WelComeActivity;
import com.lihang.selfmvvm.ui.fragment.adapter.ProjectAdapter;
import com.lihang.selfmvvm.ui.main.BottonNavigationActivity;
import com.lihang.selfmvvm.ui.mydeclare.MyDeclareActivity;
import com.lihang.selfmvvm.ui.projrctdeclare.ProjectDeclareActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.utils.LogUtils;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;

public class ProjectFragment extends BaseFragment<HomeFragmentViewModel, FragmentProjectBinding> implements BaseAdapter.OnItemClickListener<ProjectBean> {

    private static final String TAG = "ProjectFragment";
    private ArrayList<ProjectBean> projectList = new ArrayList<>();
    private ProjectAdapter adapter;

    @Override
    public void onItemClick(ProjectBean item, int position) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_project;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        getProjectList();
        updateUi();
    }

    private void updateUi() {
        boolean isGovenment = CheckPermissionUtils.getInstance().isGovernment();
        if (isGovenment) {
            binding.tvTitle.setText("企业申报项目");
            binding.tvProjectDeclare.setText("待审批项目");
            binding.tvMyDeclare.setText("已审批项目");
        } else {
            binding.tvTitle.setText("企业项目");
            binding.tvProjectDeclare.setText("项目申报");
            binding.tvMyDeclare.setText("我的申报");
        }
    }

    private void getProjectList() {
        ProjectBean testBean1 = new ProjectBean();
        testBean1.setProjectTitle("2020年南宁服务机构能力提升研修班项目（第八期）");
        testBean1.setProjectTime("2020年6月17日");
        projectList.add(testBean1);

        ProjectBean testBean2 = new ProjectBean();
        testBean2.setProjectTitle("2020年创客南宁大赛项目");
        testBean2.setProjectTime("2020年6月17日");
        projectList.add(testBean2);

        ProjectBean testBean3 = new ProjectBean();
        testBean3.setProjectTitle("高考加油！为芊芊学子助力！");
        testBean3.setProjectTime("2020年6月17日");
        projectList.add(testBean3);

        ProjectBean testBean4 = new ProjectBean();
        testBean4.setProjectTitle("在变革中求发展 在发展中求突破项目");
        testBean4.setProjectTime("2020年6月17日");
        projectList.add(testBean4);

        ProjectBean testBean5 = new ProjectBean();
        testBean5.setProjectTitle("关于疫情下外贸营销的困难与机遇");
        testBean5.setProjectTime("2020年6月17日");
        projectList.add(testBean5);

        ProjectBean testBean6 = new ProjectBean();
        testBean6.setProjectTitle("直播营销“带货南宁”项目");
        testBean6.setProjectTime("2020年6月17日");
        projectList.add(testBean6);

        adapter = new ProjectAdapter(getContext(), projectList, "project");
        binding.rvProject.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvProject.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position) -> LogUtils.d(TAG, "menuClick===" + position));
    }

    @Override
    protected void setListener() {
        binding.tvProjectDeclare.setOnClickListener(this::onClick);
        binding.tvMyDeclare.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_project_declare:
                ActivityUtils.startActivity(getContext(), ProjectDeclareActivity.class);
                break;
            case R.id.tv_my_declare:
                ActivityUtils.startActivity(getContext(), MyDeclareActivity.class);
                break;
            default:
                break;
        }
    }
}




