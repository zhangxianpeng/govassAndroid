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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.bean.ProjectBean;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: ProjectAdapter
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/9 21:08
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<ProjectBean> projectList;
    private static final String TAG = "ProjectListAdapter";
    private String flag;

    private ProjectListAdapter.OnItemClickListener mOnItemClickListener;

    public ProjectAdapter(Context context, ArrayList<ProjectBean> projectList, String flag) {
        this.context = context;
        this.projectList = projectList;
        this.flag = flag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, this.flag);
        View view = null;
        if (this.flag == "myDeclare") {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_clare_list_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_list_item, parent, false);
        }
        MyViewHolder viewHolder = new MyViewHolder(view, flag);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ProjectBean bean = projectList.get(position);
        holder.projectTitle.setText(bean.getProjectTitle());
        holder.projectTime.setText(bean.getProjectTime());
        holder.rightImage.setTag(position);
        holder.rightImage.setOnClickListener(this);
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

    public void setOnItemClickListener(ProjectListAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView rightImage;
        private TextView projectTitle;
        private TextView projectTime;

        public MyViewHolder(View itemView, String flag) {
            super(itemView);
            if (flag == "myDeclare") {
                rightImage = itemView.findViewById(R.id.iv_right);
                projectTitle = itemView.findViewById(R.id.tv_title);
                projectTime = itemView.findViewById(R.id.tv_time);
            } else {
                rightImage = itemView.findViewById(R.id.iv_right);
                projectTitle = itemView.findViewById(R.id.tv_project_title);
                projectTime = itemView.findViewById(R.id.tv_project_time);
            }
        }
    }
}