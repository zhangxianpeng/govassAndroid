package com.lihang.selfmvvm.ui.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.Variable;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.base.BaseFragment;
import com.lihang.selfmvvm.bean.ChildModel;
import com.lihang.selfmvvm.bean.GroupModel;
import com.lihang.selfmvvm.customview.iosdialog.DialogUtil;
import com.lihang.selfmvvm.customview.iosdialog.NewIOSAlertDialog;
import com.lihang.selfmvvm.databinding.FragmentMsgBinding;
import com.lihang.selfmvvm.ui.communicate.CommunicateActivity;
import com.lihang.selfmvvm.ui.fragment.adapter.UserSearchAdapter;
import com.lihang.selfmvvm.ui.mailist.MemberManagerActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.FileUtils;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.AddGroupReqVo;
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
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import sakura.bottommenulibrary.bottompopfragmentmenu.BottomMenuFragment;
import sakura.bottommenulibrary.bottompopfragmentmenu.MenuItem;

import static com.lihang.selfmvvm.base.BaseConstant.DEFAULT_FILE_SERVER;
import static com.lihang.selfmvvm.common.SystemConst.DEFAULT_SERVER;

/**
 * ?????????
 */
public class MsgFragment extends BaseFragment<MsgFragmentViewModel, FragmentMsgBinding> implements PopupWindow.OnDismissListener {

    //??????????????? ?????????
    private List<GroupModel> groupArray;

    //??????????????? ?????????????????????
    private List<List<ChildModel>> childArray;

    //?????????????????????
    private ExpandableAdapter expandableAdapter;

    //?????????????????????????????? ??????true ????????????????????????????????????????????????????????????????????????????????????????????????
    private boolean use_default_indicator = false;

    private PopupWindow mailistPop;

    /**
     * ?????????????????????
     * ????????????
     */
    private int defaultType = 0;

    /**
     * ??????????????????
     */
    private PopupWindow sendMsgPop;

    /**
     * ?????????????????????
     */
    private CommonAdapter attachmentAdapter;
    private List<AttachmentResVo> attachmentList = new ArrayList<>();

    /**
     * ??????????????????VO
     */
    private String plainMsgContent = "";
    private String plainMsgTitle = "";
    private PlainMsgReqVo plainMsgReqVo = new PlainMsgReqVo();

    private static final int REMOVE_REQUEST_CODE = 101;
    private static final int ADD_REQUEST_CODE = 102;
    private static final int REMOVE_RESPONSE_CODE = 103;
    private static final int ADD_RESPONSE_CODE = 104;

    /**
     * ?????????????????????
     */
    private NewIOSAlertDialog deleteGroupDialog;

    //??????????????????
    private PopupWindow userSearchPop;
    private ArrayList<MemberDetailResVo> searchResultList = new ArrayList<>();
    private UserSearchAdapter userSearchAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initView();
        initData(defaultType);

        if (defaultType == 0) {
            getAllGovermentUser();
        } else {
            getAllEnterpriseUser();
        }
    }

    private void getAllGovermentUser() {
        mViewModel.getAllGovernment().observe(getActivity(), res -> {
            res.handler(new OnCallback<List<MemberDetailResVo>>() {
                @Override
                public void onSuccess(List<MemberDetailResVo> data) {
                    List<ChildModel> childModels = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        MemberDetailResVo memberDetailResVo = data.get(i);
                        String realName = memberDetailResVo.getRealname();
                        String userName = memberDetailResVo.getUsername();
                        String companyName = memberDetailResVo.getEnterpriseName();
                        String result = TextUtils.isEmpty(companyName) ? (TextUtils.isEmpty(realName) ? userName : realName) : ((TextUtils.isEmpty(realName) ? userName : realName) + "-" + companyName);
                        childModels.add(new ChildModel(memberDetailResVo.getHeadUrl(), result, String.valueOf(memberDetailResVo.getUserId()), memberDetailResVo.getMobile()));
                    }

                    Variable.userList.clear();
                    Variable.userList = childModels;
                }
            });
        });
    }

    private void getAllEnterpriseUser() {
        mViewModel.getAllEnterprise().observe(getActivity(), res -> {
            res.handler(new OnCallback<List<MemberDetailResVo>>() {
                @Override
                public void onSuccess(List<MemberDetailResVo> data) {
                    List<ChildModel> childModels = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        MemberDetailResVo memberDetailResVo = data.get(i);
                        String realName = memberDetailResVo.getRealname();
                        String userName = memberDetailResVo.getUsername();
                        String companyName = memberDetailResVo.getEnterpriseName();
                        String result = TextUtils.isEmpty(companyName) ? (TextUtils.isEmpty(realName) ? userName : realName) : ((TextUtils.isEmpty(realName) ? userName : realName) + "-" + companyName);
                        childModels.add(new ChildModel(memberDetailResVo.getHeadUrl(), result, String.valueOf(memberDetailResVo.getUserId()), memberDetailResVo.getMobile()));
                    }
                    Variable.userList.clear();
                    Variable.userList = childModels;
                }
            });
        });
    }

    /**
     * ???????????????
     */
    private void initView() {
        groupArray = new ArrayList<>();
        childArray = new ArrayList<>();

        if (use_default_indicator) {
            //????????????????????????
        } else {
            binding.exListView.setGroupIndicator(null);
        }

        //???????????????
        expandableAdapter = new ExpandableAdapter(getContext(), groupArray, R.layout.exlistview_group_item, childArray, R.layout.exlistview_child_item);
        binding.exListView.setAdapter(expandableAdapter);

        binding.exListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
                //????????????????????? ????????????
                if (binding.exListView.isGroupExpanded(groupPosition)) {
                    binding.exListView.collapseGroup(groupPosition);
                } else {
                    if (groupPosition == 0) {  //????????????
                        if (defaultType == 0) {
                            mViewModel.getAllGovernment().observe(getActivity(), res -> {
                                res.handler(new OnCallback<List<MemberDetailResVo>>() {
                                    @Override
                                    public void onSuccess(List<MemberDetailResVo> data) {
                                        List<ChildModel> childModels = childArray.get(0);
                                        childModels.clear();
                                        for (int i = 0; i < data.size(); i++) {
                                            MemberDetailResVo memberDetailResVo = data.get(i);
                                            String realName = memberDetailResVo.getRealname();
                                            String userName = memberDetailResVo.getUsername();
                                            String companyName = memberDetailResVo.getEnterpriseName();
                                            String result = TextUtils.isEmpty(companyName) ? (TextUtils.isEmpty(realName) ? userName : realName) : ((TextUtils.isEmpty(realName) ? userName : realName) + "-" + companyName);
                                            childModels.add(new ChildModel(memberDetailResVo.getHeadUrl(), result, String.valueOf(memberDetailResVo.getUserId()), memberDetailResVo.getMobile()));
                                        }

                                        //????????????
                                        binding.exListView.expandGroup(groupPosition, true);
                                    }
                                });
                            });
                        } else if (defaultType == 1) {
                            mViewModel.getAllEnterprise().observe(getActivity(), res -> {
                                res.handler(new OnCallback<List<MemberDetailResVo>>() {
                                    @Override
                                    public void onSuccess(List<MemberDetailResVo> data) {
                                        List<ChildModel> childModels = childArray.get(0);
                                        childModels.clear();
                                        for (int i = 0; i < data.size(); i++) {
                                            MemberDetailResVo memberDetailResVo = data.get(i);
                                            String realName = memberDetailResVo.getRealname();
                                            String userName = memberDetailResVo.getUsername();
                                            String companyName = memberDetailResVo.getEnterpriseName();
                                            String result = TextUtils.isEmpty(companyName) ? (TextUtils.isEmpty(realName) ? userName : realName) : ((TextUtils.isEmpty(realName) ? userName : realName) + "-" + companyName);
                                            childModels.add(new ChildModel(memberDetailResVo.getHeadUrl(), result, String.valueOf(memberDetailResVo.getUserId()), memberDetailResVo.getMobile()));
                                        }

                                        //????????????
                                        binding.exListView.expandGroup(groupPosition, true);
                                    }
                                });
                            });
                        }
                    } else {  //??????id
                        if (defaultType == 0) {
                            mViewModel.getGovernmentFromId(Integer.parseInt(groupArray.get(groupPosition).getOnline())).observe(getActivity(), res -> {
                                res.handler(new OnCallback<List<MemberDetailResVo>>() {
                                    @Override
                                    public void onSuccess(List<MemberDetailResVo> data) {
                                        List<ChildModel> childModels = childArray.get(groupPosition);
                                        childModels.clear();
                                        for (int i = 0; i < data.size(); i++) {
                                            MemberDetailResVo memberDetailResVo = data.get(i);
                                            String realName = memberDetailResVo.getRealname();
                                            String userName = memberDetailResVo.getUsername();
                                            String companyName = memberDetailResVo.getEnterpriseName();
                                            String result = TextUtils.isEmpty(companyName) ? (TextUtils.isEmpty(realName) ? userName : realName) : ((TextUtils.isEmpty(realName) ? userName : realName) + "-" + companyName);
                                            childModels.add(new ChildModel(memberDetailResVo.getHeadUrl(), result, String.valueOf(memberDetailResVo.getUserId()), memberDetailResVo.getMobile()));
                                        }

                                        //????????????
                                        binding.exListView.expandGroup(groupPosition, true);
                                    }
                                });
                            });
                        } else if (defaultType == 1) {
                            mViewModel.getEnterpriseFromId(Integer.parseInt(groupArray.get(groupPosition).getOnline())).observe(getActivity(), res -> {
                                res.handler(new OnCallback<List<MemberDetailResVo>>() {
                                    @Override
                                    public void onSuccess(List<MemberDetailResVo> data) {
                                        List<ChildModel> childModels = childArray.get(groupPosition);
                                        childModels.clear();
                                        for (int i = 0; i < data.size(); i++) {
                                            MemberDetailResVo memberDetailResVo = data.get(i);
                                            String realName = memberDetailResVo.getRealname();
                                            String userName = memberDetailResVo.getUsername();
                                            String companyName = memberDetailResVo.getEnterpriseName();
                                            String result = TextUtils.isEmpty(companyName) ? (TextUtils.isEmpty(realName) ? userName : realName) : ((TextUtils.isEmpty(realName) ? userName : realName) + "-" + companyName);
                                            childModels.add(new ChildModel(memberDetailResVo.getHeadUrl(), result, String.valueOf(memberDetailResVo.getUserId()), memberDetailResVo.getMobile()));
                                        }

                                        //????????????
                                        binding.exListView.expandGroup(groupPosition, true);
                                    }
                                });
                            });
                        }
                    }
                }
                //??????false????????????????????????????????????????????? ??????true????????????????????????????????????????????????
                return true;
            }
        });

        binding.exListView.setOnChildClickListener((expandableListView, view, groupPosition, childPosition, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("realName", childArray.get(groupPosition).get(childPosition).getName());
            bundle.putString("phone", childArray.get(groupPosition).get(childPosition).getPhone());
            ActivityUtils.startActivityWithBundle(getContext(), CommunicateActivity.class, bundle);
            return true;
        });

        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                if ((event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    String key = binding.etSearch.getText().toString().trim();
                    if (!TextUtils.isEmpty(key)) {
                        searchUser(key);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * ????????????
     *
     * @param key ?????????
     */
    private void searchUser(String key) {
        if (defaultType == 0) { //??????
            mViewModel.searchGovernmentUser(key).observe(getActivity(), res -> {
                res.handler(new OnCallback<List<MemberDetailResVo>>() {
                    @Override
                    public void onSuccess(List<MemberDetailResVo> data) {
                        searchResultList.clear();
                        searchResultList.addAll(data);
                        showSearchResultListPop();
                    }
                });
            });
        } else { //??????
            mViewModel.searchEnterpriseUser(key).observe(getActivity(), res -> {
                res.handler(new OnCallback<List<MemberDetailResVo>>() {
                    @Override
                    public void onSuccess(List<MemberDetailResVo> data) {
                        searchResultList.clear();
                        searchResultList.addAll(data);
                        showSearchResultListPop();
                    }
                });
            });
        }
    }

    private void showSearchResultListPop() {
        View view1 = getActivity().getLayoutInflater().inflate(R.layout.popu_search_user_result, null, false);
        RecyclerView searchUserResultRv = (RecyclerView) view1.findViewById(R.id.rv_search_result);
        userSearchAdapter = new UserSearchAdapter(getActivity(), searchResultList);
        searchUserResultRv.setLayoutManager(new LinearLayoutManager(getContext()));
        searchUserResultRv.setAdapter(userSearchAdapter);
        userSearchPop = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        userSearchPop.setTouchable(true);
        userSearchPop.setBackgroundDrawable(new ColorDrawable(0x00000000));

        userSearchAdapter.setOnItemClickListener((view, position) -> {
            Bundle bundle = new Bundle();
            String realName = searchResultList.get(position).getRealname();
            String userName = searchResultList.get(position).getUsername();
            String phone = searchResultList.get(position).getMobile();
            bundle.putString("realName", TextUtils.isEmpty(realName) ? userName : realName);
            bundle.putString("phone", phone);
            ActivityUtils.startActivityWithBundle(getContext(), CommunicateActivity.class, bundle);
            userSearchPop.dismiss();
        });

        userSearchPop.showAsDropDown(binding.etSearch, 0, 0);
    }

    /**
     * ????????????????????????
     *
     * @param view
     */
    private void initsendMsgPopView(View view) {
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
            plainMsgReqVo.setUserType(defaultType);
            plainMsgReqVo.setReceiverType(0);
            if (!TextUtils.isEmpty(plainMsgTitle)) {
                plainMsgReqVo.setTitle(plainMsgTitle);
            } else {
                ToastUtils.showToast("?????????????????????");
            }
            if (!TextUtils.isEmpty(plainMsgContent)) {
                plainMsgReqVo.setContent(plainMsgContent);
            } else {
                ToastUtils.showToast("?????????????????????");
            }
            //??????????????????
            mViewModel.savePlainMsg(plainMsgReqVo).observe(getActivity(), res -> {
                res.handler(new OnCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        ToastUtils.showToast("??????????????????");
                        if (sendMsgPop != null) sendMsgPop.dismiss();
                    }
                });
            });
        });
    }

    /**
     * ?????????????????????
     */
    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
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
     * ???????????????
     *
     * @param groupName
     * @param groupId
     */
    private void renameGroup(String groupName, String groupId) {
        if (mailistPop != null) mailistPop.dismiss();
        DialogUtil.alertIosDialog(getActivity(), "???????????????", true, "??????", "??????", new DialogUtil.DialogAlertListener() {
            @Override
            public void yes(String groupName, String groupNameRemark) {

                if (TextUtils.isEmpty(groupName)) {
                    ToastUtils.showToast("?????????????????????");
                    return;
                } else {
                    mViewModel.updateGroupName(Integer.parseInt(groupId), defaultType, groupName).observe(getActivity(), res -> {
                        res.handler(new OnCallback<String>() {
                            @Override
                            public void onSuccess(String data) {
                                initData(defaultType);
                            }
                        });
                    });
                }
            }
        });
    }

    /**
     * ????????????
     *
     * @param groupName
     * @param groupId
     */
    private void addMember(String groupName, String groupId) {
        if (mailistPop != null) mailistPop.dismiss();
        Intent intent = new Intent(getActivity(), MemberManagerActivity.class);
        intent.putExtra("flag", "addMember");
        intent.putExtra("type", defaultType);
        intent.putExtra("groupName", groupName);
        intent.putExtra("groupId", groupId);
        startActivity(intent);
    }

    /**
     * ????????????
     *
     * @param groupName
     * @param groupId
     */
    private void removeMember(String groupName, String groupId) {
        if (mailistPop != null) mailistPop.dismiss();
        Intent intent = new Intent(getActivity(), MemberManagerActivity.class);
        intent.putExtra("flag", "removeMember");
        intent.putExtra("type", defaultType);
        intent.putExtra("groupName", groupName);
        intent.putExtra("groupId", groupId);
        startActivity(intent);
    }

    /**
     * ????????????
     *
     * @param groupName
     * @param groupId
     */
    private void deleteGroup(String groupName, String groupId) {
        deleteGroupDialog = new NewIOSAlertDialog(getContext()).builder();
        deleteGroupDialog.setGone().setMsg("?????????????????????????").setNegativeButton("??????", null).setPositiveButton("??????", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> groupIds = new ArrayList<>();
                groupIds.add(Integer.parseInt(groupId));
                mViewModel.deleteGroup(groupIds).observe(getActivity(), res -> {
                    res.handler(new OnCallback<String>() {
                        @Override
                        public void onSuccess(String data) {
                            initData(defaultType);
                        }
                    });
                });
            }
        }).show();
    }

    /**
     * ???????????????
     *
     * @param groupName
     * @param groupNameRemark
     */
    private void addGroupName(String groupName, String groupNameRemark) {
        if (mailistPop != null) mailistPop.dismiss();
        mViewModel.checkGroupNameRepeat(defaultType, groupName).observe(getActivity(), res -> {
            res.handler(new OnCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    AddGroupReqVo addGroupReqVo = new AddGroupReqVo();
                    addGroupReqVo.setName(groupName);
                    addGroupReqVo.setRemark(groupNameRemark);
                    addGroupReqVo.setType(defaultType);
                    mViewModel.saveGroup(addGroupReqVo).observe(getActivity(), res -> {
                        res.handler(new OnCallback<String>() {
                            @Override
                            public void onSuccess(String data) {
                                initData(defaultType);
                            }
                        });
                    });
                }
            });
        });
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        //0.0-1.0
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1.0f);
        if (mailistPop != null) mailistPop.dismiss();
    }

    /**
     * ????????????
     */
    private void initData(int type) {
        getGroupList(type);
    }

    /**
     * ??????????????????
     *
     * @param type
     */
    private void getGroupList(int type) {
        mViewModel.getGroupList(type).observe(getActivity(), res -> {
            res.handler(new OnCallback<GroupResVo>() {
                @Override
                public void onSuccess(GroupResVo data) {
                    List<GroupModel> list = trasnsfer(inisertDefaultItem(data.getList()));
                    groupArray.clear();
                    groupArray.addAll(list);
                    for (int i = 0; i < groupArray.size(); i++) {
                        List<ChildModel> tempArray = new ArrayList<>();
                        childArray.add(tempArray);
                    }
                    expandableAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    private List<GroupModel> trasnsfer(List<GroupDetailsResVo> groupDetailsResVos) {
        List<GroupModel> groupDataBeanList = new ArrayList<>();
        for (int i = 0; i < groupDetailsResVos.size(); i++) {
            GroupDetailsResVo groupDetailsResVo = groupDetailsResVos.get(i);
            GroupModel groupDataBean = new GroupModel(groupDetailsResVo.getName(), String.valueOf(groupDetailsResVo.getId()));
            groupDataBeanList.add(groupDataBean);
        }
        return groupDataBeanList;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param groupNameList
     */
    private List<GroupDetailsResVo> inisertDefaultItem(List<GroupDetailsResVo> groupNameList) {
        GroupDetailsResVo groupDetailsResVo = new GroupDetailsResVo();
        groupDetailsResVo.setId(0);
        groupDetailsResVo.setName("???????????????");
        groupNameList.add(0, groupDetailsResVo);
        return groupNameList;
    }

    @Override
    protected void setListener() {
        binding.tvCompanyUser.setOnClickListener(this::onClick);
        binding.tvGovermentUser.setOnClickListener(this::onClick);
        binding.ivAddGroup.setOnClickListener(this::onClick);
        binding.tvManage.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_manage:
                initAddMsgGroupDialog(view);
                break;
            case R.id.iv_add_group:
                DialogUtil.alertIosDialog(getActivity(), "????????????", true, "??????", "??????", new DialogUtil.DialogAlertListener() {
                    @Override
                    public void yes(String groupName, String groupNameRemark) {
                        if (!TextUtils.isEmpty(groupName)) {
                            addGroupName(groupName, groupNameRemark);
                        } else {
                            ToastUtils.showToast("?????????????????????");
                        }
                    }
                });
                break;
            case R.id.tv_cancel:
                if (mailistPop != null) mailistPop.dismiss();
                break;
            case R.id.tv_company_user:
                binding.tvCompanyUser.setTextSize((float) 18.0);
                binding.tvGovermentUser.setTextSize((float) 14.0);
                binding.allTitle.setText(getString(R.string.company_user));
                collapseGroup();
                //?????????????????????
                defaultType = 1;
                initData(defaultType);
                getAllEnterpriseUser();
                break;
            case R.id.tv_goverment_user:
                binding.tvCompanyUser.setTextSize((float) 14.0);
                binding.tvGovermentUser.setTextSize((float) 18.0);
                binding.allTitle.setText(getString(R.string.goverment_user));
                collapseGroup();
                defaultType = 0;
                initData(defaultType);
                getAllGovermentUser();
                break;
            default:
                break;
        }
    }

    /**
     * ????????????
     */
    private void initAddMsgGroupDialog(View rootView) {
        new BottomMenuFragment(getActivity())
                .addMenuItems(new MenuItem("????????????"))
                .addMenuItems(new MenuItem("????????????"))
                .addMenuItems(new MenuItem("????????????"))
                .setOnItemClickListener(new BottomMenuFragment.OnItemClickListener() {
                    @Override
                    public void onItemClick(TextView menu_item, int position) {
                        switch (position) {
                            case 0:   //??????
                                initSendMsgPop(rootView);
                                break;
                            case 1:  //??????
                                Bundle bundle = new Bundle();
                                bundle.putString("flag", "sendGroupPlainMsg");
                                bundle.putInt("type",defaultType);
                                ActivityUtils.startActivityWithBundle(getActivity(), MemberManagerActivity.class, bundle);
                                break;
                            case 2:  //??????
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("flag", "sendMemberPlainMsg");
                                bundle1.putInt("type",defaultType);
                                ActivityUtils.startActivityWithBundle(getActivity(), MemberManagerActivity.class, bundle1);
                                break;
                            case 3: //??????
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    /**
     * ????????????
     *
     * @param rootView
     */
    private void initSendMsgPop(View rootView) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
        initsendMsgPopView(contentView);
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
     * ??????tab???????????????????????????group ?????????????????????
     */
    private void collapseGroup() {
        if (expandableAdapter.getGroupCount() != 0) {
            int count = expandableAdapter.getGroupCount();
            for (int i = 0; i < count; i++) {
                binding.exListView.collapseGroup(i);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REMOVE_REQUEST_CODE && resultCode == REMOVE_RESPONSE_CODE) {
            initData(defaultType);
        }
        if (resultCode == Activity.RESULT_OK) {
            if (data.getData() != null) { //???????????????????????????
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
            } else {   //??????????????????
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
     * ??????????????????????????????
     *
     * @param attachmentList
     */
    private void uploadFile(List<AttachmentResVo> attachmentList) {
        List<MultipartBody.Part> parts = new ArrayList<>();
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
     * ????????????????????????
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

    class ExpandableAdapter extends BaseExpandableListAdapter {
        //???????????????
        private LayoutInflater mInflater;
        private Context mContext;
        private int mExpandedGroupLayout;
        private int mChildLayout;
        private List<GroupModel>    mGroupArray;
        private List<List<ChildModel>> mChildArray;

        /**
         * ????????????
         *
         * @param context
         * @param groupData
         * @param expandedGroupLayout ??????????????????
         * @param childData
         * @param childLayout         ??????????????????
         */
        public ExpandableAdapter(Context context, List<GroupModel> groupData, int expandedGroupLayout,
                                 List<List<ChildModel>> childData, int childLayout) {
            mContext = context;
            mExpandedGroupLayout = expandedGroupLayout;
            mChildLayout = childLayout;
            mGroupArray = groupData;
            mChildArray = childData;
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public ExpandableAdapter(int msgFragment) {
        }

        public Object getChild(int groupPosition, int childPosition) {
            return childArray.get(groupPosition).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            // ???????????????????????????????????????????????????????????????
            View v;
            if (convertView == null) {
                v = newChildView(parent);
            } else {
                v = convertView;
            }
            bindChildView(v, groupPosition, mChildArray.get(groupPosition).get(childPosition));
            return v;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            // ????????????????????????????????????
            return mChildArray.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            // ???????????????????????????????????????
            return mGroupArray.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            // ???????????????
            return mGroupArray.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            // ?????????????????????ID?????????ID???????????????????????????????????????ID ?????????getCombinedGroupId(long)???
            // ???????????????????????????ID????????????????????????ID??????
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            // ?????????????????????????????????????????? ????????????????????????????????????????????? ???????????????????????????????????????
            // ??????????????? getChildView(int, int, boolean, View, ViewGroup)???
            View v;
            if (convertView == null) {
                v = newGroupView(parent);
            } else {
                v = convertView;
            }
            bindGroupView(v, groupPosition, mGroupArray.get(groupPosition), isExpanded);
            return v;
        }

        /**
         * ???????????????
         *
         * @param view
         * @param data
         * @param isExpanded
         */
        private void bindGroupView(View view, int position, GroupModel data, boolean isExpanded) {
            // ???????????????????????? ???????????????????????????
            TextView tv_title = view.findViewById(R.id.tv_group_name);
            TextView tv_online = view.findViewById(R.id.tv_msg);
            tv_title.setText(data.getTitle());
            tv_online.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            if (!use_default_indicator) {
                ImageView iv_tip = view.findViewById(R.id.iv_left);
                if (isExpanded) {
                    iv_tip.setImageResource(R.mipmap.down);
                } else {
                    iv_tip.setImageResource(R.mipmap.right);
                }
            }

            tv_online.setOnClickListener(view1 -> {
                new BottomMenuFragment(getActivity())
                        .setTitle(data.getTitle())
                        .addMenuItems(new MenuItem("????????????"))
                        .addMenuItems(new MenuItem("????????????"))
                        .addMenuItems(new MenuItem("????????????"))
                        .addMenuItems(new MenuItem("????????????"))
                        .setOnItemClickListener(new BottomMenuFragment.OnItemClickListener() {
                            @Override
                            public void onItemClick(TextView menu_item, int position) {
                                switch (position) {
                                    case 1:   //????????????
                                        removeMember(data.getTitle(), data.getOnline());
                                        break;
                                    case 2:  //????????????
                                        addMember(data.getTitle(), data.getOnline());
                                        break;
                                    case 3: //????????????
                                        renameGroup(data.getTitle(), data.getOnline());
                                        break;
                                    case 4: //????????????
                                        deleteGroup(data.getTitle(), data.getOnline());
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).show();
            });
        }

        /**
         * ???????????????
         *
         * @param view
         * @param data
         */
        private void bindChildView(View view, int groupPosition, ChildModel data) {
            // ???????????????????????? ???????????????????????????
            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView deleteTv = view.findViewById(R.id.tv_delete);
            CircleImageView iv_head = view.findViewById(R.id.iv_head);
            tv_name.setText(data.getName());
            Glide.with(view).load(DEFAULT_SERVER + DEFAULT_FILE_SERVER + data.getHeadUrl()).placeholder(R.mipmap.default_tx_img)
                    .error(R.mipmap.default_tx_img).into(iv_head);
            deleteTv.setOnClickListener(view1 -> {
                DialogUtil.alertIosDialog(getActivity(), "?????????????????????????????????????", false, "??????", "??????", new DialogUtil.DialogAlertListener() {
                    @Override
                    public void yes(String groupName, String groupNameRemark) {
                        RemoveUserReqVo removeUserReqVo = new RemoveUserReqVo();
                        List<Integer> userIdList = new ArrayList<>();
                        try {
                            int id = Integer.parseInt(data.getUserId());
                            userIdList.add(id);
                        } catch (NumberFormatException e) {
                            System.out.print("NumberFormatException" + e.getMessage());
                        }
                        removeUserReqVo.setGroupId(groupPosition);
                        removeUserReqVo.setUserIdList(userIdList);
                        mViewModel.removeUser(removeUserReqVo).observe(getActivity(), res -> {
                            res.handler(new OnCallback<String>() {
                                @Override
                                public void onSuccess(String data) {
                                    initData(defaultType);
                                }
                            });
                        });
                    }
                });
            });
        }

        /**
         * ?????????????????????
         *
         * @param parent
         * @return
         */
        public View newGroupView(ViewGroup parent) {
            return mInflater.inflate(mExpandedGroupLayout, parent, false);
        }

        /**
         * ?????????????????????
         *
         * @param parent
         * @return
         */
        public View newChildView(ViewGroup parent) {
            return mInflater.inflate(mChildLayout, parent, false);
        }

        public boolean hasStableIds() {
            // ??????????????????????????????????????????ID??????????????????????????????????????????ID???
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // ??????????????????????????????????????????
            return true;
        }
    }

}