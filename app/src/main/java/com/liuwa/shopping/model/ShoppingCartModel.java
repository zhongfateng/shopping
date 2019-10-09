package com.liuwa.shopping.model;

/**
 * Created by AYD on 2016/11/22.
 * <p>
 *     "salePrice": 1,
 "cartId": 129,
 "num": 1,
 "kuCun": "201",
 "proChildId": 1,
 "proName": "测试商品",
 "guiGe": "1",
 "proHeadId": 1,
 "type": "1",
 "memberId": 7,
 "createDate": {
 "date": 5,
 "hours": 15,
 "seconds": 46,
 "month": 8,
 "nanos": 0,
 "timezoneOffset": -480,
 "year": 119,
 "minutes": 24,
 "time": 1567668286000,
 "day": 4
 }
 * 购物车
 */
public class ShoppingCartModel {

    private int id;
    private String imageUrl="";
    private String shoppingName;

    private int dressSize;
    private String attribute;


    public boolean isChoosed;
    public boolean isCheck = false;
    private int num;
    public String fristimg;
    public String proName;
    public String proHeadId;
    public String proChildId;
    public String cartId;
    public String guiGe;
    public double showprice;

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getCartId() {
        return cartId;
    }

    public String getProName() {
        return proName;
    }

    public String getProHeadId() {
        return proHeadId;
    }

    public String getProChildId() {
        return proChildId;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public void setProHeadId(String proHeadId) {
        this.proHeadId = proHeadId;
    }

    public void setProChildId(String proChildId) {
        this.proChildId = proChildId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public ShoppingCartModel() {
    }

    public ShoppingCartModel(int id, String shoppingName, String attribute, int dressSize,
                             double salePrice, int num) {
        this.id = id;
        this.shoppingName = shoppingName;
        this.attribute = attribute;
        this.dressSize = dressSize;
        this.num = num;

    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShoppingName() {
        return shoppingName;
    }

    public void setShoppingName(String shoppingName) {
        this.shoppingName = shoppingName;
    }


    public int getDressSize() {
        return dressSize;
    }

    public void setDressSize(int dressSize) {
        this.dressSize = dressSize;
    }
}
