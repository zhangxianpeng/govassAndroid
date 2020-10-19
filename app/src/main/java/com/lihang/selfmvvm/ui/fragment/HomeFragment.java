package com.lihang.selfmvvm.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.lihang.selfmvvm.MyApplication;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.adapter.MyFragmentPagerAdapter;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.bean.HomeMenuBean;
import com.lihang.selfmvvm.customview.iosdialog.NewIOSAlertDialog;
import com.lihang.selfmvvm.databinding.FragmentHomeBinding;
import com.lihang.selfmvvm.ui.customserver.CustomServerActivity;
import com.lihang.selfmvvm.ui.enpriceofficedoc.EnpriceODActivity;
import com.lihang.selfmvvm.ui.enpriceofficedoc.GovermentEnpriceODActivity;
import com.lihang.selfmvvm.ui.fragment.adapter.HomeMenuAdapter;
import com.lihang.selfmvvm.ui.fragment.adapter.ProjectListAdapter;
import com.lihang.selfmvvm.ui.login.GovassLoginActivity;
import com.lihang.selfmvvm.ui.msgdetail.MsgDetailActivity;
import com.lihang.selfmvvm.ui.myenterprises.MyEnterprisesListActivity;
import com.lihang.selfmvvm.ui.newmsg.NewMsgActivity;
import com.lihang.selfmvvm.ui.officialdoc.OfficialDocDetailActivity;
import com.lihang.selfmvvm.ui.officialdoc.OfficialDocListActivity;
import com.lihang.selfmvvm.ui.project.ProjectActivity;
import com.lihang.selfmvvm.ui.projrctdeclare.ProjectDeclareActivity;
import com.lihang.selfmvvm.ui.questionnaire.QuestionNaireActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.utils.GlideImageLoader;
import com.lihang.selfmvvm.utils.LogUtils;
import com.lihang.selfmvvm.utils.PackageUtils;
import com.lihang.selfmvvm.utils.PreferenceUtil;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.res.ImageDataInfo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;
import com.lihang.selfmvvm.vo.res.VersionVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.lihang.selfmvvm.base.BaseConstant.APK_URL;
import static com.lihang.selfmvvm.base.BaseConstant.DEFAULT_FILE_SERVER;
import static com.lihang.selfmvvm.common.SystemConst.DEFAULT_SERVER;

/**
 * created by zhangxianpeng
 * 主界面 fragment
 */
public class HomeFragment extends BaseFragment<HomeFragmentViewModel, FragmentHomeBinding> implements OnRefreshListener, OnLoadMoreListener {
    private static final String TAG = "HomeFragment";
    private HomeMenuAdapter homeMenuAdapter;
    private ProjectListAdapter projectListAdapter;
    private List<String> bannerImagePathList = new ArrayList<>();
    private List<ImageDataInfo> bannerDataSourceList = new ArrayList<>();
    private List<String> bannerTitleList = new ArrayList<>();
    private List<HomeMenuBean> homeMenuPathList = new ArrayList<>();

    private List<MsgMeResVo> projectList = new ArrayList<>();

    private NewIOSAlertDialog myDialog;

    //默认加载页码
    private int page = 1;

    //是否删除数据源
    private boolean isClearData = false;

    //横向滑动卡片相关
    private ShowIndustryListFragment fragment1, fragment2, fragment3, fragment4;
    private ArrayList<Fragment> fragments;
    private FragmentManager fragmentManager;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initFreshLayout();
        initMenuData();
        initMsgAdapter();
        initHorizontalSwipeView();
        if (TextUtils.isEmpty(MyApplication.getToken())) {
            ActivityUtils.startActivity(getContext(), GovassLoginActivity.class);
            getActivity().finish();
        } else {
            initBannerData();
            initMsgMeList(page, true);
            getUnReadMsgCount();
            getNewAppVersion(0);
        }
    }

    private void initHorizontalSwipeView() {
        fragmentManager = getActivity().getSupportFragmentManager();
        fragments = new ArrayList<Fragment>();
        fragment1 = new ShowIndustryListFragment();
        fragment2 = new ShowIndustryListFragment();
        fragment3 = new ShowIndustryListFragment();
        fragment4 = new ShowIndustryListFragment();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);

        binding.viewPager.setOffscreenPageLimit(fragments.size());//卡片数量
        binding.viewPager.setPageMargin(10);//两个卡片之间的距离，单位dp

        if (binding.viewPager!=null){
            binding.viewPager.removeAllViews();
        }

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        binding.viewPager.setAdapter(myFragmentPagerAdapter);
    }

    /**
     * 获取版本更新
     * Android device 0
     *
     * @param device
     */
    private void getNewAppVersion(int device) {
        mViewModel.getNewVersion(device).observe(this, res -> {
            res.handler(new OnCallback<VersionVo>() {
                @Override
                public void onSuccess(VersionVo newVersion) {
                    String version = newVersion.getAppVersion();
                    String newAppVersion = version.replace(".", "");
                    int newVersionCode = 0;
                    try {
                        newVersionCode = Integer.parseInt(newAppVersion);
                    } catch (NumberFormatException e) {
                        System.out.println(e);
                    }
                    String apkUrl = newVersion.getUrl();
                    if (!TextUtils.isEmpty(apkUrl)) {
                        PreferenceUtil.put(APK_URL, apkUrl);
                    }
                    String updateContent = newVersion.getChangeLog();
                    String updateTitle = "发现新版本V" + newAppVersion;
                    int forceFlag = newVersion.getForceFlag();
                    String currentVersion = PackageUtils.getVersionName(getContext());
                    String currentVersion2 = currentVersion.replace(".", "");
                    int currentVersionCode = 0;
                    try {
                        currentVersionCode = Integer.parseInt(currentVersion2);
                    } catch (NumberFormatException e) {
                        System.out.println(e);
                    }
                    if (newVersionCode > currentVersionCode)
                        showUpdateDialog(apkUrl, updateContent, updateTitle, forceFlag);
                }
            });
        });
    }

    /**
     * 显示更新弹框
     *
     * @param apkUrl        跳转外链地址
     * @param updateContent 更新内容
     * @param updateTitle   标题
     * @param forceFlag     是否强制更新
     */
    private void showUpdateDialog(String apkUrl, String updateContent, String updateTitle, int forceFlag) {
        myDialog = new NewIOSAlertDialog(getContext()).builder();
        myDialog.setGone().setTitle(updateTitle).setMsg(updateContent).setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(apkUrl));
                startActivity(intent);
            }
        }).show();
    }

    private void initFreshLayout() {
        binding.smartfreshlayout.setOnRefreshListener(this::onRefresh);
        binding.smartfreshlayout.setOnLoadMoreListener(this::onLoadMore);
    }

    private void initMenuAdapter(String unReadMsgCount) {
        homeMenuAdapter = new HomeMenuAdapter(getContext(), homeMenuPathList, unReadMsgCount);
        binding.rvMenu.setLayoutManager(new GridLayoutManager(getContext(), 4));
        binding.rvMenu.setAdapter(homeMenuAdapter);
        homeMenuAdapter.setOnItemClickListener((view, position) -> {
            HomeMenuBean bean = homeMenuPathList.get(position);
            if (bean.getTitle().equals(getString(R.string.questionnaire))) {
                ActivityUtils.startActivity(getContext(), QuestionNaireActivity.class);
            } else if (bean.getTitle().equals(getString(R.string.project_application))) {
                if (CheckPermissionUtils.getInstance().isGovernment()) {
                    ActivityUtils.startActivity(getContext(), ProjectActivity.class);
                } else {
                    ActivityUtils.startActivity(getContext(), ProjectDeclareActivity.class);
                }
            } else if (bean.getTitle().equals(getString(R.string.enterprise_account_management))) {
                ToastUtils.showToast(getString(R.string.enterprise_account_management));
            } else if (bean.getTitle().equals(getString(R.string.system_announcement))) {
                ActivityUtils.startActivity(getContext(), OfficialDocListActivity.class);

            } else if (bean.getTitle().equals(getString(R.string.contact_customer_service))) {
                ActivityUtils.startActivity(getContext(), CustomServerActivity.class);
            } else if (bean.getTitle().equals(getString(R.string.my_business))) {
                ActivityUtils.startActivity(getContext(), MyEnterprisesListActivity.class);
            } else if (bean.getTitle().equals(getString(R.string.message_notification))) {
                ActivityUtils.startActivity(getContext(), NewMsgActivity.class);
            } else if (bean.getTitle().equals(getString(R.string.enprice_od))) {
                if (CheckPermissionUtils.getInstance().isGovernment()) {
                    ActivityUtils.startActivity(getContext(), GovermentEnpriceODActivity.class);
                } else {
                    ActivityUtils.startActivity(getContext(), EnpriceODActivity.class);
                }
            }
        });
    }

    private void getUnReadMsgCount() {
        mViewModel.getMsgUnRead().observe(getActivity(), res -> {
            res.handler(new OnCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    initMenuAdapter(data);
                }
            });
        });
    }

    private void initMsgAdapter() {
        projectListAdapter = new ProjectListAdapter(getContext(), projectList);
        binding.rvProjectMsg.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvProjectMsg.setAdapter(projectListAdapter);
        projectListAdapter.setOnItemClickListener((view, position) -> {
            MsgMeResVo msgMeResVo = projectList.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("msgMeResVo", msgMeResVo);
            ActivityUtils.startActivityWithBundle(getContext(), MsgDetailActivity.class, bundle);
        });
    }

    private void initBannerData() {
        mViewModel.getGovassBannerList(MyApplication.getToken()).observe(this, res -> {
            res.handler(new OnCallback<List<ImageDataInfo>>() {
                @Override
                public void onSuccess(List<ImageDataInfo> data) {
                    bannerImagePathList.clear();
                    bannerTitleList.clear();
                    for (int i = 0; i < data.size(); i++) {
                        bannerDataSourceList.addAll(data);
                        bannerImagePathList.add(DEFAULT_SERVER + DEFAULT_FILE_SERVER + data.get(i).getImageUrl());
                        bannerTitleList.add("");
                    }
                    initBannerWidget();
                }
            });
        });
    }

    private void initBannerWidget() {
        //样式设置
        binding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        binding.banner.setImageLoader(new GlideImageLoader());
        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
        binding.banner.setBannerAnimation(Transformer.ZoomOutSlide);
        //设置轮播间隔时间
        binding.banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是true
        binding.banner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        binding.banner.setIndicatorGravity(BannerConfig.LEFT);
        //设置图片加载地址
        binding.banner.setImages(bannerImagePathList)
                .setBannerTitles(bannerTitleList)
                .start();
        binding.banner.setOnBannerListener(new OnBannerListener() {
            @SuppressLint("NewApi")
            @Override
            public void OnBannerClick(int position) {
                for (int i = 0; i < bannerDataSourceList.size(); i++) {
                    if (i == position) {
                        ImageDataInfo imageDataInfo = bannerDataSourceList.get(i);
                        Bundle bundle = new Bundle();
                        bundle.putString("flag", "homebanner");
                        bundle.putSerializable("imageDataInfo", imageDataInfo);
                        ActivityUtils.startActivityWithBundle(getContext(), OfficialDocDetailActivity.class, bundle);
                    }
                }
            }
        });
    }

    private void initMenuData() {
        HomeMenuBean surveyBean = new HomeMenuBean();
        surveyBean.setImageUrl(R.mipmap.home_ic_survey);
        surveyBean.setTitle(getContext().getString(R.string.questionnaire));
        homeMenuPathList.add(surveyBean);

        HomeMenuBean declareBean = new HomeMenuBean();
        declareBean.setImageUrl(R.mipmap.home_ic_declare);
        declareBean.setTitle(getContext().getString(R.string.project_application));
        homeMenuPathList.add(declareBean);

        HomeMenuBean accountBean = new HomeMenuBean();
        accountBean.setImageUrl(R.mipmap.home_ic_account);
        accountBean.setTitle(getContext().getString(R.string.enterprise_account_management));
        homeMenuPathList.add(accountBean);

        HomeMenuBean announcementBean = new HomeMenuBean();
        announcementBean.setImageUrl(R.mipmap.home_ic_announcement);
        announcementBean.setTitle(getContext().getString(R.string.system_announcement));
        homeMenuPathList.add(announcementBean);

        HomeMenuBean callBean = new HomeMenuBean();
        callBean.setImageUrl(R.mipmap.home_ic_call);
        callBean.setTitle(getContext().getString(R.string.contact_customer_service));
        homeMenuPathList.add(callBean);

        HomeMenuBean mybusinessBean = new HomeMenuBean();
        mybusinessBean.setImageUrl(R.mipmap.home_ic_mybusiness);
        mybusinessBean.setTitle(getContext().getString(R.string.my_business));
        if (CheckPermissionUtils.getInstance().isGovernment()) {
            homeMenuPathList.add(mybusinessBean);
        }

        HomeMenuBean msgBean = new HomeMenuBean();
        msgBean.setImageUrl(R.mipmap.home_ic_alerts);
        msgBean.setTitle(getContext().getString(R.string.message_notification));
        homeMenuPathList.add(msgBean);

        HomeMenuBean odBean = new HomeMenuBean();
        odBean.setImageUrl(R.mipmap.home_ic_send);
        odBean.setTitle(getContext().getString(R.string.enprice_od));
        homeMenuPathList.add(odBean);
    }

    private void initMsgMeList(int page, boolean isClearData) {
        mViewModel.getMsgMeList(page).observe(getActivity(), res -> {
            res.handler(new OnCallback<ListBaseResVo<MsgMeResVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<MsgMeResVo> data) {
                    if (isClearData) {
                        projectList.clear();
                    }
                    projectList.addAll(data.getList());
                    projectListAdapter.notifyDataSetChanged();
                }
            });
        });
    }


    @Override
    protected void setListener() {
        binding.banner.setOnBannerListener(position -> {
            LogUtils.d(TAG, "CLICK===" + position);
        });

        binding.flNewMsg.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_new_msg:
                ActivityUtils.startActivity(getContext(), NewMsgActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page += 1;
        initMsgMeList(page, false);
        binding.smartfreshlayout.finishLoadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getUnReadMsgCount();
        initMsgMeList(1, true);
        initBannerData();
        binding.smartfreshlayout.finishRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUnReadMsgCount();
    }
}