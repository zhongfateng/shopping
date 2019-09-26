package com.liuwa.shopping.model;

import java.util.ArrayList;

/**
 * Created by ZFT on 2019/8/28.
 */

public class TuanModel<T> {
    public TuanItem tuan;
    public ArrayList<T> tuaninfolist;
    public class TuanItem{
        public String tuanCode;
        public TimeModel beginTime;
        public TimeModel endTime;
        public String tuanId;
        public class TimeModel{
            public String  beginTime;
            public long    time;
        }
    }

}
