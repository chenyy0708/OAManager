package com.huitian.oamanager.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Author:Chen
 * Data:2017/4/21
 * <p>
 * DiskCacheStrategy.NONE 什么都不缓存
 * DiskCacheStrategy.SOURCE 只缓存最高解析图的image
 * DiskCacheStrategy.RESULT 缓存最后一次那个image,比如有可能你对image做了转化
 * DiskCacheStrategy.ALL image的所有版本都会缓存
 */

public class ImageUtils {
    public static void load(Context context, String url, ImageView iv) {
        Glide.with(context).load(url)
                .crossFade() // 淡入效果
                .centerCrop() // 缩放类型
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)    // 只缓存最高解析图的image，节约内存
                .into(iv);
    }

    public static void load(Activity activity, String url, ImageView iv) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!activity.isDestroyed()) {  // 当前activity是否销毁
                Glide.with(activity).load(url)
                        .crossFade()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
            }
        }
    }

    public static void loadCircle(Activity activity, String url, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!activity.isDestroyed()) {
                Glide.with(activity).load(url).
                        transform(new GlideCircleTransform(activity)).into(iv);
            }
        }
    }

    public static void loadCircle(Context context, String url, ImageView iv) {    //使用Glide加载圆形ImageView(如头像)时，不要使用占位图
        Glide.with(context).load(url).
                transform(new GlideCircleTransform(context)).into(iv);
    }

    public static void loadAll(Context context, String url, ImageView iv) {    //不缓存，全部从网络加载
        Glide.with(context).load(url)
                .crossFade()
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
    }

    public static void loadAll(Activity activity, String url, ImageView iv) {    //不缓存，全部从网络加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!activity.isDestroyed()) {
                Glide.with(activity).load(url)
                        .crossFade()
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
            }
        }
    }
}
