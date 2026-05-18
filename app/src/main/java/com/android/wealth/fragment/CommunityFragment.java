package com.android.wealth.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.wealth.activity.CommentDetailActivity;
import com.android.wealth.activity.PostCommentActivity;
import com.android.wealth.adapter.CommentAdapter;
import com.android.wealth.base.ViewBindingFragment;
import com.android.wealth.bean.Comment;
import com.android.wealth.data.UserManager;
import com.android.wealth.database.CommunityDBHelper;
import com.android.wealth.databinding.FragCommunityBinding;
import com.android.wealth.databinding.IncludeEmptyLayoutBinding;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

public class CommunityFragment extends ViewBindingFragment<FragCommunityBinding> {

    CommentAdapter commentAdapter;

    private ActivityResultLauncher<Intent> commentLauncher;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        commentAdapter = new CommentAdapter(getContext());

        getViewBinding().list.setAdapter(commentAdapter);

        commentAdapter.setEmptyView(IncludeEmptyLayoutBinding.inflate(getLayoutInflater()).getRoot());


        commentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Comment comment = commentAdapter.getData().get(position);
                Intent intent = new Intent(getActivity(), CommentDetailActivity.class);
                intent.putExtra(CommentDetailActivity.ID,comment.commentId);
                intent.putExtra(CommentDetailActivity.USER_ID,comment.userId);
                startActivity(intent);
            }
        });


        commentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent data = result.getData();
            if (result.getResultCode() == Activity.RESULT_OK && data != null) {
                String comment = data.getStringExtra(PostCommentActivity.DATA_KEY);

                String userId = UserManager.getUserManager().getUserId();
                String userName = UserManager.getUserManager().getUserName();
                CommunityDBHelper.getInstance().addComment(userId,userName,comment);

                List<Comment> list = CommunityDBHelper.getInstance().queryCommentList();
                commentAdapter.setNewData(list);
            }
        });

        getViewBinding().toAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostCommentActivity.class);
                commentLauncher.launch(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Comment> list = CommunityDBHelper.getInstance().queryCommentList();
        commentAdapter.setNewData(list);
    }
}
