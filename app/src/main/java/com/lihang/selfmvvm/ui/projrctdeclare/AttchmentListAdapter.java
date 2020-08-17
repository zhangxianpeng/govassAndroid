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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseConstant;
import com.lihang.selfmvvm.vo.res.AttachmentResVo;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: AttchmentListAdapter
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/12 0:15
 */
public class AttchmentListAdapter extends RecyclerView.Adapter<AttchmentListAdapter.MyViewHolder> implements View.OnClickListener {
    private String TAG = "AttchmentListAdapter";
    private Context context;
    private List<AttachmentResVo> attachmentPathList;

    private AttchmentListAdapter.OnItemClickListener mOnItemClickListener;

    // 界面
    private String flag;

    public AttchmentListAdapter(Context context, String flag, List<AttachmentResVo> attachmentPathList) {
        this.context = context;
        this.flag = flag;
        this.attachmentPathList = attachmentPathList;
    }

    @Override
    public AttchmentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, this.flag);
        View view = null;
        if (this.flag.equals(BaseConstant.UI_PROJECT_DECLARE_ACTIVITY)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attachment_list_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addmsg_attachment_list_item, parent, false);
        }
        AttchmentListAdapter.MyViewHolder viewHolder = new AttchmentListAdapter.MyViewHolder(view, flag);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AttchmentListAdapter.MyViewHolder holder, final int position) {
        AttachmentResVo attachmentResVo = attachmentPathList.get(position);
        holder.filePathName.setText(attachmentResVo.getName());
        if (holder.deleteTextView != null) {
            holder.deleteTextView.setTag(position);
            holder.deleteTextView.setOnClickListener(this);
        }
        if (holder.deleteImageView != null) {
            holder.deleteImageView.setTag(position);
            holder.deleteImageView.setOnClickListener(this);
        }
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
        private ImageView deleteImageView;


        public MyViewHolder(View itemView, String flag) {
            super(itemView);
            if (flag.equals(BaseConstant.UI_PROJECT_DECLARE_ACTIVITY)) {
                filePathName = itemView.findViewById(R.id.tv_file_path);
                deleteTextView = itemView.findViewById(R.id.tv_delete);
            } else {
                filePathName = itemView.findViewById(R.id.tv_file_path);
                deleteImageView = itemView.findViewById(R.id.iv_delete);
            }
        }
    }
}
