package com.example.parinaz.chainstoresapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parinaz.chainstoresapp.R;
import com.example.parinaz.chainstoresapp.data.Url;
import com.example.parinaz.chainstoresapp.object.Product;
import com.example.parinaz.chainstoresapp.object.Store;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parinaz on 7/19/19.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<Product> productList;
    Context context;
    List<Store> storeList;
    int xmlView;

    //ToggleButton mark;
   // MarkedProductsDBHelper dbHelper;


    public ProductAdapter(List<Product> productList,List<Store> storeList, Context context ,int xmlView) {
        this.productList = (productList==null) ? new ArrayList<Product>():productList;

        this.context = context;
        this.xmlView = xmlView;
        this.storeList=(storeList==null) ? new ArrayList<Store>():storeList;
       // this.dbHelper=new MarkedProductsDBHelper(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(xmlView , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name , price , reducedPrice , discount , stock;
        ImageView image , storeIcon;
         public   Button mRemoveButton;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name_tv);
            price = itemView.findViewById(R.id.price_tv);
            reducedPrice = itemView.findViewById(R.id.reduced_price_tv);
            discount = itemView.findViewById(R.id.discount_tv);
            image = itemView.findViewById(R.id.product_image);
            stock = itemView.findViewById(R.id.product_stock);
            storeIcon = itemView.findViewById(R.id.product_store_icon);
            mRemoveButton = itemView.findViewById(R.id.ib_remove);
        }
        public void bind(Product product) {
            if(product.getDiscount() == 0){
                discount.setVisibility(View.GONE);
                reducedPrice.setVisibility(View.GONE);
                price.setTextColor(context.getResources().getColor(R.color.text_color));
                price.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.largeTextSize));
            } else {
                discount.setText(product.getDiscount() + "% تخفیف");
                reducedPrice.setText(product.getReducedPrice() + " تومان");
                price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            name.setText(product.getName());
            price.setText(product.getPrice() + " تومان");
            Picasso.with(context).load(Url.productsImageUrl  + product.getPicAddress()).into(image);
            for(Store s : storeList) {
                if (product.getStoreBranchId() == s.getBranchId()) {
                    Picasso.with(context).load(Url.storesImageUrl + s.getPicAddress()).into(storeIcon);
                }

            }
            if(product.getStock() == 0) {
                stock.setVisibility(View.VISIBLE);
                price.setVisibility(View.GONE);
                reducedPrice.setVisibility(View.GONE);
                discount.setVisibility(View.GONE);
            }else {
                stock.setVisibility(View.GONE);
            }

        }

    }
    public void productListOnclickListener(int position , Intent intent){
        intent.putExtra("productName" , productList.get(position).getName());
        intent.putExtra("productDiscount" , productList.get(position).getDiscount());
        intent.putExtra("productPrice" , productList.get(position).getPrice());
        intent.putExtra("productReducedPrice" , productList.get(position).getReducedPrice());
        intent.putExtra("productImage" , productList.get(position).getPicAddress());
        intent.putExtra("productCode" , productList.get(position).getCode());
        intent.putExtra("productCategory" , productList.get(position).getCategory());
        intent.putExtra("productStoreBranchId" , productList.get(position).getStoreBranchId());
        intent.putExtra("stock" , productList.get(position).getStock());
    }
}
