package com.lihang.selfmvvm.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.lihang.selfmvvm.R;

import java.util.List;

public class MyViewFlipper extends ViewFlipper {
    private Context mContext;
    //是否设置动画
    private boolean isSetAnimDuration = false;
    //设置数据展示时间
    private int interval = 3000;
    /**
     * 动画时间
     */
    private int animDuration = 300;

    public MyViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        setFlipInterval(interval);
        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.push_up_in);
        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);
        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.push_up_out);
        if (isSetAnimDuration)
            animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    /**
     * 设置循环滚动的View数组
     *
     * @param views
     */
    public void setViews(List<ImageView> views) {
        if (views == null || views.size() == 0)
            return;
        removeAllViews();
        for (int i = 0; i < views.size(); i++) {
            addView(views.get(i));
        }
        if (views.size() > 1) {
            startFlipping();
        } else {
            stopFlipping();
        }
    }
}

