package com.android.wealth.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;


import com.android.wealth.R;
import com.android.wealth.bean.Stock;
import com.android.wealth.databinding.ItemStockBinding;
import com.android.wealth.view.SyncHorizontalScrollManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.luck.picture.lib.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends BaseQuickAdapter<Stock, BaseViewHolder> {

    private final Context mContext;
    private final SyncHorizontalScrollManager mScrollManager;
    private final int windowWidth;

    public StockAdapter(Context context, SyncHorizontalScrollManager scrollManager) {
        super(R.layout.item_stock);
        this.mContext = context;
        windowWidth = DensityUtil.getRealScreenWidth((Activity) mContext);
        this.mScrollManager = scrollManager;
    }

    @Override
    protected void convert(BaseViewHolder helper, Stock item) {
        mScrollManager.addView(helper.getView(R.id.value_scroll_view));

        helper.setText(R.id.name, item.name);
        helper.setText(R.id.code, item.code);

        int color = Color.parseColor("#333333");
        if (item.list.get(1).value.startsWith("+")) {
            color = Color.parseColor("#fc0002");
        } else if (item.list.get(1).value.startsWith("-")) {
            color = Color.parseColor("#1DAA34");
        }

        helper.setText(R.id.value0, item.list.get(0).value);
        helper.setText(R.id.value1, item.list.get(1).value)
                .setTextColor(R.id.value1, color);
        helper.setText(R.id.value2, item.list.get(6).value);
        helper.setText(R.id.value3, item.list.get(4).value);
        helper.setText(R.id.value4, item.list.get(3).value);
        helper.setText(R.id.value5, item.list.get(2).value);
        helper.setText(R.id.value6, item.list.get(5).value);
    }


}
