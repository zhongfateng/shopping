package com.liuwa.shopping.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.client.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by ZFT on 2019/9/3.
 */

public class ImageShowUtil {
    public static void showImage(String url, ImageView imageView){
        ImageLoader.getInstance().displayImage(Constants.IMAGEHOSTPRE+url, imageView, new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_default)
                .showImageOnFail(R.mipmap.ic_default)
                .showImageOnLoading(R.mipmap.ic_default)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100))
                .build(), new SimpleImageLoadingListener());
    }
    public static void showImageByType(String url, ImageView imageView){
        ImageLoader.getInstance().displayImage(Constants.IMAGEHOSTPRE+url, imageView, new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_top)
                .showImageOnFail(R.mipmap.ic_top)
                .showImageOnLoading(R.mipmap.ic_top)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100))
                .build(), new SimpleImageLoadingListener());
    }
}
