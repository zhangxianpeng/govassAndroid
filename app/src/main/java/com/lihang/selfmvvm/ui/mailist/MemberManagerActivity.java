package com.lihang.selfmvvm.ui.mailist;

import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityMemberManagerBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.LogUtils;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.res.GroupDetailsResVo;
import com.lihang.selfmvvm.vo.res.MemberDetailResVo;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

public class MemberManagerActivity extends BaseActivity<MemberManagerViewModel, ActivityMemberManagerBinding> {
    /**
     * 分组id
     */
    private int groupId = 0;
    private MemberManagerAdapter memberManagerAdapter;
    private List<MemberDetailResVo> memberDetailResVoList = new ArrayList<>();
    private static final String TAG = "MemberManagerActivity";

    /**
     * 选中的用户数组
     */
    private List<Integer> selectedUserList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_member_manager;
    }

    @Override
    protected void processLogic() {
        groupId = getIntent().getExtras().getInt("groupId");
        getGroupAllUser(groupId);
    }

    private void getGroupAllUser(int groupId) {
        mViewModel.getGroupAllUser(groupId).observe(this, res -> {
            res.handler(new OnCallback<GroupDetailsResVo>() {
                @Override
                public void onSuccess(GroupDetailsResVo data) {
                    memberDetailResVoList = data.getUserList();
                    initAdapter();
                }
            });
        });
    }

    private void initAdapter() {
        memberManagerAdapter = new MemberManagerAdapter(getContext(), memberDetailResVoList);
        binding.rvGroupUser.setLayoutManager(new LinearLayoutManager(this));
        binding.rvGroupUser.setAdapter(memberManagerAdapter);
        memberManagerAdapter.setOnItemClickListener((view, position) -> LogUtils.d(TAG, "project===" + position));
    }

    @Override
    protected void setListener() {
        binding.tvCancel.setOnClickListener(this::onClick);
        binding.tvDelete.setOnClickListener(this::onClick);
        binding.tvMove.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_delete:
                if (selectedUserList.isEmpty()) {
                    ToastUtils.showToast(getString(R.string.select_user));
                } else {
                    //TODO 从分组中删除选择用户
                }
                break;
            case R.id.tv_move:
                if (selectedUserList.isEmpty()) {
                    ToastUtils.showToast(getString(R.string.select_user));
                } else {
                    //TODO 从分组中删除选择用户
                }
                break;
            default:
                break;
        }
    }
}
