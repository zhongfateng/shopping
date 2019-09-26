package com.liuwa.shopping.model;

/**
 * Created by ZFT on 2019/9/2.
 * {"score":1,"memScoreId":1,"orderId":1,"type":1,"memId":5,"createDate":{"date":1,"hours":1,"seconds":30,"month":8,"nanos":0,"timezoneOffset":-480,"year":119,"minutes":57,"time":1567274250000,"day":0}}
 */

public class IntegralModel {
   public int score;
   public CreateData createDate;
   public String type;
   public class CreateData{
       public long time;
   }

}
