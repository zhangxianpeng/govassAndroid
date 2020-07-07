package com.lihang.selfmvvm.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.lihang.nbadapter.BaseAdapter;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.bean.HomeMenuBean;
import com.lihang.selfmvvm.databinding.FragmentHomeBinding;
import com.lihang.selfmvvm.ui.main.HomeMenuAdapter;
import com.lihang.selfmvvm.ui.main.ProjectListAdapter;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.utils.GlideImageLoader;
import com.lihang.selfmvvm.utils.LogUtils;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;


/**
 * 主界面 fragment
 */
public class HomeFragment extends BaseFragment<HomeFragmentViewModel, FragmentHomeBinding> implements BaseAdapter.OnItemClickListener<HomeMenuBean> {
    private static final String TAG = "HomeFragment";
    private HomeMenuAdapter homeMenuAdapter;
    private ProjectListAdapter projectListAdapter;
    private ArrayList<Integer> bannerImagePathList = new ArrayList<>();
    private ArrayList<String> bannerTitleList = new ArrayList<>();
    private ArrayList<HomeMenuBean> homeMenuPathList = new ArrayList<>();
    private ArrayList<String> projectList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        //默认 banner
        //默认菜单
        //默认 projectList
        initData();
        initBanner();
        initMenu();
        initProjectList();
    }

    private void initData() {
        //轮播
        bannerImagePathList.add(R.mipmap.home_default_img);
        bannerImagePathList.add(R.mipmap.default_img);
        bannerImagePathList.add(R.mipmap.default_img);
        bannerTitleList.add("");
        bannerTitleList.add("");
        bannerTitleList.add("");
    }

    private void initBanner() {
        //样式设置
        binding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        binding.banner.setImageLoader(new GlideImageLoader());
        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
        binding.banner.setBannerAnimation(Transformer.ZoomOutSlide);
        //轮播图片的文字
//        binding.banner.setBannerTitles(imageTitle);
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

//        binding.banner.setBannerTitles(titles);
//        binding.banner.setImages(urls);
//        binding.banner.start();
    }

    private void initMenu() {
        initMenuData();
        homeMenuAdapter = new HomeMenuAdapter(getContext(), homeMenuPathList);
        binding.rvMenu.setLayoutManager(new GridLayoutManager(getContext(), 4));
        binding.rvMenu.setAdapter(homeMenuAdapter);
        homeMenuAdapter.setOnItemClickListener((view, position) -> LogUtils.d(TAG, "menuClick===" + position));
    }

    private void initMenuData() {
        //菜单
        HomeMenuBean surveyBean = new HomeMenuBean();
        surveyBean.setImageUrl(R.mipmap.home_ic_survey);
        surveyBean.setTitle("调查问卷");
        homeMenuPathList.add(surveyBean);

        HomeMenuBean declareBean = new HomeMenuBean();
        declareBean.setImageUrl(R.mipmap.home_ic_declare);
        declareBean.setTitle("项目申报");
        homeMenuPathList.add(declareBean);

        HomeMenuBean accountBean = new HomeMenuBean();
        accountBean.setImageUrl(R.mipmap.home_ic_account);
        accountBean.setTitle("企业账户管理");
        homeMenuPathList.add(accountBean);

        HomeMenuBean announcementBean = new HomeMenuBean();
        announcementBean.setImageUrl(R.mipmap.home_ic_announcement);
        announcementBean.setTitle("系统公告");
        homeMenuPathList.add(announcementBean);

        HomeMenuBean callBean = new HomeMenuBean();
        callBean.setImageUrl(R.mipmap.home_ic_call);
        callBean.setTitle("联系客服");
        homeMenuPathList.add(callBean);

        HomeMenuBean mybusinessBean = new HomeMenuBean();
        mybusinessBean.setImageUrl(R.mipmap.home_ic_mybusiness);
        mybusinessBean.setTitle("我的企业");
        homeMenuPathList.add(mybusinessBean);
    }

    private void initProjectList() {
        initProjectLitData();
        projectListAdapter = new ProjectListAdapter(getContext(),projectList);
        binding.rvProjectMsg.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvProjectMsg.setAdapter(projectListAdapter);
        projectListAdapter.setOnItemClickListener((view, position) -> LogUtils.d(TAG, "project===" + position));
    }

    private void initProjectLitData() {
        //项目列表
        projectList.add("关于《政企消息》的审核结果");
        projectList.add("关于《活动中心》的通知日期时间");
        projectList.add("你的项目申请进度已有更新请查看");
        projectList.add("关于《政企消息》的审核结果");
        projectList.add("关于《活动中心》的通知日期时间");
    }

    @Override
    protected void setListener() {
        binding.banner.setOnBannerListener(position -> {
            LogUtils.d(TAG, "CLICK===" + position);
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(HomeMenuBean item, int position) {

    }
}