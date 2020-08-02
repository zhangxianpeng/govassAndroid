package com.lihang.selfmvvm.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.bean.HomeMenuBean;
import com.lihang.selfmvvm.databinding.FragmentHomeBinding;
import com.lihang.selfmvvm.ui.fragment.adapter.HomeMenuAdapter;
import com.lihang.selfmvvm.ui.fragment.adapter.ProjectListAdapter;
import com.lihang.selfmvvm.ui.login.GovassLoginActivity;
import com.lihang.selfmvvm.ui.newmsg.NewMsgActivity;
import com.lihang.selfmvvm.ui.questionnaire.QuestionNaireActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.GlideImageLoader;
import com.lihang.selfmvvm.utils.LogUtils;
import com.lihang.selfmvvm.utils.PreferenceUtil;
import com.lihang.selfmvvm.vo.res.ImageDataInfo;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.lihang.selfmvvm.base.BaseConstant.DEFAULT_FILE_SERVER;
import static com.lihang.selfmvvm.base.BaseConstant.USER_LOGIN_TOKEN;
import static com.lihang.selfmvvm.common.SystemConst.DEFAULT_SERVER;


/**
 * 主界面 fragment
 */
public class HomeFragment extends BaseFragment<HomeFragmentViewModel, FragmentHomeBinding> {
    private static final String TAG = "HomeFragment";
    private HomeMenuAdapter homeMenuAdapter;
    private ProjectListAdapter projectListAdapter;
    private ArrayList<String> bannerImagePathList = new ArrayList<>();
    private ArrayList<String> bannerTitleList = new ArrayList<>();
    private ArrayList<HomeMenuBean> homeMenuPathList = new ArrayList<>();
    private ArrayList<String> projectList = new ArrayList<>();

    private String token = "";


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        token = (String) PreferenceUtil.get(USER_LOGIN_TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            ActivityUtils.startActivity(getContext(), GovassLoginActivity.class);
        } else {
            initBannerData();
        }
        initBannerWidget();
        initMenu();
        initProjectList();
    }

    private void initBannerData() {
        mViewModel.getGovassBannerList(token).observe(this, res -> {
            res.handler(new OnCallback<List<ImageDataInfo>>() {
                @Override
                public void onSuccess(List<ImageDataInfo> data) {
                    for (int i = 0; i < data.size(); i++) {
                        bannerImagePathList.add(DEFAULT_SERVER + DEFAULT_FILE_SERVER + data.get(i).getImageUrl());
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


        //轮播
//        bannerImagePathList.add(R.mipmap.home_default_img);
//        bannerImagePathList.add(R.mipmap.default_img);
//        bannerImagePathList.add(R.mipmap.default_img);
        bannerTitleList.add("");
        bannerTitleList.add("");
        bannerTitleList.add("");
    }

    private void initBannerWidget() {
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
        homeMenuAdapter.setOnItemClickListener((view, position) -> {
            switch (position) {
                case 0:  //调查问卷
                    ActivityUtils.startActivity(getContext(), QuestionNaireActivity.class);
                    break;
                case 1: //项目申报
                    break;
                case 2: //企业账户管理
                    break;
                case 3: //系统公告
                    break;
                case 4: //联系客服
                    break;
                case 5: //我的企业
                    break;
                case 6: //消息通知
                    ActivityUtils.startActivity(getContext(), NewMsgActivity.class);
                    break;
//                case 7:
//                    break;
                default:
                    break;
            }
        });
    }

    private void initMenuData() {
        //菜单
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
        homeMenuPathList.add(mybusinessBean);

        HomeMenuBean msgBean = new HomeMenuBean();
        msgBean.setImageUrl(R.mipmap.home_ic_alerts);
        msgBean.setTitle(getContext().getString(R.string.message_notification));
        homeMenuPathList.add(msgBean);
    }

    private void initProjectList() {
        initProjectLitData();
        projectListAdapter = new ProjectListAdapter(getContext(), projectList);
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
}