package com.example.parinaz.chainstoresapp.object;

public class MarkedProducts extends Product {

    String image , storeIcon , storeName;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStoreIcon() {
        return storeIcon;
    }

    public void setStoreIcon(String storeIcon) {
        this.storeIcon = storeIcon;
    }
}
