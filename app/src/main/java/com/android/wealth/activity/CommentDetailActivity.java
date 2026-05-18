package com.android.wealth.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.android.wealth.R;
import com.android.wealth.adapter.CommentAdapter;
import com.android.wealth.base.ViewBindingActivity;
import com.android.wealth.bean.Comment;
import com.android.wealth.bean.User;
import com.android.wealth.data.UserManager;
import com.android.wealth.database.CommunityDBHelper;
import com.android.wealth.database.UserDBHelper;
import com.android.wealth.databinding.ActivityCommentDetailBinding;
import com.android.wealth.databinding.IncludeEmptyLayoutBinding;
import com.android.wealth.utils.TimeUtils;
import com.android.wealth.utils.imageloader.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommentDetailActivity extends ViewBindingActivity<ActivityCommentDetailBinding> {

    public static final String ID = "ID";
    public static final String USER_ID = "userId";

    CommentAdapter commentAdapter;

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ActivityResultLauncher<Intent> commentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userId = getIntent().getStringExtra(USER_ID);
        String commentId = getIntent().getStringExtra(ID);

        getViewBinding().back.setOnClickListener(v -> finish());

        Comment comment = CommunityDBHelper.getInstance().queryComment(userId, commentId);

        getViewBinding().tvName.setText(comment.userName);
        getViewBinding().tvContent.setText(comment.content);
        getViewBinding().tvTime.setText(String.format("发表于：%s", formatter.format(comment.createTime)));

        User user = UserDBHelper.getInstance().queryUserById(userId);
        if (user != null){
            ImageLoader.loadCircleImage(this,user.avatar,getViewBinding().ivAvatar, R.drawable.default_avatar);
        }else {
            getViewBinding().ivAvatar.setImageResource(R.drawable.default_avatar);
        }


        commentAdapter = new CommentAdapter(this, false);

        getViewBinding().list.setAdapter(commentAdapter);

        IncludeEmptyLayoutBinding binding = IncludeEmptyLayoutBinding.inflate(getLayoutInflater());
        binding.text.setText("暂无评论");
        commentAdapter.setEmptyView(binding.getRoot());


        List<Comment> list = CommunityDBHelper.getInstance().queryCommentList(commentId);
        commentAdapter.setNewData(list);

        commentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                if (result.getResultCode() == Activity.RESULT_OK && data != null) {
                    String commentData = data.getStringExtra(PostCommentActivity.DATA_KEY);

                    String userId = UserManager.getUserManager().getUserId();
                    String userName = UserManager.getUserManager().getUserName();
                    CommunityDBHelper.getInstance().addComment(userId, userName, commentData, commentId, list.size() + 1);

                    Comment comment = new Comment();
                    comment.userId = userId;
                    comment.content = commentData;
                    comment.userName = userName;
                    comment.createTime = System.currentTimeMillis();

                    commentAdapter.addData(0,comment);
                    getViewBinding().list.scrollToPosition(0);

                }
            }
        });

        getViewBinding().toAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentDetailActivity.this, PostCommentActivity.class);
                commentLauncher.launch(intent);
            }
        });

    }
}