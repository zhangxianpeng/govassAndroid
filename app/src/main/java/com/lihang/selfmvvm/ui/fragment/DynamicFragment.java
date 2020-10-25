package com.lihang.selfmvvm.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.adapter.MyAdapter;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.bean.basebean.ResponModel;
import com.lihang.selfmvvm.databinding.FragmentDynamicBinding;
import com.lihang.selfmvvm.ui.senddynamic.SendDynamicActivity;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
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
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * created by zhangxianpeng on 20201017
 * 千企动态
 */
public class DynamicFragment extends BaseFragment<DynamicFragmentViewModel, FragmentDynamicBinding> {

    private CommonAdapter commonAdapter;
    private List<DynamicVo> friendDynamicList = new ArrayList<>();

    private BaseAdapter mAdapter = null;
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
                holder.setText(R.id.tv_nick_name, String.valueOf(friend.getCreateUserId()));
                holder.setText(R.id.tv_send_time, friend.getCreateTime());
                holder.setText(R.id.tv_content, friend.getContent());
                holder.setText(R.id.tv_like_count, getString(R.string.like_count, friend.getLikeCount()));
                GridView gridView = holder.getView(R.id.image_gridView);
                if (friend.getAttachmentList() != null && !friend.getAttachmentList().isEmpty()) {
                    setGridViewData(gridView, friend.getAttachmentList());
                }
                holder.setOnClickListener(R.id.btn_like, view -> {
                    Button like = holder.itemView.findViewById(R.id.btn_like);
                    //有问题
                    if (!isLike) {
                        isLike = true;
                        like.setBackgroundResource(R.mipmap.ic_lianxi_love);
                        holder.setText(R.id.tv_like_count, getString(R.string.like_count, friend.getLikeCount() + 1));
                    } else {
                        isLike = false;
                        like.setBackgroundResource(R.mipmap.ic_like);
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
                Glide.with(getContext()).load(obj.getImageRes()).placeholder(R.mipmap.default_tx_img)
                        .error(R.mipmap.default_tx_img).into(imageView);
            }
        };
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

    //切换点赞状态
    private void switchLikeStatus(int position) {

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
        binding.btnSend.setOnLongClickListener(view -> {
            gotoSendDynamicPage();
            return false;
        });
    }

    private void gotoSendDynamicPage() {
        Intent intent = new Intent(getContext(), SendDynamicActivity.class);
        startActivityForResult(intent, SKIP_TO_SEND_DYNAMIC);
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
//                ActivityUtils.startActivity(getContext(), SendDynamicActivity.class);
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
