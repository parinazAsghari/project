package com.example.parinaz.chainstoresapp.object;

/**
 * Created by parinaz on 7/23/19.
 */

public class Store {
    public static int REFAH_STORE_ID = 1;
    public static int CANBO_STORE_ID = 2;
    public static int HYPERSTAR_STORE_ID = 3;
    public static int ETKA_STORE_ID = 4 ;
    String name , picAddress , productsUrlAddress , productsImageAddress;
    int branchId , storeId;
//    private double lon , lat;

    public Store() {
    }

//    public double getLon() {
//        return lon;
//    }
//
//    public void setLon(double lon) {
//        this.lon = lon;
//    }
//
//    public double getLat() {
//        return lat;
//    }
//
//    public void setLat(double lat) {
//        this.lat = lat;
//    }

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

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getProductsUrlAddress() {
        return productsUrlAddress;
    }

    public void setProductsUrlAddress(String productsUrlAddress) {
        this.productsUrlAddress = productsUrlAddress;
    }

    public String getProductsImageAddress() {
        return productsImageAddress;
    }

    public void setProductsImageAddress(String productsImageAddress) {
        this.productsImageAddress = productsImageAddress;
    }
}
