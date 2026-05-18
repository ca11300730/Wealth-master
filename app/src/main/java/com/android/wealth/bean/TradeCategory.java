package com.android.wealth.bean;

public class TradeCategory {

    public int tradeCategory;
    public String name;
    public int selectedIcon;
    public int unSelectedIcon;

    public TradeCategory(int category, String name, int selectedIcon, int unSelectedIcon) {
        this.tradeCategory = category;
        this.name = name;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }
}
