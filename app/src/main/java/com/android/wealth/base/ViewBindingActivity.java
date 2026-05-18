package com.android.wealth.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;


import com.android.wealth.R;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * ViewBindingActivity 基类
 *
 * @param <T> 系统生成的页面布局ViewBinding泛型
 */
public abstract class ViewBindingActivity<T extends ViewBinding> extends AppCompatActivity {

    private T mViewBinding;

    private Toolbar toolbar;

    private ProgressDialog mProgressDialog = null;// loading

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Class<?> clazz = getViewBindingClass();
        // 反射处理
        try {
            Method[] methods = clazz.getMethods();
            for (Method m : methods) {
                Class<?>[] parameterTypes = m.getParameterTypes();
                if (parameterTypes.length == 1 && parameterTypes[0] == LayoutInflater.class) {
                    mViewBinding = (T) m.invoke(null, getLayoutInflater());
                    // 设置页面布局  setContentView
                    setContentView(mViewBinding.getRoot());

                    toolbar = findViewById(R.id.toolbar);
                    if (toolbar != null){
                        toolbar.setTitle("");
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        toolbar.setNavigationOnClickListener(v -> finish());
                    }
                    return;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    //  返回系统生成页面布局ViewBinding类
    protected final T getViewBinding() {
        return mViewBinding;
    }

    //  返回页面布局ViewBinding类class
    private Class<?> getViewBindingClass() {
        Class<?> clazz = this.getClass();
        while (clazz.getSuperclass() != ViewBindingActivity.class) {
            clazz = clazz.getSuperclass();
        }
        ParameterizedType type = (ParameterizedType) clazz.getGenericSuperclass();
        return (Class<?>) type.getActualTypeArguments()[0];
    }

    protected void setTitle(String title) {
        if (toolbar != null){
            toolbar.setTitle(title);
        }
    }

    public void showToast(CharSequence toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    public  void hideInput( View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 显示进度框
     */
    protected void showProgressDialog() {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage("正在搜索");
        mProgressDialog.show();
    }

    /**
     * 隐藏进度框
     */
    protected void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


}
