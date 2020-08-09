package com.lihang.selfmvvm.ui.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.bean.ChildModel;
import com.lihang.selfmvvm.bean.GroupModel;
import com.lihang.selfmvvm.databinding.FragmentMsgBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.GroupDetailsResVo;
import com.lihang.selfmvvm.vo.res.GroupResVo;
import com.lihang.selfmvvm.vo.res.MemberDetailResVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录
 */
public class MsgFragment extends BaseFragment<MsgFragmentViewModel, FragmentMsgBinding> implements PopupWindow.OnDismissListener {

    //最外面一层 分组名
    private List<GroupModel> groupArray;

    //最外面一层 分组下面的详情
    private List<List<ChildModel>> childArray;

    //自定义的适配器
    private ExpandableAdapter expandableAdapter;

    //是否使用默认的指示器 默认true 使用者可以在这里通过改变这个值观察默认指示器和自定义指示器的区别
    private boolean use_default_indicator = false;

    private PopupWindow mailistPop;

    /**
     * 默认传入的参数
     * 政府用户
     */
    private int defaultType = 0;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initView();
        initData(defaultType);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        if (use_default_indicator) {
            //不做处理就是默认
        } else {
            binding.exListView.setGroupIndicator(null);
        }

        //这里是通过改变默认的setGroupIndicator方式实现自定义指示器 但是效果不好 图标会被拉伸的很难看 不信你可以自己试试
//        expandableListView.setGroupIndicator(this.getResources().getDrawable(R.drawable.shape_expendable_listview));

        groupArray = new ArrayList<>();
        childArray = new ArrayList<>();

        //创建适配器
        expandableAdapter = new ExpandableAdapter(getContext(), groupArray, R.layout.exlistview_group_item, childArray, R.layout.exlistview_child_item);
        binding.exListView.setAdapter(expandableAdapter);

        binding.exListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
                //如果分组被打开 直接关闭
                if (binding.exListView.isGroupExpanded(groupPosition)) {
                    binding.exListView.collapseGroup(groupPosition);
                } else {
//                    //显示对话框
//                    showDialog("加载Child数据");
//                    //模拟加载数据 1000后通知handler新增一条数据
//                    new Timer().schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            Message message = handler.obtainMessage();
//                            message.what = 1;
//                            message.arg1 = groupPosition;
//                            handler.sendMessage(message);
//                        }
//                    }, 1000);

                    if (groupPosition == 0) {  //获取全部
                        mViewModel.getAllGovernment().observe(getActivity(), res -> {
                            res.handler(new OnCallback<List<MemberDetailResVo>>() {
                                @Override
                                public void onSuccess(List<MemberDetailResVo> data) {

                                }
                            });
                        });
                    } else {  //根据id

                    }
                }
                //返回false表示系统自己处理展开和关闭事件 返回true表示调用者自己处理展开和关闭事件
                return true;
            }
        });
    }

    private void showMenuDialog(View view, String groupName) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.mailistpop, null);
        initPopView(contentView, groupName);
        int height = (int) getResources().getDimension(R.dimen.dp_263);
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
    private void initData(int type) {
        getGroupList(type);
    }

    /**
     * 获取分组数据
     *
     * @param type
     */
    private void getGroupList(int type) {
        mViewModel.getGroupList(type).observe(getActivity(), res -> {
            res.handler(new OnCallback<GroupResVo>() {
                @Override
                public void onSuccess(GroupResVo data) {
                    groupArray = trasnsfer(inisertDefaultItem(data.getList()));
                    for (int i = 0; i < groupArray.size(); i++) {
                        List<ChildModel> tempArray = new ArrayList<>();
                        childArray.add(tempArray);
                    }
                    expandableAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    private List<GroupModel> trasnsfer(List<GroupDetailsResVo> groupDetailsResVos) {
        List<GroupModel> groupDataBeanList = new ArrayList<>();
        for (int i = 0; i < groupDetailsResVos.size(); i++) {
            GroupDetailsResVo groupDetailsResVo = groupDetailsResVos.get(i);
            GroupModel groupDataBean = new GroupModel(groupDetailsResVo.getName(), String.valueOf(groupDetailsResVo.getId()));
            groupDataBeanList.add(groupDataBean);
        }
        return groupDataBeanList;
    }

    /**
     * 数组首位插入“全部联系人”
     *
     * @param groupNameList
     */
    private List<GroupDetailsResVo> inisertDefaultItem(List<GroupDetailsResVo> groupNameList) {
        GroupDetailsResVo groupDetailsResVo = new GroupDetailsResVo();
        groupDetailsResVo.setId(0);
        groupDetailsResVo.setName("全部联系人");
        groupNameList.add(0, groupDetailsResVo);
        return groupNameList;
    }


    private void getAllUser(int type) {
        switch (type) {
            case 0:
                mViewModel.getAllGovernment().observe(this, res -> {

                });
                break;
            case 1:
                mViewModel.getAllEnterprise().observe(this, res -> {

                });
                break;
        }
    }

    private void getUserFromGroupId(int id) {
        mViewModel.getGovernmentFromId(id).observe(this, res -> {
            res.handler(new OnCallback<List<MemberDetailResVo>>() {
                @Override
                public void onSuccess(List<MemberDetailResVo> data) {
                    //TODO 把数据加到child
                }
            });
        });
    }

    @Override
    protected void setListener() {
        binding.tvCompanyUser.setOnClickListener(this::onClick);
        binding.tvGovermentUser.setOnClickListener(this::onClick);
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
            case R.id.tv_company_user:
                binding.tvCompanyUser.setTextSize((float) 18.0);
                binding.tvGovermentUser.setTextSize((float) 14.0);
                binding.allTitle.setText(getString(R.string.company_user));
                //选择企业后传参
                defaultType = 1;
                initData(defaultType);
                break;
            case R.id.tv_goverment_user:
                binding.tvCompanyUser.setTextSize((float) 14.0);
                binding.tvGovermentUser.setTextSize((float) 18.0);
                binding.allTitle.setText(getString(R.string.goverment_user));
                defaultType = 0;
                initData(defaultType);
                break;
            default:
                break;
        }
    }

    class ExpandableAdapter extends BaseExpandableListAdapter {
        //视图加载器
        private LayoutInflater mInflater;
        private Context mContext;
        private int mExpandedGroupLayout;
        private int mChildLayout;
        private List<GroupModel> mGroupArray;
        private List<List<ChildModel>> mChildArray;

        /**
         * 构造函数
         *
         * @param context
         * @param groupData
         * @param expandedGroupLayout 分组视图布局
         * @param childData
         * @param childLayout         详情视图布局
         */
        public ExpandableAdapter(Context context, List<GroupModel> groupData, int expandedGroupLayout,
                                 List<List<ChildModel>> childData, int childLayout) {
            mContext = context;
            mExpandedGroupLayout = expandedGroupLayout;
            mChildLayout = childLayout;
            mGroupArray = groupData;
            mChildArray = childData;
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public ExpandableAdapter(int msgFragment) {
        }

        public Object getChild(int groupPosition, int childPosition) {
            return childArray.get(groupPosition).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            // 取得显示给定分组给定子位置的数据用的视图。
            View v;
            if (convertView == null) {
                v = newChildView(parent);
            } else {
                v = convertView;
            }
            bindChildView(v, mChildArray.get(groupPosition).get(childPosition));
            return v;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            // 取得指定分组的子元素数。
            return mChildArray.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            // 取得与给定分组关联的数据。
            return mGroupArray.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            // 取得分组数
            return mGroupArray.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            // 取得指定分组的ID。该组ID必须在组中是唯一的。组合的ID （参见getCombinedGroupId(long)）
            // 必须不同于其他所有ID（分组及子项目的ID）。
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            // 取得用于显示给定分组的视图。 这个方法仅返回分组的视图对象， 要想获取子元素的视图对象，
            // 就需要调用 getChildView(int, int, boolean, View, ViewGroup)。
            View v;
            if (convertView == null) {
                v = newGroupView(parent);
            } else {
                v = convertView;
            }
            bindGroupView(v, mGroupArray.get(groupPosition), isExpanded);
            return v;
        }

        /**
         * 绑定组数据
         *
         * @param view
         * @param data
         * @param isExpanded
         */
        private void bindGroupView(View view, GroupModel data, boolean isExpanded) {
            // 绑定组视图的数据 当然这些都是模拟的
            TextView tv_title = (TextView) view.findViewById(R.id.tv_group_name);
            TextView tv_online = (TextView) view.findViewById(R.id.tv_msg);
            tv_title.setText(data.getTitle());
            tv_online.setText(data.getOnline());
            if (!use_default_indicator) {
                ImageView iv_tip = (ImageView) view.findViewById(R.id.iv_left);
                if (isExpanded) {
                    iv_tip.setImageResource(R.mipmap.down);
                } else {
                    iv_tip.setImageResource(R.mipmap.right);
                }
            }
        }

        /**
         * 绑定子数据
         *
         * @param view
         * @param data
         */
        private void bindChildView(View view, ChildModel data) {
            // 绑定组视图的数据 当然这些都是模拟的
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
//            TextView tv_sig = ( TextView ) view.findViewById(R.id.tv_sig);
            tv_name.setText(data.getName());
//            tv_sig.setText(data.getSig());
        }

        /**
         * 创建新的组视图
         *
         * @param parent
         * @return
         */
        public View newGroupView(ViewGroup parent) {
            return mInflater.inflate(mExpandedGroupLayout, parent, false);
        }

        /**
         * 创建新的子视图
         *
         * @param parent
         * @return
         */
        public View newChildView(ViewGroup parent) {
            return mInflater.inflate(mChildLayout, parent, false);
        }

        public boolean hasStableIds() {
            // 是否指定分组视图及其子视图的ID对应的后台数据改变也会保持该ID。
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // 指定位置的子视图是否可选择。
            return true;
        }
    }

}