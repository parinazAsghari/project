package com.example.parinaz.chainstoresapp;


import com.example.parinaz.chainstoresapp.object.Product;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortProducts {
//    List<Product> sortedList;
//    List<Product> products;

    public SortProducts() {
//        sortedList = new ArrayList<>();
//        this.products = products;
    }

    public static void sortByMostDiscount (List<Product> products){
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                if (p1.getDiscount() == p2.getDiscount())
                    return (p1.getReducedPrice() > p2.getReducedPrice()) ? -1 : 1;
                return (p1.getDiscount() > p2.getDiscount()) ? -1 : 1;
            }
        });
//        return sortedList;
    }
    public static void sortByDefault (List<Product> products){
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return p1.getName().compareTo(p2.getName());
            }
        });
//        return sortedList;
    }
    public static void sortByLessPrice (List<Product> products){
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                if (p1.getPrice() == p2.getPrice())
                    return (p1.getReducedPrice() > p2.getReducedPrice()) ? 1 : -1;
                return (p1.getPrice() > p2.getPrice()) ? 1 : -1;
            }
        });
//        return sortedList;
    }
}
