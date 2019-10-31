package com.example.parinaz.chainstoresapp.object;

/**
 * Created by parinaz on 7/17/19.
 */

public class Category {
    String name , pic ;
    int id;

    public Category(String name, String pic , int id) {
        this.name = name;
        this.pic = pic;
        this.id = id;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicAddress() {
        return pic;
    }

    public void setPicAddress(String pic) {
        this.pic = pic;
    }
}