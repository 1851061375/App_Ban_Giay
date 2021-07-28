package com.example.appbangiay.model;

public class Category {
    public int Type;
    public String Name;

    public Category(int type, String name) {
        Type = type;
        Name = name;
    }

    public int getType() {
        return Type;
    }

    public String getName() {
        return Name;
    }

    public void setType(int type) {
        Type = type;
    }

    public void setName(String name) {
        Name = name;
    }
}
