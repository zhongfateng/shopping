package com.liuwa.shopping.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZFT on 2019/12/7.
 * "orderpsId": 33,
 "psday": "今天",
 "psdaytype": 1,
 "pstime": "一小时即达（9点-18点）",
 "pstimetype": 18,
 "pstype": 1
 */

public class PeiSongTime implements Serializable{
    public String day;
    public ArrayList<ChildModel> dayval;
    public class ChildModel{
        public String  orderpsId;
        public String psday;
        public String psdaytype;
        public String pstime;
        public String pstype;
    }
}
