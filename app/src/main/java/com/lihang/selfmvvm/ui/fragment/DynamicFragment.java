package com.lihang.selfmvvm.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.bean.basebean.ResponModel;
import com.lihang.selfmvvm.databinding.FragmentDynamicBinding;
import com.lihang.selfmvvm.ui.fragment.adapter.FriendAdapter;
import com.lihang.selfmvvm.ui.senddynamic.SendDynamicActivity;
import com.lihang.selfmvvm.ui.senddynamic.SendDynamicListActivity;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.LogUtils;
import com.lihang.selfmvvm.vo.model.FriendGridItemVo;
import com.lihang.selfmvvm.vo.req.IdReqVo;
import com.lihang.selfmvvm.vo.res.AttachmentResVo;
import com.lihang.selfmvvm.vo.res.DynamicVo;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by zhangxianpeng on 20201017
 * 千企动态
 */
public class DynamicFragment extends BaseFragment<DynamicFragmentViewModel, FragmentDynamicBinding> {

    private CommonAdapter commonAdapter;
    private List<DynamicVo> friendDynamicList = new ArrayList<>();

    private FriendAdapter mAdapter = null;
    private ArrayList<FriendGridItemVo> mData = new ArrayList<>();

    private int page = 1;

    //区分千企还是商业  默认千企=0  商业 = 1
    private int contentType = 0;

    private boolean isLike = false;

    private static final int SKIP_TO_SEND_DYNAMIC = 1000;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initFreshLayout();
        initAdapter();
        initData(page, 0, true);
    }

    private void initFreshLayout() {
        binding.smartfreshlayout.setOnRefreshListener(this::refresh);
        binding.smartfreshlayout.setOnLoadMoreListener(this::loadMore);
    }

    private void initAdapter() {
        binding.rvDynamic.setLayoutManager(new LinearLayoutManager(getContext()));
        commonAdapter = new CommonAdapter<DynamicVo>(getContext(), R.layout.list_item_friend, friendDynamicList) {

            @Override
            protected void convert(ViewHolder holder, DynamicVo friend, int position) {
                holder.setImageResource(R.id.iv_head, R.mipmap.default_tx_img);
                holder.setText(R.id.tv_nick_name, friend.getEnterpriseName());
                holder.setText(R.id.tv_send_time, friend.getCreateTime());
                holder.setText(R.id.tv_content, friend.getContent());
                holder.setText(R.id.tv_like_count, getString(R.string.like_count, friend.getLikeCount()));
                holder.setBackgroundRes(R.id.btn_like, friend.isLiked() ? R.mipmap.ic_lianxi_love : R.mipmap.ic_like);
                RecyclerView gridView = holder.getView(R.id.rv_pic);
                if (friend.getAttachmentList() != null && !friend.getAttachmentList().isEmpty()) {
                    setGridViewData(gridView, friend.getAttachmentList());
                }
                holder.setOnClickListener(R.id.btn_like, view -> {
                    if (!isLike) {
                        isLike = true;
                        holder.setBackgroundRes(R.id.btn_like, R.mipmap.ic_lianxi_love);
                        holder.setText(R.id.tv_like_count, getString(R.string.like_count, friend.getLikeCount() + 1));
                    } else {
                        isLike = false;
                        holder.setBackgroundRes(R.id.btn_like, R.mipmap.ic_like);
                        holder.setText(R.id.tv_like_count, getString(R.string.like_count, friend.getLikeCount()));
                    }

                    likeDynamic(friend.getId());
                });
            }
        };
        binding.rvDynamic.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvDynamic.setAdapter(commonAdapter);
    }

    private void likeDynamic(int id) {
        IdReqVo idReqVo = new IdReqVo();
        idReqVo.setId(String.valueOf(id));
        mViewModel.likeDynamic(idReqVo).observe(this, res -> {
            res.handler(new OnCallback<ResponModel<String>>() {
                @Override
                public void onSuccess(ResponModel<String> data) {
                }
            });
        });
    }

    private void setGridViewData(RecyclerView gridView, List<AttachmentResVo> imageList) {
        mData.clear();
        for (AttachmentResVo attachmentResVo : imageList) {
            FriendGridItemVo friendGridItemVo = new FriendGridItemVo();
            friendGridItemVo.setImageRes(attachmentResVo.getUrl());
            mData.add(friendGridItemVo);

        }
        LogUtils.e("zzzzz" + mData.size());
        mAdapter = new FriendAdapter(mData);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        gridView.setLayoutManager(layoutManager);
        gridView.setAdapter(mAdapter);
    }

    private void refresh(RefreshLayout refresh) {
        initData(1, contentType, true);
        binding.smartfreshlayout.finishRefresh();
    }

    private void loadMore(RefreshLayout layout) {
        page += 1;
        initData(page, contentType, false);
        binding.smartfreshlayout.finishLoadMore();
    }

    private void initData(int page, int contentType, boolean isClearData) {
        mViewModel.getDynamicList(page, contentType).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<DynamicVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<DynamicVo> data) {
                    if (isClearData) {
                        friendDynamicList.clear();
                    }
                    friendDynamicList.addAll(data.getList());
                    if (commonAdapter != null) commonAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    @Override
    protected void setListener() {
        binding.tvDynamic.setOnClickListener(this::onClick);
        binding.tvBusiness.setOnClickListener(this::onClick);
        binding.btnSend.setOnClickListener(this::onClick);
        binding.btnSendHistory.setOnClickListener(this::onClick);
    }

    private void gotoSendDynamicPage() {
        Intent intent = new Intent(getContext(), SendDynamicActivity.class);
        intent.putExtra("contentType", contentType);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.tv_dynamic:
                updateView(0);
                break;
            case R.id.tv_business:
                updateView(1);
                break;
            case R.id.btn_send:
                gotoSendDynamicPage();
                break;
            case R.id.btn_send_history:
                Intent intent = new Intent(getContext(), SendDynamicListActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void updateView(int i) {
        if (i == 0) {
            binding.tvDynamic.setTextSize((float) 18.0);
            binding.tvDynamic.setTextColor(getResources().getColor(R.color.selected_color));
            binding.tvBusiness.setTextSize((float) 14.0);
            binding.tvBusiness.setTextColor(getResources().getColor(R.color.normal_color));
            contentType = 0;
        } else if (i == 1) {
            binding.tvBusiness.setTextSize((float) 18.0);
            binding.tvBusiness.setTextColor(getResources().getColor(R.color.selected_color));
            binding.tvDynamic.setTextSize((float) 14.0);
            binding.tvDynamic.setTextColor(getResources().getColor(R.color.normal_color));
            contentType = 1;
        }
        initData(1, contentType, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SKIP_TO_SEND_DYNAMIC) {
            //重新刷新当前界面
        }
    }
}
