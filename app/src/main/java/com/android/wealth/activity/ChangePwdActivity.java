package com.android.wealth.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.android.wealth.base.ViewBindingActivity;
import com.android.wealth.bean.User;
import com.android.wealth.database.UserDBHelper;
import com.android.wealth.databinding.ActivityChangePwdBinding;


public class ChangePwdActivity extends ViewBindingActivity<ActivityChangePwdBinding> {

    public static void toDetail(String userId, Context context){
        Intent intent = new Intent(context,ChangePwdActivity.class);
        intent.putExtra("userId",userId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("密码修改");

        getViewBinding().save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePwd();
            }
        });

    }
    private void changePwd() {
        String oldPwd = getViewBinding().oldPwd.getText().toString().trim();
        String newPwd = getViewBinding().newPwd.getText().toString().trim();

        if (TextUtils.isEmpty(oldPwd)){
            showToast("请输入旧密码");
            return;
        }
        if (TextUtils.isEmpty(newPwd)){
            showToast("请输入新密码");
            return;
        }
        String userId = getIntent().getStringExtra("userId");

        User user = UserDBHelper.getInstance().queryUserById(userId);
        if (!TextUtils.equals(oldPwd,user.passWord)){
            showToast("密码输入错误，请重新输入");
            return;
        }
        UserDBHelper.getInstance().changePwd(userId,newPwd);
        showToast("修改成功");
        finish();
    }
}