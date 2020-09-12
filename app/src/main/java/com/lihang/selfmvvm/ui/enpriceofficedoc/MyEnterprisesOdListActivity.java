package com.lihang.selfmvvm.ui.enpriceofficedoc;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityMyEnterprisesOdListBinding;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.AddOdReqVo;
import com.lihang.selfmvvm.vo.res.EnpriceOdVo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 我的企业公告（企业端）
 * created  by zhangxianpeng
 */
public class MyEnterprisesOdListActivity extends BaseActivity<EnpriceODViewModel, ActivityMyEnterprisesOdListBinding> implements PopupWindow.OnDismissListener {

    /**
     * 企業公告列表
     */
    private List<EnpriceOdVo> enterpriseVoList = new ArrayList<>();

    /**
     * 默认页数
     */
    private int page = 1;

    private CommonAdapter commonAdapter;

    /**
     * 默认的status
     */
    private int status = 1;

    /**
     * 发送公告弹框
     */
    private PopupWindow sendOdPop;

    /**
     * 发送消息请求VO
     */
    private String plainMsgContent = "";
    private String plainMsgTitle = "";
    private AddOdReqVo addOdReqVo = new AddOdReqVo();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_enterprises_od_list;
    }

    @Override
    protected void processLogic() {
        initFreshLayout();
        initAdapter();
        // 待审核 status 传 1
        getEnterpriseNoticeList(1, true, 1);
    }

    private void getEnterpriseNoticeList(int page, boolean isClearSourceData, int status) {
        mViewModel.getEnterpriseNoticeListComment(page, status).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<EnpriceOdVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<EnpriceOdVo> data) {
                    if (isClearSourceData) {
                        enterpriseVoList.clear();
                    }
                    enterpriseVoList.addAll(data.getList());
                    if (commonAdapter != null) commonAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    private void initFreshLayout() {
        binding.smartfreshlayout.setOnRefreshListener(this::refresh);
        binding.smartfreshlayout.setOnLoadMoreListener(this::loadMore);
    }

    private void initAdapter() {
        binding.rvEnterpriseOd.setLayoutManager(new LinearLayoutManager(getContext()));
        commonAdapter = new CommonAdapter<EnpriceOdVo>(getContext(), R.layout.goverment_project_list_item, enterpriseVoList) {

            @Override
            protected void convert(ViewHolder holder, EnpriceOdVo enpriceOdVo, int position) {
                holder.setText(R.id.tv_title, enpriceOdVo.getTitle());
                holder.setText(R.id.tv_time, enpriceOdVo.getCreateTime());
                holder.setText(R.id.tv_ui_flag, enpriceOdVo.getEnterpriseName());
                holder.setOnClickListener(R.id.rl_container, view -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("enpriceOdVo", enpriceOdVo);
                    ActivityUtils.startActivityWithBundle(MyEnterprisesOdListActivity.this, OdDetailActivity.class, bundle);
                });
            }
        };
        binding.rvEnterpriseOd.setAdapter(commonAdapter);
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
        binding.rlTabDaishenhe.setOnClickListener(this::onClick);
        binding.rlTabYishenhe.setOnClickListener(this::onClick);
        binding.rlTabShenheFail.setOnClickListener(this::onClick);
        binding.rlTabYifabu.setOnClickListener(this::onClick);
        binding.rlTabYixiaxian.setOnClickListener(this::onClick);
        binding.tvMyOd.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.rl_tab_daishenhe:
                status = 1;
                updateTabBtnView(status, binding.viewDaishenhe, binding.viewYishenhe,
                        binding.viewShenheFail, binding.viewYifabu, binding.viewYixiaxian);
                getEnterpriseNoticeList(1, true, 1);
                break;
            case R.id.rl_tab_yishenhe:
                status = 2;
                updateTabBtnView(status, binding.viewDaishenhe, binding.viewYishenhe,
                        binding.viewShenheFail, binding.viewYifabu, binding.viewYixiaxian);
                getEnterpriseNoticeList(1, true, 2);
                break;
            case R.id.rl_tab_shenhe_fail:
                status = 3;
                updateTabBtnView(status, binding.viewDaishenhe, binding.viewYishenhe,
                        binding.viewShenheFail, binding.viewYifabu, binding.viewYixiaxian);
                getEnterpriseNoticeList(1, true, 3);
                break;
            case R.id.rl_tab_yifabu:
                status = 4;
                updateTabBtnView(status, binding.viewDaishenhe, binding.viewYishenhe,
                        binding.viewShenheFail, binding.viewYifabu, binding.viewYixiaxian);
                getEnterpriseNoticeList(1, true, 4);
                break;
            case R.id.rl_tab_yixiaxian:
                status = 5;
                updateTabBtnView(status, binding.viewDaishenhe, binding.viewYishenhe,
                        binding.viewShenheFail, binding.viewYifabu, binding.viewYixiaxian);
                getEnterpriseNoticeList(1, true, 5);
                break;
            case R.id.tv_my_od:
                showAddOfficeDocDialog(view);
                break;
            default:
                break;
        }
    }

    /**
     * 更新状态
     *
     * @param status
     * @param viewDaishenhe
     * @param viewYishenhe
     * @param viewShenheFail
     * @param viewYifabu
     * @param viewYixiaxian
     */
    @SuppressLint("NewApi")
    private void updateTabBtnView(int status, View viewDaishenhe, View viewYishenhe, View viewShenheFail, View viewYifabu, View viewYixiaxian) {
        if (status == 1) {
            viewDaishenhe.setBackgroundColor(getContext().getColor(R.color.tab_selected));
            viewYishenhe.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewShenheFail.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewYifabu.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewYixiaxian.setBackgroundColor(getContext().getColor(R.color.tab_normal));
        } else if (status == 2) {
            viewDaishenhe.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewYishenhe.setBackgroundColor(getContext().getColor(R.color.tab_selected));
            viewShenheFail.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewYifabu.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewYixiaxian.setBackgroundColor(getContext().getColor(R.color.tab_normal));
        } else if (status == 3) {
            viewDaishenhe.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewYishenhe.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewShenheFail.setBackgroundColor(getContext().getColor(R.color.tab_selected));
            viewYifabu.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewYixiaxian.setBackgroundColor(getContext().getColor(R.color.tab_normal));
        } else if (status == 4) {
            viewDaishenhe.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewYishenhe.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewShenheFail.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewYifabu.setBackgroundColor(getContext().getColor(R.color.tab_selected));
            viewYixiaxian.setBackgroundColor(getContext().getColor(R.color.tab_normal));
        } else if (status == 5) {
            viewDaishenhe.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewYishenhe.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewShenheFail.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewYifabu.setBackgroundColor(getContext().getColor(R.color.tab_normal));
            viewYixiaxian.setBackgroundColor(getContext().getColor(R.color.tab_selected));
        }
    }

    private void showAddOfficeDocDialog(View rootView) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        initsendOdPopView(contentView);
        int height = (int) getResources().getDimension(R.dimen.dp_350);
        sendOdPop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, height, true);
        sendOdPop.setAnimationStyle(R.style.ActionSheetDialogAnimation);
        sendOdPop.setOutsideTouchable(true);
        backgroundAlpha(0.3f);
        sendOdPop.setOnDismissListener(this);
        sendOdPop.setBackgroundDrawable(new BitmapDrawable());
        sendOdPop.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    private void initsendOdPopView(View view) {
        ImageView closeIv = view.findViewById(R.id.iv_close);
        EditText titleEt = view.findViewById(R.id.et_title);
        EditText textEt = view.findViewById(R.id.et_text);
        RecyclerView attachmentRv = view.findViewById(R.id.rv_attachment);
        attachmentRv.setVisibility(View.GONE);
        view.findViewById(R.id.tv_attachment).setVisibility(View.GONE);
        view.findViewById(R.id.rl_add_attachment).setVisibility(View.GONE);
        Button sendBtn = view.findViewById(R.id.btn_send);

        closeIv.setOnClickListener((view1 -> {
            if (sendOdPop != null) sendOdPop.dismiss();
        }));

        textEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                plainMsgContent = charSequence.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        titleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                plainMsgTitle = charSequence.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendBtn.setOnClickListener(view1 -> {
            if (!TextUtils.isEmpty(plainMsgTitle)) {
                addOdReqVo.setTitle(plainMsgTitle);
            } else {
                ToastUtils.showToast("请输入公告标题");
            }
            if (!TextUtils.isEmpty(plainMsgContent)) {
                addOdReqVo.setContent(plainMsgContent);
            } else {
                ToastUtils.showToast("请输入公告内容");
            }
            //新增企业公告
            mViewModel.saveOd(addOdReqVo).observe(this, res -> {
                res.handler(new OnCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        ToastUtils.showToast("公告发布成功");
                        if (sendOdPop != null) sendOdPop.dismiss();
                    }
                });
            });
        });
    }

    private void refresh(RefreshLayout refresh) {
        getEnterpriseNoticeList(1, true, status);
        binding.smartfreshlayout.finishRefresh();
    }

    private void loadMore(RefreshLayout layout) {
        page += 1;
        getEnterpriseNoticeList(page, false, status);
        binding.smartfreshlayout.finishLoadMore();
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //0.0-1.0
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1.0f);
        if (sendOdPop != null) sendOdPop.dismiss();
    }
}
