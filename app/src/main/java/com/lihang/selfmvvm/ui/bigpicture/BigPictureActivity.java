package com.lihang.selfmvvm.ui.bigpicture;

import android.view.View;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivityBigPictureBinding;
import com.lihang.selfmvvm.utils.ButtonClickUtils;

import static com.lihang.selfmvvm.base.BaseConstant.DEFAULT_FILE_SERVER;
import static com.lihang.selfmvvm.common.SystemConst.DEFAULT_SERVER;

public class BigPictureActivity extends BaseActivity<BigPictureViewModel, ActivityBigPictureBinding> {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_big_picture;
    }

    @Override
    protected void processLogic() {
        String imgUrl = getIntent().getStringExtra("imgUrl");
        Glide.with(this).load(DEFAULT_SERVER + DEFAULT_FILE_SERVER + imgUrl).placeholder(R.mipmap.default_img)
                .error(R.mipmap.default_img).into(binding.ivBigPicture);
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
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
            default:
                break;
        }
    }
}
