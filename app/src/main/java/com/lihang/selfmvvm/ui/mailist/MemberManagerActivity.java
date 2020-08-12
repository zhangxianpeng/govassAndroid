package com.lihang.selfmvvm.ui.mailist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityMemberManagerBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.LogUtils;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.RemoveUserReqVo;
import com.lihang.selfmvvm.vo.res.MemberDetailResVo;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

public class MemberManagerActivity extends BaseActivity<MemberManagerViewModel, ActivityMemberManagerBinding> {
    /**
     * 分组id
     */
    private int groupId = 0;
    /**
     * 类型
     */
    private int type = 0;
    /**
     * 分组名
     */
    private String groupName = "";
    /**
     * 界面标志  区分 新增和移除
     */
    private String flag = "";
    private MemberManagerAdapter memberManagerAdapter;
    private List<MemberDetailResVo> memberDetailResVoList = new ArrayList<>();
    private static final String TAG = "MemberManagerActivity";

    private static final String REMOVE_MEMBER_FROM_CURRENT_GROUP = "removeMember";
    private static final String ADD_MEMBER_TO_GROUP = "addMember";
    private static final int REMOVE_REQUEST_CODE = 101;
    private static final int ADD_REQUEST_CODE = 102;
    private static final int REMOVE_RESPONSE_CODE = 103;
    private static final int ADD_RESPONSE_CODE = 104;

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
        String groupIdStr = getIntent().getStringExtra("groupId");
        try {
            groupId = Integer.parseInt(groupIdStr);
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
        type = getIntent().getIntExtra("type", 0);
        groupName = getIntent().getStringExtra("groupName");
        flag = getIntent().getStringExtra("flag");
        initAdapter();
        updateTitle();
        updateData(flag);
    }

    private void updateTitle() {
        if (!TextUtils.isEmpty(groupName)) binding.tvGroupName.setText(groupName);
    }

    private void updateData(String flag) {
        if (flag.equals(REMOVE_MEMBER_FROM_CURRENT_GROUP)) {
            switch (type) {
                case 0: //政府
                    mViewModel.getGovernmentFromId(groupId).observe(this, res -> {
                        res.handler(new OnCallback<List<MemberDetailResVo>>() {
                            @Override
                            public void onSuccess(List<MemberDetailResVo> data) {
                                memberDetailResVoList.clear();
                                memberDetailResVoList.addAll(data);
                                memberManagerAdapter.notifyDataSetChanged();
                            }
                        });
                    });
                    break;
                case 1: //企业
                    mViewModel.getEnterpriseFromId(groupId).observe(this, res -> {
                        res.handler(new OnCallback<List<MemberDetailResVo>>() {
                            @Override
                            public void onSuccess(List<MemberDetailResVo> data) {
                                memberDetailResVoList.clear();
                                memberDetailResVoList.addAll(data);
                                memberManagerAdapter.notifyDataSetChanged();
                            }
                        });
                    });
                    break;
                default:
                    break;
            }
        } else if (flag.equals(ADD_MEMBER_TO_GROUP)) {
            switch (type) {
                case 0: //政府
                    mViewModel.getGovernmentFromId(groupId).observe(this, res -> {
                        res.handler(new OnCallback<List<MemberDetailResVo>>() {
                            @Override
                            public void onSuccess(List<MemberDetailResVo> data) {
                                memberDetailResVoList.clear();
                                memberDetailResVoList.addAll(data);
                                memberManagerAdapter.notifyDataSetChanged();
                            }
                        });
                    });
                    break;
                case 1: //企业
                    mViewModel.getEnterpriseFromId(groupId).observe(this, res -> {
                        res.handler(new OnCallback<List<MemberDetailResVo>>() {
                            @Override
                            public void onSuccess(List<MemberDetailResVo> data) {
                                memberDetailResVoList.clear();
                                memberDetailResVoList.addAll(data);
                                memberManagerAdapter.notifyDataSetChanged();
                            }
                        });
                    });
                    break;
                default:
                    break;
            }
        }
    }

    private void initAdapter() {
        memberManagerAdapter = new MemberManagerAdapter(getContext(), memberDetailResVoList);
        binding.rvGroupUser.setLayoutManager(new LinearLayoutManager(this));
        binding.rvGroupUser.setAdapter(memberManagerAdapter);
        memberManagerAdapter.setOnItemClickListener((view, position) -> LogUtils.d(TAG, "project===" + position));
        memberManagerAdapter.setOnItemChenedListener(new MemberManagerAdapter.OnItemCheckedListener() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void onItemChecked(View view, int position) {
                MemberDetailResVo memberDetailResVo = memberDetailResVoList.get(position);
                selectedUserList.add(memberDetailResVo.getUserId());
                binding.tvDelete.setText(getString(R.string.have_selected, selectedUserList.size()));
            }

            @SuppressLint("StringFormatMatches")
            @Override
            public void onItemNoChecked(View view, int position) {
                MemberDetailResVo memberDetailResVo = memberDetailResVoList.get(position);
                selectedUserList.remove(getEqualId(memberDetailResVo.getUserId(), selectedUserList));
                binding.tvDelete.setText(getString(R.string.have_selected, selectedUserList.size()));
            }
        });
    }

    private int getEqualId(int userId, List<Integer> selectedUserList) {
        int returnId = 0;
        for (int i = 0; i < selectedUserList.size(); i++) {
            if (userId == selectedUserList.get(i)) {
                returnId = i;
            }
        }
        return returnId;
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
            case R.id.tv_move:
                if (selectedUserList.isEmpty()) {
                    ToastUtils.showToast(getString(R.string.select_user));
                } else {

                    if (!TextUtils.isEmpty(flag)) {
                        switch (flag) {
                            case REMOVE_MEMBER_FROM_CURRENT_GROUP:
                                RemoveUserReqVo removeUserReqVo = new RemoveUserReqVo();
                                removeUserReqVo.setGroupId(groupId);
                                removeUserReqVo.setUserIdList(selectedUserList);
                                mViewModel.removeUser(removeUserReqVo).observe(this, res -> {
                                    res.handler(new OnCallback<String>() {
                                        @Override
                                        public void onSuccess(String data) {
                                            Intent intent = new Intent();
                                            setResult(REMOVE_RESPONSE_CODE, intent);
                                            finish();
                                        }
                                    });
                                });
                                break;
                            case ADD_MEMBER_TO_GROUP:
                                RemoveUserReqVo moveUserReqVo = new RemoveUserReqVo();
                                moveUserReqVo.setGroupId(groupId);
                                moveUserReqVo.setUserIdList(selectedUserList);
                                mViewModel.removeUser(moveUserReqVo).observe(this, res -> {
                                    res.handler(new OnCallback<String>() {
                                        @Override
                                        public void onSuccess(String data) {
                                            Intent intent = new Intent();
                                            setResult(REMOVE_RESPONSE_CODE, intent);
                                            finish();
                                        }
                                    });
                                });
                                break;
                            default:
                                break;
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
