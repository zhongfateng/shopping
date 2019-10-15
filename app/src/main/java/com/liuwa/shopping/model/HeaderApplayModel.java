package com.liuwa.shopping.model;

import java.io.Serializable;

/**
 * Created by ZFT on 2019/9/23.
 */

public class HeaderApplayModel implements Serializable {
    public String memberId;
    public String headImg;
    public String nickname;
    public CreateTime createDate;
    public class CreateTime implements Serializable{
        public long time;
    }
}
