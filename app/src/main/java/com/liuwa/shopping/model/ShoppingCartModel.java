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
    private String imageUrl="http://pic25.nipic.com/20121112/9252150_150552938000_2.jpg";
    private String shoppingName;

    private int dressSize;
    private String attribute;

    private double salePrice;

    public boolean isChoosed;
    public boolean isCheck = false;
    private int num;

    public String proName;
    public String proHeadId;
    public String proChildId;
    public String cartId;

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
        this.salePrice = salePrice;
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


    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }


}
