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
 * 通讯录
 */
public class MsgFragment extends BaseFragment<MsgFragmentViewModel, FragmentMsgBinding> implements PopupWindow.OnDismissListener {

    //最外面一层 分组名
    private List<GroupModel> groupArray;

    //最外面一层 分组下面的详情
    private List<List<ChildModel>> childArray;

    //自定义的适配器
    private ExpandableAdapter expandableAdapter;

    //是否使用默认的指示器 默认true 使用者可以在这里通过改变这个值观察默认指示器和自定义指示器的区别
    private boolean use_default_indicator = false;

    private PopupWindow mailistPop;

    /**
     * 默认传入的参数
     * 政府用户
     */
    private int defaultType = 0;

    /**
     * 发送消息弹框
     */
    private PopupWindow sendMsgPop;

    /**
     * 发送类型
     */
    private int sendType = 0;

    /**
     * 附件列表适配器
     */
    private CommonAdapter attachmentAdapter;
    private List<AttachmentResVo> attachmentList = new ArrayList<>();

    /**
     * 发送消息请求VO
     */
    private String plainMsgContent = "";
    private String plainMsgTitle = "";
    private PlainMsgReqVo plainMsgReqVo = new PlainMsgReqVo();

    private static final int REMOVE_REQUEST_CODE = 101;
    private static final int ADD_REQUEST_CODE = 102;
    private static final int REMOVE_RESPONSE_CODE = 103;
    private static final int ADD_RESPONSE_CODE = 104;

    /**
     * 删除分组提示框
     */
    private NewIOSAlertDialog deleteGroupDialog;

    //用户搜索结果
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
    }

    /**
     * 初始化控件
     */
    private void initView() {
        groupArray = new ArrayList<>();
        childArray = new ArrayList<>();

        if (use_default_indicator) {
            //不做处理就是默认
        } else {
            binding.exListView.setGroupIndicator(null);
        }

        //创建适配器
        expandableAdapter = new ExpandableAdapter(getContext(), groupArray, R.layout.exlistview_group_item, childArray, R.layout.exlistview_child_item);
        binding.exListView.setAdapter(expandableAdapter);

        binding.exListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
                //如果分组被打开 直接关闭
                if (binding.exListView.isGroupExpanded(groupPosition)) {
                    binding.exListView.collapseGroup(groupPosition);
                } else {
                    if (groupPosition == 0) {  //获取全部
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

                                        //打开分组
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

                                        //打开分组
                                        binding.exListView.expandGroup(groupPosition, true);
                                    }
                                });
                            });
                        }
                    } else {  //根据id
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

                                        //打开分组
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

                                        //打开分组
                                        binding.exListView.expandGroup(groupPosition, true);
                                    }
                                });
                            });
                        }
                    }
                }
                //返回false表示系统自己处理展开和关闭事件 返回true表示调用者自己处理展开和关闭事件
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
     * 搜索用户
     *
     * @param key 关键字
     */
    private void searchUser(String key) {
        if (defaultType == 0) { //政府
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
        } else { //企业
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
     * 初始化发消息界面
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
                ToastUtils.showToast("请输入消息标题");
            }
            if (!TextUtils.isEmpty(plainMsgContent)) {
                plainMsgReqVo.setContent(plainMsgContent);
            } else {
                ToastUtils.showToast("请输入消息内容");
            }
            //发送全员消息
            mViewModel.savePlainMsg(plainMsgReqVo).observe(getActivity(), res -> {
                res.handler(new OnCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        ToastUtils.showToast("消息发布成功");
                        if (sendMsgPop != null) sendMsgPop.dismiss();
                    }
                });
            });
        });
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
     * 重命名分组
     *
     * @param groupName
     * @param groupId
     */
    private void renameGroup(String groupName, String groupId) {
        if (mailistPop != null) mailistPop.dismiss();
        DialogUtil.alertIosDialog(getActivity(), "修改分组名", true, "确定", "取消", new DialogUtil.DialogAlertListener() {
            @Override
            public void yes(String groupName, String groupNameRemark) {

                if (TextUtils.isEmpty(groupName)) {
                    ToastUtils.showToast("分组名不能为空");
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
     * 新增成员
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
     * 移除成员
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
     * 删除分组
     *
     * @param groupName
     * @param groupId
     */
    private void deleteGroup(String groupName, String groupId) {
        deleteGroupDialog = new NewIOSAlertDialog(getContext()).builder();
        deleteGroupDialog.setGone().setMsg("确定要删除该分组?").setNegativeButton("取消", null).setPositiveButton("确定", new View.OnClickListener() {
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
     * 新增分组名
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
     * 加载数据
     */
    private void initData(int type) {
        getGroupList(type);
    }

    /**
     * 获取分组数据
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
     * 数组首位插入“全部联系人”
     *
     * @param groupNameList
     */
    private List<GroupDetailsResVo> inisertDefaultItem(List<GroupDetailsResVo> groupNameList) {
        GroupDetailsResVo groupDetailsResVo = new GroupDetailsResVo();
        groupDetailsResVo.setId(0);
        groupDetailsResVo.setName("全部联系人");
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
                DialogUtil.alertIosDialog(getActivity(), "新增分组", true, "确定", "取消", new DialogUtil.DialogAlertListener() {
                    @Override
                    public void yes(String groupName, String groupNameRemark) {
                        if (!TextUtils.isEmpty(groupName)) {
                            addGroupName(groupName, groupNameRemark);
                        } else {
                            ToastUtils.showToast("分组名不能为空");
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
                //选择企业后传参
                defaultType = 1;
                initData(defaultType);
                break;
            case R.id.tv_goverment_user:
                binding.tvCompanyUser.setTextSize((float) 14.0);
                binding.tvGovermentUser.setTextSize((float) 18.0);
                binding.allTitle.setText(getString(R.string.goverment_user));
                collapseGroup();
                defaultType = 0;
                initData(defaultType);
                break;
            default:
                break;
        }
    }

    /**
     * 底部弹框
     */
    private void initAddMsgGroupDialog(View rootView) {
        new BottomMenuFragment(getActivity())
                .addMenuItems(new MenuItem("全员消息"))
                .addMenuItems(new MenuItem("分组消息"))
                .addMenuItems(new MenuItem("成员消息"))
                .setOnItemClickListener(new BottomMenuFragment.OnItemClickListener() {
                    @Override
                    public void onItemClick(TextView menu_item, int position) {
                        switch (position) {
                            case 0:   //全员
                                sendType = 0;
                                initSendMsgPop(rootView);
                                break;
                            case 1:  //分组
                                Bundle bundle = new Bundle();
                                bundle.putString("flag", "sendGroupPlainMsg");
                                ActivityUtils.startActivityWithBundle(getActivity(), MemberManagerActivity.class, bundle);
                                break;
                            case 2:  //成员
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("flag", "sendMemberPlainMsg");
                                ActivityUtils.startActivityWithBundle(getActivity(), MemberManagerActivity.class, bundle1);
                                break;
                            case 3: //取消
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    /**
     * 添加消息
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
     * 切换tab默认全部关闭，点击group 后重新去拉数据
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

    class ExpandableAdapter extends BaseExpandableListAdapter {
        //视图加载器
        private LayoutInflater mInflater;
        private Context mContext;
        private int mExpandedGroupLayout;
        private int mChildLayout;
        private List<GroupModel> mGroupArray;
        private List<List<ChildModel>> mChildArray;

        /**
         * 构造函数
         *
         * @param context
         * @param groupData
         * @param expandedGroupLayout 分组视图布局
         * @param childData
         * @param childLayout         详情视图布局
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
            // 取得显示给定分组给定子位置的数据用的视图。
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
            // 取得指定分组的子元素数。
            return mChildArray.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            // 取得与给定分组关联的数据。
            return mGroupArray.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            // 取得分组数
            return mGroupArray.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            // 取得指定分组的ID。该组ID必须在组中是唯一的。组合的ID （参见getCombinedGroupId(long)）
            // 必须不同于其他所有ID（分组及子项目的ID）。
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            // 取得用于显示给定分组的视图。 这个方法仅返回分组的视图对象， 要想获取子元素的视图对象，
            // 就需要调用 getChildView(int, int, boolean, View, ViewGroup)。
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
         * 绑定组数据
         *
         * @param view
         * @param data
         * @param isExpanded
         */
        private void bindGroupView(View view, int position, GroupModel data, boolean isExpanded) {
            // 绑定组视图的数据 当然这些都是模拟的
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
                        .addMenuItems(new MenuItem("移除成员"))
                        .addMenuItems(new MenuItem("新增成员"))
                        .addMenuItems(new MenuItem("重新命名"))
                        .addMenuItems(new MenuItem("删除分组"))
                        .setOnItemClickListener(new BottomMenuFragment.OnItemClickListener() {
                            @Override
                            public void onItemClick(TextView menu_item, int position) {
                                switch (position) {
                                    case 1:   //移除成员
                                        removeMember(data.getTitle(), data.getOnline());
                                        break;
                                    case 2:  //新增成员
                                        addMember(data.getTitle(), data.getOnline());
                                        break;
                                    case 3: //重新命名
                                        renameGroup(data.getTitle(), data.getOnline());
                                        break;
                                    case 4: //删除分组
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
         * 绑定子数据
         *
         * @param view
         * @param data
         */
        private void bindChildView(View view, int groupPosition, ChildModel data) {
            // 绑定组视图的数据 当然这些都是模拟的
            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView deleteTv = view.findViewById(R.id.tv_delete);
            CircleImageView iv_head = view.findViewById(R.id.iv_head);
            tv_name.setText(data.getName());
            Glide.with(view).load(DEFAULT_SERVER + DEFAULT_FILE_SERVER + data.getHeadUrl()).placeholder(R.mipmap.default_tx_img)
                    .error(R.mipmap.default_tx_img).into(iv_head);
            deleteTv.setOnClickListener(view1 -> {
                DialogUtil.alertIosDialog(getActivity(), "确定从该分组中删除此用户?", false, "确定", "取消", new DialogUtil.DialogAlertListener() {
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
         * 创建新的组视图
         *
         * @param parent
         * @return
         */
        public View newGroupView(ViewGroup parent) {
            return mInflater.inflate(mExpandedGroupLayout, parent, false);
        }

        /**
         * 创建新的子视图
         *
         * @param parent
         * @return
         */
        public View newChildView(ViewGroup parent) {
            return mInflater.inflate(mChildLayout, parent, false);
        }

        public boolean hasStableIds() {
            // 是否指定分组视图及其子视图的ID对应的后台数据改变也会保持该ID。
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // 指定位置的子视图是否可选择。
            return true;
        }
    }

}