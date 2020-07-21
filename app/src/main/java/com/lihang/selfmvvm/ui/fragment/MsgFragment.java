package com.lihang.selfmvvm.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.bean.ChildDataBean;
import com.lihang.selfmvvm.bean.GroupDataBean;
import com.lihang.selfmvvm.databinding.FragmentMsgBinding;
import com.lihang.selfmvvm.ui.communicate.CommunicateActivity;
import com.lihang.selfmvvm.ui.fragment.adapter.ExListViewAdapter;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录
 */
public class MsgFragment extends BaseFragment<MsgFragmentViewModel, FragmentMsgBinding> {

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

        adapter.setOnItemClickListener(((view, position) -> ToastUtils.showToast("群发消息===" + position)));

//        binding.exListView.setOnGroupClickListener((parent, view, groupPosition, id) -> {
//            if (view.getId() == R.id.img_msg) {
//                ToastUtils.showToast("群发消息");
//                return false;
//            }
//
//            if (parent.isGroupExpanded(groupPosition)) {
//                parent.collapseGroup(groupPosition);
//            } else {
//                //第二个参数false表示展开时是否触发默认滚动动画
//                parent.expandGroup(groupPosition, false);
//            }
//            return true;
//        });


        binding.exListView.setOnChildClickListener((expandableListView, view, groupPosition, childPosition, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("nickName", childList.get(groupPosition).get(childPosition).getNickName());
            ActivityUtils.startActivityWithBundle(getContext(), CommunicateActivity.class,bundle);
            return true;
        });
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

    }
}