package com.lihang.selfmvvm.ui.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.vo.model.Friend;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * created by zhangxianpeng on 20201017
 * 朋友圈列表适配器
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private Context mContext;
    private List<Friend> mFriendList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        CircleImageView headCirceleImageView;
        TextView nameTv;
        TextView timeTv;
        GridView imageGridView;
        Button likeBtn;
        TextView likeCountTv;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            headCirceleImageView = view.findViewById(R.id.iv_head);
            nameTv = view.findViewById(R.id.tv_nick_name);
            timeTv = view.findViewById(R.id.tv_send_time);
            imageGridView = view.findViewById(R.id.image_gridView);
            likeBtn = view.findViewById(R.id.btn_like);
            likeCountTv = view.findViewById(R.id.tv_like_count);
        }
    }

    public FriendAdapter(List<Friend> friendList) {
        mFriendList = friendList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Friend friend = mFriendList.get(position);

    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }
}
