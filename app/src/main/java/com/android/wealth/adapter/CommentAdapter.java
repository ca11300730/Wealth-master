package com.android.wealth.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.android.wealth.R;
import com.android.wealth.bean.Comment;
import com.android.wealth.bean.User;
import com.android.wealth.database.UserDBHelper;
import com.android.wealth.utils.TimeUtils;
import com.android.wealth.utils.imageloader.ImageLoader;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;



public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

    private Context mContext;
    private boolean showComment;

    public CommentAdapter(Context context) {
       this(context,true);
    }

    public CommentAdapter(Context context,boolean showComment) {
        super(R.layout.item_comment);
        mContext = context;
        this.showComment = showComment;
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment commentData) {
        helper.setText(R.id.tv_name, commentData.userName)
                .setText(R.id.tv_content, commentData.content)
                .setText(R.id.commentNum, String.valueOf(commentData.commentNum))
                .setText(R.id.tv_time, TimeUtils.getShortTime(commentData.createTime));

        ImageView ivAvatar = helper.getView(R.id.iv_avatar);

        User user = UserDBHelper.getInstance().queryUserById(commentData.userId);
        if (user != null){
            ImageLoader.loadCircleImage(ivAvatar.getContext(),user.avatar,ivAvatar, R.drawable.default_avatar);
        }else {
            ivAvatar.setImageResource(R.drawable.default_avatar);
        }

        helper.setVisible(R.id.commentNumLayout,showComment);
    }
}
