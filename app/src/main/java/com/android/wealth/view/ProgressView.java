package com.android.wealth.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;

import com.android.wealth.R;

public class ProgressView extends View {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mColor1, mColor2;

    private float mProgress;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mColor1 = context.getResources().getColor(R.color.common_color);
        mColor2 = Color.parseColor("#FC3C3E");
    }

    public void setProgress(@FloatRange(from = 0, to = 1) float progress) {
        mProgress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mColor1);
        canvas.drawRoundRect(0, 0, getWidth(), getHeight(),
                getHeight() / 2f, getHeight() / 2f, mPaint);

        mPaint.setColor(mColor2);
        canvas.drawRoundRect(0, 0, mProgress * getWidth(), getHeight(),
                getHeight() / 2f, getHeight() / 2f, mPaint);

        if (mProgress != 0 && mProgress != 1) {
            mPaint.setColor(Color.WHITE);
            float interval = getHeight() / 2f;
            float progress = mProgress * getWidth();
            canvas.drawRect(progress - interval / 2f, 0, progress + interval / 2f, getHeight(), mPaint);
        }
    }

}
