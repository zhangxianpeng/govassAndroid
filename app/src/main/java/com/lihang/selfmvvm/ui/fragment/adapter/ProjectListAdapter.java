/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: ProjectListAdapter
 * Author: zhang
 * Date: 2020/7/4 13:34
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.ui.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: ProjectListAdapter
 * @Description: 首页消息展示
 * @Author: zhang
 * @Date: 2020/7/4 13:34
 */
public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<MsgMeResVo> stringList;
    private static final String TAG = "ProjectListAdapter";

    private ProjectListAdapter.OnItemClickListener mOnItemClickListener;

    //两次点击按钮的最小间隔，目前为1000
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime;

    public ProjectListAdapter(Context context, List<MsgMeResVo> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_project_list_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        MsgMeResVo msgMeResVo = stringList.get(position);
        holder.title.setText(msgMeResVo.getTitle());
        holder.containerRl.setTag(position);
        holder.containerRl.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return stringList == null ? 0 : stringList.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            long curClickTime = System.currentTimeMillis();
            if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                lastClickTime = curClickTime;
                mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(ProjectListAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout containerRl;
        private TextView title;


        public MyViewHolder(View itemView) {
            super(itemView);
            containerRl = itemView.findViewById(R.id.rl_project_list_item);
            title = itemView.findViewById(R.id.tv_title);
        }
    }
}