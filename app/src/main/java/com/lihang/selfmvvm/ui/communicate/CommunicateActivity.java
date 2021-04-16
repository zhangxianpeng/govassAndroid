package com.lihang.selfmvvm.ui.communicate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityCommunicateBinding;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.PlainMsgResVo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import sakura.bottommenulibrary.bottompopfragmentmenu.BottomMenuFragment;
import sakura.bottommenulibrary.bottompopfragmentmenu.MenuItem;

/**
 * created by zhangxianpeng
 * 聊天界面
 */
public class CommunicateActivity extends BaseActivity<CommunicateViewModel, ActivityCommunicateBinding> {
    private static final String TAG = CommunicateActivity.class.getSimpleName();
    /**
     * 消息列表
     */
    private List<PlainMsgResVo> projectList = new ArrayList<>();
    private CommonAdapter projectAdapter;

    private String realName = "";
    private String phone = "";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_communicate;
    }

    @Override
    protected void processLogic() {
        realName = getIntent().getStringExtra("realName");
        phone = getIntent().getStringExtra("phone");
        initView();
        initAdapter();
//        initData();
        if (!TextUtils.isEmpty(phone)) {
            initAddMsgGroupDialog(getWindow().getDecorView());
        }
    }

    private void initView() {
        if (!TextUtils.isEmpty(realName)) binding.tvUserNickName.setText(realName);
    }

    private void initAdapter() {
        projectAdapter = new CommonAdapter<PlainMsgResVo>(getContext(), R.layout.home_project_list_item, projectList) {

            @Override
            protected void convert(ViewHolder holder, PlainMsgResVo msgMeResVo, int position) {
                holder.setText(R.id.tv_title, msgMeResVo.getTitle());
                holder.setOnClickListener(R.id.rl_project_list_item, view -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", msgMeResVo.getId());
                    ActivityUtils.startActivityWithBundle(getContext(), PlainMsgDetailActivity.class, bundle);
                });
                holder.setOnClickListener(R.id.tv_delete, view -> deletePlainMsg(msgMeResVo.getId()));
            }
        };
        binding.rvMsg.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMsg.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.rvMsg.setAdapter(projectAdapter);
    }

    /**
     * 删除 普通消息
     *
     * @param id
     */
    private void deletePlainMsg(int id) {
        List<Integer> list = new ArrayList<>();
        list.add(id);
        mViewModel.deletePlainMsgList(list).observe(this, res -> {
            res.handler(new OnCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    initData();
                }
            });
        });
    }

    private void initData() {
        mViewModel.getPlainMsgList().observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<PlainMsgResVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<PlainMsgResVo> data) {
                    projectList.clear();
                    projectList.addAll(data.getList());
                    projectAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
    }

    private void initAddMsgGroupDialog(View rootView) {
        new BottomMenuFragment(this)
                .addMenuItems(new MenuItem("呼叫 " + phone))
                .setOnItemClickListener(new BottomMenuFragment.OnItemClickListener() {
                    @Override
                    public void onItemClick(TextView menu_item, int position) {
                        switch (position) {
                            case 0://拨号
                                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                                startActivity(dialIntent);
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }
}