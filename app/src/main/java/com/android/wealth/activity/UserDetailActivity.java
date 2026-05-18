package com.android.wealth.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.android.wealth.R;
import com.android.wealth.base.ViewBindingActivity;
import com.android.wealth.bean.User;
import com.android.wealth.database.UserDBHelper;
import com.android.wealth.databinding.ActivityUserDetailBinding;
import com.android.wealth.utils.imageloader.GlideEngine;
import com.android.wealth.utils.imageloader.ImageLoader;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class UserDetailActivity extends ViewBindingActivity<ActivityUserDetailBinding> implements View.OnClickListener
        ,EasyPermissions.PermissionCallbacks{


    private String userId;

    public static void toDetail(String userId, Context context){
        Intent intent = new Intent(context,UserDetailActivity.class);
        intent.putExtra("userId",userId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("个人信息");
        userId = getIntent().getStringExtra("userId");

        getViewBinding().changeAvatar.setOnClickListener(this);
        getViewBinding().changePwd.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        showView();
    }


    private void showView() {
        User user = UserDBHelper.getInstance().queryUserById(userId);
        getViewBinding().userName.setText(user.userName);
        ImageLoader.loadCircleImage(this,user.avatar,getViewBinding().avatar, R.drawable.default_avatar);
    }


    @Override
    public void onClick(View v) {
        if (v == getViewBinding().changeAvatar){
            checkPermissions();
        } else if (v == getViewBinding().changePwd){
            ChangePwdActivity.toDetail(userId,this);
        }
    }

    private void checkPermissions() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        EasyPermissions.requestPermissions(this, "申请权限", 1, perms);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setMaxSelectNum(1)
                .setMinSelectNum(1)
                .setImageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        int size = result.size();
                        if (size > 0) {
                            LocalMedia localMedia = result.get(0);
                            String path = "";
                            if (!TextUtils.isEmpty(localMedia.getRealPath())) {
                                path = localMedia.getRealPath();
                            }
                            if (!TextUtils.isEmpty(localMedia.getCompressPath())) {
                                path = localMedia.getCompressPath();
                            }
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            getViewBinding().avatar.setImageBitmap(bitmap);

                            UserDBHelper.getInstance().updateUserAvatar(userId,path);

                            showToast("修改成功");
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }


    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // 一些权限被禁止
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}