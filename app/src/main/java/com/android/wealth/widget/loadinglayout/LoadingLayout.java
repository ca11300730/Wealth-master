package com.android.wealth.widget.loadinglayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.wealth.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LoadingLayout extends FrameLayout {

    /**
     * 空数据视图
     */
    private View mEmptyView;
    /**
     * 加载出错视图
     */
    private View mRetryView;
    /**
     * 无网络视图
     */
    private View mNetErrorView;
    /**
     * 加载中视图
     */
    private View mLoadingView;

    /**
     * 空视图布局id
     */
    private int mEmptyResource;
    /**
     * 加载出错视图布局id
     */
    private int mRetryResource;
    /**
     * 无网络视图布局id
     */
    private int mNetErrorResource;
    /**
     * 加载中视图布局id
     */
    private int mLoadingResource;

    private OnRetryClickListener mRetryClickListener;
    // 加载中...
    public static final int STATUS_LOADING = 1;
    // 网络错误...
    public static final int STATUS_NET_ERROR = 2;
    // 无数据...
    public static final int STATUS_NO_DATA = 3;
    // 数据出错...
    public static final int STATUS_DATA_ERROR = 4;
    // 内容
    public static final int STATUS_CONTENT = 5;

    LayoutInflater mLayoutInflater;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_LOADING, STATUS_NET_ERROR, STATUS_NO_DATA,STATUS_DATA_ERROR,STATUS_CONTENT})
    private  @interface LoadingStatus{}
    private int mCurrentStatus = -1;
    public LoadingLayout(@NonNull Context context) {
        this(context, null);
    }

    public LoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout);
        mEmptyResource = a.getResourceId(R.styleable.LoadingLayout_emptyResource, 0);
        mRetryResource = a.getResourceId(R.styleable.LoadingLayout_retryResource, 0);
        mLoadingResource = a.getResourceId(R.styleable.LoadingLayout_loadingResource, 0);
        mNetErrorResource = a.getResourceId(R.styleable.LoadingLayout_netErrorResource, 0);
        a.recycle();
        mLayoutInflater = LayoutInflater.from(getContext());
        if (mEmptyResource == 0){
            mEmptyResource = R.layout.page_empty;
        }
        if (mRetryResource == 0){
            mRetryResource = R.layout.page_net_error;
        }
        if (mLoadingResource == 0) {
            mLoadingResource = R.layout.page_loading;
        }
        if (mNetErrorResource == 0) {
            mNetErrorResource = R.layout.page_net_error;
        }
        setWillNotDraw(true);
    }
    private void changeStatus(@LoadingStatus int loadingStatus){
       // setAllViewGone();
        switch (loadingStatus){
            case STATUS_LOADING:
                if (mCurrentStatus == STATUS_LOADING){
                    return;
                }
                mCurrentStatus = STATUS_LOADING;
                setVisibility(mLoadingView,VISIBLE);
                setVisibility(mNetErrorView,GONE);
                setVisibility(mEmptyView,GONE);
                setVisibility(mRetryView,GONE);
                break;
            case STATUS_NET_ERROR:
                if (mCurrentStatus == STATUS_NET_ERROR){
                    return;
                }
                mCurrentStatus = STATUS_NET_ERROR;
                setVisibility(mLoadingView,GONE);
                setVisibility(mNetErrorView,VISIBLE);
                setVisibility(mEmptyView,GONE);
                setVisibility(mRetryView,GONE);
                break;
            case STATUS_NO_DATA:
                if (mCurrentStatus == STATUS_NO_DATA){
                    return;
                }
                mCurrentStatus = STATUS_NO_DATA;
                setVisibility(mLoadingView,GONE);
                setVisibility(mNetErrorView,GONE);
                setVisibility(mEmptyView,VISIBLE);
                setVisibility(mRetryView,GONE);
                break;
            case STATUS_DATA_ERROR:
                if (mCurrentStatus == STATUS_DATA_ERROR){
                    return;
                }
                mCurrentStatus = STATUS_DATA_ERROR;
                setVisibility(mLoadingView,GONE);
                setVisibility(mNetErrorView,GONE);
                setVisibility(mEmptyView,GONE);
                setVisibility(mRetryView,VISIBLE);
                break;
        }
    }

    /**
     * 显示Loading界面
     */
    public void showLoading() {
        if (mLoadingView==null){
            mLoadingView =mLayoutInflater.inflate(mLoadingResource,null,false);
            addView(mLoadingView);
        }
        changeStatus(STATUS_LOADING);
    }
    /**
     * 显示空数据界面
     */
    public void showEmpty() {
        if (mEmptyView==null){
            mEmptyView =mLayoutInflater.inflate(mEmptyResource,null,false);
            addView(mEmptyView);
        }
        changeStatus(STATUS_NO_DATA);
    }
    /**
     * 显示网络错误界面
     */
    public void showNetError() {
        if (mNetErrorView==null){
            mNetErrorView =mLayoutInflater.inflate(mNetErrorResource,null,false);
            mNetErrorView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRetryClickListener != null){
                        showLoading();
                        mRetryClickListener.onRetryClick();
                    }
                }
            });
            addView(mNetErrorView);
        }
        changeStatus(STATUS_NET_ERROR);
    }
    /**
     * 显示数据出错界面
     */
    public void showDataError() {
        if (mRetryView==null){
            mRetryView =mLayoutInflater.inflate(mRetryResource,null,false);
            mRetryView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRetryClickListener != null){
                        showLoading();
                        mRetryClickListener.onRetryClick();
                    }
                }
            });
            addView(mRetryView);
        }
        changeStatus(STATUS_DATA_ERROR);
    }
    /**
     * 显示内容界面
     */
    public void showContentView() {
        setAllViewGone();
        mCurrentStatus = STATUS_CONTENT;
    }
    private void setVisibility(View view, int visibility){
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        showLoading();
    }

    private void setAllViewGone(){
        if (mLoadingView != null) {
            mLoadingView.setVisibility(GONE);
        }
        if (mNetErrorView != null) {
            mNetErrorView.setVisibility(GONE);
        }
        if (mRetryView != null) {
            mRetryView.setVisibility(GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(GONE);
        }
    }
    /**
     * 点击重试监听器
     * @param listener {@link OnRetryClickListener}
     */
    public void setOnRetryClickListener(OnRetryClickListener listener){
        this.mRetryClickListener = listener;
    }

    public interface OnRetryClickListener{
        void onRetryClick();
    }
}
