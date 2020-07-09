package com.lihang.selfmvvm.ui.mydeclare;

import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.bean.ProjectBean;
import com.lihang.selfmvvm.databinding.ActivityMyDeclareBinding;
import com.lihang.selfmvvm.ui.fragment.adapter.ProjectAdapter;
import com.lihang.selfmvvm.utils.LogUtils;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 我的申报
 */
public class MyDeclareActivity extends BaseActivity<MyDeclareViewModel, ActivityMyDeclareBinding> {

    private static final  String TAG = "MyDeclareActivity";
    private ArrayList<ProjectBean> myDecalreList = new ArrayList<>();
    private ProjectAdapter myDeclareAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_declare;
    }

    @Override
    protected void processLogic() {
         getMyDeclareList();
    }

    private void getMyDeclareList() {
        ProjectBean testBean1 = new ProjectBean();
        testBean1.setProjectTitle("关于做好2019年度自治区小企业贷款风控系统...");
        testBean1.setProjectTime("2020年5月21日 15:03");
        myDecalreList.add(testBean1);

        ProjectBean testBean2 = new ProjectBean();
        testBean2.setProjectTitle("自治区科技厅关于征集2021年国家自xxxxxxx...");
        testBean2.setProjectTime("2020年6月17日 15:03");
        myDecalreList.add(testBean2);

        ProjectBean testBean3 = new ProjectBean();
        testBean3.setProjectTitle("关于做好2019年度自治区小区也贷款xxxx");
        testBean3.setProjectTime("2020年6月17日 15:03");
        myDecalreList.add(testBean3);

        ProjectBean testBean4 = new ProjectBean();
        testBean4.setProjectTitle("在变革中求发展 在发展中求突破项目");
        testBean4.setProjectTime("2020年6月17日 15:03");
        myDecalreList.add(testBean4);

        ProjectBean testBean5 = new ProjectBean();
        testBean5.setProjectTitle("关于疫情下外贸营销的困难与机遇");
        testBean5.setProjectTime("2020年6月17日 15:03");
        myDecalreList.add(testBean5);

        ProjectBean testBean6 = new ProjectBean();
        testBean6.setProjectTitle("直播营销“带货南宁”项目");
        testBean6.setProjectTime("2020年6月17日 15:03");
        myDecalreList.add(testBean6);

        myDeclareAdapter = new ProjectAdapter(getContext(), myDecalreList, "myDeclare");
        binding.lvMyDeclare.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.lvMyDeclare.setAdapter(myDeclareAdapter);
        myDeclareAdapter.setOnItemClickListener((view, position) -> LogUtils.d(TAG, "menuClick===" + position));
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {

    }
}