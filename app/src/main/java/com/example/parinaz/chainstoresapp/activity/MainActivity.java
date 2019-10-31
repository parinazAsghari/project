package com.example.parinaz.chainstoresapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parinaz.chainstoresapp.AppController;
import com.example.parinaz.chainstoresapp.GpsTracker;
import com.example.parinaz.chainstoresapp.R;
import com.example.parinaz.chainstoresapp.data.DataLoader;
import com.example.parinaz.chainstoresapp.data.VolleyCallbackStores;
import com.example.parinaz.chainstoresapp.fragment.CategoryFragment;
import com.example.parinaz.chainstoresapp.fragment.HomeFragment;
import com.example.parinaz.chainstoresapp.fragment.SearchFragment;
import com.example.parinaz.chainstoresapp.fragment.marked_list_fragment;
import com.example.parinaz.chainstoresapp.object.Store;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bottomMenuHome, bottomMenuCategory, bottomMenuShoppingList, bottomMenuSearch , bottomMenuLocation;
    FragmentManager fragmentManager;
    //public static List<Store> stores ;
    GpsTracker gpstracker;
    EditText locationSearchField;
    Button locationSearchBtn , userLocationBtn;
    List<Address> list;
    Dialog locationDialog , locationSettingDialog;
    Button settings, cancel;
    private static final int PERMISSION_REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();


      //  stores = new ArrayList<>();
        locationDialog = new Dialog(MainActivity.this);
        locationDialog.setContentView(R.layout.location_dialog);
        locationDialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialogAnimation;
        locationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        locationSettingDialog = new Dialog(this);
        locationSettingDialog.setContentView(R.layout.alert_location);
        locationSettingDialog.setCancelable(false);
        locationSettingDialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialogAnimation;
        locationSettingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        bottomMenuHome = (Button) findViewById(R.id.home_btn);
        bottomMenuCategory = (Button) findViewById(R.id.category_btn);
        bottomMenuSearch = (Button) findViewById(R.id.search_btn);
        bottomMenuShoppingList = (Button) findViewById(R.id.marked_list_btn);
        bottomMenuLocation = (Button) findViewById(R.id.location_btn);

        locationSearchField = locationDialog.findViewById(R.id.location_search_field);
        locationSearchBtn = locationDialog.findViewById(R.id.location_search_btn);
        userLocationBtn = locationDialog.findViewById(R.id.my_location_btn);
        settings = locationSettingDialog.findViewById(R.id.settings);
        cancel = locationSettingDialog.findViewById(R.id.cancel);

        bottomMenuHome.setOnClickListener(this);
        bottomMenuCategory.setOnClickListener(this);
        bottomMenuSearch.setOnClickListener(this);
        bottomMenuShoppingList.setOnClickListener(this);
        bottomMenuLocation.setOnClickListener(this);

        userLocationBtn.setOnClickListener(this);
        locationSearchBtn.setOnClickListener(this);
        settings.setOnClickListener(this);
        cancel.setOnClickListener(this);

        Fragment f = fragmentManager.findFragmentById(R.id.fragment);
        if(f == null) {
            findUserLocation();
            replaceFragment(new HomeFragment());
            homeBtnDesign();


        }else
            reloadCurrentFragment();


    }



    public void findUserLocation() {
       /* if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQ_CODE);



        }*/


            gpstracker = new GpsTracker(this);
            if (gpstracker.canGetLocation()) {
                //  if (gpstracker.getLatitude() !=0 & gpstracker.getLongitude()!=0) {
                AppController.getInstance().setLatitude(gpstracker.getLatitude());
                AppController.getInstance().setLongitude(gpstracker.getLongitude());
                loadStores();
            }
            else {
                locationSettingDialog.show();
            }


          //  replaceFragment(new HomeFragment());
       // }
       // else{
         //findUserLocation();
       // }

    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQ_CODE){

                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            gpstracker = new GpsTracker(this);
            if (gpstracker.canGetLocation()) {
                //  if (gpstracker.getLatitude() !=0 & gpstracker.getLongitude()!=0) {
                AppController.getInstance().setLatitude(gpstracker.getLatitude());
                AppController.getInstance().setLongitude(gpstracker.getLongitude());
                loadStores();
            }
            } else {

            Toast.makeText(this, "Permission denied to access location", Toast.LENGTH_SHORT).show();

            }
        }*/



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==1 ){
            Log.i("result main" , "!");
            findUserLocation();
        }
    }

    public void loadStores(){
        new DataLoader().getStores(getWindow().getDecorView().getRootView(), new VolleyCallbackStores() {
            @Override
            public void onSuccess(List<Store> list) {
                AppController.getInstance().clearStores();
               AppController.getInstance().setStores(list);
               // Log.d("main stores", stores.size() + "");
                reloadCurrentFragment();
               //replaceFragment(new HomeFragment());
            }
        });
    }


    @Override
    public void onBackPressed() {
        Fragment f = fragmentManager.findFragmentById(R.id.fragment);
        if(f instanceof HomeFragment) {
            finish();
        }
        else {
            bottomMenuHome.setTextColor(getResources().getColor(R.color.button_selected));
            bottomMenuHome.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_home_selected),null,null);
            bottomMenuCategory.setTextColor(getResources().getColor(R.color.gray));
            bottomMenuCategory.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_list),null,null);
            bottomMenuSearch.setTextColor(getResources().getColor(R.color.gray));
            bottomMenuSearch.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_search),null,null);
            bottomMenuShoppingList.setTextColor(getResources().getColor(R.color.gray));
            bottomMenuShoppingList.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_checklist),null,null);
            replaceFragment(new HomeFragment());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_btn: {
                homeBtnDesign();
                Fragment f = fragmentManager.findFragmentById(R.id.fragment);
                if(!(f instanceof HomeFragment))
                    replaceFragment(new HomeFragment());
                break;
            }
            case R.id.category_btn: {
                categoryBtnDesign();
                Fragment f = fragmentManager.findFragmentById(R.id.fragment);
                if(!(f instanceof CategoryFragment))
                    replaceFragment(new CategoryFragment());
                break;
            }

            case R.id.search_btn: {
                searchBtnDesign();
                Fragment f = fragmentManager.findFragmentById(R.id.fragment);
                if(!(f instanceof SearchFragment))
                    replaceFragment(new SearchFragment());
                break;
            }

            case R.id.marked_list_btn: {
                markedListBtnDesign();
                Fragment f = fragmentManager.findFragmentById(R.id.fragment);
                if(!(f instanceof marked_list_fragment))
                    replaceFragment(new marked_list_fragment());
                break;
            }
            case R.id.location_btn :{
                locationBtnDesign();
                locationDialog.show();
                break;
            }
            case R.id.location_search_btn :{
                String key = "تهران"+locationSearchField.getText().toString().trim();
                Geocoder gc = new Geocoder(MainActivity.this);
                try {
                    list=gc.getFromLocationName(key,5);
                    if (list.size() != 0){
                        Log.i("size", list.size() + "");
                        Address address = list.get(0);
                        AppController.getInstance().setLatitude(address.getLatitude());
                        AppController.getInstance().setLongitude(address.getLongitude());
                        loadStores();
                        reloadCurrentFragment();
                        locationDialog.dismiss();
                    }else {
                        Toast.makeText(MainActivity.this, "موقعیت مورد نظر یافت نشد.", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /////////Test
//                lat = 35.6931722 ;
//                lon = 51.3531967 ;
//                AppController.getInstance().setLat(35.6931722);
//                AppController.getInstance().setLon(51.3531967);
//                loadStores();
//                reloadCurrentFragment();
//                locationDialog.dismiss();
                ///////////
                break;
            }
            case R.id.my_location_btn :{
                findUserLocation();
                locationDialog.dismiss();
                break;
            }
            case R.id.settings :{
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent , 1);
                locationSettingDialog.dismiss();
                break;
            }
            case R.id.cancel :{
                locationSettingDialog.dismiss();
                break;
            }
        }

    }

    public void reloadCurrentFragment(){
        Fragment f = fragmentManager.findFragmentById(R.id.fragment);
        if(f instanceof HomeFragment) {
            replaceFragment(new HomeFragment());
            homeBtnDesign();
        }else if(f instanceof CategoryFragment) {
            replaceFragment(new CategoryFragment());
            categoryBtnDesign();
        }else if(f instanceof marked_list_fragment) {
            replaceFragment(new marked_list_fragment());
            markedListBtnDesign();
        }else if(f instanceof SearchFragment) {
            replaceFragment(new SearchFragment());
            searchBtnDesign();
        }
    }
    public void searchBtnDesign(){
        bottomMenuSearch.setTextColor(getResources().getColor(R.color.button_selected));
        bottomMenuSearch.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_search_selected),null,null);
        bottomMenuHome.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuHome.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_home),null,null);
        bottomMenuCategory.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuCategory.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_list),null,null);
        bottomMenuShoppingList.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuShoppingList.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_checklist),null,null);
        bottomMenuLocation.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuLocation.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_location_unmarked),null,null);
    }
    public void homeBtnDesign(){
        bottomMenuHome.setTextColor(getResources().getColor(R.color.button_selected));
        bottomMenuHome.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_home_selected),null,null);
        bottomMenuCategory.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuCategory.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_list),null,null);
        bottomMenuSearch.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuSearch.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_search),null,null);
        bottomMenuShoppingList.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuShoppingList.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_checklist),null,null);
        bottomMenuLocation.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuLocation.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_location_unmarked),null,null);
    }
    public void markedListBtnDesign(){
        bottomMenuShoppingList.setTextColor(getResources().getColor(R.color.button_selected));
        bottomMenuShoppingList.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_checklist_selected),null,null);
        bottomMenuHome.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuHome.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_home),null,null);
        bottomMenuCategory.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuCategory.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_list),null,null);
        bottomMenuSearch.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuSearch.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_search),null,null);
        bottomMenuLocation.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuLocation.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_location_unmarked),null,null);
    }
    public void categoryBtnDesign(){
        bottomMenuCategory.setTextColor(getResources().getColor(R.color.button_selected));
        bottomMenuCategory.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_list_selected),null,null);
        bottomMenuHome.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuHome.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_home),null,null);
        bottomMenuSearch.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuSearch.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_search),null,null);
        bottomMenuShoppingList.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuShoppingList.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_checklist),null,null);
        bottomMenuLocation.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuLocation.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_location_unmarked),null,null);
    }
    public void locationBtnDesign(){
        bottomMenuLocation.setTextColor(getResources().getColor(R.color.button_selected));
        bottomMenuLocation.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_location_menu),null,null);
        bottomMenuHome.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuHome.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_home),null,null);
        bottomMenuCategory.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuCategory.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_list),null,null);
        bottomMenuSearch.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuSearch.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_search),null,null);
        bottomMenuShoppingList.setTextColor(getResources().getColor(R.color.gray));
        bottomMenuShoppingList.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.ic_checklist),null,null);
    }
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }



    /*private void requestProduct() {
        StringRequest stringRequest = new StringRequest("http://192.168.43.208/chainStoreDB/getAllProducts.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonobject = new JSONObject(s);
                    if(!jsonobject.getBoolean("error")){
                        for(Store store : stores){
                            JSONArray productArray =jsonobject.getJSONArray(store.getProductsAddress());

                        for(int i = 0;i<productArray.length();i++) {


                            JSONObject jsonProduct = productArray.getJSONObject(i);
                            Product products = new Product();


                            products.setCode(jsonProduct.getInt("code"));
                            products.setName(jsonProduct.getString("name"));
                            products.setCategory(jsonProduct.getInt("category"));
                            products.setDiscount(jsonProduct.getInt("discount"));
                            products.setPicAdress(store.getProductsImage() + jsonProduct.getString("image"));
                            products.setPrice(jsonProduct.getInt("price"));
                            products.setStock(jsonProduct.getInt("stock"));
                            products.setReducedPrice(products.getPrice() * (100 - products.getDiscount()) / 100);
                            products.setStoreId(store.getId());
                           // products.setStoreId(1);
                            productList.add(products);
                        }
                        }
                        addFragment(new HomeFragment());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });


        requestQueue.add(stringRequest);}


    private void requestStores() {
        StringRequest request = new StringRequest("http://192.168.43.208/chainStoreDB/get_all_stores.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonobject = new JSONObject(s);
                    if(!jsonobject.getBoolean("error")){
                        JSONArray storeArray = jsonobject.getJSONArray("message");
                        for(int i = 0;i<storeArray.length();i++){
                            Store store = new Store();
                            JSONObject jsonstore= storeArray.getJSONObject(i);
                            store.setId(jsonstore.getInt("id"));
                            store.setName(jsonstore.getString("name"));
                            store.setPicAdress(jsonstore.getString("image"));
                            store.setProductsAddress(jsonstore.getString("products_address"));
                            store.setProductsImage(jsonstore.getString("products_image"));
                            stores.add(store);

                        }

                    }
                    addFragment(new HomeFragment());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });


        requestQueue.add(request);

    }

    private void requestBrands() {
        StringRequest stringRequest = new StringRequest("http://192.168.43.208/chainStoreDB/get_brands.php"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (!jsonObject.getBoolean("error")) {
                        JSONArray brandArray = jsonObject.getJSONArray("message");
                        for (int i = 0; i < brandArray.length(); i++) {
                            JSONObject jsonBrand = brandArray.getJSONObject(i);
                            Brand brand = new Brand();
                            brand.setName(jsonBrand.getString("name"));
                            brand.setPicAddress(jsonBrand.getString("image"));

                            brands.add(brand);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void requestCategories() {
        StringRequest stringRequest = new StringRequest("http://192.168.43.208/chainStoreDB/get_categories.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (!jsonObject.getBoolean("error")) {
                        JSONArray categoryArray = jsonObject.getJSONArray("message");
                        for (int i = 0; i < categoryArray.length(); i++) {
                            JSONObject jsonCategory = categoryArray.getJSONObject(i);
                            Category category = new Category();
                            category.setId(jsonCategory.getInt("id"));
                            category.setName(jsonCategory.getString("name"));
                            category.setPicAddress(jsonCategory.getString("image"));

                            categories.add(category);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(stringRequest);

    }*/

}
