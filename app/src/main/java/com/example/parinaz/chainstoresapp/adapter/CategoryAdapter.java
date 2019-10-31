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
import com.example.parinaz.chainstoresapp.object.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parinaz on 7/18/19.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    List<Category> categoryList ;
    Context context;


    public CategoryAdapter(List<Category> categoryList, Context context) {
        this.categoryList = (categoryList==null)? new ArrayList<Category>():categoryList;
        this.context = context;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {
            holder.bind(categoryList.get(position));

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name ;
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.category_name);
            image= itemView.findViewById(R.id.category_image);
            //itemView.setOnClickListener((View.OnClickListener) context);

        }

        public void bind(Category category){
            name.setText(category.getName());
            Picasso.with(context).load(Url.categoriesImageUrl + category.getPicAddress()).into(image);

        }
    }
}
