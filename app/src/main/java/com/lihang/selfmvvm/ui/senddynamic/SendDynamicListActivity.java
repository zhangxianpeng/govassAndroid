package com.lihang.selfmvvm.ui.senddynamic;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.adapter.MyAdapter;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivitySendDynamicListBinding;
import com.lihang.selfmvvm.vo.model.FriendGridItemVo;
import com.lihang.selfmvvm.vo.res.AttachmentResVo;
import com.lihang.selfmvvm.vo.res.DynamicVo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

import static com.lihang.selfmvvm.base.BaseConstant.DEFAULT_FILE_SERVER;
import static com.lihang.selfmvvm.common.SystemConst.DEFAULT_SERVER;

/**
 * created by zhangxianpeng on 2020/10/26
 * 动态发布历史
 */
public class SendDynamicListActivity extends BaseActivity<SendDynamicListViewModel, ActivitySendDynamicListBinding> {

    private List<DynamicVo> dynamicVoList = new ArrayList<>();
    private CommonAdapter msgAdapter;

    private BaseAdapter mAdapter = null;
    private ArrayList<FriendGridItemVo> mData = new ArrayList<>();

    /**
     * 默认请求页码
     */
    private int page = 1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_send_dynamic_list;
    }

    @Override
    protected void processLogic() {
        initFreshLayout();
        initAdapter();
        initData(page, true);
    }

    private void initFreshLayout() {
        binding.smartfreshlayout.setOnRefreshListener(this::refresh);
        binding.smartfreshlayout.setOnLoadMoreListener(this::loadMore);
    }

    private void initAdapter() {
        msgAdapter = new CommonAdapter<DynamicVo>(getContext(), R.layout.list_item_dynamic, dynamicVoList) {

            @Override
            protected void convert(ViewHolder holder, DynamicVo dynamicVo, int position) {
                holder.setImageResource(R.id.iv_head, R.mipmap.default_tx_img);
                holder.setText(R.id.tv_nick_name, dynamicVo.getEnterpriseName());
                holder.setText(R.id.tv_send_time, dynamicVo.getCreateTime());
                holder.setText(R.id.tv_content, dynamicVo.getContent());
                TextView dynamicStatus = holder.getView(R.id.tv_dynamic_status);
                TextView dynamicContentType = holder.getView(R.id.tv_content_type);
                setDynamicStatus(dynamicStatus, dynamicVo.getStatus());
                setContentType(dynamicContentType, dynamicVo.getContentType());
                GridView gridView = holder.getView(R.id.image_gridView);
                if (dynamicVo.getAttachmentList() != null && !dynamicVo.getAttachmentList().isEmpty()) {
                    setGridViewData(gridView, dynamicVo.getAttachmentList());
                }
            }
        };
        binding.rvNewMsg.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvNewMsg.setAdapter(msgAdapter);
    }

    private void setDynamicStatus(TextView dynamicStatus, int status) {
        String statusText = "";
        switch (status) {
            case 1:
                statusText = "待审核";
                break;
            case 2:
                statusText = "审核通过";
                break;
            case 3:
                statusText = "审核不通过";
                break;
            case 4:
                statusText = "已发布";
                break;
            case 5:
                statusText = "已下线";
                break;
        }
        dynamicStatus.setText(statusText);
    }

    private void setContentType(TextView dynamicContentType, int contentType) {
        dynamicContentType.setText(contentType == 0 ? getContext().getString(R.string.trends_of_thousand_enterprises) : getContext().getString(R.string.business_information));
    }

    private void setGridViewData(GridView gridView, List<AttachmentResVo> imageList) {
        mData.clear();
        for (AttachmentResVo attachmentResVo : imageList) {
            FriendGridItemVo friendGridItemVo = new FriendGridItemVo();
            friendGridItemVo.setImageRes(attachmentResVo.getUrl());
            mData.add(friendGridItemVo);
        }
        mAdapter = new MyAdapter<FriendGridItemVo>(mData, R.layout.grid_item_friend) {
            @Override
            public void bindView(ViewHolder holder, FriendGridItemVo obj) {
                ImageView imageView = holder.getView(R.id.img_icon);
                Glide.with(getContext()).load(DEFAULT_SERVER + DEFAULT_FILE_SERVER + obj.getImageRes()).placeholder(R.mipmap.default_tx_img)
                        .error(R.mipmap.default_tx_img).into(imageView);
            }
        };
        gridView.setAdapter(mAdapter);
    }

    private void initData(int page, boolean isClearData) {
        mViewModel.getDynamicHistoryList(page).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<DynamicVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<DynamicVo> data) {
                    if (isClearData) {
                        dynamicVoList.clear();
                    }
                    dynamicVoList.addAll(data.getList());
                    msgAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(1, true);
    }

    private void refresh(RefreshLayout refresh) {
        initData(1, true);
        binding.smartfreshlayout.finishRefresh();
    }

    private void loadMore(RefreshLayout layout) {
        page += 1;
        initData(page, false);
        binding.smartfreshlayout.finishLoadMore();
    }
}
