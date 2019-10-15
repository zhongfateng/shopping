package com.liuwa.shopping.model;

import java.io.Serializable;

/**
 * Created by ZFT on 2019/9/9.
 * "buyPrice": 1,
 "fristimg": "111.jpg",
 "orderChildId": 31,
 "buyNum": 1,
 "orderId": 55,
 "proChildId": 1,
 "proName": "测试商品",
 "guiGe": "1",
 "proHeadId": 1,
 "type": "0",
 "memberId": 5,
 "leaderId": 1
 */

public class OrderProductItem implements Serializable{
    public String name;
    public String time;
    public double buyPrice;
    public String fristimg;
    public int  buyNum;
    public String proName;
    public String guiGe;
    public double total;
    public String orderChildId;
    public boolean isChoosed;
    public String typename;
    public String type;
    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

}
