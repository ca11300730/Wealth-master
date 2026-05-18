package com.android.wealth.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.wealth.base.ViewBindingActivity;
import com.android.wealth.database.UserDBHelper;
import com.android.wealth.databinding.ActivityRegisterBinding;

/**
 * 注册页
 */
public class RegisterActivity extends ViewBindingActivity<ActivityRegisterBinding> implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    // 初始化View
    private void initViews() {
        setTitle("注册");

        // 设置点击事件
        getViewBinding().login.setOnClickListener(this);
        getViewBinding().register.setOnClickListener(this);

    }

    // 点击事件
    @Override
    public void onClick(View v) {
        if (v == getViewBinding().login ){
            login();
        }else if (v == getViewBinding().register ){
            register();
        }
    }

    // 注册
    private void register() {
        if (!validate()){
            return;
        }
        // 注册
        String username = getViewBinding().etUsername.getText().toString();
        String password = getViewBinding().etPassword.getText().toString();
        // 查询数据库中是否存在当前用户
        if (UserDBHelper.getInstance().queryUser(username)){
            showToast("用户名已存在");
            return;
        }
        // 向数据库中插入用户
        if (!UserDBHelper.getInstance().addUser(username,password)){
            showToast("注册失败,请重试");
            return;
        }
        showToast("注册成功");
        login();
    }

    // 注册参数校验
    private boolean validate() {
        boolean valid = true;
        String username = getViewBinding().etUsername.getText().toString();
        String password = getViewBinding().etPassword.getText().toString();
        String rePassword = getViewBinding().etRepassword.getText().toString();

        if (username.isEmpty()) {
            showToast("用户名不能为空");
            valid = false;
        }
        if (password.isEmpty()) {
            showToast("密码不能为空");
            valid = false;
        }
        if (rePassword.isEmpty()) {
            showToast("确认密码不能为空");
            valid = false;
        }
        if (!password.equals(rePassword)) {
            showToast("两次密码不相同");
            valid = false;
        }
        return valid;
    }

    // 跳转登录
    private void login(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}