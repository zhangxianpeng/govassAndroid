package com.lihang.selfmvvm.ui.msgdetail;

import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityMsgDetailBinding;
import com.lihang.selfmvvm.ui.bigpicture.BigPictureActivity;
import com.lihang.selfmvvm.ui.filepreview.FilePreviewActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.vo.res.AttachmentResVo;
import com.lihang.selfmvvm.vo.res.MsgMeResVo;
import com.lihang.selfmvvm.vo.res.OfficialDocResVo;
import com.lihang.selfmvvm.vo.res.PlainMsgAttachmentListResVo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * created by zhangxianpeng
 * 消息详情界面
 */
public class MsgDetailActivity extends BaseActivity<MsgDetailActivityViewModel, ActivityMsgDetailBinding> {

    private List<PlainMsgAttachmentListResVo> attachmentList = new ArrayList<>();
    private CommonAdapter attachmentAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_msg_detail;
    }

    @Override
    protected void processLogic() {
        initAdapter();

        String uiflag = getIntent().getStringExtra("uiflag");
        if (uiflag.equals("newmsg")) {
            MsgMeResVo msgMeResVo = (MsgMeResVo) getIntent().getSerializableExtra("msgMeResVo");
            int readFlag = msgMeResVo.getReadFlag();
            int id = msgMeResVo.getId();
            String title = msgMeResVo.getTitle();
            binding.tvTitle.setText(title);
            getPlainMsgAttachmentList(msgMeResVo.getPrimaryId());
            getPlainMsdDetail(id);
            transferReadFlag(readFlag, id);
        } else if (uiflag.equals("policy")) {
            OfficialDocResVo pilocyResVo = (OfficialDocResVo) getIntent().getSerializableExtra("noticeResVo");
            binding.tvTitle.setText(pilocyResVo.getTitle());
            binding.tvTitle.setText(pilocyResVo.getContent());
            List<AttachmentResVo> data = pilocyResVo.getAttachmentList();
            if (data != null && data.size() > 0) {
                binding.cardView.setVisibility(View.VISIBLE);
                attachmentList.clear();
                attachmentList.addAll(transfer(data));
                if (attachmentAdapter != null) attachmentAdapter.notifyDataSetChanged();
            }
        }
    }

    private List<PlainMsgAttachmentListResVo> transfer(List<AttachmentResVo> data) {
        List<PlainMsgAttachmentListResVo> resultList = new ArrayList<>();
        for (AttachmentResVo attachmentResVo : data) {
            PlainMsgAttachmentListResVo plainMsgAttachmentListResVo = new PlainMsgAttachmentListResVo();
            plainMsgAttachmentListResVo.setName(attachmentResVo.getName());
            plainMsgAttachmentListResVo.setUrl(attachmentResVo.getUrl());
            resultList.add(plainMsgAttachmentListResVo);
        }
        return resultList;
    }

    private void initAdapter() {
        attachmentAdapter = new CommonAdapter<PlainMsgAttachmentListResVo>(getContext(), R.layout.rv_attachment_item, attachmentList) {
            @Override
            protected void convert(ViewHolder holder, PlainMsgAttachmentListResVo plainMsgAttachmentListResVo, int position) {
                holder.setText(R.id.tv_project_title, plainMsgAttachmentListResVo.getName());
                holder.setOnClickListener(R.id.rl_container, (view -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("plainMsgAttachmentUrl", plainMsgAttachmentListResVo.getUrl());
                    //图片文件跳转到图片预览界面
                    if (plainMsgAttachmentListResVo.getName().endsWith("PNG") ||
                            plainMsgAttachmentListResVo.getName().endsWith("JPG") ||
                            plainMsgAttachmentListResVo.getName().endsWith("JEPG")) {
                        bundle.putString("imgUrl", plainMsgAttachmentListResVo.getUrl());
                        ActivityUtils.startActivityWithBundle(getContext(), BigPictureActivity.class, bundle);
                    } else {
                        bundle.putString("fileUrl", plainMsgAttachmentListResVo.getUrl());
                        bundle.putString("fileName", plainMsgAttachmentListResVo.getName());
                        ActivityUtils.startActivityWithBundle(getContext(), FilePreviewActivity.class, bundle);
                    }
                }));
            }
        };
        binding.rvPlainMsgAttachment.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPlainMsgAttachment.setAdapter(attachmentAdapter);
    }

    private void getPlainMsgAttachmentList(int primaryId) {
        mViewModel.getPlainMsgAttachmentList(primaryId).observe(this, res -> {
            res.handler(new OnCallback<List<PlainMsgAttachmentListResVo>>() {
                @Override
                public void onSuccess(List<PlainMsgAttachmentListResVo> data) {
                    if (data != null && data.size() > 0) {
                        binding.cardView.setVisibility(View.VISIBLE);
                        attachmentList.clear();
                        attachmentList.addAll(data);
                        if (attachmentAdapter != null) attachmentAdapter.notifyDataSetChanged();
                    }
                }
            });
        });
    }

    private void getPlainMsdDetail(int id) {
        mViewModel.getMsgDetail(id).observe(this, res -> {
            res.handler(new OnCallback<MsgMeResVo>() {
                @Override
                public void onSuccess(MsgMeResVo data) {
                    binding.tvContent.setText(data.getContent());
                }
            });
        });
    }

    private void transferReadFlag(int readFlag, int id) {
        if (readFlag != 1) {
            mViewModel.transferReadFlag(id).observe(this, res -> {
                res.handler(new OnCallback<MsgMeResVo>() {
                    @Override
                    public void onSuccess(MsgMeResVo data) {
                    }
                });
            });
        }
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }
}
