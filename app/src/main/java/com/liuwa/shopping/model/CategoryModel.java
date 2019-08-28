package com.liuwa.shopping.model;

import java.io.Serializable;

/**
 * Created by ZFT on 2019/8/20.
 */

public class CategoryModel implements Serializable {
    private String proClassesName;
    private String imgPath;
    private String proClassesId;
    public String getProClassesName() {
        return proClassesName;
    }
    public String getImgPath() {
        return imgPath;
    }

    public void setProClassesName(String proClassesName) {
        this.proClassesName = proClassesName;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getProClassesId() {
        return proClassesId;
    }

    public void setProClassesId(String proClassesId) {
        this.proClassesId = proClassesId;
    }
}
