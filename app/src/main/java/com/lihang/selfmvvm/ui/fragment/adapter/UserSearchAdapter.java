/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: ProjectAdapter
 * Author: zhang
 * Date: 2020/7/9 21:08
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.ui.fragment.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.vo.res.MemberDetailResVo;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: UserSearchAdapter 用户搜索
 * @Description: 用户搜索适配器
 * @Author: zhang
 * @Date: 2021/2/21 21:08
 */
public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<MemberDetailResVo> projectList;
    private static final String TAG = "UserSearchAdapter";

    private UserSearchAdapter.OnItemClickListener mOnItemClickListener;

    public UserSearchAdapter(Context context, ArrayList<MemberDetailResVo> projectList) {
        this.context = context;
        this.projectList = projectList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search_user, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        MemberDetailResVo memberDetailResVo = projectList.get(position);
        String realName = memberDetailResVo.getRealname();
        String userName = memberDetailResVo.getUsername();
        String companyName = memberDetailResVo.getEnterpriseName();
        String result = TextUtils.isEmpty(companyName) ? (TextUtils.isEmpty(realName) ? userName : realName) : realName + "-" + companyName;
        holder.user.setText(result);
        holder.user.setTag(position);
        holder.user.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return projectList == null ? 0 : projectList.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(UserSearchAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView user;

        public MyViewHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.tv_user_name);
        }
    }
}