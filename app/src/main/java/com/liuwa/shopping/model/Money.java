package com.liuwa.shopping.model;

/**
 * Created by ZFT on 2019/9/3.
 * {"money":5,"orderId":5,"memMoneyid":1,"type":1,"memId":5,"createDate":{"date":1,"hours":1,"seconds":35,"month":8,"nanos":0,"timezoneOffset":-480,"year":119,"minutes":47,"time":1567273655000,"day":0}}
 */

public class Money {
   public double money;
   public CreateDate createDate;
   public String type;
   public class CreateDate{
       public long time;
   }
}
