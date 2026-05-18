package com.android.wealth.widget.loadinglayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.wealth.R;

public class LoadingView2 extends FrameLayout {

    ProgressDrawable mProgressDrawable;

    private ImageView loading;

    public LoadingView2(Context context) {
        this(context, null);
    }

    public LoadingView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        mProgressDrawable = new ProgressDrawable();

        LayoutInflater.from(context).inflate(R.layout.loading_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        loading = findViewById(R.id.load);
        loading.setImageDrawable(mProgressDrawable);
        showLoading();
    }

    public void showLoading() {
        if (getVisibility() != View.VISIBLE)
            return;
        mProgressDrawable.start();
    }

    private void hideLoading() {
        mProgressDrawable.stop();
    }



    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility != VISIBLE){
            hideLoading();
        }else {
            showLoading();
        }
    }


}