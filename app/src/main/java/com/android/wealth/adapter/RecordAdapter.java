package com.android.wealth.adapter;


import android.graphics.Color;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.android.wealth.R;
import com.android.wealth.bean.TradeCategory;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class RecordAdapter extends BaseQuickAdapter<TradeCategory, BaseViewHolder> {

    public int selectPosition = -1;


    public RecordAdapter( @Nullable List<TradeCategory> data) {
        super(R.layout.item_record, data);
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    public TradeCategory getSelectData() {
        return getData().get(selectPosition);
    }

    @Override
    protected void convert(BaseViewHolder helper, TradeCategory item) {

        helper.setText(R.id.name,item.name);

        ImageView imageView = helper.getView(R.id.icon);

        imageView.setImageResource(item.unSelectedIcon);

        imageView.setBackgroundColor( selectPosition == helper.getLayoutPosition() ?
                imageView.getContext().getResources().getColor(R.color.common_color) : imageView.getContext().getResources().getColor(R.color.color_F3F6F3));

    }
}
