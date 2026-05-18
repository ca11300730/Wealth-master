package com.android.wealth.base;

import androidx.appcompat.app.AppCompatActivity;

/**
 * BaseActivity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    // 初始化view
    protected void initViews() {

    }


}
