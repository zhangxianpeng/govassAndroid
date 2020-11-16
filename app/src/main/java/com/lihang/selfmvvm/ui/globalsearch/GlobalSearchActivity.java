package com.lihang.selfmvvm.ui.globalsearch;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.bean.GroupModel;
import com.lihang.selfmvvm.databinding.ActivityGlobalSearchBinding;
import com.lihang.selfmvvm.ui.officialdoc.OfficialDocDetailActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.PreferenceUtil;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.res.ListBaseResVo;
import com.lihang.selfmvvm.vo.res.SearchValueResVo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * created by zhangxianpeng on 2020/11/14
 * 全局搜索
 */
public class GlobalSearchActivity extends BaseActivity<GlobalSearchViewModel, ActivityGlobalSearchBinding> {
    private List<GroupModel> groupArray = new ArrayList<>();
    private List<List<SearchValueResVo>> childArray = new ArrayList<>();

    private LayoutInflater hisFlInflater;
    private List<String> mHistoryKeywords;

    private ExpandableAdapter expandableAdapter;

    private String[] classification = {"千企动态", "系统公告", "政策文件库"};

    @Override
    protected int getContentViewId() {
        return R.layout.activity_global_search;
    }

    @Override
    protected void processLogic() {
        initHistoryView();
        initSearchResultExpandLv();
    }

    private void initSearchResultExpandLv() {
        expandableAdapter = new ExpandableAdapter(this, groupArray, R.layout.exlistview_searchresult_group_item, childArray, R.layout.exlistview_searchresult_child_item);
        binding.searchResult.setAdapter(expandableAdapter);
        //取消指示器
        binding.searchResult.setGroupIndicator(null);
        //默认全部展开
        int groupCount = binding.searchResult.getCount();
        for (int i = 0; i < groupCount; i++) {
            binding.searchResult.expandGroup(i);
        }
        binding.searchResult.setOnChildClickListener((expandableListView, view, groupPosition, childPosition, l) -> {
            SearchValueResVo searchValueResVo = childArray.get(groupPosition).get(childPosition);
            Bundle bundle = new Bundle();
            bundle.putString("flag", "commonSearch");
            bundle.putSerializable("searchValueResVo", searchValueResVo);
            ActivityUtils.startActivityWithBundle(getContext(), OfficialDocDetailActivity.class, bundle);
            return true;
        });
    }

    private void initHistoryView() {
        mHistoryKeywords = new ArrayList<>();
        mHistoryKeywords = PreferenceUtil.getStrListValue(this, "searchHistory");
        if (mHistoryKeywords != null && mHistoryKeywords.size() > 0) {
            binding.llSearchHistory.setVisibility(View.VISIBLE);
            hisFlInflater = LayoutInflater.from(this);
            for (int i = 0; i < mHistoryKeywords.size(); i++) {
                TextView tv = (TextView) hisFlInflater.inflate(R.layout.search_label_tv, binding.rvSearchHistory, false);
                tv.setText(mHistoryKeywords.get(i));
                final String str = tv.getText().toString();
                tv.setOnClickListener(view -> {
                    hideKeyBoard();
                    binding.etSearch.setText(str);
                    search(str);
                });
                binding.rvSearchHistory.addView(tv);
            }
        }
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
        binding.tvSearch.setOnClickListener(this::onClick);
        binding.ivClearSearchHistory.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.tv_search:
                String searchValue = binding.etSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(searchValue)) {
                    search(searchValue);
                    saveSearchHistory(searchValue);
                }
                break;
            case R.id.iv_clear_searchHistory:
                PreferenceUtil.removeStrList(this, "searchHistory");
                binding.llSearchHistory.setVisibility(View.GONE);
                binding.searchResult.setVisibility(View.GONE);
                ToastUtils.showToast("搜索历史清除成功");
                break;
            default:
                break;
        }
    }

    /**
     * 保存搜索历史到本地
     *
     * @param searchValue
     */
    private void saveSearchHistory(String searchValue) {
        mHistoryKeywords.add(searchValue);
        //去除重复数据
        Set set = new HashSet();
        List newList = new ArrayList();
        for (String cd : mHistoryKeywords) {
            if (set.add(cd)) {
                newList.add(cd);
            }
        }
        PreferenceUtil.putStrListValue(this, "searchHistory", newList);
    }

    private void search(String searchValue) {
        mViewModel.getSearchValue(1, 100, searchValue).observe(this, res -> {
            res.handler(new OnCallback<ListBaseResVo<SearchValueResVo>>() {
                @Override
                public void onSuccess(ListBaseResVo<SearchValueResVo> data) {
                    groupArray.clear();
                    childArray.clear();
                    groupArray.addAll(operateGroupData(data.getList()));
                    childArray.add(data.getList());
                    runOnUiThread(() -> {
                        expandableAdapter.notifyDataSetChanged();
                        binding.llSearchHistory.setVisibility(View.GONE);
                        binding.searchResult.setVisibility(View.VISIBLE);
                    });
                }
            });
        });
    }

    /**
     * 处理原始数据,配置分组数据
     *
     * @param list
     * @return
     */
    private List<GroupModel> operateGroupData(List<SearchValueResVo> list) {
        int i = 0, j = 0, k = 0;
        boolean isExsit0 = false, isExsit1 = false, isExsit2 = false;
        List<GroupModel> listTemp = new ArrayList();
        for (SearchValueResVo valueResVo : list) {
            if (valueResVo.getType() == 0) {
                i++;
            } else if (valueResVo.getType() == 1) {
                j++;
            } else if (valueResVo.getType() == 2) {
                k++;
            }
        }

        for (SearchValueResVo valueResVo : list) {
            if (!isExsit0 && valueResVo.getType() == 0) {
                GroupModel groupModel = new GroupModel(classification[0], String.valueOf(i));
                isExsit0 = true;
                listTemp.add(groupModel);
            }
            if (!isExsit1 && valueResVo.getType() == 1) {
                GroupModel groupModel = new GroupModel(classification[1], String.valueOf(j));
                isExsit1 = true;
                listTemp.add(groupModel);
            }
            if (!isExsit2 && valueResVo.getType() == 2) {
                GroupModel groupModel = new GroupModel(classification[2], String.valueOf(k));
                isExsit2 = true;
                listTemp.add(groupModel);
            }
        }

        return listTemp;
    }

    class ExpandableAdapter extends BaseExpandableListAdapter {
        //视图加载器
        private LayoutInflater mInflater;
        private Context mContext;
        private int mExpandedGroupLayout;
        private int mChildLayout;
        private List<GroupModel> mGroupArray;
        private List<List<SearchValueResVo>> mChildArray;

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
                                 List<List<SearchValueResVo>> childData, int childLayout) {
            mContext = context;
            mExpandedGroupLayout = expandedGroupLayout;
            mChildLayout = childLayout;
            mGroupArray = groupData;
            mChildArray = childData;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public Object getChild(int groupPosition, int childPosition) {
            return childArray.get(groupPosition).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            // 取得显示给定分组给定子位置的数据用的视图。
            View v;
            if (convertView == null) {
                v = newChildView(parent);
            } else {
                v = convertView;
            }
            bindChildView(v, groupPosition, mChildArray.get(groupPosition).get(childPosition));
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
            TextView classificationTextView = view.findViewById(R.id.tv_group_name);
            TextView number = view.findViewById(R.id.check_more);
            classificationTextView.setText(data.getTitle());
            number.setText(getString(R.string.check_more, data.getOnline()));
        }

        /**
         * 绑定子数据
         *
         * @param view
         * @param data
         */
        private void bindChildView(View view, int groupPosition, SearchValueResVo data) {
            TextView equalContent = view.findViewById(R.id.tv_equal_content);
            TextView source = view.findViewById(R.id.tv_source);
            TextView createdTime = view.findViewById(R.id.tv_created_time);
            equalContent.setText(data.getContent());
            String sourceName = "";
            switch (data.getType()) {
                case 0:
                    sourceName = classification[0];
                    break;
                case 1:
                    sourceName = classification[1];
                    break;
                case 2:
                    sourceName = classification[2];
                    break;
            }
            source.setText("来源名称：" + sourceName);
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
