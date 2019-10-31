package com.example.parinaz.chainstoresapp.fragment;

import android.content.Intent;
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
import com.example.parinaz.chainstoresapp.R;
import com.example.parinaz.chainstoresapp.RecyclerTouchListener;
import com.example.parinaz.chainstoresapp.activity.ProductListActivity;
import com.example.parinaz.chainstoresapp.adapter.CategoryAdapter;
import com.example.parinaz.chainstoresapp.data.DataLoader;
import com.example.parinaz.chainstoresapp.data.VolleyCallbackCategories;
import com.example.parinaz.chainstoresapp.object.Category;

import java.util.List;

/**
 * Created by parinaz on 7/17/19.
 */

public class CategoryFragment extends Fragment {
    RecyclerView recyclerView;
    CategoryAdapter adapter;
    SwipeRefreshLayout swipe;
    RelativeLayout loading;
    RecyclerView.OnItemTouchListener listener;
    Button retry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retry = view.findViewById(R.id.network_try_again);
        loading = view.findViewById(R.id.loading_layout);
        loading.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.category_recycler);
        swipe = view.findViewById(R.id.category_recycler_refresh);
        loadCategory();
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.connection_error_layout).setVisibility(View.GONE);
                loadCategory();
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCategory();
            }
        });


    }

    public void loadCategory() {
        LayoutInflater.from(getContext()).inflate(R.layout.category_fragment, null);
        if (AppController.getInstance().storeListIsEmpty()) {
            getView().findViewById(R.id.connection_error_layout).setVisibility(View.VISIBLE);
        }
        else {
            new DataLoader().getCategory(getView(), new VolleyCallbackCategories() {
                @Override
                public void onSuccess(final List<Category> categories) {

                    adapter = new CategoryAdapter(categories, getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                    recyclerView.removeOnItemTouchListener(listener);
                    listener=new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Intent intent = new Intent(getContext(), ProductListActivity.class);
                            intent.putExtra("categoryId", categories.get(position).getId());
                            intent.putExtra("toolbarTitle", categories.get(position).getName());

                            getContext().startActivity(intent);
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    });
                    recyclerView.addOnItemTouchListener(listener);
                    loading.setVisibility(View.GONE);
                    if (swipe.isRefreshing())
                        swipe.setRefreshing(false);
                }
            });
        }

    }
}