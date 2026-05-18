package com.android.wealth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.android.wealth.base.ViewBindingActivity;
import com.android.wealth.data.UserManager;
import com.android.wealth.databinding.ActivitySplashBinding;

import java.util.Timer;
import java.util.TimerTask;

/**
 *  Splash 页面
 */
public class SplashActivity extends ViewBindingActivity<ActivitySplashBinding> implements View.OnClickListener {

    private static final String TAG = "SplashActivity";

    //显示倒计时的工具类
    private final Timer mTimer = new Timer();
    private final TimerTask mTimerTask = new DingTimerTask();
    private final int TIMER_COUNT = 3;
    private int mTimerCount = TIMER_COUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 状态栏处理
        View decorView = getWindow().getDecorView();
        decorView.post(() -> {
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        });
        getViewBinding().getRoot().post(new Runnable() {
            @Override
            public void run() {
                // 倒计时
                mTimer.scheduleAtFixedRate(mTimerTask, 500, 1000);
            }
        });
    }
    /**
     * 倒计时的Timer
     */
    public class DingTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mTimerCount == TIMER_COUNT) {
                        getViewBinding().ding.setVisibility(View.VISIBLE);
                        getViewBinding().ding.setOnClickListener(SplashActivity.this);
                    }

                    if (mTimerCount != 0) {
                        getViewBinding().ding.setText(String.format("跳过 %s", mTimerCount));
                        mTimerCount--;
                    } else {
                        goMain();
                    }
                }
            });
        }
    }
    /**
     * 跳转首页
     */
    private void goMain() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        Intent intent = new Intent();
        if (UserManager.getUserManager().isLogin()){
            intent.setClass(this, MainActivity.class);
        }else {
            intent.setClass(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        if (v == getViewBinding().ding){
            goMain();
        }
    }

    // 返回键拦截
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
    // 取消倒计时
    private void cancelTime(){
        mTimerTask.cancel();
        mTimer.cancel();
    }

    @Override
    public void finish() {
        super.finish();
        cancelTime();
    }
}
