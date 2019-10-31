package com.example.parinaz.chainstoresapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parinaz.chainstoresapp.R;
import com.example.parinaz.chainstoresapp.data.Url;
import com.example.parinaz.chainstoresapp.object.Store;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parinaz on 7/23/19.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
        List<Store> stores;
        Context context;


public StoreAdapter(List<Store> stores, Context context) {
        this.stores = (stores==null)? new ArrayList<Store>():stores;
        this.context = context;

        }

@Override
public StoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stores_home_list_item,parent,false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(StoreAdapter.ViewHolder holder, int position) {
        holder.bind(stores.get(position));
        }

@Override
public int getItemCount() {
        return stores.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView storeName;
    public ViewHolder(View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.home_stores_list_img);
        storeName = itemView.findViewById(R.id.store_name);
    }

    public  void bind(Store store){

       // Picasso.with(context).load("http://192.168.43.208/chainStoreDB/image/stores/"+store.getPicAdress()).into(imageView);
        Picasso.with(context).load(Url.storesImageUrl + store.getPicAddress()).into(imageView);
        storeName.setText(store.getName());
    }
}
}
