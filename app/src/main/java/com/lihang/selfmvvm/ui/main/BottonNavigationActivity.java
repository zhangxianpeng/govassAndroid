package com.lihang.selfmvvm.ui.main;

import android.Manifest;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.lihang.selfmvvm.MyApplication;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityBottonNavigationBinding;
import com.lihang.selfmvvm.ui.fragment.DynamicFragment;
import com.lihang.selfmvvm.ui.fragment.HomeFragment;
import com.lihang.selfmvvm.ui.fragment.MsgFragment;
import com.lihang.selfmvvm.ui.fragment.PolicyDocumentLibraryFragment;
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
import static com.lihang.selfmvvm.base.BaseConstant.USER_NICK_NAME;

/**
 * created by zhangxianpeng
 * main主界面
 */
public class BottonNavigationActivity extends BaseActivity<BottomNavigationViewModel, ActivityBottonNavigationBinding> {

    private String[] tabText;
    //未选中icon
    private int[] normalIcon;
    //选中时icon
    private int[] selectIcon;

    private List<Fragment> fragments = new ArrayList<>();

    /**
     * 权限组
     */
    private static final String[] permissionsGroup = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_SETTINGS, Manifest.permission.CAMERA};

    //动态权限
    RxPermissions rxPermissions = new RxPermissions(this);

    @Override
    protected int getContentViewId() {
        return R.layout.activity_botton_navigation;
    }

    @Override
    protected void processLogic() {
        if (TextUtils.isEmpty(MyApplication.getToken())) {
            ActivityUtils.startActivity(this, GovassLoginActivity.class);
            finish();
        } else {
            getUserInfo();
        }
        initPermission();
    }

    private void getUserInfo() {
        mViewModel.getUserInfo().observe(this, res -> {
            res.handler(new OnCallback<UserInfoVo>() {
                @Override
                public void onSuccess(UserInfoVo data) {
                    if (data != null) {
                        if (!TextUtils.isEmpty(data.getHeadUrl()))
                            PreferenceUtil.put(USER_LOGIN_HEAD_URL, data.getHeadUrl());
                        if (!TextUtils.isEmpty(data.getRealname()))
                            PreferenceUtil.put(USER_NICK_NAME, data.getRealname());
                        if (data.getUserType() == 0) { //政府
                            CheckPermissionUtils.getInstance().setGovernment(true);
                        } else if (data.getUserType() == 1) {  //企业
                            CheckPermissionUtils.getInstance().setGovernment(false);
                        }
                        updateUi(CheckPermissionUtils.getInstance().isGovernment());
                    } else {
                        ActivityUtils.startActivity(BottonNavigationActivity.this, GovassLoginActivity.class);
                        finish();
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    super.onError(throwable);
                    ActivityUtils.startActivity(BottonNavigationActivity.this, GovassLoginActivity.class);
                    finish();
                }

                @Override
                public void onFailure(String msg) {
                    super.onFailure(msg);
                    ActivityUtils.startActivity(BottonNavigationActivity.this, GovassLoginActivity.class);
                    finish();
                }
            });
        });
    }

    private void updateUi(boolean isGovernment) {
        if (isGovernment) {
            tabText = new String[]{"政企工作间", "千企动态", "通讯录", "政策文件库"};
            normalIcon = new int[]{R.mipmap.tabar_default_home, R.mipmap.tabar_default_dynamic, R.mipmap.tabar_default_address, R.mipmap.tabar_default_user};
            selectIcon = new int[]{R.mipmap.tabar_selected_home, R.mipmap.tabar_selected_dynamic, R.mipmap.tabar_selected_address, R.mipmap.tabar_selected_user};
            fragments.add(new HomeFragment());
            fragments.add(new DynamicFragment());
            fragments.add(new MsgFragment());
            fragments.add(new PolicyDocumentLibraryFragment());
        } else {
            tabText = new String[]{"政企工作间", "千企动态", "政策文件库"};
            normalIcon = new int[]{R.mipmap.tabar_default_home, R.mipmap.tabar_default_dynamic, R.mipmap.tabar_default_home};
            selectIcon = new int[]{R.mipmap.tabar_selected_home, R.mipmap.tabar_selected_dynamic, R.mipmap.tabar_selected_user};
            fragments.add(new HomeFragment());
            fragments.add(new DynamicFragment());
            fragments.add(new PolicyDocumentLibraryFragment());
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
                    public void accept(Permission permission) {
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