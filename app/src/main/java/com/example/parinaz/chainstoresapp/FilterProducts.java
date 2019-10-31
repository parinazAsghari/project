package com.example.parinaz.chainstoresapp;


import com.example.parinaz.chainstoresapp.object.Product;

import java.util.ArrayList;
import java.util.List;

public class FilterProducts {
    List<Product> filteredProducts;


    public List<Product> getFilteredList(List<Product> products, boolean refah, boolean canbo, boolean hyperstar, boolean etka, int priceRang, boolean stock) {
        filteredProducts = new ArrayList<>();
        for (Product p : products) {
            if (stock) {
                filteredProducts.add(p);
            } else {
                if (p.getStock() == 1) {
                    filteredProducts.add(p);
                }
            }
            if (!refah) {
                if (p.getStoreId() == 1)
                    filteredProducts.remove(p);
            }
            if (!canbo) {
                if (p.getStoreId() == 2)
                    filteredProducts.remove(p);
            }
            if (!hyperstar) {
                if (p.getStoreId() == 3)
                    filteredProducts.remove(p);
            }
            if (!etka) {
                if (p.getStoreId() == 4)
                    filteredProducts.remove(p);
            }
            if (p.getPrice() >= (priceRang * 1000)) {
                filteredProducts.remove(p);
            }
        }
        return filteredProducts;
    }
}
