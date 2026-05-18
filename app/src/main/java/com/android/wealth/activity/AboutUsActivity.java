package com.android.wealth.activity;

import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.android.wealth.R;
import com.android.wealth.base.ViewBindingActivity;
import com.android.wealth.databinding.ActivityAboutUsBinding;
import com.android.wealth.utils.StatusBarUtil;
import com.android.wealth.utils.interpolator.ElasticOutInterpolator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;


/**
 * 关于页面
 */
public class AboutUsActivity extends ViewBindingActivity<ActivityAboutUsBinding> {


    private View.OnClickListener mThemeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        initView();
    }

    // 初始化title
    protected void initToolbar() {
        setSupportActionBar(getViewBinding().aboutUsToolbar);
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, getViewBinding().aboutUsToolbar);
        getViewBinding().aboutUsToolbar.setNavigationOnClickListener(v -> finish());
    }

    // 初始化view
    protected void initView() {
        // 设置内容
        showAboutContent();
        setSmartRefreshLayout();

        getViewBinding().aboutUsToolbar.setTitle(String.format("关于%s",getString(R.string.app_name)));

        //进入界面时自动刷新
        getViewBinding().aboutUsRefreshLayout.autoRefresh();

        //点击悬浮按钮时自动刷新
        getViewBinding().aboutUsFab.setOnClickListener(v -> getViewBinding().aboutUsRefreshLayout.autoRefresh());

        //监听 AppBarLayout 的关闭和开启 给 FlyView（纸飞机） 和 ActionButton 设置关闭隐藏动画
        getViewBinding().aboutUsAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean misAppbarExpand = true;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = appBarLayout.getTotalScrollRange();
                float fraction = 1f * (scrollRange + verticalOffset) / scrollRange;
                double minFraction = 0.1;
                double maxFraction = 0.8;
                if (fraction < minFraction && misAppbarExpand) {
                    misAppbarExpand = false;
                    getViewBinding().aboutUsFab.animate().scaleX(0).scaleY(0);
                    getViewBinding().aboutUsFlyView.animate().scaleX(0).scaleY(0);
                    ValueAnimator animator = ValueAnimator.ofInt(getViewBinding().aboutUsContent.aboutUsContent.getPaddingTop(), 0);
                    animator.setDuration(300);
                    animator.addUpdateListener(animation -> {
                            getViewBinding().aboutUsContent.aboutUsContent.setPadding(0, (int) animation.getAnimatedValue(), 0, 0);
                    });
                    animator.start();
                }
                if (fraction > maxFraction && !misAppbarExpand) {
                    misAppbarExpand = true;
                    getViewBinding().aboutUsFab.animate().scaleX(1).scaleY(1);
                    getViewBinding().aboutUsFlyView.animate().scaleX(1).scaleY(1);
                    ValueAnimator animator = ValueAnimator.ofInt(getViewBinding().aboutUsContent.aboutUsContent.getPaddingTop(), DensityUtil.dp2px(25));
                    animator.setDuration(300);
                    animator.addUpdateListener(animation -> {
                            getViewBinding().aboutUsContent.aboutUsContent.setPadding(0, (int) animation.getAnimatedValue(), 0, 0);
                    });
                    animator.start();
                }
            }
        });
    }

    private void setSmartRefreshLayout() {
        //绑定场景和纸飞机
        getViewBinding().aboutUsFlyRefresh.setUp(getViewBinding().aboutUsMountain, getViewBinding().aboutUsFlyView);
        getViewBinding().aboutUsRefreshLayout.setReboundInterpolator(new ElasticOutInterpolator());
        getViewBinding().aboutUsRefreshLayout.setReboundDuration(800);
        getViewBinding().aboutUsRefreshLayout.setOnRefreshListener(refreshLayout -> {
            updateTheme();
            refreshLayout.finishRefresh(1000);
        });

        //设置让Toolbar和AppBarLayout的滚动同步
        getViewBinding().aboutUsRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                refreshLayout.finishRefresh(2000);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                refreshLayout.finishLoadMore(3000);
            }

            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                super.onHeaderMoving(header, isDragging, percent, offset, headerHeight, maxDragHeight);
                getViewBinding().aboutUsAppBar.setTranslationY(offset);
                getViewBinding().aboutUsToolbar.setTranslationY(-offset);
            }
        });
    }

    private void showAboutContent() {
        getViewBinding().aboutUsContent.aboutContent.setText(getString(R.string.about_content));
        getViewBinding().aboutUsContent.aboutContent.setMovementMethod(LinkMovementMethod.getInstance());
        try {
            String versionStr = getString(R.string.app_name)
                    + " V" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            getViewBinding().aboutUsContent.aboutVersion.setText(versionStr);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Glide.with(this).load(R.mipmap.ic_launcher).transform(new RoundedCorners(DensityUtil.dp2px(20))).into(getViewBinding().aboutUsContent.icon);
    }

    /**
     * Update appbar theme
     */
    private void updateTheme() {
        if (mThemeListener == null) {
            mThemeListener = new View.OnClickListener() {
                int index = 0;
                int[] ids = new int[]{
                        R.color.colorPrimary,
                        android.R.color.holo_green_light,
                        android.R.color.holo_red_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_blue_bright,
                };

                @Override
                public void onClick(View v) {
                    int color = ContextCompat.getColor(getApplication(), ids[index % ids.length]);
                    getViewBinding().aboutUsRefreshLayout.setPrimaryColors(color);
                    getViewBinding().aboutUsFab.setBackgroundColor(color);
                    getViewBinding().aboutUsFab.setBackgroundTintList(ColorStateList.valueOf(color));
                    getViewBinding().aboutUsToolbarLayout.setContentScrimColor(color);
                    index++;
                }
            };
        }
        mThemeListener.onClick(null);
    }

}
