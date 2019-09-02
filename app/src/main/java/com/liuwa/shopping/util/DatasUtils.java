package com.liuwa.shopping.util;

import com.liuwa.shopping.model.ImageItemModel;
import com.liuwa.shopping.model.MoneyModel;

import java.util.ArrayList;

/**
 * Created by ZFT on 2019/8/20.
 */

public class DatasUtils {
    public static ArrayList<ImageItemModel> imageList=new ArrayList<ImageItemModel>();
    public static ArrayList<MoneyModel> moneyList=new ArrayList<MoneyModel>();
    static {
        ImageItemModel model1=new ImageItemModel();
        model1.setImageUrl("http://pic33.nipic.com/20131007/13639685_123501617185_2.jpg");
        ImageItemModel model2=new ImageItemModel();
        model2.setImageUrl("http://pic31.nipic.com/20130801/11604791_100539834000_2.jpg");
        imageList.add(model1);
        imageList.add(model2);
        MoneyModel moneyModel1=new MoneyModel();
        moneyModel1.money=300d;
        moneyModel1.jifen=600;
        MoneyModel moneyModel2=new MoneyModel();
        moneyModel2.money=200d;
        moneyModel2.jifen=400;
        MoneyModel moneyModel3=new MoneyModel();
        moneyModel3.money=100d;
        moneyModel3.jifen=200;
        MoneyModel moneyModel4=new MoneyModel();
        moneyModel4.money=50d;
        moneyModel4.jifen=100;
        moneyList.add(moneyModel1);
        moneyList.add(moneyModel2);
        moneyList.add(moneyModel3);
        moneyList.add(moneyModel4);
    }
}
