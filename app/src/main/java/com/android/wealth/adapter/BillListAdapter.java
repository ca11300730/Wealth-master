package com.android.wealth.adapter;


import android.widget.ImageView;

import com.android.wealth.R;
import com.android.wealth.bean.Bill;
import com.android.wealth.bean.BillTitle;
import com.android.wealth.bean.TradeCategory;
import com.android.wealth.data.TradeCategoryUtils;
import com.android.wealth.utils.NumberUtil;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.chad.library.adapter.base.provider.BaseItemProvider;


public class BillListAdapter extends MultipleItemRvAdapter<Object, BaseViewHolder> {

    public static final int TYPE_TITLE = 0;

    public static final int TYPE_BILL = 1;

    public BillListAdapter() {
        super(null);
        finishInitialize();
    }

    @Override
    protected int getViewType(Object obj) {
        if (obj instanceof Bill) {
            return TYPE_BILL;
        }
        return TYPE_TITLE;
    }

    @Override
    public void registerItemProvider() {
        mProviderDelegate.registerProvider(new TitleItemProvider());
        mProviderDelegate.registerProvider(new BillItemProvider());
    }

    public static class BillItemProvider extends BaseItemProvider<Bill, BaseViewHolder> {

        @Override
        public int viewType() {
            return TYPE_BILL;
        }

        @Override
        public int layout() {
            return R.layout.item_bill_list;
        }

        @Override
        public void convert(BaseViewHolder helper, Bill data, int position) {
            ImageView icon0 = helper.getView(R.id.icon0);
            ImageView icon1 = helper.getView(R.id.icon1);

            TradeCategory tradeCategory = TradeCategoryUtils.getTradeCategory(data.category);

//            icon0.setBackgroundColor(icon0.getContext().getResources().getColor(R.color.color_F3F6F3));
            icon0.setImageResource(tradeCategory.selectedIcon);
            icon1.setImageResource(tradeCategory.unSelectedIcon);

            helper.setText(R.id.name, tradeCategory.name)
                    .setText(R.id.money, String.format("%s%s", data.status == 0 ? "-" : "", NumberUtil.formatNum(data.money)));
        }
    }


    public static class TitleItemProvider extends BaseItemProvider<BillTitle, BaseViewHolder> {

        @Override
        public int viewType() {
            return TYPE_TITLE;
        }

        @Override
        public int layout() {
            return R.layout.item_bill_title;
        }

        @Override
        public void convert(BaseViewHolder helper, BillTitle data, int position) {
            helper.setText(R.id.time, data.createTime);
            StringBuilder ssb = new StringBuilder();

            if (data.income > 0){
                ssb.append("收入：").append(NumberUtil.formatNum(data.income)).append("   ");
            }

            if (data.export > 0){
                ssb.append("支出：").append(NumberUtil.formatNum(data.export));
            }

            helper.setText(R.id.money, ssb);
        }
    }

}
