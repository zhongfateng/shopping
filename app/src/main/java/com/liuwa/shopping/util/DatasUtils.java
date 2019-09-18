package com.liuwa.shopping.util;

import com.liuwa.shopping.model.ImageItemModel;
import com.liuwa.shopping.model.MoneyModel;
import com.liuwa.shopping.model.OrderTitleModel;
import com.liuwa.shopping.model.ProductChildModel;
import com.liuwa.shopping.model.ProductModel;

import java.util.ArrayList;

/**
 * Created by ZFT on 2019/8/20.
 */

public class DatasUtils {
    public static ArrayList<ImageItemModel> imageList=new ArrayList<ImageItemModel>();
    public static ArrayList<MoneyModel> moneyList=new ArrayList<MoneyModel>();
    public static ArrayList<ProductModel> productModels=new ArrayList<ProductModel>();
    public static ArrayList<ProductChildModel> productChildModels=new ArrayList<ProductChildModel>();
    public static ArrayList<String> strings=new ArrayList<String>();
    public static ArrayList<OrderTitleModel> orderTitleModels=new ArrayList<OrderTitleModel>();
    static {
        ImageItemModel model1=new ImageItemModel();
        model1.setImageUrl("http://p0.so.qhmsg.com/t018fd7fe7982a05245.jpg");
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
        ProductModel p1=new ProductModel();
        p1.proName="测试数据测试数据";
        p1.showprice=100.00d;
        p1.price=200.00d;
        ProductModel p2=new ProductModel();
        p2.proName="测试数据测试数据测试数据测试数据测试数据";
        p2.showprice=100.00d;
        p2.price=200.00d;
        ProductModel p3=new ProductModel();
        p3.proName="测试数据测试数据测试数据测试数据测试数据";
        p3.showprice=100.00d;
        p3.price=200.00d;
        productModels.add(p1);
        productModels.add(p2);
        productModels.add(p3);
        for(int i=0;i<5;i++){
            strings.add("http://pic1.win4000.com/wallpaper/c/53cdd1f7c1f21.jpg");
        }
        for(int i=0;i<3;i++){
            ProductChildModel model=new ProductChildModel();
            model.guiGe="测试"+i;
            model.productId="1";
            productChildModels.add(model);
        }
        OrderTitleModel titleModel=new OrderTitleModel();
        titleModel.tag="0";
        titleModel.title="待付款";
        OrderTitleModel titleModel2=new OrderTitleModel();
        titleModel2.tag="1";
        titleModel2.title="待发货";
        OrderTitleModel titleModel3=new OrderTitleModel();
        titleModel3.tag="2";
        titleModel3.title="待提货";
        OrderTitleModel titleModel4=new OrderTitleModel();
        titleModel4.tag="3";
        titleModel4.title="待评价";
        orderTitleModels.add(titleModel);
        orderTitleModels.add(titleModel2);
        orderTitleModels.add(titleModel3);
        orderTitleModels.add(titleModel4);

    }
}
