package com.example.parinaz.chainstoresapp.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.parinaz.chainstoresapp.AppController;
import com.example.parinaz.chainstoresapp.ClickListener;
import com.example.parinaz.chainstoresapp.MarkedProductsDBHelper;
import com.example.parinaz.chainstoresapp.R;
import com.example.parinaz.chainstoresapp.RecyclerTouchListener;
import com.example.parinaz.chainstoresapp.activity.ProductDetailsActivity;
import com.example.parinaz.chainstoresapp.adapter.MarkedProductsAdapter;
import com.example.parinaz.chainstoresapp.object.MarkedProducts;

import java.util.List;

public class marked_list_fragment extends Fragment {
    RecyclerView markedListRecycler;
    RelativeLayout loading;
    MarkedProductsAdapter adapter;
    SwipeRefreshLayout swipe ;
    RecyclerView.OnItemTouchListener listener;
    Button retry;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.marked_list_fragment, container, false);
    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retry = view.findViewById(R.id.network_try_again);
        loading = view.findViewById(R.id.loading_layout);
        loading.setVisibility(View.VISIBLE);
        markedListRecycler = view.findViewById(R.id.marked_list_recycler);
        swipe = view.findViewById(R.id.marked_list_recycler_refresh);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.connection_error_layout).setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                loadMarkedProducts();
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMarkedProducts();
            }
        });
    }
    public void loadMarkedProducts(){
        new MarkedListLoader().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMarkedProducts();
    }

    class MarkedListLoader extends AsyncTask<Void, Void ,List<MarkedProducts>> {
        @Override
        protected List<MarkedProducts> doInBackground(Void... voids) {
            MarkedProductsDBHelper dbHelper = new MarkedProductsDBHelper(getContext());
            List<MarkedProducts> list = dbHelper.getAll();
            dbHelper.close();
            return list;
        }
        @Override
        protected void onPostExecute(final List<MarkedProducts> list) {
            super.onPostExecute(list);
            adapter = new MarkedProductsAdapter(list, AppController.getInstance().getStores(), getContext(), R.layout.product_list_item);
            if(markedListRecycler!= null) {
                markedListRecycler.setAdapter(adapter);
                loading.setVisibility(View.GONE);
                markedListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
             markedListRecycler.removeOnItemTouchListener(listener);
              listener=new RecyclerTouchListener(getContext(), markedListRecycler, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                        //intent.putExtra("marked" , " "); //?????
                        adapter.productListOnclickListener(position , intent);
                        startActivity(intent);
                    }
                    @Override
                    public void onLongClick(View view, int position) {

                    }
                });
                markedListRecycler.addOnItemTouchListener(listener);
                markedListRecycler.getRecycledViewPool().setMaxRecycledViews(0, 0);
            }
            if (swipe.isRefreshing())
                swipe.setRefreshing(false);
        }
    }
}