package com.example.parinaz.chainstoresapp.data;

import com.example.parinaz.chainstoresapp.object.Product;

import java.util.List;

public interface VolleyCallbackProducts {
    void onSuccess(List<Product> products, Boolean isFinished);
}
