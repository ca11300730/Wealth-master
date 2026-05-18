package com.android.wealth.utils.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.android.wealth.utils.blur.BlurTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

// 图片加载
public class ImageLoader {

    private static boolean checkNull(Context context) {
        if (context == null) {
            return true;
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                return true;
            }
        }
        return false;
    }

    public static void preload(Context context, String url) {
        if (checkNull(context)) {
            return;
        }
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).preload();
    }


    public static void loadImage(Context context, String url, ImageView imageView) {
        loadImage(context, url, imageView, -1);
    }

    public static void loadImageWithNoCache(Context context, String url, ImageView imageView) {
        if (checkNull(context)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).load(url).apply(requestOptions).dontAnimate().skipMemoryCache(true).into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView, int placeHolderResId) {
        if (checkNull(context)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
        if (placeHolderResId != -1) {
            Glide.with(context).load(url).apply(requestOptions).dontAnimate().placeholder(placeHolderResId).into(imageView);
        } else {
            Glide.with(context).load(url).apply(requestOptions).dontAnimate().into(imageView);
        }
    }


    public static void loadImage(Context context, Uri url, ImageView imageView, int placeHolderResId) {
        if (checkNull(context)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().placeholder(placeHolderResId).diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url)
                .apply(requestOptions).into(imageView);
    }


    /**
     * 加载圆形
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView, int placeholder) {
        if (checkNull(context)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleCrop())
                .placeholder(placeholder);
        Glide.with(context).load(url).apply(requestOptions)
                .into(imageView);
    }

    /**
     * 加载圆形
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        if (checkNull(context)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleCrop());
//                .placeholder(R.mipmap.default_avatar);
        Glide.with(context).load(url).apply(requestOptions)
                .into(imageView);
    }

    public static void loadCircleImage(Context context, @DrawableRes int resourceId, ImageView imageView) {
        if (checkNull(context)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleCrop());
//                .placeholder(R.mipmap.default_avatar);
        Glide.with(context).load(resourceId).apply(requestOptions)
                .into(imageView);


    }

    public static void loadGif(Context context, String url, ImageView imageView) {
        if (checkNull(context)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(new CircleCrop());
//                .placeholder(R.mipmap.default_avatar);
        Glide.with(context).asGif().load(url).apply(requestOptions)
                .into(imageView);
    }

    public static void loadGif(Context context, @DrawableRes int resourceId, ImageView imageView) {
        if (checkNull(context)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//                .placeholder(R.mipmap.default_avatar);
        Glide.with(context).asGif().load(resourceId).apply(requestOptions)
                .into(imageView);
    }

    /**
     * 加载圆角矩形
     */
    public static void loadRoundImage(Context context, String url, ImageView imageView, int radius) {
        if (checkNull(context)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new RoundedCorners(radius));
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     *
     *  高斯模糊图片
     *  Supported range 0 < radius <= 25
     *
     */
    public static void loadBlurImage(Context context, String url, ImageView imageView,int radius, int sampling) {
        Glide.with(context)
                .load(url)
                .transform(new BlurTransformation(radius, sampling))
                .into(imageView);
    }

    public static void loadBlurImage(Context context, @DrawableRes int resourceId, ImageView imageView,int radius, int sampling) {
        Glide.with(context)
                .load(resourceId)
                .transform(new BlurTransformation(radius, sampling))
                .into(imageView);
    }


    public static void loadImageWithCallBack(Context context, ImageView imageView, String url, @Nullable ImageLoadCallBack imageLoadCallBack) {
        if (checkNull(context)) {
            return;
        }
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
        RequestBuilder<Drawable> listener = Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (imageLoadCallBack != null) {
                            imageLoadCallBack.onLoadFailed();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (imageLoadCallBack != null) {
                            imageLoadCallBack.onLoadSuccess(resource);
                        }
                        return false;
                    }
                });
        listener.into(imageView);
    }

}
