package com.lihang.selfmvvm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.vo.model.CommunicateMsgvO;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * created by zhangxianpeng
 */
public class CommunicateMsgAdapter extends RecyclerView.Adapter<CommunicateMsgAdapter.ViewHolder> {
    private List<CommunicateMsgvO> mMsgList;
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
            leftImageView = view.findViewById(R.id.left_msg);
            rightImageView = view.findViewById(R.id.left_msg);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);
        }
    }

    public CommunicateMsgAdapter(Context context, List<CommunicateMsgvO> msgList) {
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
        CommunicateMsgvO msg = mMsgList.get(i);
        if (msg.getType() == CommunicateMsgvO.TYPE_RECEIVED) {
            //如果是收到的信息，则显示在左边的消息布局，将右边的消息布局隐藏
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(msg.getContent());
            Glide.with(context).load(msg.getHeadImg()).placeholder(R.mipmap.default_tx_img)
                    .error(R.mipmap.default_tx_img).into(viewHolder.leftImageView);
            viewHolder.leftMsg.setText(msg.getContent());
        } else if (msg.getType() == CommunicateMsgvO.TYPE_SENT) {
            //如果是发出的信息，则显示在右边的消息布局，将左边的消息布局隐藏
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            Glide.with(context).load(msg.getHeadImg()).placeholder(R.mipmap.default_tx_img)
                    .error(R.mipmap.default_tx_img).into(viewHolder.rightImageView);
            viewHolder.rightMsg.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}
