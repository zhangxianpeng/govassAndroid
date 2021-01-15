package com.lihang.selfmvvm.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.MyApplication;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.customview.iosdialog.NewIOSAlertDialog;
import com.lihang.selfmvvm.databinding.FragmentHomeBinding;
import com.lihang.selfmvvm.ui.customerservicefeedback.FeedBackListActivity;
import com.lihang.selfmvvm.ui.fragment.adapter.ProjectListAdapter;
import com.lihang.selfmvvm.ui.globalsearch.GlobalSearchActivity;
import com.lihang.selfmvvm.ui.login.GovassLoginActivity;
import com.lihang.selfmvvm.ui.msgdetail.MsgDetailActivity;
import com.lihang.selfmvvm.ui.newmsg.NewMsgActivity;
import com.lihang.selfmvvm.ui.officialdoc.OfficialDocDetailActivity;
import com.lihang.selfmvvm.ui.officialdoc.OfficialDocListActivity;
import com.lihang.selfmvvm.ui.project.ProjectActivity;
import com.lihang.selfmvvm.ui.projrctdeclare.ProjectDeclareActivity;
import com.lihang.selfmvvm.ui.questionnaire.QuestionNaireActivity;
import com.lihang.selfmvvm.ui.register.RegisterStepOneActivity;
import com.lihang.selfmvvm.ui.userinfo.UserInfoActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.utils.DensityUtils;
import com.lihang.selfmvvm.utils.PackageUtils;
import com.lihang.selfmvvm.utils.PreferenceUtil;
import com.lihang.selfmvvm.vo.res.ImageDataInfo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;
import com.lihang.selfmvvm.vo.res.VersionVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.stx.xhb.androidx.XBanner;
import com.stx.xhb.androidx.entity.LocalImageInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.lihang.selfmvvm.base.BaseConstant.APK_URL;
import static com.lihang.selfmvvm.base.BaseConstant.DEFAULT_FILE_SERVER;
import static com.lihang.selfmvvm.common.SystemConst.DEFAULT_SERVER;

/**
 * created by zhangxianpeng
 * 主界面 fragment
 */
public class HomeFragment extends BaseFragment<HomeFragmentViewModel, FragmentHomeBinding> implements OnRefreshListener, OnLoadMoreListener {

    private ProjectListAdapter projectListAdapter;
    private List<ImageDataInfo> bannerDataSourceList = new ArrayList<>();
    private List<MsgMeResVo> projectList = new ArrayList<>();
    private NewIOSAlertDialog myDialog;

    //默认加载页码
    private int page = 1;

    private static final int REQUEST_CODE = 0X100;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initFreshLayout();
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
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtils.getScreenWidth() / 2);
        binding.horizontalBanner.setLayoutParams(layoutParams);
        initBanner(binding.horizontalBanner);
        initLocalImage();
    }

    private void initBanner(XBanner banner) {
        banner.setOnItemClickListener((banner1, model, view, position) -> {
            switch (position) {
                case 0:
                    ActivityUtils.startActivity(getContext(), OfficialDocListActivity.class);
                    break;
                case 1:
                    if (CheckPermissionUtils.getInstance().isGovernment()) {
                        ActivityUtils.startActivity(getContext(), ProjectActivity.class);
                    } else {
                        ActivityUtils.startActivity(getContext(), ProjectDeclareActivity.class);
                    }
                    break;
                case 2:
                    ActivityUtils.startActivity(getContext(), QuestionNaireActivity.class);
                    break;
                case 3:  //服务新功能
                    ActivityUtils.startActivity(getContext(), FeedBackListActivity.class);
                    break;
                case 4:  //党建空卡片
                    break;
                default:
                    break;
            }
        });
        banner.loadImage((banner1, model, view, position) -> ((ImageView) view).setImageResource(((LocalImageInfo) model).getXBannerUrl()));
    }

    private void initLocalImage() {
        List<LocalImageInfo> data = new ArrayList<>();
        data.add(new LocalImageInfo(R.mipmap.home_ic_gonggao));
        data.add(new LocalImageInfo(R.mipmap.home_ic_xiangmu));
        data.add(new LocalImageInfo(R.mipmap.home_ic_wenjuan));
        data.add(new LocalImageInfo(R.mipmap.home_ic_fuwu));
        data.add(new LocalImageInfo(R.mipmap.home_ic_dangjian));
        binding.horizontalBanner.setBannerData(data);
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

                @Override
                public void onFailure(String msg) {
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

    private void getUnReadMsgCount() {
        mViewModel.getMsgUnRead().observe(getActivity(), res -> {
            res.handler(new OnCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    binding.badgeView.setVisibility(data.equals("0") ? View.GONE : View.VISIBLE);
                    binding.badgeView.setText(data);
                }

                @Override
                public void onFailure(String msg) {

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
            bundle.putString("uiflag", "newmsg");
            bundle.putSerializable("msgMeResVo", msgMeResVo);
            ActivityUtils.startActivityWithBundle(getContext(), MsgDetailActivity.class, bundle);
        });
    }

    private void initBannerData() {
        mViewModel.getGovassBannerList(MyApplication.getToken()).observe(this, res -> {
            res.handler(new OnCallback<List<ImageDataInfo>>() {
                @Override
                public void onSuccess(List<ImageDataInfo> data) {
                    bannerDataSourceList.clear();
                    bannerDataSourceList.addAll(data);
                    List<ImageView> list = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        ImageView imageView = new ImageView(getActivity());
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(getActivity()).load(DEFAULT_SERVER + DEFAULT_FILE_SERVER + data.get(i).getImageUrl()).placeholder(R.mipmap.default_img)
                                .error(R.mipmap.default_img).into(imageView);
                        list.add(imageView);
                    }
                    binding.viewflipper.setViews(list);
                }
            });
        });
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

                @Override
                public void onFailure(String msg) {
                    myDialog = new NewIOSAlertDialog(getContext()).builder();
                    myDialog.setGone().setTitle("温馨提示").setCancelable(false).setMsg(msg).setPositiveButton("确定", view -> {
                        ActivityUtils.startActivity(getActivity(), RegisterStepOneActivity.class);
                        getActivity().finish();
                    }).show();
                }
            });
        });
    }

    @Override
    protected void setListener() {
        binding.flNewMsg.setOnClickListener(this::onClick);
        binding.floatingButton.setOnClickListener(this::onClick);
        binding.viewflipper.setOnClickListener(this::onClick);
        binding.etSearch.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.fl_new_msg:
                ActivityUtils.startActivity(getContext(), NewMsgActivity.class);
                break;
            case R.id.floatingButton:
                startActivityForResult(new Intent(getActivity(), UserInfoActivity.class), REQUEST_CODE);
                break;
            case R.id.viewflipper:
                ImageDataInfo imageDataInfo = bannerDataSourceList.get(binding.viewflipper.getDisplayedChild());
                Bundle bundle = new Bundle();
                bundle.putString("flag", "homebanner");
                bundle.putSerializable("imageDataInfo", imageDataInfo);
                ActivityUtils.startActivityWithBundle(getContext(), OfficialDocDetailActivity.class, bundle);
                break;
            case R.id.et_search:
                ActivityUtils.startActivity(getActivity(), GlobalSearchActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode && resultCode == 3) {
            getActivity().finish();
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