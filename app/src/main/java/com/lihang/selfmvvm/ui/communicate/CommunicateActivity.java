package com.lihang.selfmvvm.ui.communicate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityCommunicateBinding;
import com.lihang.selfmvvm.ui.projrctdeclare.AttchmentListAdapter;
import com.lihang.selfmvvm.utils.CommonUtils;
import com.lihang.selfmvvm.utils.FileUtils;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * im 聊天界面
 */
public class CommunicateActivity extends BaseActivity<CommunicateViewModel, ActivityCommunicateBinding> implements PopupWindow.OnDismissListener {
    private static final String TAG = CommunicateActivity.class.getSimpleName();
    /**
     * 消息列表
     */
    private ArrayList<String> projectList = new ArrayList<>();


    /**
     * 附件列表
     */
    private List<String> attachmentList = new ArrayList<>();
    private AttchmentListAdapter attchmentListAdapter;

    private String nickName = "";
    private PopupWindow addMsgPop;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_communicate;
    }

    @Override
    protected void processLogic() {
        nickName = getIntent().getExtras().getString("nickName");
        binding.tvUserNickName.setText(nickName);
        getProjectList();
    }

    private void getProjectList() {
        //项目列表
        projectList.add("关于《政企消息》的审核结果");
        projectList.add("关于《活动中心》的通知日期时间");
        projectList.add("你的项目申请进度已有更新请查看");
        projectList.add("关于《政企消息》的审核结果");
        projectList.add("关于《活动中心》的通知日期时间");
        binding.rvMsg.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMsg.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.rvMsg.setAdapter(new CommonAdapter<String>(getContext(), R.layout.home_project_list_item, projectList) {

            @Override
            protected void convert(ViewHolder holder, String msg, int position) {
                holder.setText(R.id.tv_title, projectList.get(position));
                holder.setOnClickListener(R.id.rl_project_list_item, (view -> {
//                    ActivityUtils.startActivity(getContext(), DeclareDetailActivity.class);
                }));
            }
        });
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
        binding.tvAddMsg.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.tv_add_msg: //显示 Dialog
                showAddMsgDialog(view);
                break;
            default:
                break;
        }
    }

    private void showAddMsgDialog(View rootView) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        initPopView(contentView);
        int height = (int) getResources().getDimension(R.dimen.dp_600);
        addMsgPop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, height, true);
        addMsgPop.setAnimationStyle(R.style.ActionSheetDialogAnimation);
        addMsgPop.setOutsideTouchable(true);
        backgroundAlpha(0.3f);
        addMsgPop.setOnDismissListener(this);
        addMsgPop.setBackgroundDrawable(new BitmapDrawable());
        addMsgPop.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    private void initPopView(View view) {
        ImageView closeIv = view.findViewById(R.id.iv_close);
        EditText titleEt = view.findViewById(R.id.et_title);
        EditText textEt = view.findViewById(R.id.et_text);
        RecyclerView attachmentRv = view.findViewById(R.id.rv_attachment);
        RelativeLayout addAttachmentRl = view.findViewById(R.id.rl_add_attachment);
        Button sendBtn = view.findViewById(R.id.btn_send);
        setAdapter(attachmentRv);
//        attachmentRv.setLayoutManager(new LinearLayoutManager(getContext()));
//        attachmentRv.setAdapter(new CommonAdapter<String>(getContext(), R.layout.addmsg_attachment_list_item, attachmentList) {
//
//            @Override
//            protected void convert(ViewHolder holder, String msg, int position) {
//                holder.setText(R.id.tv_file_path, attachmentList.get(position));
//                holder.setOnClickListener(R.id.iv_delete, (view -> {
//                    //刪除position
//                }));
//            }
//        });


        closeIv.setOnClickListener((view1 -> onDismiss()));
        sendBtn.setOnClickListener(view1 -> ToastUtils.showToast("hahah"));
        addAttachmentRl.setOnClickListener(view1 -> CommonUtils.getInstance().selectFileFromLocal(this));
        textEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        titleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setAdapter(RecyclerView attachmentRv) {
        attchmentListAdapter = new AttchmentListAdapter(getContext(), TAG, attachmentList);
        attachmentRv.setLayoutManager(new LinearLayoutManager(this));
        attachmentRv.setAdapter(attchmentListAdapter);
        attchmentListAdapter.setOnItemClickListener((view, position) -> {
            attachmentList.remove(position);
            attchmentListAdapter.notifyDataSetChanged();
        });
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //0.0-1.0
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1.0f);
        if (addMsgPop != null) addMsgPop.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //Get the Uri of the selected file
            if (data.getClipData() != null) {   //多选
                int count = data.getClipData().getItemCount();
                Log.i("zhangxianpeng===", "url count ：  " + count);
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    String imgpath = FileUtils.getPath(this, imageUri);
                    attachmentList.add(imgpath);
                }
                attchmentListAdapter.notifyDataSetChanged();
            } else if (data.getData() != null) { // 单选
                String imagePath = FileUtils.getPath(this, data.getData());
                Log.i("zhangxianpeng===", "Single image path ---- " + imagePath);
                attachmentList.add(imagePath);
                attchmentListAdapter.notifyDataSetChanged();
            }
        }
    }
}