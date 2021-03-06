package com.lihang.selfmvvm.ui.declaredetail;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityDeclareDetailBinding;
import com.lihang.selfmvvm.ui.bigpicture.BigPictureActivity;
import com.lihang.selfmvvm.ui.filepreview.FilePreviewActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.CheckPermissionUtils;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.AuditReqVo;
import com.lihang.selfmvvm.vo.res.AttachmentResVo;
import com.lihang.selfmvvm.vo.res.ProjectResVo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * created by zhangxianpeng
 * 申报详情界面
 */
public class DeclareDetailActivity extends BaseActivity<DeclareDetailViewModel, ActivityDeclareDetailBinding> {
    private CommonAdapter attachmentAdapter;
    private List<AttachmentResVo> list = new ArrayList<>();

    /**
     * 是否点击通过
     */
    private boolean isClickPass = false;

    /**
     * 是否点击不通过
     */
    private boolean isClickNoPass = false;

    private int id;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_declare_detail;
    }

    @Override
    protected void processLogic() {
        initView();
        initAdapter();
        id = getIntent().getIntExtra("id", -1);
        getDetail(id);
    }

    private void initView() {
        binding.btnSubmit.setVisibility(CheckPermissionUtils.getInstance().isGovernment() ? View.VISIBLE : View.GONE);
        binding.btnAdopt.setEnabled(CheckPermissionUtils.getInstance().isGovernment());
        binding.btnNotadopt.setEnabled(CheckPermissionUtils.getInstance().isGovernment());
    }

    private void initAdapter() {
        attachmentAdapter = new CommonAdapter<AttachmentResVo>(getContext(), R.layout.attachment_list_item, list) {
            @Override
            protected void convert(ViewHolder holder, AttachmentResVo attachmentResVo, int position) {
                holder.setText(R.id.tv_file_path, attachmentResVo.getName());
                holder.setVisible(R.id.tv_delete, false);
                holder.setOnClickListener(R.id.rl_container, view -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("plainMsgAttachmentUrl", attachmentResVo.getUrl());
                    //图片文件跳转到图片预览界面
                    if (attachmentResVo.getName().endsWith("PNG") ||
                            attachmentResVo.getName().endsWith("JPG") ||
                            attachmentResVo.getName().endsWith("JEPG") ||
                            attachmentResVo.getName().endsWith("png") ||
                            attachmentResVo.getName().endsWith("jpg") ||
                            attachmentResVo.getName().endsWith("jepg")) {
                        bundle.putString("fileName", attachmentResVo.getName());
                        bundle.putString("imgUrl", attachmentResVo.getUrl());
                        ActivityUtils.startActivityWithBundle(getContext(), BigPictureActivity.class, bundle);
                    } else {
                        bundle.putString("fileUrl", attachmentResVo.getUrl());
                        bundle.putString("fileName", attachmentResVo.getName());
                        ActivityUtils.startActivityWithBundle(getContext(), FilePreviewActivity.class, bundle);
                    }
                });
            }
        };
        binding.rvAttachment.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAttachment.setAdapter(attachmentAdapter);
    }

    @SuppressLint("NewApi")
    private void getDetail(int id) {
        mViewModel.getProjectDetail(id).observe(this, res -> {
            res.handler(new OnCallback<ProjectResVo>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onSuccess(ProjectResVo data) {
                    if (data.getStatus() == 1) {   //已通过
                        binding.btnSubmit.setVisibility(View.GONE);
                        binding.btnNotadopt.setClickable(false);
                        binding.btnAdopt.setClickable(false);
                        binding.btnNotadopt.setBackground(getContext().getDrawable(R.mipmap.ic_moren));
                        binding.btnAdopt.setBackground(getContext().getDrawable(R.mipmap.ic_pass));
                        binding.btnNotadopt.setTextColor(getContext().getColor(R.color.tab_normal));
                        binding.btnAdopt.setTextColor(getContext().getColor(R.color.tab_selected));
                    } else if (data.getStatus() == 2) {  //不通过
                        binding.btnSubmit.setVisibility(View.GONE);
                        binding.btnNotadopt.setBackground(getContext().getDrawable(R.mipmap.ic_fail));
                        binding.btnAdopt.setBackground(getContext().getDrawable(R.mipmap.ic_moren));
                        binding.btnNotadopt.setTextColor(getContext().getColor(R.color.tab_selected));
                        binding.btnAdopt.setTextColor(getContext().getColor(R.color.tab_normal));
                    } else if (data.getStatus() == 0) { //未审核
                        binding.btnSubmit.setVisibility(CheckPermissionUtils.getInstance().isGovernment() ? View.VISIBLE : View.GONE);
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
        binding.btnSubmit.setOnClickListener(this::onClick);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.ibtn_title_bar_back:
                finish();
                break;
            case R.id.btn_adopt: //通过
                binding.btnNotadopt.setBackground(getContext().getDrawable(R.mipmap.ic_moren));
                binding.btnAdopt.setBackground(getContext().getDrawable(R.mipmap.ic_pass));
                binding.btnNotadopt.setTextColor(getContext().getColor(R.color.tab_normal));
                binding.btnAdopt.setTextColor(getContext().getColor(R.color.tab_selected));
                isClickPass = true;
                isClickNoPass = false;
                break;
            case R.id.btn_notadopt:  //不通过
                binding.btnNotadopt.setBackground(getContext().getDrawable(R.mipmap.ic_fail));
                binding.btnAdopt.setBackground(getContext().getDrawable(R.mipmap.ic_moren));
                binding.btnNotadopt.setTextColor(getContext().getColor(R.color.tab_selected));
                binding.btnAdopt.setTextColor(getContext().getColor(R.color.tab_normal));
                isClickPass = false;
                isClickNoPass = true;
                break;
            case R.id.btn_submit:
                AuditReqVo auditReqVo = new AuditReqVo();
                auditReqVo.setId(id);
                auditReqVo.setAuditOpinion(getStringByUI(binding.etAppoval));
                if (isClickPass) {
                    mViewModel.pass(auditReqVo).observe(DeclareDetailActivity.this, res -> {
                        res.handler(new OnCallback<String>() {
                            @Override
                            public void onSuccess(String data) {
                                ToastUtils.showToast("审批完成");
                                finish();
                            }
                        });
                    });
                }
                if (isClickNoPass) {
                    mViewModel.noPass(auditReqVo).observe(DeclareDetailActivity.this, res -> {
                        res.handler(new OnCallback<String>() {
                            @Override
                            public void onSuccess(String data) {
                                ToastUtils.showToast("审批完成");
                                finish();
                            }
                        });
                    });
                }
                break;
            default:
                break;
        }
    }
}