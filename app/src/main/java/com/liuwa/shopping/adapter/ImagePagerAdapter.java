package com.liuwa.shopping.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liuwa.shopping.R;
import com.liuwa.shopping.activity.IndexActivity;
import com.liuwa.shopping.adapter.jakewharton.salvage.RecyclingPagerAdapter;
import com.liuwa.shopping.model.ImageItemModel;
import com.liuwa.shopping.util.ImageShowUtil;
import com.liuwa.shopping.util.ListUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by ZFT on 2019/9/8.
 */

public class ImagePagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<ImageItemModel> imageIdList;
    private LayoutInflater inflater;
    private int           size;
    private boolean       isInfiniteLoop;
    public ImagePagerAdapter(Context context, List<ImageItemModel> imageIdList) {
        this.context = context;
        this.imageIdList = imageIdList;
        this.size = ListUtils.getSize(imageIdList);
        isInfiniteLoop = false;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils.getSize(imageIdList);
    }
    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            view=inflater.inflate(
                    R.layout.index_image_list_item_centre, null);
            holder = new ViewHolder();
            holder.imageView=(ImageView) view.findViewById(R.id.image_item);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        final ImageItemModel model=imageIdList.get(position);
        ImageShowUtil.showImage(model.imgPath,holder.imageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private  class ViewHolder {

        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
