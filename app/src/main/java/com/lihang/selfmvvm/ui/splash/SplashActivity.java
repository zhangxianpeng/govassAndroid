package com.lihang.selfmvvm.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.databinding.ActivitySplashBinding;
import com.lihang.selfmvvm.ui.main.BottonNavigationActivity;

/**
 * created by zhangxianpeng
 * 引导页面
 */
public class SplashActivity extends BaseActivity<SplashViewModel, ActivitySplashBinding> {

    private final int SPLASH_DISPLAY_LENGHT = 3000;
    private Handler handler;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void processLogic() {
        startMain();
    }

    private void startMain() {
        handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, BottonNavigationActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }, SPLASH_DISPLAY_LENGHT);
    }

    @Override
    protected void setListener() {

    }


    @Override
    public void onClick(View view) {

    }
}
