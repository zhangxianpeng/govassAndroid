package com.lihang.selfmvvm.ui.main;

import android.graphics.Color;
import android.os.Bundle;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.ui.fragment.HomeFragment;
import com.lihang.selfmvvm.ui.fragment.MsgFragment;
import com.lihang.selfmvvm.ui.fragment.ProjectFragment;
import com.lihang.selfmvvm.ui.fragment.UserFragment;
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class BottonNavigationActivity extends AppCompatActivity {

    private EasyNavigationBar navigationBar;

    private String[] tabText = {"首页", "消息", "项目", "个人中心"};
    //未选中icon
    private int[] normalIcon = {R.mipmap.tabar_default_home, R.mipmap.tabar_default_address, R.mipmap.tabar_default_project, R.mipmap.tabar_default_home};
    //选中时icon
    private int[] selectIcon = {R.mipmap.tabar_selected_home, R.mipmap.tabar_selected_address, R.mipmap.tabar_selected_project, R.mipmap.tabar_selected_user};

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botton_navigation);

        navigationBar = findViewById(R.id.navigationBar);

        fragments.add(new HomeFragment());
        fragments.add(new MsgFragment());
        fragments.add(new ProjectFragment());
        fragments.add(new UserFragment());

        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .normalTextColor(Color.parseColor("#C6CBCF"))
                .selectTextColor(Color.parseColor("#222222"))
                .build();
    }

    public EasyNavigationBar getNavigationBar() {
        return navigationBar;
    }

}