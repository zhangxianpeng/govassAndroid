package com.lihang.selfmvvm.ui.main;

import android.Manifest;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityBottonNavigationBinding;
import com.lihang.selfmvvm.ui.fragment.GovermentProjectFragment;
import com.lihang.selfmvvm.ui.fragment.HomeFragment;
import com.lihang.selfmvvm.ui.fragment.MsgFragment;
import com.lihang.selfmvvm.ui.fragment.ProjectFragment;
import com.lihang.selfmvvm.ui.fragment.UserFragment;
import com.lihang.selfmvvm.ui.login.GovassLoginActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.utils.PreferenceUtil;
import com.lihang.selfmvvm.vo.res.UserInfoVo;
import com.next.easynavigation.view.EasyNavigationBar;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import io.reactivex.functions.Consumer;

import static com.lihang.selfmvvm.base.BaseConstant.USER_LOGIN_HEAD_URL;
import static com.lihang.selfmvvm.base.BaseConstant.USER_LOGIN_TOKEN;
import static com.lihang.selfmvvm.base.BaseConstant.USER_NICK_NAME;

public class BottonNavigationActivity extends BaseActivity<BottomNavigationViewModel, ActivityBottonNavigationBinding> {

    private String[] tabText;
    //未选中icon
    private int[] normalIcon = {R.mipmap.tabar_default_home, R.mipmap.tabar_default_address, R.mipmap.tabar_default_project, R.mipmap.tabar_default_home};
    //选中时icon
    private int[] selectIcon = {R.mipmap.tabar_selected_home, R.mipmap.tabar_selected_address, R.mipmap.tabar_selected_project, R.mipmap.tabar_selected_user};

    private List<Fragment> fragments = new ArrayList<>();

    private String token = "";

    /**
     * 权限组
     */
    private static final String[] permissionsGroup = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_SETTINGS,Manifest.permission.CAMERA};

    //动态权限
    RxPermissions rxPermissions = new RxPermissions(this);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_botton_navigation;
    }

    @Override
    protected void processLogic() {
        token = (String) PreferenceUtil.get(USER_LOGIN_TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            ActivityUtils.startActivity(getContext(), GovassLoginActivity.class);
        } else {
            getUserInfo();
        }
        initPermission();
    }

    private void getUserInfo() {
        mViewModel.getUserInfo(token).observe(this, res -> {
            res.handler(new OnCallback<UserInfoVo>() {
                @Override
                public void onSuccess(UserInfoVo data) {
                    if (data != null) {
                        PreferenceUtil.put(USER_LOGIN_HEAD_URL,data.getHeadUrl());
                        PreferenceUtil.put(USER_NICK_NAME,data.getRealname());
                        if (data.getUserType() == 0) { //政府
                            CheckPermissionUtils.getInstance().setGovernment(true);
                        } else if (data.getUserType() == 1) {  //企业
                            CheckPermissionUtils.getInstance().setGovernment(false);
                        }

                        updateUi(CheckPermissionUtils.getInstance().isGovernment());
                    }
                }

                @Override
                public void onFailure(String msg) {
                    super.onFailure(msg);
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                }
            });
        });

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

    private void initPermission() {
        rxPermissions.requestEach(permissionsGroup)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
//                            ToastUtils.showToast("用户已经同意该权限");
                        } else if (permission.shouldShowRequestPermissionRationale) {
//                            ToastUtils.showToast("用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框");
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                        } else {
//                            ToastUtils.showToast("用户拒绝了该权限，而且选中『不再询问』那么下次启动时，就不会提示出来了");
                            // 用户拒绝了该权限，而且选中『不再询问』那么下次启动时，就不会提示出来了，
                        }
                    }
                });
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