package com.lihang.selfmvvm.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.adapter.MyAdapter;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.databinding.FragmentDynamicBinding;
import com.lihang.selfmvvm.ui.senddynamic.SendDynamicActivity;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.model.Friend;
import com.lihang.selfmvvm.vo.model.FriendGridItemVo;
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
    private List<Friend> friendDynamicList = new ArrayList<>();

    private BaseAdapter mAdapter = null;
    private ArrayList<FriendGridItemVo> mData = new ArrayList<>();

    private static final int SKIP_TO_SEND_DYNAMIC = 1000;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initAdapter();
        initData();
    }

    private void initAdapter() {
        binding.rvDynamic.setLayoutManager(new LinearLayoutManager(getContext()));
        commonAdapter = new CommonAdapter<Friend>(getContext(), R.layout.list_item_friend, friendDynamicList) {

            @Override
            protected void convert(ViewHolder holder, Friend friend, int position) {
                holder.setImageResource(R.id.iv_head, friend.getHeadIv());
                holder.setText(R.id.tv_nick_name, friend.getName());
                holder.setText(R.id.tv_send_time, friend.getCreateTime());
                holder.setText(R.id.tv_content, friend.getContent());
                GridView gridView = holder.getView(R.id.image_gridView);
                setGridViewData(gridView, friend.getImageList());
                holder.setOnClickListener(R.id.btn_like, view -> {
                    switchLikeStatus();
                });
            }
        };
        binding.rvDynamic.setAdapter(commonAdapter);
    }

    private void setGridViewData(GridView gridView, List<Integer> imageList) {
        mData.clear();
        for (int i = 0; i < imageList.size(); i++) {
            FriendGridItemVo friendGridItemVo = new FriendGridItemVo();
            int resId = imageList.get(i);
            friendGridItemVo.setImageRes(resId);
            mData.add(friendGridItemVo);
        }
        mAdapter = new MyAdapter<FriendGridItemVo>(mData, R.layout.grid_item_friend) {
            @Override
            public void bindView(ViewHolder holder, FriendGridItemVo obj) {
                holder.setImageResource(R.id.img_icon, obj.getImageRes());
            }
        };
        gridView.setAdapter(mAdapter);
    }

    //切换点赞状态
    private void switchLikeStatus() {
    }

    private void initData() {
        List<Integer> imagelist = new ArrayList<>();
        imagelist.add(R.mipmap.default_img);
        imagelist.add(R.mipmap.default_tx_img);
        imagelist.add(R.mipmap.default_img);
        imagelist.add(R.mipmap.default_tx_img);

        Friend test1 = new Friend();
        test1.setHeadIv(R.mipmap.default_tx_img);
        test1.setName("zhangsan");
        test1.setCreateTime("2020-10-17 11:34");
        test1.setContent("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        test1.setImageList(imagelist);
        friendDynamicList.add(test1);

        Friend test2 = new Friend();
        test2.setHeadIv(R.mipmap.default_tx_img);
        test2.setName("lisi");
        test2.setCreateTime("2020-10-17 11:34");
        test2.setContent("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        test2.setImageList(imagelist);
        friendDynamicList.add(test2);

        Friend test3 = new Friend();
        test3.setHeadIv(R.mipmap.default_tx_img);
        test3.setName("wwangwu");
        test3.setCreateTime("2020-10-17 11:34");
        test3.setContent("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        test3.setImageList(imagelist);
        friendDynamicList.add(test3);

        if (commonAdapter != null) commonAdapter.notifyDataSetChanged();
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
        Intent intent = new Intent(getContext(),SendDynamicActivity.class);
        startActivityForResult(intent,SKIP_TO_SEND_DYNAMIC);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.tv_dynamic:
                break;
            case R.id.tv_business:
                break;
            case R.id.btn_send:

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == SKIP_TO_SEND_DYNAMIC) {
            //重新刷新当前界面
        }
    }
}
