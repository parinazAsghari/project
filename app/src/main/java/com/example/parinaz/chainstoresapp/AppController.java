package com.example.parinaz.chainstoresapp;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.parinaz.chainstoresapp.object.Store;

import java.util.ArrayList;
import java.util.List;

public class AppController extends Application{
    private static AppController instance;
    RequestQueue requestQueue ;
    List<Store> stores ;
    double latitude;
    double longitude;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        stores = new ArrayList<>();
    }

    public static synchronized AppController getInstance(){
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }
    public void addToRequestQueue (StringRequest request ){
        getRequestQueue().add(request);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }
    public void clearStores (){
        stores.clear();
    }
    public boolean storeListIsEmpty(){

        return stores.isEmpty();
    }

}
