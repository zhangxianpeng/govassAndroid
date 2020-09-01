package com.lihang.selfmvvm.ui.officialdoc;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityOfficialDocListBinding;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.NoticeResVo;
import com.lihang.selfmvvm.vo.res.OfficialDocResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 系统公告界面  +  我的发文/收文界面
 * 公用界面
 */
public class OfficialDocListActivity extends BaseActivity<OfficialDocListViewModel, ActivityOfficialDocListBinding> {

    private CommonAdapter officialdocAdapter;
    private List<NoticeResVo> officialDocList = new ArrayList<>(); //公告
    private List<OfficialDocResVo> articalsList = new ArrayList<>(); //收发文


    /**
     * 界面标志位
     */
    private String uiFlag = "";
    /**
     * 是否请求公告
     */
    private boolean isGetOfficeDoc = false;
    /**
     * 默认请求页码
     */
    private int page = 1;
    /**
     * 是否删除源数据
     */
    private boolean isClearData = true;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_official_doc_list;
    }

    @Override
    protected void processLogic() {
        initFreshLayout();
        uiFlag = getIntent().getStringExtra("uiFlag");
        if (!TextUtils.isEmpty(uiFlag) && uiFlag.equals("myArticles")) {  // 我的收文/发文
            binding.tvMsg.setText(CheckPermissionUtils.getInstance().isGovernment() ? getString(R.string.my_post):getString(R.string.my_receive_msg));
            initAdapter(isGetOfficeDoc);
            initData(isGetOfficeDoc, page, isClearData);
        } else {  //公告
            binding.tvMsg.setText(getString(R.string.system_announcement));
            isGetOfficeDoc = true;
            initAdapter(isGetOfficeDoc);
            initData(isGetOfficeDoc, page, isClearData);
        }
    }

    private void initFreshLayout() {
        binding.smartfreshlayout.setOnRefreshListener(this::refresh);
        binding.smartfreshlayout.setOnLoadMoreListener(this::loadMore);
    }

    /**
     * 初始化适配器
     *
     * @param isGetOfficeDoc
     */
    private void initAdapter(boolean isGetOfficeDoc) {
        if (isGetOfficeDoc) {
            officialdocAdapter = new CommonAdapter<NoticeResVo>(getContext(), R.layout.goverment_project_list_item, officialDocList) {
                @Override
                protected void convert(ViewHolder holder, NoticeResVo noticeResVo, int position) {
                    holder.setText(R.id.tv_title, noticeResVo.getTitle());
                    holder.setText(R.id.tv_time, noticeResVo.getCreateTime());
                    holder.setText(R.id.tv_ui_flag, getString(R.string.system_announcement));

                    holder.setOnClickListener(R.id.rl_container, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            bundle.putString("flag", "noticelist");
                            bundle.putSerializable("noticeResVo", noticeResVo);
                            ActivityUtils.startActivityWithBundle(OfficialDocListActivity.this, OfficialDocDetailActivity.class, bundle);
                        }
                    });
                }
            };
        } else {
            officialdocAdapter = new CommonAdapter<OfficialDocResVo>(getContext(), R.layout.goverment_project_list_item, articalsList) {
                @Override
                protected void convert(ViewHolder holder, OfficialDocResVo officialDocResVo, int position) {
                    holder.setText(R.id.tv_title, officialDocResVo.getTitle());
                    holder.setText(R.id.tv_ui_flag, officialDocResVo.getCreateTime());
                    holder.setVisible(R.id.tv_time, false);
                    holder.setOnClickListener(R.id.rl_container, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            bundle.putString("flag", "articallist");
                            bundle.putSerializable("officialDocResVo", officialDocResVo);
                            ActivityUtils.startActivityWithBundle(OfficialDocListActivity.this, OfficialDocDetailActivity.class, bundle);
                        }
                    });
                }
            };
        }
        binding.rvOfficialdoc.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvOfficialdoc.setAdapter(officialdocAdapter);
    }

    private void initData(boolean isGetOfficeDoc, int page, boolean isClearData) {
        if (isGetOfficeDoc) {
            mViewModel.getPublishedNotice(page).observe(this, res -> {
                res.handler(new OnCallback<ListBaseResVo<NoticeResVo>>() {
                    @Override
                    public void onSuccess(ListBaseResVo<NoticeResVo> data) {
                        if (isClearData) {
                            officialDocList.clear();
                        }
                        officialDocList.addAll(data.getList());
                        officialdocAdapter.notifyDataSetChanged();
                    }
                });
            });
        } else {
            mViewModel.getOfficalDoc(page).observe(this, res -> {
                res.handler(new OnCallback<ListBaseResVo<OfficialDocResVo>>() {
                    @Override
                    public void onSuccess(ListBaseResVo<OfficialDocResVo> data) {
                        if (isClearData) {
                            articalsList.clear();
                        }
                        articalsList.addAll(data.getList());
                        officialdocAdapter.notifyDataSetChanged();
                    }
                });
            });
        }
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if(ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void refresh(RefreshLayout refresh) {
        initData(isGetOfficeDoc, 1, true);
        binding.smartfreshlayout.finishRefresh();
    }

    private void loadMore(RefreshLayout layout) {
        page += 1;
        initData(isGetOfficeDoc, page, false);
        binding.smartfreshlayout.finishLoadMore();
    }
}
