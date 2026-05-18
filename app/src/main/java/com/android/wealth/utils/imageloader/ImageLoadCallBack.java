package com.android.wealth.utils.imageloader;

import android.graphics.drawable.Drawable;

public interface ImageLoadCallBack {

    void onLoadSuccess(Drawable drawable);

    void onLoadFailed();

}
