package com.example.parinaz.chainstoresapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.parinaz.chainstoresapp.AppController;
import com.example.parinaz.chainstoresapp.ClickListener;
import com.example.parinaz.chainstoresapp.R;
import com.example.parinaz.chainstoresapp.RecyclerTouchListener;
import com.example.parinaz.chainstoresapp.SortProducts;
import com.example.parinaz.chainstoresapp.activity.ProductDetailsActivity;
import com.example.parinaz.chainstoresapp.activity.ProductListActivity;
import com.example.parinaz.chainstoresapp.adapter.BrandAdapter;
import com.example.parinaz.chainstoresapp.adapter.ProductAdapter;
import com.example.parinaz.chainstoresapp.adapter.StoreAdapter;
import com.example.parinaz.chainstoresapp.data.DataLoader;
import com.example.parinaz.chainstoresapp.data.VolleyCallbackBrands;
import com.example.parinaz.chainstoresapp.data.VolleyCallbackProducts;
import com.example.parinaz.chainstoresapp.object.Brand;
import com.example.parinaz.chainstoresapp.object.Product;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    RecyclerView discountsRecycler, storesRecycler, brandsRecycler;
    ProductAdapter discountsAdapter;
    StoreAdapter storeAdapter;
    BrandAdapter brandsAdapter;
    TextView moreDiscounts;
    RelativeLayout loading;
    Button retry ;
    private static final int PERMISSION_REQ_CODE = 1234;

    public static double lat;
    public  static double lon;
    List<Product> discountsList ;
    List<Product> products ;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        discountsList = new ArrayList<>();
        products = new ArrayList<>();

        retry = view.findViewById(R.id.network_try_again);
        loading = view.findViewById(R.id.loading_layout);
        loading.setVisibility(View.VISIBLE);
        discountsRecycler = view.findViewById(R.id.home_recycler_discounts);
        storesRecycler = view.findViewById(R.id.home_recycler_stores);
        brandsRecycler = view.findViewById(R.id.home_recycler_brands);
        moreDiscounts = view.findViewById(R.id.discounts_more_tv);
        moreDiscounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                moreDiscounts.setTextColor(getResources().getColor(R.color.gray));
                Intent intent = new Intent(getContext(), ProductListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("discounts", true);
                intent.putExtra("toolbarTitle" , "بیشترین تخفیف ها");
                startActivity(intent);
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.connection_error_layout).setVisibility(View.GONE);
                loadBrands();
                loadProducts();
            }
        });
        storeAdapter = new StoreAdapter(AppController.getInstance().getStores(), getContext());
        storesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
        storesRecycler.setAdapter(storeAdapter);

        storesRecycler.addOnItemTouchListener(new RecyclerTouchListener(getContext(), storesRecycler, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), ProductListActivity.class);
                intent.putExtra("storeBranchId", AppController.getInstance().getStores().get(position).getBranchId());
                intent.putExtra("toolbarTitle",AppController.getInstance().getStores().get(position).getName());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        loadBrands();
        loadProducts();
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission denied to access location", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
    public void loadBrands(){
        new DataLoader().getBrand(getView(), new VolleyCallbackBrands() {
            @Override
            public void onSuccess(final List<Brand> brands) {
                brandsAdapter = new BrandAdapter(brands, getContext());
                brandsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, true));
                brandsRecycler.setAdapter(brandsAdapter);
                brandsRecycler.addOnItemTouchListener(new RecyclerTouchListener(getContext(), brandsRecycler, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(getContext(), ProductListActivity.class);
                        intent.putExtra("brandName", brands.get(position).getName());
                        intent.putExtra("toolbarTitle", brands.get(position).getName());
                        getContext().startActivity(intent);
                    }
                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }
        });
    }
    public void loadProducts () {
        discountsList.clear();
        products.clear();
        new DataLoader().getProducts(getView(), AppController.getInstance().getStores() , new VolleyCallbackProducts() {
            @Override
            public void onSuccess(List<Product> list , Boolean flag) {
                if(flag){
                    products = list;
                    //sort by most discount
                    SortProducts.sortByMostDiscount(products);
                    for (Product p : products) {
                        if (p.getDiscount() > 15 && discountsList.size() < 10) {
                            discountsList.add(p);
                        }
                    }
                    discountsAdapter = new ProductAdapter(discountsList, AppController.getInstance().getStores(), getContext(), R.layout.product_horizontal_list_item);
                    discountsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
                    discountsRecycler.setAdapter(discountsAdapter);
                    discountsRecycler.getRecycledViewPool().setMaxRecycledViews(0, 0);
                    discountsRecycler.addOnItemTouchListener(new RecyclerTouchListener(getContext(), discountsRecycler, new ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                            intent.putExtra("productName", discountsList.get(position).getName());
                            discountsAdapter.productListOnclickListener(position, intent);
                            startActivity(intent);
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }
}