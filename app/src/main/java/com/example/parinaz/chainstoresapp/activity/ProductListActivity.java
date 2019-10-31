package com.example.parinaz.chainstoresapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.parinaz.chainstoresapp.AppController;
import com.example.parinaz.chainstoresapp.ClickListener;
import com.example.parinaz.chainstoresapp.FilterProducts;
import com.example.parinaz.chainstoresapp.R;
import com.example.parinaz.chainstoresapp.RecyclerTouchListener;
import com.example.parinaz.chainstoresapp.SortProducts;
import com.example.parinaz.chainstoresapp.adapter.ProductAdapter;
import com.example.parinaz.chainstoresapp.data.DataLoader;
import com.example.parinaz.chainstoresapp.data.VolleyCallbackProducts;
import com.example.parinaz.chainstoresapp.object.Product;
import com.example.parinaz.chainstoresapp.object.Store;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public int STORE_Id ;
    private int CATEGORY_ID ;
    private int BRANCH_ID;
    private String BRAND ;
    RecyclerView recycler ;
    ProductAdapter adapter;
    Intent intent ;
    SwipeRefreshLayout swipe ;
    List<Product> productList , allProducts;
    RelativeLayout loading ;
    Button retry ;
    Dialog sortDialog , filterDialog;
    RadioButton defaultSort , mostDiscountSort , lessPriceSort ;
    Switch stockSwitch;
    CheckBox refahCheckBox, hyperCheckBox, etkaCheckBox, canboCheckBox;
    SeekBar priceRang;
    TextView seekBarValue;
    Button toolbarBackBtn , toolbarSortBtn , toolbarFilterBtn , closeFilterDialog , closeSortDialog;
    TextView toolbarTv ;
    RecyclerView.OnItemTouchListener listener;

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        allProducts = new ArrayList<>();
        productList = new ArrayList<>();
        intent = getIntent();
        retry = (Button) findViewById(R.id.network_try_again);
        loading = (RelativeLayout) findViewById(R.id.loading_layout);
        loading.setVisibility(View.VISIBLE);
        recycler = (RecyclerView) findViewById(R.id.product_recycler);
        toolbarBackBtn = (Button) findViewById(R.id.back_icon);
        toolbarFilterBtn = (Button) findViewById(R.id.filter_icon);
        toolbarSortBtn = (Button) findViewById(R.id.sort_icon);
        toolbarTv = (TextView) findViewById(R.id.products_list_toolbar_tv);

        //filter dialog
        filterDialog = new Dialog(this);
        filterDialog.setContentView(R.layout.filter_dialog_layout);
        stockSwitch = filterDialog.findViewById(R.id.stock_switch);
        canboCheckBox = filterDialog.findViewById(R.id.canbo_check);
        refahCheckBox = filterDialog.findViewById(R.id.refah_check);
        hyperCheckBox = filterDialog.findViewById(R.id.hyperstar_check);
        etkaCheckBox = filterDialog.findViewById(R.id.etka_check);
        priceRang = filterDialog.findViewById(R.id.price_range);
        seekBarValue = filterDialog.findViewById(R.id.seekbar_text);
        closeFilterDialog = filterDialog.findViewById(R.id.close_filter_dialog);
        priceRang.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                filter();
                seekBarValue.setText( "تا " + progress * 1000 + " تومان");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        for (Store store : AppController.getInstance().getStores()) {
            if (store.getStoreId() == Store.REFAH_STORE_ID) {
                refahCheckBox.setChecked(true);
                if(!intent.hasExtra("storeBranchId"))
                    refahCheckBox.setEnabled(true);
            }
            if (store.getStoreId() == Store.CANBO_STORE_ID) {
                canboCheckBox.setChecked(true);
                if(!intent.hasExtra("storeBranchId"))
                    canboCheckBox.setEnabled(true);
            }
            if (store.getStoreId() == Store.HYPERSTAR_STORE_ID) {
                hyperCheckBox.setChecked(true);
                if(!intent.hasExtra("storeBranchId"))
                    hyperCheckBox.setEnabled(true);
            }
            if (store.getStoreId() == Store.ETKA_STORE_ID) {
                etkaCheckBox.setChecked(true);
                if(!intent.hasExtra("storeBranchId"))
                    etkaCheckBox.setEnabled(true);
            }
        }
        stockSwitch.setOnCheckedChangeListener(this );
        refahCheckBox.setOnCheckedChangeListener( this);
        canboCheckBox.setOnCheckedChangeListener( this);
        hyperCheckBox.setOnCheckedChangeListener( this);
        etkaCheckBox.setOnCheckedChangeListener(this );
        closeFilterDialog.setOnClickListener(this);

        //sort dialog
        sortDialog = new Dialog(this);
        sortDialog.setContentView(R.layout.sort_dialog_layout);
        defaultSort = sortDialog.findViewById(R.id.default_radio);
        lessPriceSort = sortDialog.findViewById(R.id.less_price_radio);
        mostDiscountSort = sortDialog.findViewById(R.id.most_discount_radio);
        closeSortDialog = sortDialog.findViewById(R.id.close_sort_dialog);
        defaultSort.setOnClickListener(this);
        lessPriceSort.setOnClickListener(this);
        mostDiscountSort.setOnClickListener(this);
        closeSortDialog.setOnClickListener(this);

        if(intent.hasExtra("discounts")) {
            toolbarSortBtn.setVisibility(View.GONE);
            mostDiscountSort.setChecked(true);
        }
        toolbarTv.setText(intent.getStringExtra("toolbarTitle"));
        productList = new ArrayList<>();
        loadProducts();
        swipe = (SwipeRefreshLayout) findViewById(R.id.products_recycler_refresh);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadProducts();
            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.connection_error_layout).setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                loadProducts();
            }
        });

        toolbarBackBtn.setOnClickListener(this);
        toolbarSortBtn.setOnClickListener(this);
        toolbarFilterBtn.setOnClickListener(this);
    }

    public void loadProducts (){
        productList.clear();
        adapter = new ProductAdapter(productList, AppController.getInstance().getStores(), ProductListActivity.this, R.layout.product_list_item);
        new DataLoader().getProducts(getWindow().getDecorView().getRootView() , AppController.getInstance().getStores(), new VolleyCallbackProducts() {
            @Override
            public void onSuccess(final List<Product> products, Boolean flag) {
                if(flag){
                    if (intent.hasExtra("brandName")) {
                        BRAND = intent.getStringExtra("brandName");
                        for (Product p : products) {
                            if (p.getName().contains(BRAND) )
                                productList.add(p);
                        }
                        sort(productList);
                    } else if (intent.hasExtra("categoryId")) {
                        CATEGORY_ID = intent.getIntExtra("categoryId", 0);
                        for (Product p : products) {
                            if (p.getCategory() == CATEGORY_ID ) {
                                productList.add(p);
                            }
                        }
                    } else if (intent.hasExtra("branchId")) {
                        BRANCH_ID = intent.getIntExtra("branchId", 0);
                        for (Product p : products) {
                            if (p.getStoreId() == BRANCH_ID )
                                productList.add(p);
                        }
                    } else if (intent.hasExtra("discounts")) {
                        for (Product p : products) {
                            if (p.getDiscount() > 15 )
                                productList.add(p);
                        }
                    } else if (intent.hasExtra("storeBranchId")) {
                        STORE_Id = intent.getIntExtra("storeBranchId", 0);
                        for (Product p : products) {
                            if (p.getStoreBranchId() == STORE_Id )
                                productList.add(p);
                        }
                    }
                    allProducts.clear();
                    allProducts.addAll(productList);
                    filter();
                    recycler.setLayoutManager(new LinearLayoutManager(ProductListActivity.this));
                    recycler.removeOnItemTouchListener(listener);
                    listener = new RecyclerTouchListener(ProductListActivity.this, recycler, new ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Intent intent = new Intent(ProductListActivity.this, ProductDetailsActivity.class);
                            adapter.productListOnclickListener(position, intent);
                            startActivity(intent);
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    });
                    recycler.addOnItemTouchListener(listener);
                    //add this line because of "setVisibility" method in "product_list_item.xml" view in adapter.
                    recycler.getRecycledViewPool().setMaxRecycledViews(0, 0);
                    loading.setVisibility(View.GONE);
                    if (swipe.isRefreshing())
                        swipe.setRefreshing(false);
                }
            }
        });

    }

    public void filter (){
        FilterProducts filterProducts= new FilterProducts();
        productList = filterProducts.getFilteredList(allProducts , refahCheckBox.isChecked(), canboCheckBox.isChecked()
                , hyperCheckBox.isChecked() , etkaCheckBox.isChecked() , priceRang.getProgress(), stockSwitch.isChecked());
        sort(productList);

    }
    @Override
    public void onBackPressed() {
        finish();
    }

    public void sort(List<Product> products){
        if(defaultSort.isChecked())
            SortProducts.sortByDefault(products);
        else if(mostDiscountSort.isChecked())
            SortProducts.sortByMostDiscount(products);
        else if(lessPriceSort.isChecked())
            SortProducts.sortByLessPrice(products);
        adapter = new ProductAdapter(productList, AppController.getInstance().getStores(), ProductListActivity.this, R.layout.product_list_item);
        recycler.setAdapter(adapter);
    }
    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        switch (v.getId()){

            case R.id.refah_check: {
                filter();
                break;
            }
            case R.id.etka_check: {
                filter();
                break;
            }
            case R.id.canbo_check: {
                filter();
                break;
            }
            case R.id.hyperstar_check: {
                filter();
                break;
            }
            case R.id.stock_switch: {
                filter();
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.default_radio:{
                sort(productList);
                break;
            }case R.id.most_discount_radio:{
                sort(productList);
                break;
            }case R.id.less_price_radio:{
                sort(productList);
                break;
            }
            case R.id.sort_icon: {
                sortDialog.show();
                break;
            }
            case R.id.filter_icon: {
                filterDialog.show();
                break;
            }
            case R.id.back_icon: {
                finish();
                break;
            }
            case R.id.close_filter_dialog :{
                filter();
                filterDialog.dismiss();
                break;
            }
            case R.id.close_sort_dialog :{
                sort(productList);
                sortDialog.dismiss();
                break;
            }
        }
    }


}