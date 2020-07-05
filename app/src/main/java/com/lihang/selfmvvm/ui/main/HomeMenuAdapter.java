/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: HomeMenuAdapter
 * Author: zhang
 * Date: 2020/7/4 13:33
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.bean.HomeMenuBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: HomeMenuAdapter
 * @Description: 主界面菜单 adapter 横向 recycleView
 * @Author: zhang
 * @Date: 2020/7/4 13:33
 */
public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<HomeMenuBean> homeMenuBeanList;
    private static final String TAG = "HomeMenuAdapter";

    private HomeMenuAdapter.OnItemClickListener mOnItemClickListener;

    public HomeMenuAdapter(Context context, List<HomeMenuBean> homeMenuBeanList) {
        this.context = context;
        this.homeMenuBeanList = homeMenuBeanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_menu_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        HomeMenuBean bean = homeMenuBeanList.get(position);
        String name = bean.getTitle();
        Integer imagePath = bean.getImageUrl();
        holder.title.setText(name);
        holder.image.setImageResource(imagePath);
        holder.containerRl.setTag(position);
        holder.containerRl.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return homeMenuBeanList == null ? 0 : homeMenuBeanList.size();
    }

    @Override
    public void onClick(View view) {
      if(mOnItemClickListener != null) {
          mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
      }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(HomeMenuAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout containerRl;
        private TextView title;
        private ImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_menu);
            containerRl =itemView.findViewById(R.id.rl_container);
            title = itemView.findViewById(R.id.tv_muenu);
        }
    }
}


