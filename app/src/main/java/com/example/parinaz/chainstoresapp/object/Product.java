package com.example.parinaz.chainstoresapp.object;

/**
 * Created by parinaz on 7/17/19.
 */


public class Product  {
    int code ;
    String name , picAddress ;
    int storeId , storeBranchId;
    int price , discount , reducedPrice ;
    int stock ;
    int category ;


    public Product() {

    }

    public int getCode() { return code;}

    public void setCode(int code) {this.code = code;}

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicAddress() {
        return picAddress;
    }

    public void setPicAddress(String picAddress) {
        this.picAddress = picAddress;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getReducedPrice() {
        return reducedPrice;
    }

    public void setReducedPrice(int reducedPrice) {
        this.reducedPrice = reducedPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getStoreBranchId() {
        return storeBranchId;
    }

    public void setStoreBranchId(int storeBranchId) {
        this.storeBranchId = storeBranchId;
    }
}