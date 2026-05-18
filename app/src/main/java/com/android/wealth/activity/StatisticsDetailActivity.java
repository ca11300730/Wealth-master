package com.android.wealth.activity;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.android.wealth.R;
import com.android.wealth.base.ViewBindingActivity;
import com.android.wealth.bean.Bill;
import com.android.wealth.bean.TradeCategory;
import com.android.wealth.bean.User;
import com.android.wealth.data.TradeCategoryUtils;
import com.android.wealth.data.UserManager;
import com.android.wealth.database.UserDBHelper;
import com.android.wealth.databinding.ActivityStatisticsDetailBinding;
import com.android.wealth.databinding.ItemStatisiticsDetailBinding;
import com.android.wealth.databinding.ItemStatisticsBinding;
import com.android.wealth.utils.NumberUtil;
import com.android.wealth.utils.imageloader.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StatisticsDetailActivity extends ViewBindingActivity<ActivityStatisticsDetailBinding> {

    public static String YEAY = "YEAR";

    public static String MONTH = "MONTH";

    public static String LIST = "LIST";

    public static String LAST_CASH_SURPLUS = "lastCashSurplus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = UserDBHelper.getInstance().queryUserById(UserManager.getUserManager().getUserId());
        if (user != null) {
            ImageLoader.loadCircleImage(this, user.avatar, getViewBinding().ivAvatar, R.drawable.default_avatar);
            getViewBinding().name.setText(user.userName);
        } else {
            getViewBinding().ivAvatar.setImageResource(R.drawable.default_avatar);
        }

        String year = getIntent().getStringExtra(YEAY);
        String month = getIntent().getStringExtra(MONTH);
        ArrayList<Bill> billList = getIntent().getParcelableArrayListExtra(LIST);

        setTitle(String.format("%s年%s月账单", year, month));
        getViewBinding().month.setText(String.format("%s月账单", month));

        float monthIncome = 0;
        float monthExport = 0;


        HashMap<Integer, Bill> incomeMap = new HashMap<>();
        HashMap<Integer, Bill> exportMap = new HashMap<>();
        for (int i = 0; i < billList.size(); i++) {
            Bill bill = billList.get(i);

            if (bill.status == 0) {
                monthExport += bill.money;

                Bill tempBill = exportMap.get(bill.category);
                if (tempBill == null) {
                    exportMap.put(bill.category, bill);
                } else {
                    tempBill.money += bill.money;
                }
            } else {
                monthIncome += bill.money;


                Bill tempBill = incomeMap.get(bill.category);
                if (tempBill == null) {
                    incomeMap.put(bill.category, bill);
                } else {
                    tempBill.money += bill.money;
                }
            }
        }


        getViewBinding().cashSurplus.setText(NumberUtil.formatNum(monthIncome - monthExport));
        getViewBinding().lastCashSurplus.setText(getIntent().getStringExtra(LAST_CASH_SURPLUS));

        getViewBinding().progress.setProgress(monthIncome / (monthIncome + monthExport));

        getViewBinding().cashIncome.setText(NumberUtil.formatNum(monthIncome));
        getViewBinding().cashExport.setText(NumberUtil.formatNum(monthExport));

        getViewBinding().monthIncome.setText(NumberUtil.formatNum(monthIncome));
        getViewBinding().monthExport.setText(NumberUtil.formatNum(monthExport));

        incomeMap = sortByValue(incomeMap);
        exportMap = sortByValue(exportMap);

        double maxIncome = 0;
        double maxExport = 0;
        for (Integer integer : incomeMap.keySet()) {
            maxIncome = Math.max(maxIncome, incomeMap.get(integer).money);
        }
        for (Integer integer : exportMap.keySet()) {
            maxExport = Math.max(maxExport, exportMap.get(integer).money);
        }
        showListView(incomeMap,getViewBinding().incomeContainer,maxIncome,monthIncome);
        showListView(exportMap,getViewBinding().exportContainer,maxExport,monthExport);
    }

    private void showListView(HashMap<Integer, Bill> map, ViewGroup viewGroup, double max, double totalMoney) {
        viewGroup.setVisibility(map.size() > 0 ? View.VISIBLE : View.GONE);
        for (Integer integer : map.keySet()) {
            TradeCategory tradeCategory = TradeCategoryUtils.getTradeCategory(integer);
            double money = map.get(integer).money;

            ItemStatisiticsDetailBinding binding = ItemStatisiticsDetailBinding.inflate(getLayoutInflater());

            binding.icon0.setImageResource(tradeCategory.selectedIcon);
            binding.icon0.setBackgroundColor(getResources().getColor(R.color.color_F3F6F3));
            binding.icon1.setImageResource(tradeCategory.unSelectedIcon);
            binding.type.setText(tradeCategory.name);

            binding.percent.setText(String.format("%s%s", NumberUtil.formatNum(money * 100 / totalMoney), "%"));

            binding.money.setText(NumberUtil.formatNum(money));

            binding.progress.setProgress((int) (100 *  (money / max)));

            viewGroup.addView(binding.getRoot());
        }
    }

    //  hashmap按值排序
    public static HashMap<Integer, Bill> sortByValue(HashMap<Integer, Bill> map) {
        // HashMap的entry放到List中
        List<Map.Entry<Integer, Bill>> list = new LinkedList<>(map.entrySet());
        // 对List按entry的value排序
//        list.sort(Map.Entry.comparingByValue());
        //  对List按entry的value排序
        Collections.sort(list, new Comparator<Map.Entry<Integer, Bill>>() {
            public int compare(Map.Entry<Integer, Bill> o1,
                               Map.Entry<Integer, Bill> o2) {
                return Double.compare(o2.getValue().money, o1.getValue().money);
            }
        });

        // 将排序后的元素放到LinkedHashMap中
        HashMap<Integer, Bill> temp = new LinkedHashMap<>();
        for (Map.Entry<Integer, Bill> entry : list) {
            temp.put(entry.getKey(), entry.getValue());
        }
        return temp;
    }


}