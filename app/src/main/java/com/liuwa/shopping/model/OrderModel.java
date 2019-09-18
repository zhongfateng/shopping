package com.liuwa.shopping.model;

import java.util.ArrayList;

/**
 * Created by ZFT on 2019/9/17.
 */

public class OrderModel {
    public String orderId;
    public String orderCode;
    public String payment;
    public double total;
    public ArrayList<InnerModel>  childlist;
    public class InnerModel{
        public int buyNum;
        public String fristimg;
        public String proName;
        public double buyPrice;
    }
}
