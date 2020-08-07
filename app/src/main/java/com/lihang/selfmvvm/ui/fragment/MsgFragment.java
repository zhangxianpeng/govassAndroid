package com.lihang.selfmvvm.ui.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.bean.ChildDataBean;
import com.lihang.selfmvvm.bean.GroupDataBean;
import com.lihang.selfmvvm.databinding.FragmentMsgBinding;
import com.lihang.selfmvvm.ui.communicate.CommunicateActivity;
import com.lihang.selfmvvm.ui.fragment.adapter.ExListViewAdapter;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录
 */
public class MsgFragment extends BaseFragment<MsgFragmentViewModel, FragmentMsgBinding> implements PopupWindow.OnDismissListener {

    private ExListViewAdapter adapter;
    /**
     * 分组头
     */
    private List<GroupDataBean> groupList = new ArrayList<>();
    /**
     * 子列表
     */
    private List<List<ChildDataBean>> childList = new ArrayList<>();
    ;
    /**
     * 头像列表 （测试写死）
     */
    private String[] url;

    private PopupWindow mailistPop;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initData();
        adapter = new ExListViewAdapter(getContext(), groupList, childList);
        binding.exListView.setAdapter(adapter);

        //默认所有group全部展开
        int groupCount = binding.exListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            binding.exListView.expandGroup(i);
        }

        adapter.setOnItemClickListener(((view, position) -> showMenuDialog(view, groupList.get(position).getName())));

        binding.exListView.setOnChildClickListener((expandableListView, view, groupPosition, childPosition, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("nickName", childList.get(groupPosition).get(childPosition).getNickName());
            ActivityUtils.startActivityWithBundle(getContext(), CommunicateActivity.class, bundle);
            return true;
        });
    }

    private void showMenuDialog(View view, String groupName) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.mailistpop, null);
        initPopView(contentView, groupName);
        int height = (int) getResources().getDimension(R.dimen.dp_150);
        mailistPop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, height, true);
        mailistPop.setAnimationStyle(R.style.ActionSheetDialogAnimation);
        mailistPop.setOutsideTouchable(true);
        backgroundAlpha(0.3f);
        mailistPop.setOnDismissListener(this);
        mailistPop.setBackgroundDrawable(new BitmapDrawable());
        mailistPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void initPopView(View view, String groupName) {
        TextView titleTv = view.findViewById(R.id.tv_title);
        TextView memManagerTv = view.findViewById(R.id.tv_member_management);
        TextView addMemberTv = view.findViewById(R.id.tv_add_member);
        TextView delGroupTv = view.findViewById(R.id.tv_delete_group);
        TextView cancelTv = view.findViewById(R.id.tv_cancel);
        titleTv.setText(groupName);
        titleTv.setOnClickListener(this::onClick);
        memManagerTv.setOnClickListener(this::onClick);
        addMemberTv.setOnClickListener(this::onClick);
        delGroupTv.setOnClickListener(this::onClick);
        cancelTv.setOnClickListener(this::onClick);
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        //0.0-1.0
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1.0f);
        if (mailistPop != null) mailistPop.dismiss();
    }

    /**
     * 加载数据
     */
    private void initData() {
        url = new String[]{
                "http://cdn.duitang.com/uploads/item/201506/07/20150607125903_vFWC5.png",
                "http://upload.qqbody.com/ns/20160915/202359954jalrg3mqoei.jpg",
                "http://tupian.qqjay.com/tou3/2016/0726/8529f425cf23fd5afaa376c166b58e29.jpg",
                "http://cdn.duitang.com/uploads/item/201607/13/20160713094718_Xe3Tc.png",
                "http://img3.imgtn.bdimg.com/it/u=1808104956,526590423&fm=11&gp=0.jpg",
                "http://tupian.qqjay.com/tou3/2016/0725/5d6272a4acd7e21b2391aff92f765018.jpg"
        };

        List<String> group = new ArrayList<>();
        group.add("王者一区");
        group.add("吃鸡二区");
        group.add("飞车三区");
        group.add("炫舞四区");

        for (int i = 0; i < group.size(); i++) {
            GroupDataBean gd = new GroupDataBean(group.get(i), (i + 2) + "/" + (2 * i + 2));
            groupList.add(gd);
        }

        for (int i = 0; i < group.size(); i++) {
            List<ChildDataBean> list = new ArrayList<>();
            for (int j = 0; j < 2 * i + 2; j++) {
                ChildDataBean cd = null;
                if (i == 0) {
                    cd = new ChildDataBean("null", "小张");
                    list.add(cd);
                    cd = new ChildDataBean("null", "小黄");
                    list.add(cd);
                    break;
                } else {
                    cd = new ChildDataBean(url[j % url.length], "张三" + j);
                    list.add(cd);
                }
            }
            childList.add(list);
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_member_management:
                break;
            case R.id.tv_add_member:
                break;
            case R.id.tv_delete_group:
                break;
            case R.id.tv_cancel:
                if (mailistPop != null) mailistPop.dismiss();
                break;
        }
    }

}