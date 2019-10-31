package com.example.parinaz.chainstoresapp.object;

/**
 * Created by parinaz on 7/28/19.
 */

public class Brand {

    String name , image ;

    public Brand(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public Brand() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicAddress() {
        return image;
    }

    public void setPicAddress(String image) {
        this.image = image;
    }
}
