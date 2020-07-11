/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: ExListViewAdapter
 * Author: zhang
 * Date: 2020/7/11 17:53
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.ui.fragment.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.bean.ChildDataBean;
import com.lihang.selfmvvm.bean.GroupDataBean;

import java.util.List;

/**
 * @ClassName: ExListViewAdapter
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/11 17:53
 */
public class ExListViewAdapter implements ExpandableListAdapter, View.OnClickListener {

    private Context context;
    private List<GroupDataBean> groupDataList;
    private List<List<ChildDataBean>> childDataList;

    private ExListViewAdapter.OnItemClickListener mOnItemClickListener;

    public ExListViewAdapter(Context context, List<GroupDataBean> groupDataList, List<List<ChildDataBean>> childDataList) {
        this.context = context;
        this.groupDataList = groupDataList;
        this.childDataList = childDataList;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    //获取分组个数
    @Override
    public int getGroupCount() {
        return groupDataList == null ? 0 : groupDataList.size();
    }

    //获取i分组下子列表数量
    @Override
    public int getChildrenCount(int groupPosition) {
        return childDataList == null ? 0 : childDataList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupDataList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childDataList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.exlistview_group_item, null);
            holder = new GroupViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img_group);
            holder.img_msg = (ImageView) convertView.findViewById(R.id.img_msg);
            holder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        GroupDataBean groupData = groupDataList.get(groupPosition);
        //是否展开
        if (isExpanded) {
            holder.img.setImageResource(R.mipmap.address_ic_open);
        } else {
            holder.img.setImageResource(R.mipmap.address_ic_close);
        }
        holder.tv_group_name.setText(groupData.getName());
        holder.img_msg.setTag(groupPosition);
        holder.img_msg.setOnClickListener(this::onClick);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.exlistview_child_item, null);
            holder = new ChildViewHolder();
            holder.headImgView = (ImageView) convertView.findViewById(R.id.iv_head);
            holder.nickNameTextView = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        ChildDataBean childData = childDataList.get(groupPosition).get(childPosition);
        Glide.with(context).load(childData.getHeadUrl()).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(holder.headImgView);
        holder.nickNameTextView.setText(childData.getNickName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
        }
    }

    public void setOnItemClickListener(ExListViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class GroupViewHolder {
        ImageView img;
        TextView tv_group_name;
        ImageView img_msg;
    }

    class ChildViewHolder {
        ImageView headImgView;
        TextView nickNameTextView;
    }
}
