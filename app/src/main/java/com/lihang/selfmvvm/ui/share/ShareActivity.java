package com.lihang.selfmvvm.ui.share;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityShareBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;
import com.lihang.selfmvvm.utils.ToastUtils;

/**
 * 推广下载界面
 */
public class ShareActivity extends BaseActivity<ShareViewModel, ActivityShareBinding> {

    private String url = "https://www.pgyer.com/app/qrcode/0DXB";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_share;
    }

    @Override
    protected void processLogic() {
        Glide.with(this).load(url).placeholder(R.mipmap.default_img)
                .error(R.mipmap.default_img).into(binding.ivShareCode);
    }


    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
        binding.btnCopy.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.btn_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(url);
                ToastUtils.showToast("复制成功");
                break;
            default:
                break;
        }
    }
}
