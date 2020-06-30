package com.lihang.selfmvvm.ui.activity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.base.NormalViewModel;
import com.lihang.selfmvvm.databinding.ActivityWelcomBinding;
import com.lihang.selfmvvm.ui.main.BottonNavigationActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class WelComeActivity extends BaseActivity<NormalViewModel, ActivityWelcomBinding> {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_welcom;
    }

    @Override
    protected void processLogic() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha_welcome);
        binding.txt.startAnimation(animation);

        binding.imgSvg.setViewportSize(167, 220);
        binding.imgSvg.setTraceColor(getResources().getColor(R.color.white));
        binding.imgSvg.start();

        Observable.timer(2500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).compose(bindToLifecycle()).subscribe(aLong -> {
            ActivityUtils.startActivity(WelComeActivity.this, BottonNavigationActivity.class);
            ActivityUtils.finishWithAnim(WelComeActivity.this, 0, R.animator.set_anim_activity_exit);
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
