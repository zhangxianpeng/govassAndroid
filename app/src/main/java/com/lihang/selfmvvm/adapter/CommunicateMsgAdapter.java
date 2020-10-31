package com.lihang.selfmvvm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.vo.model.CommunicateMsgVo;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * created by zhangxianpeng
 */
public class CommunicateMsgAdapter extends RecyclerView.Adapter<CommunicateMsgAdapter.ViewHolder> {
    private List<CommunicateMsgVo> mMsgList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        ImageView leftImageView;
        ImageView rightImageView;
        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(View view) {
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            leftImageView = view.findViewById(R.id.left_head);
            rightImageView = view.findViewById(R.id.right_head);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);
        }
    }

    public CommunicateMsgAdapter(Context context, List<CommunicateMsgVo> msgList) {
        this.mMsgList = msgList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_communicate, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CommunicateMsgVo msg = mMsgList.get(i);
        if (!msg.isSend()) {
            if (msg.getAnswer() != null) {
                viewHolder.leftLayout.setVisibility(View.VISIBLE);
                viewHolder.rightLayout.setVisibility(View.GONE);
                viewHolder.leftMsg.setText(msg.getAnswer());
            }
        } else {
            if (msg.getContent() != null) {
                viewHolder.leftLayout.setVisibility(View.GONE);
                viewHolder.rightLayout.setVisibility(View.VISIBLE);
                viewHolder.rightMsg.setText(msg.getContent());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}
