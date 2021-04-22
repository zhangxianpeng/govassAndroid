package com.lihang.selfmvvm.ui.mailist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
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
import com.lihang.selfmvvm.Variable;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.bean.ChildModel;
import com.lihang.selfmvvm.databinding.ActivityMemberManagerBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.FileUtils;
import com.lihang.selfmvvm.utils.LogUtils;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.PlainMsgReqVo;
import com.lihang.selfmvvm.vo.req.RemoveUserReqVo;
import com.lihang.selfmvvm.vo.res.AttachmentResVo;
import com.lihang.selfmvvm.vo.res.GroupDetailsResVo;
import com.lihang.selfmvvm.vo.res.GroupResVo;
import com.lihang.selfmvvm.vo.res.MemberDetailResVo;
import com.lihang.selfmvvm.vo.res.UploadAttachmentResVo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static java.util.stream.Collectors.toList;

public class MemberManagerActivity extends BaseActivity<MemberManagerViewModel, ActivityMemberManagerBinding> implements PopupWindow.OnDismissListener {
    private static final String TAG = "MemberManagerActivity";
    private int groupId = 0;
    private int type = 0;
    private String groupName = "";
    //界面标志  区分 新增和移除
    private String flag = "";

    //分组
    private List<GroupDetailsResVo> groupResVoList = new ArrayList<>();
    private GroupManagerAdapter groupManagerAdapter;

    //成员
    private List<MemberDetailResVo> memberDetailResVoList = new ArrayList<>();
    private MemberManagerAdapter memberManagerAdapter;

    //附件
    private CommonAdapter attachmentAdapter;
    private List<AttachmentResVo> attachmentList = new ArrayList<>();

    private static final String REMOVE_MEMBER_FROM_CURRENT_GROUP = "removeMember";
    private static final String ADD_MEMBER_TO_GROUP = "addMember";
    private static final String SEND_GROUP_PLAIN_MSG = "sendGroupPlainMsg";
    private static final String SEND_MEMBER_PLAIN_MSG = "sendMemberPlainMsg";

    //消息
    private PopupWindow sendMsgPop;
    private PlainMsgReqVo plainMsgReqVo = new PlainMsgReqVo();
    private String plainMsgContent = "";
    private String plainMsgTitle = "";

    private static final int REMOVE_REQUEST_CODE = 101;
    private static final int ADD_REQUEST_CODE = 102;
    private static final int REMOVE_RESPONSE_CODE = 103;
    private static final int ADD_RESPONSE_CODE = 104;

    //选中的用户数组
    private List<Integer> selectedUserList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_member_manager;
    }

    @Override
    protected void processLogic() {
        String groupIdStr = getIntent().getStringExtra("groupId");
        try {
            groupId = Integer.parseInt(groupIdStr);
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
        type = getIntent().getIntExtra("type", 0);
        groupName = getIntent().getStringExtra("groupName");
        flag = getIntent().getStringExtra("flag");
        initView(flag);
        initAdapter(flag);
        initData(flag);
    }

    private void initView(String uiFlag) {
        binding.tvDelete.setText("");
        if (uiFlag.equals(SEND_GROUP_PLAIN_MSG) || uiFlag.equals(SEND_MEMBER_PLAIN_MSG)) {
            binding.tvGroupName.setText("选择发送到");
        } else {
            if (!TextUtils.isEmpty(groupName)) binding.tvGroupName.setText(groupName);
        }
    }

    private void initAdapter(String flag) {
        if (flag.equals(SEND_GROUP_PLAIN_MSG)) {
            initGroupAdapter();
        } else if (flag.equals(SEND_MEMBER_PLAIN_MSG) || flag.equals(REMOVE_MEMBER_FROM_CURRENT_GROUP) || flag.equals(ADD_MEMBER_TO_GROUP)) {
            initMemberAdapter();
        }
    }

    private void initData(String flag) {
        switch (flag) {
            case REMOVE_MEMBER_FROM_CURRENT_GROUP:   //删除用户
                if (type == 0) {
                    getGovermentUserByGroupId(groupId, false);
                } else {
                    getEnterpriseUserByGroupId(groupId, false);
                }
                break;
            case ADD_MEMBER_TO_GROUP:   //移动成员到分组
                if (type == 0) {
                    getGovermentUserByGroupId(groupId, true);
                } else {
                    getEnterpriseUserByGroupId(groupId, true);
                }
                break;
            case SEND_GROUP_PLAIN_MSG:  //发送分组消息
                mViewModel.getGroupList(type).observe(this, res -> {
                    res.handler(new OnCallback<GroupResVo>() {
                        @Override
                        public void onSuccess(GroupResVo data) {
                            groupResVoList.clear();
                            groupResVoList.addAll(data.getList());
                            if (groupManagerAdapter != null)
                                groupManagerAdapter.notifyDataSetChanged();
                        }
                    });
                });
                break;
            case SEND_MEMBER_PLAIN_MSG:  //成员消息
                switch (type) {
                    case 0: //政府
                        mViewModel.getAllGovernment().observe(this, res -> {
                            res.handler(new OnCallback<List<MemberDetailResVo>>() {
                                @Override
                                public void onSuccess(List<MemberDetailResVo> data) {
                                    memberDetailResVoList.clear();
                                    memberDetailResVoList.addAll(data);
                                    if (memberManagerAdapter != null)
                                        memberManagerAdapter.notifyDataSetChanged();
                                }
                            });
                        });
                        break;
                    case 1: //企业
                        mViewModel.getAllEnterprise().observe(this, res -> {
                            res.handler(new OnCallback<List<MemberDetailResVo>>() {
                                @Override
                                public void onSuccess(List<MemberDetailResVo> data) {
                                    memberDetailResVoList.clear();
                                    memberDetailResVoList.addAll(data);
                                    if (memberManagerAdapter != null)
                                        memberManagerAdapter.notifyDataSetChanged();
                                }
                            });
                        });
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    /**
     * 成员管理
     */
    private void initMemberAdapter() {
        memberManagerAdapter = new MemberManagerAdapter(getContext(), memberDetailResVoList);
        binding.rvGroupUser.setLayoutManager(new LinearLayoutManager(this));
        binding.rvGroupUser.setAdapter(memberManagerAdapter);
        memberManagerAdapter.setOnItemClickListener((view, position) -> LogUtils.d(TAG, "project===" + position));
        memberManagerAdapter.setOnItemChenedListener(new MemberManagerAdapter.OnItemCheckedListener() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void onItemChecked(View view, int position) {
                MemberDetailResVo memberDetailResVo = memberDetailResVoList.get(position);
                selectedUserList.add(memberDetailResVo.getUserId());
                binding.tvDelete.setText(getString(R.string.have_selected_member, selectedUserList.size()));
            }

            @SuppressLint("StringFormatMatches")
            @Override
            public void onItemNoChecked(View view, int position) {
                MemberDetailResVo memberDetailResVo = memberDetailResVoList.get(position);
                selectedUserList.remove(getEqualId(memberDetailResVo.getUserId(), selectedUserList));
                binding.tvDelete.setText(getString(R.string.have_selected_member, selectedUserList.size()));
                if (selectedUserList.size() == 0) binding.tvDelete.setText("");
            }
        });
    }

    private void initGroupAdapter() {
        groupManagerAdapter = new GroupManagerAdapter(getContext(), groupResVoList);
        binding.rvGroupUser.setLayoutManager(new LinearLayoutManager(this));
        binding.rvGroupUser.setAdapter(groupManagerAdapter);
        groupManagerAdapter.setOnItemClickListener((view, position) -> LogUtils.d(TAG, "project===" + position));
        groupManagerAdapter.setOnItemChenedListener(new GroupManagerAdapter.OnItemCheckedListener() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void onItemChecked(View view, int position) {
                GroupDetailsResVo groupDetailsResVo = groupResVoList.get(position);
                selectedUserList.add(groupDetailsResVo.getId());
                binding.tvDelete.setText(getString(R.string.have_selected_group, selectedUserList.size()));
            }

            @SuppressLint("StringFormatMatches")
            @Override
            public void onItemNoChecked(View view, int position) {
                GroupDetailsResVo groupDetailsResVo = groupResVoList.get(position);
                selectedUserList.remove(getEqualId(groupDetailsResVo.getId(), selectedUserList));
                binding.tvDelete.setText(getString(R.string.have_selected_group, selectedUserList.size()));
                if (selectedUserList.size() == 0) binding.tvDelete.setText("");
            }
        });
    }

    private int getEqualId(int userId, List<Integer> selectedUserList) {
        int returnId = 0;
        for (int i = 0; i < selectedUserList.size(); i++) {
            if (userId == selectedUserList.get(i)) {
                returnId = i;
            }
        }
        return returnId;
    }

    @Override
    protected void setListener() {
        binding.tvCancel.setOnClickListener(this::onClick);
        binding.tvDelete.setOnClickListener(this::onClick);
        binding.tvMove.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_move:
                if (selectedUserList.isEmpty()) {
                    ToastUtils.showToast(getString(R.string.select_user));
                } else {
                    if (!TextUtils.isEmpty(flag)) {
                        switch (flag) {
                            case REMOVE_MEMBER_FROM_CURRENT_GROUP:  //从当前分组移除
                                RemoveUserReqVo removeUserReqVo = new RemoveUserReqVo();
                                removeUserReqVo.setGroupId(groupId);
                                removeUserReqVo.setUserIdList(selectedUserList);
                                mViewModel.removeUser(removeUserReqVo).observe(this, res -> {
                                    res.handler(new OnCallback<String>() {
                                        @Override
                                        public void onSuccess(String data) {
                                            Intent intent = new Intent();
                                            setResult(REMOVE_RESPONSE_CODE, intent);
                                            finish();
                                        }
                                    });
                                });
                                break;
                            case ADD_MEMBER_TO_GROUP:   //添加到当前分组
                                RemoveUserReqVo moveUserReqVo = new RemoveUserReqVo();
                                moveUserReqVo.setGroupId(groupId);
                                moveUserReqVo.setUserIdList(selectedUserList);
                                mViewModel.addUser(moveUserReqVo).observe(this, res -> {
                                    res.handler(new OnCallback<String>() {
                                        @Override
                                        public void onSuccess(String data) {
                                            Intent intent = new Intent();
                                            setResult(REMOVE_RESPONSE_CODE, intent);
                                            finish();
                                        }
                                    });
                                });
                                break;
                            case SEND_GROUP_PLAIN_MSG:
                            case SEND_MEMBER_PLAIN_MSG:
                                initSendMsgPop(view);
                                break;
                            default:
                                break;
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 按分组发送消息
     */
    private void initSendMsgPop(View rootView) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        initSendMsgPopView(contentView);
        int height = (int) getResources().getDimension(R.dimen.dp_350);
        sendMsgPop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, height, true);
        sendMsgPop.setAnimationStyle(R.style.ActionSheetDialogAnimation);
        sendMsgPop.setOutsideTouchable(true);
        backgroundAlpha(0.3f);
        sendMsgPop.setOnDismissListener(this);
        sendMsgPop.setBackgroundDrawable(new BitmapDrawable());
        sendMsgPop.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 初始化发消息界面
     *
     * @param view
     */
    private void initSendMsgPopView(View view) {
        ImageView closeIv = view.findViewById(R.id.iv_close);
        EditText titleEt = view.findViewById(R.id.et_title);
        EditText textEt = view.findViewById(R.id.et_text);
        RecyclerView attachmentRv = view.findViewById(R.id.rv_attachment);
        RelativeLayout addAttachmentRl = view.findViewById(R.id.rl_add_attachment);
        Button sendBtn = view.findViewById(R.id.btn_send);
        initAttachmentAdapter(attachmentRv);

        closeIv.setOnClickListener((view1 -> {
            if (sendMsgPop != null) sendMsgPop.dismiss();
        }));

        addAttachmentRl.setOnClickListener(view1 -> selectFile());

        textEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                plainMsgContent = charSequence.toString().trim();
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
                plainMsgTitle = charSequence.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendBtn.setOnClickListener(view1 -> {
            plainMsgReqVo.setUserType(type);
            if (flag.equals(SEND_GROUP_PLAIN_MSG)) {
                plainMsgReqVo.setReceiverType(1);
                plainMsgReqVo.setGroupIdList(selectedUserList);
            } else if (flag.equals(SEND_MEMBER_PLAIN_MSG)) {
                plainMsgReqVo.setReceiverType(2);
                plainMsgReqVo.setUserIdList(selectedUserList);
            }
            if (!TextUtils.isEmpty(plainMsgTitle)) {
                plainMsgReqVo.setTitle(plainMsgTitle);
            } else {
                ToastUtils.showToast("请输入消息标题");
            }
            if (!TextUtils.isEmpty(plainMsgContent)) {
                plainMsgReqVo.setContent(plainMsgContent);
            } else {
                ToastUtils.showToast("请输入消息内容");
            }

            //发送分组、成员公有接口
            mViewModel.saveGroupPlainMsg(plainMsgReqVo).observe(this, res -> {
                res.handler(new OnCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        ToastUtils.showToast("消息发布成功");
                        if (sendMsgPop != null) sendMsgPop.dismiss();
                        finish();
                    }
                });
            });
        });
    }

    private void initAttachmentAdapter(RecyclerView attachmentRv) {
        attachmentAdapter = new CommonAdapter<AttachmentResVo>(getContext(), R.layout.addmsg_attachment_list_item, attachmentList) {
            @Override
            protected void convert(ViewHolder holder, AttachmentResVo attachmentResVo, int position) {
                holder.setText(R.id.tv_file_path, attachmentResVo.getName());
                holder.setOnClickListener(R.id.iv_delete, (view -> attachmentList.remove(position)));
            }
        };
        attachmentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        attachmentRv.setAdapter(attachmentAdapter);
    }

    /**
     * 从本地选择文件
     */
    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data.getData() != null) { //单次点击未使用多选
                try {
                    Uri uri = data.getData();
                    String filePath = FileUtils.getPath(getContext(), uri);
                    Log.i("zhangxianpeng===", "Single image path ---- " + filePath);
                    AttachmentResVo attachmentResVo = new AttachmentResVo();
                    attachmentResVo.setName(getFileName(filePath));
                    attachmentResVo.setUrl(filePath);
                    attachmentList.add(attachmentResVo);
                    if (attachmentAdapter != null) attachmentAdapter.notifyDataSetChanged();
                    uploadFile(attachmentList);
                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }
            } else {   //长按使用多选
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    List<String> pathList = new ArrayList<>();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        String filePath = FileUtils.getPath(getContext(), uri);
                        AttachmentResVo attachmentResVo = new AttachmentResVo();
                        attachmentResVo.setName(getFileName(filePath));
                        attachmentResVo.setUrl(filePath);
                        attachmentList.add(attachmentResVo);
                    }
                    if (attachmentAdapter != null) attachmentAdapter.notifyDataSetChanged();
                    uploadFile(attachmentList);
                }
            }
        }
    }

    /**
     * 上传文件到后台服务器
     *
     * @param attachmentList
     */
    private void uploadFile(List<AttachmentResVo> attachmentList) {
        List<MultipartBody.Part> parts
                = new ArrayList<>();
        for (File file : trasfer2FileList(attachmentList)) {
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
                    plainMsgReqVo.setAttachmentList(newList);
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

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //0.0-1.0
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1.0f);
        if (sendMsgPop != null) sendMsgPop.dismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getNotInCurrentUserList(int userType) {
        List<ChildModel> resultData = new ArrayList<>();
        if (userType == 0) {
            resultData = Variable.userList.stream().filter(item -> !Variable.govermentGroupUserList.contains(((ChildModel) item).getUserId())).collect(toList());
        } else if (userType == 1) {
            resultData = Variable.userList.stream().filter(item -> !Variable.enterpriseGroupUserList.contains(((ChildModel) item).getUserId())).collect(toList());
        }

        memberDetailResVoList.clear();
        memberDetailResVoList.addAll(transferData(resultData));
        if (memberManagerAdapter != null) memberManagerAdapter.notifyDataSetChanged();
    }

    private Collection<? extends MemberDetailResVo> transferData(List<ChildModel> originalData) {
        List<MemberDetailResVo> resultData = new ArrayList<>();
        for (int i = 0; i < originalData.size(); i++) {
            MemberDetailResVo memberDetailResVo = new MemberDetailResVo();
            memberDetailResVo.setHeadUrl(originalData.get(i).getHeadUrl());
            memberDetailResVo.setUsername(originalData.get(i).getName());
            memberDetailResVo.setUserId((Integer.parseInt(originalData.get(i).getUserId())));
            memberDetailResVo.setMobile(originalData.get(i).getPhone());
            resultData.add(memberDetailResVo);
        }
        return resultData;
    }

    private void getGovermentUserByGroupId(int groupId, boolean isGet) {
        mViewModel.getGovernmentFromId(groupId).observe(this, res -> {
            res.handler(new OnCallback<List<MemberDetailResVo>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onSuccess(List<MemberDetailResVo> data) {
                    memberDetailResVoList.clear();
                    memberDetailResVoList.addAll(data);
                    memberManagerAdapter.notifyDataSetChanged();
                    Variable.govermentGroupUserList.clear();
                    Variable.govermentGroupUserList = memberDetailResVoList;
                    if (isGet) {
                        getNotInCurrentUserList(0);
                    }
                }
            });
        });
    }

    private void getEnterpriseUserByGroupId(int groupId, boolean isGet) {
        mViewModel.getEnterpriseFromId(groupId).observe(this, res -> {
            res.handler(new OnCallback<List<MemberDetailResVo>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onSuccess(List<MemberDetailResVo> data) {
                    memberDetailResVoList.clear();
                    memberDetailResVoList.addAll(data);
                    memberManagerAdapter.notifyDataSetChanged();
                    Variable.enterpriseGroupUserList.clear();
                    Variable.enterpriseGroupUserList = memberDetailResVoList;

                    if (isGet) {
                        getNotInCurrentUserList(1);
                    }
                }
            });
        });
    }
}
