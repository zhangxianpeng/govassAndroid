/**
 * Copyright (C), 2015-2020, XXX有限公司
 * FileName: BadgeView
 * Author: zhang
 * Date: 2020/7/5 13:18
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.lihang.selfmvvm.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lihang.selfmvvm.R;

import androidx.annotation.Nullable;

/**
 * @ClassName: BadgeView
 * @Description: java类作用描述
 * @Author: zhang
 * @Date: 2020/7/5 13:18
 */
public class BadgeView extends View {

    private Paint roundRectPaint;
    private RectF roundRectF;
    private int height;
    private int roundWidth;

    private Paint textPaint;
    private float textSize;
    private String text;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    public BadgeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        text = "2";
        roundRectPaint = new Paint();
        roundRectPaint.setStyle(Paint.Style.FILL);
        roundRectPaint.setAntiAlias(true);
        roundRectPaint.setColor(Color.RED);
        roundRectF = new RectF();

        //设置字体为粗体
        textPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        //实现抗锯齿
        
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取控件高度
        height = MeasureSpec.getSize(heightMeasureSpec);
        textSize = height - getResources().getDimensionPixelSize(R.dimen.dp_6);
        textPaint.setTextSize(textSize);
        //获取文本宽度
        int textWidth = (int) textPaint.measureText(text);
        //区分画圆的是圆形还是圆角矩形
        roundWidth = height;
//        if (text.length() > 1) {
//            roundWidth = textWidth + height - textWidth / text.length();
//        } else {
//            roundWidth = height;
//        }
        //重新测量控件
        setMeasuredDimension(roundWidth, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画矩形
        canvas.setDrawFilter(paintFlagsDrawFilter);
        roundRectF.set(0, 0, roundWidth, height);
        canvas.drawRoundRect(roundRectF, height / 2, height / 2, roundRectPaint);

        //画字
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLintY = (int) (roundRectF.centerY() - top / 2 - bottom / 2);
        canvas.drawText(text, roundRectF.centerX(), baseLintY, textPaint);
    }

    public void setText(String text) {
        this.text = text;
        //重走onMeasure
        requestLayout();
    }

}
