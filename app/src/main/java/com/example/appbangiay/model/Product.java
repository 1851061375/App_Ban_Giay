package com.example.appbangiay.model;

import java.io.Serializable;

public class Product implements Serializable {
    public int Id;
    public String Name;
    public int Price;
    public String Picture;
    public int Type;
    public String Description;


    public Product(int id, String name, int price, String picture, int type, String description) {
        Id = id;
        Name = name;
        Price = price;
        Picture = picture;
        Type = type;
        Description = description;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public void setType(int type) {
        Type = type;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public int getPrice() {
        return Price;
    }

    public String getPicture() {
        return Picture;
    }

    public int getType() {
        return Type;
    }

    public String getDescription() {
        return Description;
    }
}
