package com.lihang.selfmvvm.ui.newmsg;

import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.bean.NewMsgBean;
import com.lihang.selfmvvm.databinding.ActivityNewMsgBinding;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

public class NewMsgActivity extends BaseActivity<NewMsgViewModel, ActivityNewMsgBinding> {
    private static final String TAG = NewMsgActivity.class.getSimpleName();

    private List<NewMsgBean> msgBeanList = new ArrayList<>();


    @Override
    protected int getContentViewId() {
        return R.layout.activity_new_msg;
    }

    @Override
    protected void processLogic() {
        initData();
    }

    private void initData() {
        //测试代码
        NewMsgBean bean1 = new NewMsgBean();
        bean1.setStatus(1);
        bean1.setTime("07.28 13:08");
        bean1.setDetailInfo("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        msgBeanList.add(bean1);

        NewMsgBean bean2 = new NewMsgBean();
        bean2.setStatus(0);
        bean2.setTime("07.29 15:08");
        bean2.setDetailInfo("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        msgBeanList.add(bean2);

        binding.rvNewMsg.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvNewMsg.setAdapter(new CommonAdapter<NewMsgBean>(getContext(), R.layout.new_msg_list_item, msgBeanList) {

            @Override
            protected void convert(ViewHolder holder, NewMsgBean projectBean, int position) {
                if (msgBeanList.get(position).getStatus() == 0) {
                    holder.setBackgroundRes(R.id.tv_declare_status, R.drawable.circle_shape_fail);
                    holder.setText(R.id.tv_declare_result, "审核失败通知");
                } else {
                    holder.setBackgroundRes(R.id.tv_declare_status, R.drawable.circle_shape_success);
                    holder.setText(R.id.tv_declare_result, "审核成功通知");
                }
                holder.setText(R.id.tv_declare_time, msgBeanList.get(position).getTime());
                holder.setText(R.id.tv_declare_msg, msgBeanList.get(position).getDetailInfo());

//                holder.setOnClickListener(R.id.rl_container, (view -> {
//                    ActivityUtils.startActivity(getContext(), DocumentDetailActivity.class);
//                }));
            }
        });
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }
}