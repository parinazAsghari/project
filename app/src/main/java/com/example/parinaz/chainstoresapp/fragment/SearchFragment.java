package com.example.parinaz.chainstoresapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.parinaz.chainstoresapp.activity.ProductDetailsActivity;
import com.example.parinaz.chainstoresapp.adapter.ProductAdapter;
import com.example.parinaz.chainstoresapp.data.DataLoader;
import com.example.parinaz.chainstoresapp.data.VolleyCallbackProducts;
import com.example.parinaz.chainstoresapp.object.Product;
import com.example.parinaz.chainstoresapp.object.Store;

import java.util.ArrayList;
import java.util.List;



public class SearchFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    List<Product> filteredProducts;
    List<Product> products;
    //List<Store> stores ;
    EditText search_et;
    RecyclerView search_recycler;
    ProductAdapter adapter;
    ImageView navigationIcon , clearSearchEditText;
    DrawerLayout drawerLayout;
    SwipeRefreshLayout swipe ;
    //menu
    RadioButton defaultBtn, lessPriceBtn, mostDiscountBtn;
    Switch stockSwitch;
    CheckBox refahCheckBox, hyperCheckBox, etkaCheckBox, canboCheckBox;
    SeekBar priceRang;
    TextView seekBarValue;
    RelativeLayout loading;
    Button retry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        filteredProducts = new ArrayList<>();
        loading = rootView.findViewById(R.id.loading_layout);
        loading.setVisibility(View.GONE);
        retry = rootView.findViewById(R.id.network_try_again);
        search_et = rootView.findViewById(R.id.search_et);
        search_recycler = rootView.findViewById(R.id.search_recycler);
        clearSearchEditText = rootView.findViewById(R.id.clear_search_et);
        search_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        search_recycler.addOnItemTouchListener(new RecyclerTouchListener(getContext(), search_recycler, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                adapter.productListOnclickListener(position , intent);
                startActivity(intent);
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        swipe = rootView.findViewById(R.id.search_recycler_refresh);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.findViewById(R.id.connection_error_layout).setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                loadProducts();
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadProducts();
            }
        });
        clearSearchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_et.setText(null);
            }
        });
        //menu
        navigationIcon = rootView.findViewById(R.id.navigation_icon);
        drawerLayout = rootView.findViewById(R.id.drawer);
        mostDiscountBtn = rootView.findViewById(R.id.most_discount_radio);
        lessPriceBtn = rootView.findViewById(R.id.less_price_radio);
        defaultBtn = rootView.findViewById(R.id.default_radio);
        stockSwitch = rootView.findViewById(R.id.stock_switch);
        canboCheckBox = rootView.findViewById(R.id.canbo_check);
        refahCheckBox = rootView.findViewById(R.id.refah_check);
        hyperCheckBox = rootView.findViewById(R.id.hyperstar_check);
        etkaCheckBox = rootView.findViewById(R.id.etka_check);
        priceRang = rootView.findViewById(R.id.price_range);
        seekBarValue = rootView.findViewById(R.id.seekbar_text);

        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    loading.setVisibility(View.VISIBLE);
                    loadProducts();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //menu
        navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Menu menu = new Menu();
//                menu.getList(filteredProducts);
//                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.navigation_container, menu).commit();
                
                drawerLayout.openDrawer(Gravity.RIGHT);
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
                    drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
        priceRang.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                filter( search_et.getText());
                seekBarValue.setText( "تا " + progress * 1000 + " تومان");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        for(Store store : AppController.getInstance().getStores()){
            if(store.getStoreId() == Store.REFAH_STORE_ID) {
                refahCheckBox.setEnabled(true);
                refahCheckBox.setChecked(true);
            }if(store.getStoreId() == Store.CANBO_STORE_ID) {
                canboCheckBox.setChecked(true);
                canboCheckBox.setEnabled(true);
            }if(store.getStoreId() == Store.HYPERSTAR_STORE_ID) {
                hyperCheckBox.setChecked(true);
                hyperCheckBox.setEnabled(true);
            }if(store.getStoreId() == Store.ETKA_STORE_ID) {
                etkaCheckBox.setChecked(true);
                etkaCheckBox.setEnabled(true);
            }
        }

        defaultBtn.setOnCheckedChangeListener(this);
        lessPriceBtn.setOnCheckedChangeListener(this);
        mostDiscountBtn.setOnCheckedChangeListener(this);
        stockSwitch.setOnCheckedChangeListener(this);
        refahCheckBox.setOnCheckedChangeListener(this);
        canboCheckBox.setOnCheckedChangeListener(this);
        hyperCheckBox.setOnCheckedChangeListener(this);
        etkaCheckBox.setOnCheckedChangeListener(this);
    }

    public void loadProducts(){
        if(AppController.getInstance().storeListIsEmpty())
            getView().findViewById(R.id.connection_error_layout).setVisibility(View.VISIBLE);
        products = new ArrayList<>();
        new DataLoader().getProducts( getView() ,AppController.getInstance().getStores() , new VolleyCallbackProducts() {
            @Override
            public void onSuccess(List<Product> productList, Boolean flag) {
                if (flag){
                    products = productList;
                    filter(search_et.getText());
                    loading.setVisibility(View.GONE);
                    if (swipe.isRefreshing())
                        swipe.setRefreshing(false);
                }
            }
        });
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if(!filteredProducts.isEmpty())
//            filter( search_et.getText());

            switch (buttonView.getId()) {
                case R.id.refah_check: {
                    filter(search_et.getText());
                    break;
                }
                case R.id.etka_check: {
                    filter(search_et.getText());
                    break;
                }
                case R.id.canbo_check: {
                    filter(search_et.getText());
                    break;
                }
                case R.id.hyperstar_check: {
                    filter(search_et.getText());
                    break;
                }
                case R.id.stock_switch: {
                    filter(search_et.getText());
                    break;
                }
                case R.id.default_radio: {
                    sort(filteredProducts);
                    break;
                }
                case R.id.most_discount_radio: {
                    sort(filteredProducts);
                    break;
                }
                case R.id.less_price_radio: {
                    sort(filteredProducts);
                    break;
                }
            }

    }

    public void filter (CharSequence charSequence) {
        List<Product> searchedProducts = new ArrayList<>();
        filteredProducts.clear();
        if (charSequence.length() == 0) {
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }else {
            for (Product p : products) {
                if (p.getName().contains(charSequence)) {
                    searchedProducts.add(p);
                }
                FilterProducts filterProducts= new FilterProducts();
                filteredProducts = filterProducts.getFilteredList(searchedProducts , refahCheckBox.isChecked(), canboCheckBox.isChecked()
                        , hyperCheckBox.isChecked() , etkaCheckBox.isChecked() , priceRang.getProgress(), stockSwitch.isChecked());
                adapter = new ProductAdapter(filteredProducts, AppController.getInstance().getStores(), getContext(), R.layout.product_list_item);
                search_recycler.setAdapter(adapter);
                search_recycler.getRecycledViewPool().setMaxRecycledViews(0, 0);

                sort(filteredProducts);
//                search_recycler.setAdapter(adapter);
            }
        }
    }
        public void sort(List<Product> products ){
//            SortProducts sortProducts = new SortProducts(products);
        if (mostDiscountBtn.isChecked()) {
            SortProducts.sortByMostDiscount(products);
        } else if (lessPriceBtn.isChecked()) {
            SortProducts.sortByLessPrice(products);
        } else {
            SortProducts.sortByDefault(products);
        }
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}

//    public void filter (CharSequence charSequence) {
//        filteredProducts.clear();
//        if (charSequence.length() == 0)
//            adapter.notifyDataSetChanged();
//        else {
//            for (Product p : products) {
//                if (p.getName().contains(charSequence)) {
//                    if (stockSwitch.isChecked())
//                        filteredProducts.add(p);
//                    else {
//                        if (p.getStock() == 1)
//                            filteredProducts.add(p);
//                    }
//                    if (!refahCheckBox.isChecked()) {
//                        if (p.getStoreId() == 1)
//                            filteredProducts.remove(p);
//                    }
//                    if (!canboCheckBox.isChecked()) {
//                        if (p.getStoreId() == 2)
//                            filteredProducts.remove(p);
//                    }
//                    if (!hyperCheckBox.isChecked()) {
//                        if (p.getStoreId() == 3)
//                            filteredProducts.remove(p);
//                    }
//                    if (!etkaCheckBox.isChecked()) {
//                        if (p.getStoreId() == 4)
//                            filteredProducts.remove(p);
//                    }
//                    if (p.getPrice() >= (priceRang.getProgress() * 1000)) {
//                        filteredProducts.remove(p);
//                    }
//                }
//                adapter = new ProductAdapter(filteredProducts, stores, getContext(), R.layout.product_list_item);
//                search_recycler.setAdapter(adapter);
//                search_recycler.getRecycledViewPool().setMaxRecycledViews(0, 0);
//            }
//            //sort
//            if (mostDiscountBtn.isChecked()) {
//                Collections.sort(filteredProducts, new Comparator<Product>() {
//                    @Override
//                    public int compare(Product p1, Product p2) {
//                        if (p1.getDiscount() == p2.getDiscount())
//                            return (p1.getReducedPrice() > p2.getReducedPrice()) ? -1 : 1;
//                        return (p1.getDiscount() > p2.getDiscount()) ? -1 : 1;
//                    }
//                });
//            } else if (lessPriceBtn.isChecked()) {
//                Collections.sort(filteredProducts, new Comparator<Product>() {
//                    @Override
//                    public int compare(Product p1, Product p2) {
//                        if (p1.getPrice() == p2.getPrice())
//                            return (p1.getReducedPrice() > p2.getReducedPrice()) ? 1 : -1;
//                        return (p1.getPrice() > p2.getPrice()) ? 1 : -1;
//                    }
//                });
//            } else {
//                Collections.sort(filteredProducts, new Comparator<Product>() {
//                    @Override
//                    public int compare(Product p1, Product p2) {
//                        return p1.getName().compareTo(p2.getName());
//                    }
//                });
//            }
//            if (adapter != null)
//                adapter.notifyDataSetChanged();
//        }
//    }

