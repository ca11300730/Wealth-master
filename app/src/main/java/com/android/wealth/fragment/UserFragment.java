package com.android.wealth.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.wealth.R;
import com.android.wealth.activity.AboutUsActivity;
import com.android.wealth.activity.LoginActivity;
import com.android.wealth.activity.StatisticsActivity;
import com.android.wealth.activity.UserDetailActivity;
import com.android.wealth.base.ViewBindingFragment;
import com.android.wealth.bean.User;
import com.android.wealth.constants.Constant;
import com.android.wealth.data.UserManager;
import com.android.wealth.database.UserDBHelper;
import com.android.wealth.databinding.FragmentUserBinding;
import com.android.wealth.utils.FileUtil;
import com.android.wealth.utils.imageloader.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.io.File;

public class UserFragment extends ViewBindingFragment<FragmentUserBinding> implements View.OnClickListener {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 设置点击事件
        setOnClickListener();

        // 设置背景
        setLogoutBg();

        // 注册登录广播
        registerReceiver();

    }

    // 设置点击事件
    private void setOnClickListener() {
        getViewBinding().loginLayout.setOnClickListener(this);
        getViewBinding().aboutUs.setOnClickListener(this);
        getViewBinding().logout.setOnClickListener(this);
        getViewBinding().clearCache.setOnClickListener(this);
        getViewBinding().statistics.setOnClickListener(this);
    }

    // 设置背景
    private void setLogoutBg() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor("#F6F6F6"));
        drawable.setCornerRadius(DensityUtil.dp2px(22));
        getViewBinding().logout.setBackground(drawable);
    }

    // 点击处理
    @Override
    public void onClick(View v) {
        if (v == getViewBinding().loginLayout){
            if (UserManager.getUserManager().isLogin()){
                UserDetailActivity.toDetail(UserManager.getUserManager().getUserId(), getActivity());
            }else {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }

        }else if (v == getViewBinding().aboutUs){
            startActivity(new Intent(getContext(), AboutUsActivity.class));
        }else if (v == getViewBinding().logout){
            showToast("退出登录成功");
            UserManager.getUserManager().logout();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }else if (v == getViewBinding().clearCache){
            clearCache();
        }else if (v == getViewBinding().statistics){
            startActivity(new Intent(getContext(), StatisticsActivity.class));
        }
    }

    // 清理缓存
    private void clearCache() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("清理缓存");

        long size = FileUtil.getFileSize(new File(getContext().getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR));
        String sizeString = FileUtil.FormetFileSize(size) + "";
        StringBuilder s = new StringBuilder();
        s.append("缓存大小为").append(sizeString).append(",")
                .append("确定要清理缓存吗").append("?");
        builder.setMessage(s);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Glide.get(getActivity()).clearMemory();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(getActivity()).clearDiskCache();
                        FileUtil.trimCache(getActivity());
                    }
                }).start();
                showToast("缓存清理成功");
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // 页面可见
    @Override
    public void onUserVisible() {
        super.onUserVisible();
        boolean login = UserManager.getUserManager().isLogin();
        if (login){
            showLoginUi();
        }else {
            showUnLoginUi();
        }
    }

    // 显示未登录UI
    private void showUnLoginUi() {
        getViewBinding().name.setText("未登录");
        getViewBinding().logout.setVisibility(View.GONE);
    }

    // 显示登录UI
    private void showLoginUi() {
        String userId = UserManager.getUserManager().getUserId();
        User user = UserDBHelper.getInstance().queryUserById(userId);
        ImageLoader.loadCircleImage(getContext(),user.avatar,getViewBinding().ivAvatar, R.drawable.default_avatar);
        getViewBinding().name.setText(user.userName);
        getViewBinding().logout.setVisibility(View.VISIBLE);
    }


    // 登录广播监听
    private final BroadcastReceiver mLoginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 登录成功广播
            if (TextUtils.equals(intent.getAction(), Constant.LOGIN_ACTION)){
                showLoginUi();
            }
        }
    };

    // 注册登录广播
    private void registerReceiver() {
        if (getActivity() != null){
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constant.LOGIN_ACTION);
            //注册
            getActivity().registerReceiver(mLoginReceiver,intentFilter);
        }
    }

    // 取消广播注册
    private void unregisterReceiver() {
        if (getActivity() != null){
            getActivity().unregisterReceiver(mLoginReceiver);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 取消广播注册
        unregisterReceiver();
    }
}
