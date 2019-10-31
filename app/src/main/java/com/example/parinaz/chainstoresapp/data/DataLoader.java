package com.example.parinaz.chainstoresapp.data;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.parinaz.chainstoresapp.AppController;
import com.example.parinaz.chainstoresapp.R;
import com.example.parinaz.chainstoresapp.SortProducts;
import com.example.parinaz.chainstoresapp.object.Brand;
import com.example.parinaz.chainstoresapp.object.Category;
import com.example.parinaz.chainstoresapp.object.Product;
import com.example.parinaz.chainstoresapp.object.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLoader {
  //  static Context context;
    static int  counter;
    static Boolean flag;



    public static void getProducts (final View view , final List<Store> stores, final VolleyCallbackProducts callback ) {
        final List<Product> products = new ArrayList<>();
        flag = false;
        counter = 0;
        for ( int j=0 ; j< stores.size() ; j++){
            final Store store = stores.get(j);
            StringRequest request = new StringRequest(Url.baseUrl + store.getProductsUrlAddress() , new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        counter ++;
                        JSONObject jsonObject = new JSONObject(s);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray productArray = jsonObject.getJSONArray("message");
                            for (int i = 0; i < productArray.length(); i++) {
                                JSONObject jsonProduct = productArray.getJSONObject(i);
                                Product product = new Product();
                                product.setCode(jsonProduct.getInt("code"));
                                product.setName(jsonProduct.getString("name"));
                                product.setPicAddress(store.getProductsImageAddress() + jsonProduct.getString("image"));
                                product.setPrice(jsonProduct.getInt("price"));
                                product.setDiscount(jsonProduct.getInt("discount"));
                                product.setStock(jsonProduct.getInt("stock"));
                                product.setCategory(jsonProduct.getInt("category"));
                                product.setReducedPrice(product.getPrice() * (100 - product.getDiscount()) / 100);
                                product.setStoreId(store.getStoreId());
                                product.setStoreBranchId(store.getBranchId());
                                products.add(product);
                            }
                        }
                        if(counter == stores.size()) {
                            flag = true;
                            Log.d("finish!" , "" + flag);
                        }
                        SortProducts.sortByDefault(products);
                        callback.onSuccess(products , flag);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("volley error", volleyError + "error");
                    view.findViewById(R.id.connection_error_layout).setVisibility(View.VISIBLE);

                }
            });
            AppController.getInstance().addToRequestQueue(request);
        }
    }
    public static void getBrand ( final View view , final VolleyCallbackBrands callback ){
        StringRequest request = new StringRequest(Url.getBrandsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                List<Brand> brands = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (!jsonObject.getBoolean("error")) {
                        JSONArray storeArray = jsonObject.getJSONArray("message");
                        for (int i = 0; i < storeArray.length(); i++) {
                            JSONObject jsonBrand = storeArray.getJSONObject(i);
                            Brand brand = new Brand();
                            brand.setName(jsonBrand.getString("name"));
                            brand.setPicAddress(jsonBrand.getString("image"));
                            brands.add(brand);
                        }
                        callback.onSuccess(brands);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                view.findViewById(R.id.connection_error_layout).setVisibility(View.VISIBLE);
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }
    public static void getCategory(final View view , final VolleyCallbackCategories callback ){
        final StringRequest request = new StringRequest(Url.getCategoriesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                List<Category> categories = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (!jsonObject.getBoolean("error")) {
                        JSONArray storeArray = jsonObject.getJSONArray("message");
                        for (int i = 0; i < storeArray.length(); i++) {
                            JSONObject jsonCategory = storeArray.getJSONObject(i);
                            Category category = new Category();
                            category.setId(jsonCategory.getInt("id"));
                            category.setName(jsonCategory.getString("name"));
                            category.setPicAddress(jsonCategory.getString("image"));
                            categories.add(category);
                        }
                        Log.d("cat size init" , categories.size() + "");
                        callback.onSuccess(categories);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                view.findViewById(R.id.connection_error_layout).setVisibility(View.VISIBLE);
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    public static void getStores(final View view, final VolleyCallbackStores callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.branchesUrl, new Response.Listener<String>() {
            List<Store> storeList = new ArrayList<>();
            @Override
            public void onResponse(String response) {
                Log.i(">>>>>", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        JSONArray branchArray = jsonObject.getJSONArray("msg");
                        Log.i("stores length", branchArray.length() + "");
                        for (int i = 0; i < branchArray.length(); i++) {
                            JSONObject jsonBrand = branchArray.getJSONObject(i);
                            Store store = new Store();
                            store.setBranchId(jsonBrand.getInt("id"));
                            store.setName(jsonBrand.getString("name").toString());
                            store.setPicAddress(jsonBrand.getString("image"));
                            store.setStoreId(jsonBrand.getInt("storeId"));
                            store.setProductsImageAddress(jsonBrand.getString("products_image"));
                            store.setProductsUrlAddress(jsonBrand.getString("products_address"));
                            storeList.add(store);
                        }
                        Log.i("storeListSize",storeList.size()+"");
                    } else {
                        Toast.makeText(view.getContext(), "فروشگاهی اطراف شما یافت نشد.", Toast.LENGTH_SHORT).show();
                    }
                    callback.onSuccess(storeList);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                view.findViewById(R.id.connection_error_layout).setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

              // params.put("lat", "35.715085");
               //params.put("lon", "51.4122175");
               params.put("lat" , Double.toString(AppController.getInstance().getLatitude()));
                params.put("lon" , Double.toString(AppController.getInstance().getLongitude()));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}