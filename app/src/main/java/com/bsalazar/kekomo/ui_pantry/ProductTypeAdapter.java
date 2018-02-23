package com.bsalazar.kekomo.ui_pantry;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.data.entities.Product;

import java.util.ArrayList;

/**
 * Created by bsalazar on 19/10/17.
 */

public class ProductTypeAdapter extends RecyclerView.Adapter<ProductTypeAdapter.ProductTypeViewHolder> {


    private ArrayList<ProductType> productsType;
    private Context mContext;


    public ProductTypeAdapter(Activity context) {
        this.mContext = context;
        productsType = new ArrayList<>();
        productsType.add(new ProductType(Product.MEAT, R.drawable.steak, "Carne"));
        productsType.add(new ProductType(Product.FISH, R.drawable.fish, "Pescado"));
        productsType.add(new ProductType(Product.VEGETABLE, R.drawable.vegetables, "Verduras"));
        productsType.add(new ProductType(Product.DAIRY, R.drawable.dairy, "Lacteos"));
        productsType.add(new ProductType(Product.SAUCE, R.drawable.sauces, "Salsa"));
        productsType.add(new ProductType(Product.FRUIT, R.drawable.fruit, "Fruta"));
    }

    @Override
    public ProductTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_type_item, parent, false);
        return new ProductTypeAdapter.ProductTypeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductTypeViewHolder holder, int position) {
        ProductType productType = productsType.get(holder.getAdapterPosition());

        if(productType != null){
            holder.image_type.setVisibility(View.VISIBLE);
            holder.name_type.setVisibility(View.VISIBLE);
            holder.image_type.setImageResource(productType.resource);
            holder.name_type.setText(productType.name);
            holder.setViewTag(productType.type);

        } else {
            holder.image_type.setVisibility(View.INVISIBLE);
            holder.name_type.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return productsType.size();
    }

    public class ProductTypeViewHolder extends RecyclerView.ViewHolder {

        View view;

        ImageView image_type;
        TextView name_type;


        public ProductTypeViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            image_type = (ImageView) itemView.findViewById(R.id.image_type);
            name_type = (TextView) itemView.findViewById(R.id.name_type);

        }

        public void setViewTag(int tag){
            view.setTag(tag);
        }
    }

    private class ProductType {

        private int type;
        private int resource;
        private String name;

        public ProductType(int type, int resource, String name) {
            this.type = type;
            this.resource = resource;
            this.name = name;
        }
    }
}
