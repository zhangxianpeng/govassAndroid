package com.lihang.selfmvvm.ui.declaredetail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityDeclareDetailBinding;
import com.lihang.selfmvvm.vo.res.AttachmentResVo;
import com.lihang.selfmvvm.vo.res.ProjectResVo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 申报详情
 */
public class DeclareDetailActivity extends BaseActivity<DeclareDetailViewModel, ActivityDeclareDetailBinding> {
    private CommonAdapter attachmentAdapter;
    private List<AttachmentResVo> list = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_declare_detail;
    }

    @Override
    protected void processLogic() {
        initAdapter();
        int id = getIntent().getIntExtra("id", 0);
        getDetail(id);
    }

    private void initAdapter() {
        attachmentAdapter = new CommonAdapter<AttachmentResVo>(getContext(), R.layout.attachment_list_item, list) {

            @Override
            protected void convert(ViewHolder holder, AttachmentResVo attachmentResVo, int position) {
                holder.setText(R.id.tv_file_path, attachmentResVo.getName());
                holder.setVisible(R.id.tv_delete, false);
                holder.setOnClickListener(R.id.rl_container, view -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("attachmentResVo", attachmentResVo);
//                    ActivityUtils.startActivityWithBundle(getContext(), PlainMsgDetailActivity.class, bundle);
                });
            }
        };
        binding.rvAttachment.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAttachment.setAdapter(attachmentAdapter);
    }

    private void getDetail(int id) {
        mViewModel.getProjectDetail(id).observe(this, res -> {
            res.handler(new OnCallback<ProjectResVo>() {
                @Override
                public void onSuccess(ProjectResVo data) {
                    if (data.getStatus() == 1) {   //已通过
                        binding.btnSubmit.setVisibility(View.GONE);
                        binding.btnNotadopt.setClickable(false);
                        binding.btnAdopt.setClickable(false);
                    } else {
                        binding.btnSubmit.setVisibility(View.VISIBLE);
                        binding.btnNotadopt.setClickable(true);
                        binding.btnAdopt.setClickable(true);
                    }
                    binding.tvProjectName.setText(data.getName());
                    binding.tvDeclareStyle.setText(data.getType());
                    binding.tvCompanyName.setText(data.getEnterpriseName());
                    binding.tvProjectInfo.setText(data.getDescription());
                    binding.tvProjectMoney.setText(String.valueOf(data.getAmount()));
                    binding.tvCompanyAddress.setText(data.getAddress());
                    binding.tvUser.setText(data.getLinkman());
                    binding.tvUserNumber.setText(data.getContact());
                    binding.etAppoval.setText(data.getAuditOpinion());

                    list.clear();
                    list.addAll(data.getProjectAttachmentEntityList());
                    attachmentAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    @Override
    protected void setListener() {
        binding.ibtnTitleBarBack.setOnClickListener(this::onClick);
        binding.btnAdopt.setOnClickListener(this::onClick);
        binding.btnNotadopt.setOnClickListener(this::onClick);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_title_bar_back:
                finish();
                break;
            case R.id.btn_adopt:
                binding.btnNotadopt.setBackground(getContext().getDrawable(R.mipmap.ic_moren));
                binding.btnAdopt.setBackground(getContext().getDrawable(R.mipmap.ic_pass));
                binding.btnNotadopt.setTextColor(getContext().getColor(R.color.tab_normal));
                binding.btnAdopt.setTextColor(getContext().getColor(R.color.tab_selected));
                break;
            case R.id.btn_notadopt:
                binding.btnNotadopt.setBackground(getContext().getDrawable(R.mipmap.ic_fail));
                binding.btnAdopt.setBackground(getContext().getDrawable(R.mipmap.ic_moren));
                binding.btnNotadopt.setTextColor(getContext().getColor(R.color.tab_selected));
                binding.btnAdopt.setTextColor(getContext().getColor(R.color.tab_normal));
                break;
            default:
                break;
        }

    }
}