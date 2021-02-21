package com.lihang.selfmvvm.ui.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.vo.model.Friend;
import com.lihang.selfmvvm.vo.model.FriendGridItemVo;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.lihang.selfmvvm.base.BaseConstant.DEFAULT_FILE_SERVER;
import static com.lihang.selfmvvm.common.SystemConst.DEFAULT_SERVER;

/**
 * created by zhangxianpeng on 20201017
 * 朋友圈列表适配器
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private Context mContext;
    private List<FriendGridItemVo> mFriendList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView headCirceleImageView;
        public ViewHolder(View view) {
            super(view);
            headCirceleImageView = view.findViewById(R.id.img_icon);
        }
    }

    public FriendAdapter(List<FriendGridItemVo> friendList) {
        mFriendList = friendList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FriendGridItemVo friend = mFriendList.get(position);
        Glide.with(mContext).load(DEFAULT_SERVER + DEFAULT_FILE_SERVER + friend.getImageRes()).placeholder(R.mipmap.default_tx_img)
                .error(R.mipmap.default_tx_img).into(holder.headCirceleImageView);
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }
}
