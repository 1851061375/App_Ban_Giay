package com.example.appbangiay.model;

import java.io.Serializable;

public class Cart implements Serializable {
    public int idProduct, count, productPrice;
    public String productName, picture;

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Cart(int idProduct, int count, String productName, int productPrice, String picture) {
        this.idProduct = idProduct;
        this.count = count;
        this.productName = productName;
        this.productPrice = productPrice;
        this.picture = picture;
    }
}
