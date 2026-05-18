package com.android.wealth.utils;

import java.text.DecimalFormat;
import java.util.UUID;

public class NumberUtil {

    public static String formatNum(double d) {
        DecimalFormat df = new DecimalFormat("0.#");
        df.setMaximumFractionDigits(2);
        return df.format(d);
//        double wan = Math.pow(10, 8); //  一亿以下
//        double yi = Math.pow(10, 12); //  万亿以下
//        if (d < 10000) {
//            return (int) d + "";
//        } else if (d < wan) {
//            return df.format(d / Math.pow(10, 4)) + "万";
//        } else if (d < yi) {
//            return df.format(d / Math.pow(10, 8)) + "亿";
//        } else {
//            return df.format(d / Math.pow(10, 12)) + "万亿";
//        }
    }

    public static String getRandomId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    public static int parseInt(String data) {
        try {
            return Integer.parseInt(data);
        } catch (Exception e) {

        }
        return 0;
    }




}
