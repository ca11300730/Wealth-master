package com.android.wealth.utils;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

public class TyDateUtils {
    public static final long ONE_MINUTE_MILLIONS = 60 * 1000;
    public static final long ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS;
    public static final long ONE_DAY_MILLIONS = 24 * ONE_HOUR_MILLIONS;

    /**
     * 一小时的秒数
     */
    private static final int HOUR_SECOND = 60 * 60;

    /**
     * 一分钟的秒数
     */
    private static final int MINUTE_SECOND = 60;


    /**
     * 与当前时间比较，得到多少年，多少月，多少天前,多少小时前，多小分钟前
     */
    public static String getFriendlytimeByDate(Long date) {
        long delta = (Long.valueOf(String.format("%s",System.currentTimeMillis()).substring(0,10)) - date);
      //  if (delta <= 0) return d.toLocaleString();
        if (delta / (60 * 60 * 24 * 365) > 0) return delta / (60 * 60 * 24 * 365) + "年前";
        if (delta / (60 * 60 * 24 * 30) > 0) return delta / (60 * 60 * 24 * 30) + "个月前";
        if (delta / (60 * 60 * 24 * 7) > 0) return delta / (60 * 60 * 24 * 7) + "周前";
        if (delta / (60 * 60 * 24) > 0) return delta / (60 * 60 * 24) + "天前";
        if (delta / (60 * 60) > 0) return delta / (60 * 60) + "小时前";
        if (delta / (60) > 0) return delta / (60) + "分钟前";
        return "刚刚";
    }

    public static String getFriendlytimeByTime(Long date) {
        long time = (Long.valueOf(String.format("%s",System.currentTimeMillis()).substring(0,10)) - date);
        // 1小时内  
        if (time > 0 && time < 60) {
           // return time + "秒前";
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 48) {
            return "昨天";
        } else if (time >= 3600 * 48 && time < 3600 * 72) {
            return "前天";
        }else {
            return "long long ";
        }
//                else if (time >= 3600 * 72) {  
//            return dateToString(dateStr, DATE_FORMAT);  
//        }
    }
    /**
     * 获取短时间格式
     *
     * @return
     */
    public static String getShortTime(long millis) {
        Date date = new Date(millis);
        Date curDate = new Date();

        String str = "";
        long durTime = curDate.getTime() - date.getTime();

        int dayStatus = calculateDayStatus(date, new Date());

        if (durTime <= 10 * ONE_MINUTE_MILLIONS) {
            str = "刚刚";
        } else if (durTime < ONE_HOUR_MILLIONS) {
            str = durTime / ONE_MINUTE_MILLIONS + "分钟前";
        } else if (dayStatus == 0) {
            str = durTime / ONE_HOUR_MILLIONS + "小时前";
        } else if (dayStatus == -1) {
            str = "昨天" + DateFormat.format("HH:mm", date);
        } else if (isSameYear(date, curDate) && dayStatus < -1) {
            str = DateFormat.format("MM-dd", date).toString();
        } else {
            str = DateFormat.format("yyyy-MM", date).toString();
        }
        return str;
    }
    /**
     * 判断是否处于今天还是昨天，0表示今天，-1表示昨天，小于-1则是昨天以前
     * @param targetTime
     * @param compareTime
     * @return
     */
    public static int calculateDayStatus(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarDayOfYear = tarCalendar.get(Calendar.DAY_OF_YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comDayOfYear = compareCalendar.get(Calendar.DAY_OF_YEAR);

        return tarDayOfYear - comDayOfYear;
    }
    /**
     * 判断是否是同一年
     * @param targetTime
     * @param compareTime
     * @return
     */
    public static boolean isSameYear(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarYear = tarCalendar.get(Calendar.YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comYear = compareCalendar.get(Calendar.YEAR);

        return tarYear == comYear;
    }
    /**
     * 根据秒数获取时间串
     * @param secondTime (eg: 100s)
     * @return (eg: 00:01:40)
     */
    public static String getTimeStrBySecond(int secondTime) {
        if (secondTime <= 0) {
            return "00:00";
        }
        StringBuilder sb = new StringBuilder();
        int hours = secondTime / HOUR_SECOND;
        long minute = secondTime / MINUTE_SECOND % MINUTE_SECOND;
        long second = secondTime % MINUTE_SECOND;
        if (hours > 10 ) {
            sb.append(String.format("%s:",hours));
        }else if (hours> 0){
            sb.append(String.format("0%s:",hours));
        }
        if (minute >= 10) {
            sb.append(String.format("%s:",minute));
        }else {
            sb.append(String.format("0%s:",minute));
        }
        if (second >= 10) {
            sb.append(String.format("%s",second));
        }else {
            sb.append(String.format("0%s",second));
        }
        return sb.toString();
    }
}
