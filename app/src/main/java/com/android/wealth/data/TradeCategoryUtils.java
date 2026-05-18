package com.android.wealth.data;


import androidx.annotation.Nullable;

import com.android.wealth.R;
import com.android.wealth.bean.TradeCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeCategoryUtils {

    private static List<TradeCategory> expensesTradeList = new ArrayList<>();
    private static List<TradeCategory> incomeTradeList = new ArrayList<>();

    static {
        expensesTradeList.add(new TradeCategory(1, "餐饮", R.drawable.category_e_catering_fill, R.drawable.category_e_catering_stroke));
        expensesTradeList.add(new TradeCategory(2, "购物", R.drawable.category_e_shopping_fill, R.drawable.category_e_shopping_stroke));
        expensesTradeList.add(new TradeCategory(3, "日用", R.drawable.category_e_commodity_fill, R.drawable.category_e_commodity_stroke));
        expensesTradeList.add(new TradeCategory(4, "交通", R.drawable.category_e_traffic_fill, R.drawable.category_e_traffic_stroke));
        expensesTradeList.add(new TradeCategory(5, "运动", R.drawable.category_e_sport_fill, R.drawable.category_e_sport_stroke));
        expensesTradeList.add(new TradeCategory(6, "娱乐", R.drawable.category_e_entertainmente_fill, R.drawable.category_e_entertainmente_stroke));
        expensesTradeList.add(new TradeCategory(7, "通讯", R.drawable.category_e_communicate_fill, R.drawable.category_e_communicate_stroke));
        expensesTradeList.add(new TradeCategory(8, "服饰", R.drawable.category_e_dress_fill, R.drawable.category_e_dress_stroke));
        expensesTradeList.add(new TradeCategory(9, "住房", R.drawable.category_e_house_fill, R.drawable.category_e_house_stroke));
        expensesTradeList.add(new TradeCategory(10, "旅行", R.drawable.category_e_travel_fill, R.drawable.category_e_travel_stroke));
        expensesTradeList.add(new TradeCategory(11, "烟酒", R.drawable.category_e_smoke_fill, R.drawable.category_e_smoke_stroke));
        expensesTradeList.add(new TradeCategory(12, "汽车", R.drawable.category_e_car_fill, R.drawable.category_e_car_stroke));
        expensesTradeList.add(new TradeCategory(13, "医疗", R.drawable.category_e_medical_fill, R.drawable.category_e_medical_stroke));
        expensesTradeList.add(new TradeCategory(15, "宠物", R.drawable.category_e_pet_fill, R.drawable.category_e_pet_stroke));
        expensesTradeList.add(new TradeCategory(16, "礼金", R.drawable.category_e_money_fill, R.drawable.category_e_money_stroke));
        expensesTradeList.add(new TradeCategory(17, "办公", R.drawable.category_e_office_fill, R.drawable.category_e_office_stroke));
        expensesTradeList.add(new TradeCategory(18, "维修", R.drawable.category_e_repair_fill, R.drawable.category_e_repair_stroke));

    }

    static {
        incomeTradeList.add(new TradeCategory(19, "工资", R.drawable.category_i_wage_fill, R.drawable.category_i_wage_stroke));
        incomeTradeList.add(new TradeCategory(20, "兼职", R.drawable.category_i_parttimework_fill, R.drawable.category_i_parttimework_stroke));
        incomeTradeList.add(new TradeCategory(21, "理财", R.drawable.category_i_finance_fill, R.drawable.category_i_finance_stroke));
        incomeTradeList.add(new TradeCategory(22, "礼金", R.drawable.category_i_money_fill, R.drawable.category_i_money_stroke));
        incomeTradeList.add(new TradeCategory(23, "其他", R.drawable.category_i_other_fill, R.drawable.category_i_other_stroke));
    }


    public static List<TradeCategory> getCategoryList(int position) {
        return position == 0 ? getExportCategoryList() : getIncomeCategoryList();
    }


    private static List<TradeCategory> getExportCategoryList() {
        return expensesTradeList;
    }

    private static List<TradeCategory> getIncomeCategoryList() {
        return incomeTradeList;
    }

    public static @Nullable TradeCategory getTradeCategory(int category) {
        for (TradeCategory tradeCategory : expensesTradeList) {
            if (tradeCategory.tradeCategory == category) {
                return tradeCategory;
            }
        }

        for (TradeCategory tradeCategory : incomeTradeList) {
            if (tradeCategory.tradeCategory == category) {
                return tradeCategory;
            }
        }
        return null;
    }


}
