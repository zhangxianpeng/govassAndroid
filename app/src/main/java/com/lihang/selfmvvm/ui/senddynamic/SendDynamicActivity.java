package com.lihang.selfmvvm.ui.senddynamic;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivitySendDynamicBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.CommonUtils;
import com.lihang.selfmvvm.utils.FileUtils;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.model.FriendGridItemVo;
import com.lihang.selfmvvm.vo.req.AddDynamicReqVo;
import com.lihang.selfmvvm.vo.res.AttachmentResVo;
import com.lihang.selfmvvm.vo.res.UploadAttachmentResVo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.lihang.selfmvvm.base.BaseConstant.ADD_ICON_URL;
import static com.lihang.selfmvvm.base.BaseConstant.DEFAULT_FILE_SERVER;
import static com.lihang.selfmvvm.common.SystemConst.DEFAULT_SERVER;

/**
 * created by zhangxianpeng
 * 发布动态
 */
public class SendDynamicActivity extends BaseActivity<SendDynamicViewModel, ActivitySendDynamicBinding> implements TextWatcher, PopupWindow.OnDismissListener {

    //照片adapter
    private CommonAdapter picAdapter;

    private PopupWindow commonPop;

    //用于保存拍照图片的uri
    private Uri mCameraUri;

    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;

    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    //需要上传的图片列表
    private List<AttachmentResVo> attachmentList = new ArrayList<>();

    //前端展示的图片列表
    private ArrayList<FriendGridItemVo> mData = new ArrayList<>();

    private AddDynamicReqVo addDynamicReqVo = new AddDynamicReqVo();

    private int contentType = -1;

    private static final int CAMERA_REQUEST_CODE = 1001;
    private static final int RC_CHOOSE_PHOTO = 1002;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_send_dynamic;
    }

    @Override
    protected void processLogic() {
        initGridAdapter();
        contentType= getIntent().getIntExtra("contentType",-1);
    }

    private void initGridAdapter() {
        //默认的+号
        FriendGridItemVo friendGridItemVo = new FriendGridItemVo();
        friendGridItemVo.setImageRes(ADD_ICON_URL);
        mData.add(friendGridItemVo);
        picAdapter = new CommonAdapter<FriendGridItemVo>(getContext(), R.layout.grid_item_friend, mData) {
            @Override
            protected void convert(ViewHolder holder, FriendGridItemVo friendGridItemVo, int position) {
                ImageView imageView = holder.getView(R.id.img_icon);
                if (position == 0) {
                    Glide.with(getContext()).load(friendGridItemVo.getImageRes()).placeholder(R.mipmap.btn_addimages)
                            .error(R.mipmap.btn_addimages).into(imageView);
                } else {
                    Glide.with(getContext()).load(friendGridItemVo.getImageRes()).placeholder(R.mipmap.default_tx_img)
                            .error(R.mipmap.default_tx_img).into(imageView);
                }
                holder.setOnClickListener(R.id.img_icon, view -> {
                    if (position == 0) {
                        showPicDialog(view);
                    }
                });
            }
        };
        binding.rvPic.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.rvPic.setAdapter(picAdapter);
    }

    @Override
    protected void setListener() {
        binding.tvCancel.setOnClickListener(this::onClick);
        binding.btnSend.setOnClickListener(this::onClick);
        binding.etDynamic.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.tv_cancel:
                onBackPressed();
                break;
            case R.id.btn_send:
                sendDynamic();
                break;
            case R.id.btn_camera:
                openCamera();
                break;
            case R.id.btn_photo:
                choosePhoto();
                break;
            case R.id.btn_cancel:
                if (commonPop != null) commonPop.dismiss();
                break;
            default:
                break;
        }
    }

    private void showPicDialog(View rootView) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.imagepop, null);
        initPopView(contentView);
        int height = (int) getResources().getDimension(R.dimen.dp_150);
        commonPop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, height, true);
        commonPop.setAnimationStyle(R.style.ActionSheetDialogAnimation);
        commonPop.setOutsideTouchable(true);
        backgroundAlpha(0.3f);
        commonPop.setOnDismissListener(this);
        commonPop.setBackgroundDrawable(new BitmapDrawable());
        commonPop.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    private void initPopView(View view) {
        TextView cameraTv = view.findViewById(R.id.btn_camera);
        TextView photoTv = view.findViewById(R.id.btn_photo);
        TextView cancelTv = view.findViewById(R.id.btn_cancel);
        cameraTv.setOnClickListener(this::onClick);
        photoTv.setOnClickListener(this::onClick);
        cancelTv.setOnClickListener(this::onClick);
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
        if (commonPop != null) commonPop.dismiss();
    }

    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intentToPickPic.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
    }

    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;

            if (isAndroidQ) {
                // 适配android 10
                photoUri = createImageUri();
            } else {
                photoFile = createImageFile();
                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
            }

            mCameraUri = photoUri;
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    private File createImageFile() {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (commonPop != null)
            commonPop.dismiss();
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (isAndroidQ) {
                    uploadFile(null, CommonUtils.uriToFile(mCameraUri, this));
                } else {
                    File file = new File(mCameraImagePath);
                    uploadFile(null, file);
                }
            } else {
                ToastUtils.showToast("取消");
            }
        } else if (requestCode == RC_CHOOSE_PHOTO) {
            if (data.getData() != null) { //单次点击未使用多选
                try {
                    Uri uri = data.getData();
                    String filePath = FileUtils.getPath(getContext(), uri);
                    Log.i("zhangxianpeng===", "Single image path ---- " + filePath);
                    AttachmentResVo attachmentResVo = new AttachmentResVo();
                    attachmentResVo.setName(getFileName(filePath));
                    attachmentResVo.setUrl(filePath);
                    attachmentList.add(attachmentResVo);
                    uploadFile(attachmentList, null);
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
                    uploadFile(attachmentList, null);
                }
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

    private void uploadFile(List<AttachmentResVo> attachmentList, File mFile) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        if (attachmentList != null && attachmentList.size() > 0) {
            for (File file : trasfer2FileList(attachmentList)) {
                RequestBody body = MultipartBody.create(MultipartBody.FORM, file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), body);
                parts.add(part);
            }
        }

        if (mFile != null) {
            RequestBody body = MultipartBody.create(MultipartBody.FORM, mFile);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", mFile.getName(), body);
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

                        FriendGridItemVo friendGridItemVo = new FriendGridItemVo();
                        friendGridItemVo.setImageRes(DEFAULT_SERVER + DEFAULT_FILE_SERVER + uploadAttachmentResVo.getFilePath());
                        mData.add(friendGridItemVo);
                    }

                    addDynamicReqVo.setAttachmentList(newList);
                    if (picAdapter != null) picAdapter.notifyDataSetChanged();
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        updateSendBtn();
    }

    private void updateSendBtn() {
        String dynamicContent = binding.etDynamic.getText().toString().trim();
        if (!TextUtils.isEmpty(dynamicContent)) {
            binding.btnSend.setEnabled(true);
        } else {
            binding.btnSend.setEnabled(false);
        }
    }

    private void sendDynamic() {
        addDynamicReqVo.setContent(binding.etDynamic.getText().toString().trim());
        addDynamicReqVo.setContentType(contentType);
        addDynamicReqVo.setTitle("");
        mViewModel.saveDynamic(addDynamicReqVo).observe(this, res -> {
            res.handler(new OnCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    ToastUtils.showToast("发布成功，后台审核通过后方可显示");
                    finish();
                }
            });
        });
    }
}
