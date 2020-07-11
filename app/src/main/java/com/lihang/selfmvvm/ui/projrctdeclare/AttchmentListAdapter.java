/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: AttchmentListAdapter
 * Author: zhang
 * Date: 2020/7/12 0:15
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.ui.projrctdeclare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lihang.selfmvvm.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: AttchmentListAdapter
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/12 0:15
 */
public class AttchmentListAdapter extends RecyclerView.Adapter<AttchmentListAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<String> attachmentPathList;

    private AttchmentListAdapter.OnItemClickListener mOnItemClickListener;

    public AttchmentListAdapter(Context context, List<String> attachmentPathList) {
        this.context = context;
        this.attachmentPathList = attachmentPathList;
    }

    @Override
    public AttchmentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attachment_list_item, parent, false);
        AttchmentListAdapter.MyViewHolder viewHolder = new AttchmentListAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AttchmentListAdapter.MyViewHolder holder, final int position) {
        String filePathName = attachmentPathList.get(position);
        holder.filePathName.setText(filePathName);
        holder.deleteTextView.setTag(position);
        holder.deleteTextView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return attachmentPathList == null ? 0 : attachmentPathList.size();
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

    public void setOnItemClickListener(AttchmentListAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView filePathName;
        private TextView deleteTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            filePathName = itemView.findViewById(R.id.tv_file_path);
            deleteTextView = itemView.findViewById(R.id.tv_delete);
        }
    }
}
