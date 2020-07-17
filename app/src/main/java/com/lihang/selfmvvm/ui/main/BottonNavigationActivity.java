package com.lihang.selfmvvm.ui.main;

import android.graphics.Color;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityBottonNavigationBinding;
import com.lihang.selfmvvm.ui.fragment.GovermentProjectFragment;
import com.lihang.selfmvvm.ui.fragment.HomeFragment;
import com.lihang.selfmvvm.ui.fragment.MsgFragment;
import com.lihang.selfmvvm.ui.fragment.ProjectFragment;
import com.lihang.selfmvvm.ui.fragment.UserFragment;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

public class BottonNavigationActivity extends BaseActivity<BottomNavigationViewModel, ActivityBottonNavigationBinding> {

    private String[] tabText;
    //未选中icon
    private int[] normalIcon = {R.mipmap.tabar_default_home, R.mipmap.tabar_default_address, R.mipmap.tabar_default_project, R.mipmap.tabar_default_home};
    //选中时icon
    private int[] selectIcon = {R.mipmap.tabar_selected_home, R.mipmap.tabar_selected_address, R.mipmap.tabar_selected_project, R.mipmap.tabar_selected_user};

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_botton_navigation;
    }

    @Override
    protected void processLogic() {
        CheckPermissionUtils.getInstance().setGovernment(true);
        boolean isGovernment = CheckPermissionUtils.getInstance().isGovernment();
        updateUi(isGovernment);
    }

    private void updateUi(boolean isGovernment) {
        if (isGovernment) {
            tabText = new String[]{"首页", "通讯录", "企业项目", "个人中心"};
            normalIcon = new int[]{R.mipmap.tabar_default_home, R.mipmap.tabar_default_address, R.mipmap.tabar_default_project, R.mipmap.tabar_default_home};
            selectIcon = new int[]{R.mipmap.tabar_selected_home, R.mipmap.tabar_selected_address, R.mipmap.tabar_selected_project, R.mipmap.tabar_selected_user};
            fragments.add(new HomeFragment());
            fragments.add(new MsgFragment());
            fragments.add(new GovermentProjectFragment());
            fragments.add(new UserFragment());
        } else {
            tabText = new String[]{"首页", "企业项目", "个人中心"};
            normalIcon = new int[]{R.mipmap.tabar_default_home, R.mipmap.tabar_default_project, R.mipmap.tabar_default_home};
            selectIcon = new int[]{R.mipmap.tabar_selected_home, R.mipmap.tabar_selected_project, R.mipmap.tabar_selected_user};
            fragments.add(new HomeFragment());
            fragments.add(new ProjectFragment());
            fragments.add(new UserFragment());
        }

        binding.navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .normalTextColor(Color.parseColor("#C6CBCF"))
                .selectTextColor(Color.parseColor("#222222"))
                .build();
    }

    @Override
    protected void setListener() {

    }

    public EasyNavigationBar getNavigationBar() {
        return binding.navigationBar;
    }

    @Override
    public void onClick(View view) {

    }
}