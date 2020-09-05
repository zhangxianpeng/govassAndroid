package com.lihang.selfmvvm.ui.mailist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.vo.res.GroupDetailsResVo;
import com.lihang.selfmvvm.vo.res.MemberDetailResVo;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 成员管理页面适配器
 * author: zhangxianpeng
 * crateTime: 20200808
 */
public class MemberManagerAdapter extends RecyclerView.Adapter<MemberManagerAdapter.MyViewHolder> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Context context;
    private List<MemberDetailResVo> memberDetailResVoList;
    private static final String TAG = "MemberManagerAdapter";

    private MemberManagerAdapter.OnItemClickListener mOnItemClickListener;
    private MemberManagerAdapter.OnItemCheckedListener onItemCheckedListener;

    //两次点击按钮的最小间隔，目前为1000
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime;

    public MemberManagerAdapter(Context context, List<MemberDetailResVo> memberDetailResVoList) {
        this.context = context;
        this.memberDetailResVoList = memberDetailResVoList;
    }

    @Override
    public MemberManagerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.membermanage_list_item, parent, false);
        MemberManagerAdapter.MyViewHolder viewHolder = new MemberManagerAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MemberManagerAdapter.MyViewHolder holder, final int position) {
        MemberDetailResVo memberDetailResVo = memberDetailResVoList.get(position);
        Glide.with(context).load(memberDetailResVo.getHeadUrl()).placeholder(R.mipmap.default_tx_img)
                .error(R.mipmap.default_tx_img).into(holder.headIv);
        holder.nameTv.setText(memberDetailResVo.getRealname());

        holder.selectCb.setTag(position);
        holder.selectCb.setOnCheckedChangeListener(this);
    }

    @Override
    public int getItemCount() {
        return memberDetailResVoList == null ? 0 : memberDetailResVoList.size();
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


    public interface OnItemCheckedListener {
        void onItemChecked(View view, int position);

        void onItemNoChecked(View view, int position);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            if (onItemCheckedListener != null) {
                //注意这里使用getTag方法获取position
                onItemCheckedListener.onItemChecked(compoundButton, (Integer) compoundButton.getTag());
            }
        } else {
            if (onItemCheckedListener != null) {
                //注意这里使用getTag方法获取position
                onItemCheckedListener.onItemNoChecked(compoundButton, (Integer) compoundButton.getTag());
            }
        }
    }

    public void setOnItemChenedListener(MemberManagerAdapter.OnItemCheckedListener listener) {
        this.onItemCheckedListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(MemberManagerAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private CheckBox selectCb;
        private ImageView headIv;
        private TextView nameTv;


        public MyViewHolder(View itemView) {
            super(itemView);
            selectCb = itemView.findViewById(R.id.cb_select);
            headIv = itemView.findViewById(R.id.iv_head);
            nameTv = itemView.findViewById(R.id.tv_name);
        }
    }
}
