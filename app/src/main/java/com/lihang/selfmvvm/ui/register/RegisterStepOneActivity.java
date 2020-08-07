package com.lihang.selfmvvm.ui.register;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityRegisterStepOneBinding;
import com.lihang.selfmvvm.ui.login.GovassLoginActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.CommonUtils;
import com.lihang.selfmvvm.utils.FileUtils;
import com.lihang.selfmvvm.utils.LogUtils;
import com.lihang.selfmvvm.utils.NoDoubleClickListener;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.RegisterReqVo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;


public class RegisterStepOneActivity extends BaseActivity<RegisterStepOneViewModel, ActivityRegisterStepOneBinding> implements PopupWindow.OnDismissListener {
    private static final String TAG = "RegisterStepOneActivity";

    private PopupWindow commonPop;

    //用于保存拍照图片的uri
    private Uri mCameraUri;

    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;

    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    private static final int CAMERA_REQUEST_CODE = 1001;
    private static final int RC_CHOOSE_PHOTO = 1002;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register_step_one;
    }

    @Override
    protected void processLogic() {

    }

    //营业执照
    private String businessLicenseImg = "";

    @Override
    protected void setListener() {
        binding.ibtnTitleBarBack.setOnClickListener(mNoDoubleClickListener);
        binding.tvNextStep.setOnClickListener(mNoDoubleClickListener);
        binding.ivHead.setOnClickListener(mNoDoubleClickListener);
        binding.tvLogin.setOnClickListener(mNoDoubleClickListener);
        binding.flAddCompanyFile.setOnClickListener(mNoDoubleClickListener);
        binding.etSetUpDate.setOnClickListener(mNoDoubleClickListener);

    }

    private NoDoubleClickListener mNoDoubleClickListener = new NoDoubleClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            switch (view.getId()) {
                case R.id.ibtn_title_bar_back:
                    finish();
                    break;
                case R.id.tv_next_step:
                    gotoNextStep();
                    break;
                case R.id.iv_head:
                    showPicDialog(view);
                    break;
                case R.id.tv_login:
                    ActivityUtils.startActivity(RegisterStepOneActivity.this, GovassLoginActivity.class);
                    break;
                case R.id.fl_add_company_file:
                    showPicDialog(view);
                    break;
                case R.id.et_setUpDate:
                    showCalendarSelectDialog();
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
    };

    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
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
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建保存图片的文件
     */
    private File createImageFile() throws IOException {
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


    private void gotoNextStep() {
        if (TextUtils.isEmpty(getStringByUI(binding.etCompanyName))) {
            ToastUtils.showToast(getContext().getString(R.string.message_must));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etCompanyCode))) {
            ToastUtils.showToast(getContext().getString(R.string.message_must));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etBusinessType))) {
            ToastUtils.showToast(getContext().getString(R.string.message_must));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etBusinessTerm))) {
            ToastUtils.showToast(getContext().getString(R.string.message_must));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etBusinessScope))) {
            ToastUtils.showToast(getContext().getString(R.string.message_must));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etLegalRepresentative))) {
            ToastUtils.showToast(getContext().getString(R.string.message_must));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etRegisteredCapital))) {
            ToastUtils.showToast(getContext().getString(R.string.message_must));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etComAddress))) {
            ToastUtils.showToast(getContext().getString(R.string.message_must));
            return;
        }

        if (TextUtils.isEmpty(getStringByUI(binding.etSetUpDate))) {
            ToastUtils.showToast(getContext().getString(R.string.message_must));
            return;
        }

        RegisterReqVo registerReqVo = new RegisterReqVo();
        registerReqVo.setEnterpriseName(getStringByUI(binding.etCompanyName));
        registerReqVo.setAddress(getStringByUI(binding.etComAddress));
        registerReqVo.setBusinessLicenseImg(businessLicenseImg);
        registerReqVo.setBusinessScope(getStringByUI(binding.etBusinessScope));
        registerReqVo.setBusinessTerm(getStringByUI(binding.etBusinessTerm));
        registerReqVo.setBusinessType(getStringByUI(binding.etBusinessType));
        registerReqVo.setEnterpriseCode(getStringByUI(binding.etCompanyCode));
        registerReqVo.setLegalRepresentative(getStringByUI(binding.etLegalRepresentative));
        registerReqVo.setRegisteredCapital(getStringByUI(binding.etRegisteredCapital));
        registerReqVo.setSetUpDate(getStringByUI(binding.etSetUpDate));
        registerReqVo.setBusinessLicenseImg("https://csdnimg.cn/cdn/content-toolbar/csdn-logo.png?v=20200416.1");
        Bundle bundle = new Bundle();
        bundle.putSerializable("registerReqVo", registerReqVo);
        ActivityUtils.startActivityWithBundle(this, RegisterStepTwoActivity.class, bundle);
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

    private void showCalendarSelectDialog() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                binding.etSetUpDate.setText(new StringBuilder()
                        .append(year)
                        .append("-")
                        .append((month + 1) < 10 ? "0"
                                + (month + 1) : (month + 1))
                        .append("-")
                        .append((day < 10) ? "0" + day : day));
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();

    }

    private void initPopView(View view) {
        TextView cameraTv = view.findViewById(R.id.btn_camera);
        TextView photoTv = view.findViewById(R.id.btn_photo);
        TextView cancelTv = view.findViewById(R.id.btn_cancel);
        cameraTv.setOnClickListener(mNoDoubleClickListener);
        photoTv.setOnClickListener(mNoDoubleClickListener);
        cancelTv.setOnClickListener(mNoDoubleClickListener);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (commonPop != null)
            commonPop.dismiss();
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (isAndroidQ) {
                    // Android 10 使用图片uri加载
                    binding.ivPreview.setVisibility(View.VISIBLE);
                    binding.ivAdd.setVisibility(View.GONE);
                    binding.ivPreview.setImageURI(mCameraUri);
                    uploadPic(CommonUtils.uriToFile(mCameraUri, this));
                    LogUtils.d("photoUri===", mCameraUri.toString());
                } else {
                    // 使用图片路径加载
                    binding.ivPreview.setVisibility(View.VISIBLE);
                    binding.ivAdd.setVisibility(View.GONE);
                    binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                    File file = new File(mCameraImagePath);
                    uploadPic(file);
                }
            } else {
                ToastUtils.showToast("取消");
            }
        } else if (requestCode == RC_CHOOSE_PHOTO) {
            Uri uri = data.getData();
            String filePath = FileUtils.getFilePathByUri(this, uri);

            if (!TextUtils.isEmpty(filePath)) {
                RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                //将照片显示在 ivImage上
                binding.ivPreview.setVisibility(View.VISIBLE);
                binding.ivAdd.setVisibility(View.GONE);
                Glide.with(this).load(filePath).apply(requestOptions1).into(binding.ivPreview);
            }
        }
    }

    private void uploadPic(File file) {
        CommonUtils.imageUpload(file);
    }


    @Override
    public void onClick(View view) {

    }
}
