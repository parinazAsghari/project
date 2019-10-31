package com.example.parinaz.chainstoresapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.parinaz.chainstoresapp.R;
import com.example.parinaz.chainstoresapp.data.Url;
import com.example.parinaz.chainstoresapp.object.Brand;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by parinaz on 7/28/19.
 */

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {
    List<Brand> brandList;
    Context context;

    public BrandAdapter(List<Brand> brandList, Context context) {
        this.brandList = brandList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.brands_home_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(brandList.get(position));
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.home_brands_list_img);
        }

        public void bind(Brand brand) {
            Picasso.with(context).load(Url.brandsImageUrl + brand.getPicAddress()).into(image);
            Log.i("image brand", brand.getPicAddress());
        }
    }
}
