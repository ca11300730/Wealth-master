package com.android.wealth.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.wealth.BuildConfig;
import com.android.wealth.base.ViewBindingActivity;
import com.android.wealth.bean.User;
import com.android.wealth.constants.Constant;
import com.android.wealth.data.UserManager;
import com.android.wealth.database.UserDBHelper;
import com.android.wealth.databinding.ActivityLoginBinding;
import com.android.wealth.utils.Code;

/**
 * 登录页面
 */
public class LoginActivity extends ViewBindingActivity<ActivityLoginBinding> implements View.OnClickListener {

    private String realCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    // 初始化View
    private void initViews() {
        setTitle("登录");

        // 设置点击事件
        getViewBinding().loginBtn.setOnClickListener(this);
        getViewBinding().register.setOnClickListener(this);

        showCode();

        getViewBinding().ivCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCode();
            }
        });
    }

    private void showCode(){
        getViewBinding().ivCode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }

    // 点击处理
    @Override
    public void onClick(View v) {
        if (v == getViewBinding().loginBtn ){
            if (!validate()){
                return;
            }
            // 登录
            String username = getViewBinding().etUsername.getText().toString();
            String password = getViewBinding().etPassword.getText().toString();
            if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                showToast("用户名或密码不能为空");
                return;
            }
            String code = getViewBinding().etCode.getText().toString();
            if (TextUtils.isEmpty(code)){
                showToast("验证码不能为空");
            }
            if (!TextUtils.equals(code.toLowerCase(),realCode)){
                showToast("验证码错误");
                showCode();
                getViewBinding().etCode.getText().clear();
                return;
            }
            // 查询数据库中是否存在当前用户
            User user = UserDBHelper.getInstance().queryUser(username, password);
            if (user != null){
                UserManager.getUserManager().login(username,user.userId);
                showToast("登录成功");
                // 发送登录成功广播
                Intent intent = new Intent(Constant.LOGIN_ACTION);
                intent.setPackage(BuildConfig.APPLICATION_ID);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                intent.setClass(this, MainActivity.class);

                startActivity(intent);
                finish();
            }else {
                showToast("请输入正确的用户名或密码");
            }
        }else if (v == getViewBinding().register ){
            // 跳转注册
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    // 登录校验
    private boolean validate() {
        boolean valid = true;
        String username = getViewBinding().etUsername.getText().toString();
        String password = getViewBinding().etPassword.getText().toString();

        if (username.isEmpty()) {
            getViewBinding().etUsername.setError("用户名不能为空");
            valid = false;
        }
        if (password.isEmpty()) {
            getViewBinding().etPassword.setError("密码不能为空");
            valid = false;
        }
        return valid;
    }
}