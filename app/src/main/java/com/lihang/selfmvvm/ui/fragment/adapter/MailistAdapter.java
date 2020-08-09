package com.lihang.selfmvvm.ui.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.vo.res.GroupDetailsResVo;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MailistAdapter extends RecyclerView.Adapter<MailistAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private static final String TAG = "MailistAdapter";

    private MailistAdapter.OnItemClickListener mOnItemClickListener;

    //两次点击按钮的最小间隔，目前为1000
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime;

    private List<GroupDetailsResVo> groupDetailsResVoList = new ArrayList<>();

    public MailistAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<GroupDetailsResVo> groupDetailsResVoList) {
        this.groupDetailsResVoList = groupDetailsResVoList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exlistview_group_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        GroupDetailsResVo groupDetailsResVo = groupDetailsResVoList.get(position);
        holder.groupName.setText(groupDetailsResVo.getName());
        holder.msg.setTag(position);
        holder.msg.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return groupDetailsResVoList == null ? 0 : groupDetailsResVoList.size();
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

    public void setOnItemClickListener(MailistAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView groupName;
        private TextView msg;


        public MyViewHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.tv_group_name);
            msg = itemView.findViewById(R.id.tv_msg);
        }
    }
}