package com.liuwa.shopping.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.liuwa.shopping.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by ZFT on 2019/9/3.
 */

public class ImageShowUtil {
    public static void showImage(String url, ImageView imageView){
        ImageLoader.getInstance().displayImage(url, imageView, new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100))
                .build(), new SimpleImageLoadingListener());
    }
}
