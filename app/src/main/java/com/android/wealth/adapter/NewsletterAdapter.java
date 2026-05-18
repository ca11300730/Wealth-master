package com.android.wealth.adapter;


import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.wealth.R;
import com.android.wealth.bean.Newsletter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * 快讯
 */
public class NewsletterAdapter extends BaseQuickAdapter<Newsletter.ResultDTO.ContentDTO.ListDTO, BaseViewHolder> {


//    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

    public NewsletterAdapter() {
        super(R.layout.item_news_letter);
    }

    @Override
    protected void convert(BaseViewHolder helper, Newsletter.ResultDTO.ContentDTO.ListDTO item) {
        helper.setText(R.id.time, formatter.format(new Date(item.publish_time * 1000)));
        TextView contentView = helper.getView(R.id.content);
        StringBuilder content = new StringBuilder();
        if (item.content != null && item.content.items != null && item.content.items.size() > 0){
            if (!TextUtils.isEmpty(item.title)){
                content.append("【").append(item.title).append("】");
            }
            content.append(item.content.items.get(0).data);
        }else if (!TextUtils.isEmpty(item.title)){
            content.append(item.title);
        }
        contentView.setText(content);
        contentView.setMaxLines(10);

        contentView.setOnClickListener(null);
        contentView.post(() -> {
            Layout layout = contentView.getLayout();
            int lineCount = layout.getLineCount();
            if (lineCount <= 0) {
                return;
            }
            int ellipsisCount = layout.getEllipsisCount(lineCount - 1);
            if (ellipsisCount <= 0) {
                return;
            }

            float lineWidth = layout.getLineWidth(lineCount - 1);

            float textWidth = layout.getPaint().measureText("展开");

            SpannableString text;
            if (textWidth + lineWidth <= layout.getWidth()) {
                text = new SpannableString(layout.getText().subSequence(0,
                        layout.getLineEnd(lineCount - 1)).toString().replace("\uFEFF", "") + "展开");
            } else {
                StringBuilder builder = new StringBuilder(layout.getText().subSequence(0,
                        layout.getLineEnd(lineCount - 1)).toString().replace("\uFEFF", ""));
                int endIndex = builder.length() - 1;
                int startIndex = endIndex - 1;
                do {
                    startIndex -= 1;
                } while (layout.getPaint().measureText(builder, startIndex, endIndex) < textWidth);
                builder.delete(startIndex, endIndex);
                builder.append("展开");
                text = new SpannableString(builder);
            }
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#0074FA")),
                    text.length() - 2, text.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            contentView.setText(text);


            SpannableString text2 = text;
            contentView.setOnClickListener(v -> {
                if (contentView.getMaxLines() != Integer.MAX_VALUE) {
                    contentView.setText(content);
                    contentView.setMaxLines(Integer.MAX_VALUE);
                } else {
                    contentView.setText(text2);
                    contentView.setMaxLines(10);
                }
            });

        });


        ViewGroup stockLayout = helper.getView(R.id.stock_layout);
        stockLayout.removeAllViews();
//        if (!TextUtils.isEmpty(item.stocks)) {
//            stockLayout.setVisibility(View.VISIBLE);
//            String[] split = item.stocks.split(",");
//            for (String str : split) {
//                SelfStock selfStock = new SelfStock();
//                selfStock.setLocalCode(str);
//
//                PersonalStockView stockView = new PersonalStockView(mContext);
//                stockView.setBackgroundResource(R.drawable.news_item_label_background);
//                stockView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelSize(R.dimen.sp_13));
//                stockView.setCompoundDrawablePadding(mContext.getResources().getDimensionPixelSize(R.dimen.dp_3_5));
//                stockView.setTextColor(Color.parseColor("#ff666666"));
//                stockView.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.dp_7), mContext.getResources().getDimensionPixelSize(R.dimen.dp_2),
//                        mContext.getResources().getDimensionPixelSize(R.dimen.dp_7), mContext.getResources().getDimensionPixelSize(R.dimen.dp_2));
//                stockView.setGravity(Gravity.CENTER);
//
//                stockView.setCode(selfStock.toStock());
//
//                stockLayout.addView(stockView);
//            }
//        } else {
//            stockLayout.setVisibility(View.GONE);
//        }
    }


}
