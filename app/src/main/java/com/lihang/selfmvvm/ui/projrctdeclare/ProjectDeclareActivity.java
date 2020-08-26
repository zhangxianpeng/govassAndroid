package com.lihang.selfmvvm.ui.projrctdeclare;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityProjectDeclareBinding;
import com.lihang.selfmvvm.utils.CommonUtils;
import com.lihang.selfmvvm.utils.FileUtils;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.AddProjectReqVo;
import com.lihang.selfmvvm.vo.res.AttachmentResVo;
import com.lihang.selfmvvm.vo.res.UploadAttachmentResVo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 项目申报页面
 */
public class ProjectDeclareActivity extends BaseActivity<ProjectDeclareViewModel, ActivityProjectDeclareBinding> {

    private List<AttachmentResVo> attachmentPathList = new ArrayList<>();
    private AttchmentListAdapter attchmentListAdapter;

    private static final String TAG = ProjectDeclareActivity.class.getSimpleName();

    /**
     * 新增项目申请请求VO
     */
    private AddProjectReqVo addProjectReqVo = new AddProjectReqVo();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_project_declare;
    }

    @Override
    protected void processLogic() {
        initView();
        initAdapter();
    }

    private void initView() {
        mViewModel.setEditTextHintWithSize(binding.etProjectName, getContext().getResources().getString(R.string.hint_project_name), 14);
        mViewModel.setEditTextHintWithSize(binding.etProjectType, getContext().getResources().getString(R.string.hint_project_type), 14);
        mViewModel.setEditTextHintWithSize(binding.etProjectInfo, getContext().getResources().getString(R.string.hint_project_info), 14);
        mViewModel.setEditTextHintWithSize(binding.etProjectMoney, getContext().getResources().getString(R.string.hint_project_money), 14);
        mViewModel.setEditTextHintWithSize(binding.etAddress, getContext().getResources().getString(R.string.hint_address), 14);
        mViewModel.setEditTextHintWithSize(binding.etContractPerson, getContext().getResources().getString(R.string.hint_contract_person), 14);
        mViewModel.setEditTextHintWithSize(binding.etContractPhone, getContext().getResources().getString(R.string.hint_phone), 14);
    }


    private void initAdapter() {
        attchmentListAdapter = new AttchmentListAdapter(getContext(), TAG, attachmentPathList);
        binding.rvAttachment.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAttachment.setAdapter(attchmentListAdapter);
        attchmentListAdapter.setOnItemClickListener((view, position) -> {
            attachmentPathList.remove(position);
            attchmentListAdapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void setListener() {
        binding.rlAttachment.setOnClickListener(this::onClick);
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
        binding.btnSubmit.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_attachment:
                CommonUtils.getInstance().selectFileFromLocal(this);
                break;
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.btn_submit:
                doDeclare();
                break;
            default:
                break;
        }
    }

    private void doDeclare() {
        if (TextUtils.isEmpty(getStringByUI(binding.etProjectName))) {
            ToastUtils.showToast(getContext().getString(R.string.hint_project_name));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etProjectType))) {
            ToastUtils.showToast(getContext().getString(R.string.hint_project_type));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etProjectInfo))) {
            ToastUtils.showToast(getContext().getString(R.string.hint_project_info));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etProjectMoney))) {
            ToastUtils.showToast(getContext().getString(R.string.hint_project_money));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etAddress))) {
            ToastUtils.showToast(getContext().getString(R.string.hint_address));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etContractPerson))) {
            ToastUtils.showToast(getContext().getString(R.string.hint_contract_person));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etContractPhone))) {
            ToastUtils.showToast(getContext().getString(R.string.hint_phone));
            return;
        }

        if (attachmentPathList.size() < 1) {
            ToastUtils.showToast(getContext().getString(R.string.add_attachment));
            return;
        }

        addProjectReqVo.setAddress(getStringByUI(binding.etAddress));
        try {
            int amount = Integer.parseInt(getStringByUI(binding.etProjectMoney));
            addProjectReqVo.setAmount(amount);
        } catch (NumberFormatException e) {

        }

        addProjectReqVo.setContact(getStringByUI(binding.etContractPhone));
        addProjectReqVo.setDescription(getStringByUI(binding.etProjectInfo));
        addProjectReqVo.setLinkman(getStringByUI(binding.etContractPerson));
        addProjectReqVo.setName(getStringByUI(binding.etProjectName));
        addProjectReqVo.setType(getStringByUI(binding.etProjectType));

        if (addProjectReqVo.getAttachmentList().size() > 0) {
            mViewModel.saveProject(addProjectReqVo).observe(this, res -> {
                res.handler(new OnCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        ToastUtils.showToast("项目申报添加成功");
                    }
                });
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //Get the Uri of the selected file
            if (data.getClipData() != null) {   //多选
                int count = data.getClipData().getItemCount();
                Log.i("zhangxianpeng===", "url count ：  " + count);
                int currentItem = 0;

                while (currentItem < count) {
                    Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
                    String imgpath = FileUtils.getPath(this, imageUri);
                    Log.i("zhangxianpeng===", "url " + imgpath);
                    AttachmentResVo attachmentResVo = new AttachmentResVo();
                    attachmentResVo.setName(getFileName(imgpath));
                    attachmentResVo.setUrl(imgpath);
                    currentItem = currentItem + 1;
                    attachmentPathList.add(attachmentResVo);
                    attchmentListAdapter.notifyDataSetChanged();
                    uploadFile(attachmentPathList);
                }
            } else if (data.getData() != null) { // 单选
                String imagePath = FileUtils.getPath(this, data.getData());
                Log.i("zhangxianpeng===", "Single image path ---- " + imagePath);
                AttachmentResVo attachmentResVo = new AttachmentResVo();
                attachmentResVo.setName(getFileName(imagePath));
                attachmentResVo.setUrl(imagePath);
                attachmentPathList.add(attachmentResVo);
                attchmentListAdapter.notifyDataSetChanged();
                uploadFile(attachmentPathList);
            }
        }
    }

    /**
     * 从路径获取文件名
     *
     * @param imgpath
     * @return
     */
    private String getFileName(String imgpath) {
        int start = imgpath.lastIndexOf("/");
        if (start != -1) {
            return imgpath.substring(start + 1);
        } else {
            return "";
        }
    }

    /**
     * 上传文件到后台服务器
     *
     * @param attachmentPathList
     */
    private void uploadFile(List<AttachmentResVo> attachmentPathList) {
        List<MultipartBody.Part> parts
                = new ArrayList<>();
        for (File file : trasfer2FileList(attachmentPathList)) {
            RequestBody body = MultipartBody.create(MultipartBody.FORM, file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), body);
            parts.add(part);
        }

        mViewModel.uploadMultyFile(parts).observe(this, res -> {
            res.handler(new OnCallback<List<UploadAttachmentResVo>>() {
                @Override
                public void onSuccess(List<UploadAttachmentResVo> data) {
                    List<AttachmentResVo> newList = new ArrayList<>();
                    for (UploadAttachmentResVo uploadAttachmentResVo : data) {
                        AttachmentResVo attachmentResVo = new AttachmentResVo();
                        attachmentResVo.setName(uploadAttachmentResVo.getFileName());
                        attachmentResVo.setUrl(uploadAttachmentResVo.getFilePath());
                        newList.add(attachmentResVo);
                    }
                    addProjectReqVo.setAttachmentList(newList);
                }
            });
        });
    }

    private List<File> trasfer2FileList(List<AttachmentResVo> attachmentPathList) {
        List<File> newList = new ArrayList<>();
        for (AttachmentResVo attachmentResVo : attachmentPathList) {
            File file = new File(attachmentResVo.getUrl());
            newList.add(file);
        }
        return newList;
    }
}