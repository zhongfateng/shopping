package com.liuwa.shopping.util;

import com.liuwa.shopping.model.ImageItemModel;

import java.util.ArrayList;

/**
 * Created by ZFT on 2019/8/20.
 */

public class DatasUtils {
    public static ArrayList<ImageItemModel> imageList=new ArrayList<ImageItemModel>();
    static {
        ImageItemModel model1=new ImageItemModel();
        model1.setImageUrl("http://pic33.nipic.com/20131007/13639685_123501617185_2.jpg");
        ImageItemModel model2=new ImageItemModel();
        model2.setImageUrl("http://pic31.nipic.com/20130801/11604791_100539834000_2.jpg");
        imageList.add(model1);
        imageList.add(model2);
    }
}
