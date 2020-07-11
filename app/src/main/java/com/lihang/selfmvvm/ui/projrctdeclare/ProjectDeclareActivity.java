package com.lihang.selfmvvm.ui.projrctdeclare;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityProjectDeclareBinding;
import com.lihang.selfmvvm.ui.fragment.adapter.HomeMenuAdapter;
import com.lihang.selfmvvm.utils.FileUtils;
import com.lihang.selfmvvm.utils.LogUtils;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 项目申报页面
 */
public class ProjectDeclareActivity extends BaseActivity<ProjectDeclareViewModel, ActivityProjectDeclareBinding> {

    private ArrayList<String> attachmentPathList = new ArrayList<>();
    private AttchmentListAdapter attchmentListAdapter;

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
        mViewModel.setEditTextHintWithSize(binding.etCompanyName, getContext().getResources().getString(R.string.hint_company_name), 14);
        mViewModel.setEditTextHintWithSize(binding.etProjectInfo, getContext().getResources().getString(R.string.hint_project_info), 14);
        mViewModel.setEditTextHintWithSize(binding.etProjectMoney, getContext().getResources().getString(R.string.hint_project_money), 14);
        mViewModel.setEditTextHintWithSize(binding.etAddress, getContext().getResources().getString(R.string.hint_address), 14);
        mViewModel.setEditTextHintWithSize(binding.etContractPerson, getContext().getResources().getString(R.string.hint_contract_person), 14);
        mViewModel.setEditTextHintWithSize(binding.etContractPhone, getContext().getResources().getString(R.string.hint_phone), 14);
    }

    private void initAdapter() {
        attchmentListAdapter = new AttchmentListAdapter(getContext(), attachmentPathList);
        binding.rvAttachment.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAttachment.setAdapter(attchmentListAdapter);
        attchmentListAdapter.setOnItemClickListener((view, position) -> LogUtils.d("zhang delete", "menuClick===" + position));
    }

    @Override
    protected void setListener() {
        binding.rlAttachment.setOnClickListener(this::onClick);
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_attachment:
                pickFile();
                break;
            case R.id.iv_title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void pickFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);//打开多个文件
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择文件"), 1);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
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
                    attachmentPathList.add(imgpath);
                    attchmentListAdapter.notifyDataSetChanged();


                    //do something with the image (save it to some directory or whatever you need to do with it here)
                    currentItem = currentItem + 1;
                }
            } else if (data.getData() != null) { // 单选
                String imagePath = FileUtils.getPath(this, data.getData());
                Log.i("zhangxianpeng===", "Single image path ---- " + imagePath);
                attachmentPathList.add(imagePath);
                attchmentListAdapter.notifyDataSetChanged();
            }
        }
    }
}