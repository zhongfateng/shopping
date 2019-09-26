package com.liuwa.shopping.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZFT on 2019/9/17.
 */

public class OrderModel implements Serializable{
    public String orderId;
    public String orderCode;
    public String payment;
    public double total;
    public String type;
    public String address;
    public int allbuynum;
    public double youhui;
    public ArrayList<InnerModel>  childlist;
    public class InnerModel{
        public int buyNum;
        public String fristimg;
        public String proName;
        public double buyPrice;
        public boolean isChoosed;
        public String typename;

        public void setChoosed(boolean choosed) {
            isChoosed = choosed;
        }
        public boolean isChoosed() {
            return isChoosed;
        }


    }
    public CreateTime createDate;
    public class CreateTime{
        public long time;
    }
}
