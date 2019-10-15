package com.liuwa.shopping.model;

/**
 * Created by ZFT on 2019/8/20.
 */

public class ImageItemModel {
    private String imageId;
    private String imageUrl;
    private String imageType;
    public  String  imgPath;
    public String newsId;
    public String img;
    public String newsurl;

    public String getImageId() {
        return imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

}
