package com.android.wealth.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import com.android.wealth.R;
import com.android.wealth.view.SyncHorizontalScrollManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.luck.picture.lib.utils.DensityUtil;

public class FundAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    private final Context mContext;
    private final SyncHorizontalScrollManager mScrollManager;
    private final int windowWidth;

    public FundAdapter(Context context, SyncHorizontalScrollManager scrollManager) {
        super(R.layout.item_fund);
        this.mContext = context;
        windowWidth = DensityUtil.getRealScreenWidth((Activity) mContext);
        this.mScrollManager = scrollManager;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        mScrollManager.addView(helper.getView(R.id.value_scroll_view));
        String[] split = item.split("\\|");
        helper.setText(R.id.name, split[1]);
        helper.setText(R.id.code, split[0]);
        helper.setText(R.id.value0, split[4]);
        helper.setText(R.id.value1, split[3].replaceFirst("-","\n"));
        setText(helper.getView(R.id.value2),split[5]);
        setText(helper.getView(R.id.value3),split[6]);
        setText(helper.getView(R.id.value4),split[7]);
        setText(helper.getView(R.id.value5),split[8]);
        setText(helper.getView(R.id.value6),split[9]);
        setText(helper.getView(R.id.value7),split[10]);
        setText(helper.getView(R.id.value8),split[11]);
        setText(helper.getView(R.id.value9),split[12]);
        setText(helper.getView(R.id.value10),split[13]);
        setText(helper.getView(R.id.value11),split[14]);
    }

    private void setText(TextView text,String value){
        int color = -1;
         if (value.startsWith("-")) {
            color = Color.parseColor("#1DAA34");
        }else if (TextUtils.isEmpty(value)){
             color = Color.parseColor("#333333");
         }else{
            color = Color.parseColor("#fc0002");
        }
        if (TextUtils.isEmpty(value)){
            text.setText("--");
        }else {
            text.setText(String.format("%s%s",value,"%"));
        }
         text.setTextColor(color);

    }


}
