package com.lihang.selfmvvm.ui.register;

import android.app.DatePickerDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.PopupWindow;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityRegisterStepOneBinding;
import com.lihang.selfmvvm.ui.login.GovassLoginActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.lihang.selfmvvm.utils.NoDoubleClickListener;
import com.lihang.selfmvvm.utils.ToastUtils;
import com.lihang.selfmvvm.vo.req.RegisterReqVo;

import java.util.Calendar;


public class RegisterStepOneActivity extends BaseActivity<RegisterStepOneViewModel, ActivityRegisterStepOneBinding> implements PopupWindow.OnDismissListener {
    private static final String TAG = "RegisterStepOneActivity";

    private PopupWindow commonPop;

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
                default:
                    break;
            }
        }
    };

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

        if(TextUtils.isEmpty(getStringByUI(binding.etSetUpDate))) {
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

    private void initPopView(View contentView) {

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
    public void onClick(View view) {

    }
}
