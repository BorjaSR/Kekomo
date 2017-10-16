package com.bsalazar.kekomo.ui_pantry;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextClock;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd.entities.Product;

import java.util.ArrayList;

/**
 * Created by bsalazar on 25/05/2017.
 */

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ProductViewHolder> {

    private Activity mContext;
    private ArrayList<Product> products;

    public PantryAdapter(Activity context, ArrayList<Product> products) {
        this.mContext = context;
        this.products = products;
    }
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final Product product = products.get(holder.getAdapterPosition());

        holder.pantry_product_image.setImageResource(getProductIcon(product.getType()));
        holder.pantry_product_name.setText(product.getName());
        holder.pantry_product_frozen.setVisibility(product.isFrozen() ? View.VISIBLE : View.GONE);

        holder.pantry_product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PantryActivity)mContext).displayPopupWindow(holder.pantry_product_name, product);
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }



    private int getProductIcon(int type) {
        switch (type){
            case Product.MEAT:
                return R.drawable.steak;
            case Product.FISH:
                return R.drawable.fish;
            case Product.VEGETABLE:
                return R.drawable.vegetables;
            case Product.DAIRY:
                return R.drawable.dairy;
            case Product.SAUCE:
                return R.drawable.sauces;
            case Product.FRUIT:
                return R.drawable.fruit;
            default:
                return R.drawable.steak;
        }

    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView pantry_product_frozen;
        ImageView pantry_product_image;
        TextView pantry_product_name;


        public ProductViewHolder(View itemView) {
            super(itemView);

            pantry_product_frozen = (ImageView) itemView.findViewById(R.id.pantry_product_frozen);
            pantry_product_image = (ImageView) itemView.findViewById(R.id.pantry_product_image);
            pantry_product_name = (TextView) itemView.findViewById(R.id.pantry_product_name);

        }
    }
}
