package com.lihang.selfmvvm.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.lihang.nbadapter.BaseAdapter;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.bean.ProjectBean;
import com.lihang.selfmvvm.databinding.FragmentProjectBinding;
import com.lihang.selfmvvm.ui.fragment.adapter.ProjectAdapter;
import com.lihang.selfmvvm.ui.mydeclare.MyDeclareActivity;
import com.lihang.selfmvvm.ui.projrctdeclare.ProjectDeclareActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.utils.LogUtils;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;

public class ProjectFragment extends BaseFragment<HomeFragmentViewModel, FragmentProjectBinding> implements BaseAdapter.OnItemClickListener<ProjectBean> {

    private ArrayList<ProjectBean> projectList = new ArrayList<>();
    private ProjectAdapter adapter;

    private static final String TAG = ProjectFragment.class.getSimpleName();

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
            binding.tvTitle.setText(getContext().getString(R.string.project_approval));
            binding.tvProjectDeclare.setText(getContext().getString(R.string.projects_to_be_approved));
            binding.tvMyDeclare.setText(getContext().getString(R.string.approved_projects));
        } else {
            binding.tvTitle.setText(getContext().getString(R.string.enterprise_project));
            binding.tvProjectDeclare.setText(getContext().getString(R.string.project_application));
            binding.tvMyDeclare.setText(getContext().getString(R.string.my_declaration));
        }
    }

    private void getProjectList() {
        ProjectBean testBean1 = new ProjectBean();
        testBean1.setProjectTitle("2020???????????????????????????????????????????????????????????????");
        testBean1.setProjectTime("2020???6???17???");
        projectList.add(testBean1);

        ProjectBean testBean2 = new ProjectBean();
        testBean2.setProjectTitle("2020???????????????????????????");
        testBean2.setProjectTime("2020???6???17???");
        projectList.add(testBean2);

        ProjectBean testBean3 = new ProjectBean();
        testBean3.setProjectTitle("???????????????????????????????????????");
        testBean3.setProjectTime("2020???6???17???");
        projectList.add(testBean3);

        ProjectBean testBean4 = new ProjectBean();
        testBean4.setProjectTitle("????????????????????? ???????????????????????????");
        testBean4.setProjectTime("2020???6???17???");
        projectList.add(testBean4);

        ProjectBean testBean5 = new ProjectBean();
        testBean5.setProjectTitle("?????????????????????????????????????????????");
        testBean5.setProjectTime("2020???6???17???");
        projectList.add(testBean5);

        ProjectBean testBean6 = new ProjectBean();
        testBean6.setProjectTitle("????????????????????????????????????");
        testBean6.setProjectTime("2020???6???17???");
        projectList.add(testBean6);

        adapter = new ProjectAdapter(getContext(), projectList, TAG);
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




