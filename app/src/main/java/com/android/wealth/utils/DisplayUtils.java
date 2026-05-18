package com.android.wealth.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;


public class DisplayUtils {
    public static int dp2px(float f) {
        return (int) (TypedValue.applyDimension(1, f, getDM()) + 0.5f);
    }

    public static int sp2px( float f) {
        return (int) (TypedValue.applyDimension(2, f, getDM()) + 0.5f);
    }

    public static int px2dp(float f) {
        return (int) ((f / getDM().density) + 0.5f);
    }

    public static int getWidth() {
        DisplayMetrics dm = getDM();
        return (int) (((float) dm.widthPixels) / dm.density);
    }

    public static int getHeightDp() {
        DisplayMetrics dm = getDM();
        return (int) (((float) dm.heightPixels) / dm.density);
    }

    public static int getScreenWidth() {
        return getDM().widthPixels;
    }

    public static int getScreenHeight() {
        return getDM().heightPixels;
    }

    public static int getDpi(Context context) {
        return getDM().densityDpi;
    }

    public static float getAnyScaleHeightPixels( float f) {
        return ((float) getDM().heightPixels) / f;
    }

    private static DisplayMetrics getDM() {
        return Resources.getSystem().getDisplayMetrics();
    }

}
