package com.liuwa.shopping.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZFT on 2019/8/22.
 * "fristimg": "111.jpg",
 "showprice": 100.0,
 "area": 1,
 "allSaleNum": 100,
 "allKuCun": 100,
 "Price": 1.0,
 "classes": "1",
 "proName": "测试商品1",
 "proHeadId": 1,
 "type": "1",
 "paixuNum": 1
 */

public class ProductModel implements Serializable{
    public String fristimg;
    public double showprice;
    public String proName;
    public double price;
    public String allSaleNum;
    public String allKuCun;
    public String classes;
    public String proHeadId;
    public String type;
    public String paixuNum;
    public String area;
    public String peiSong;
}
