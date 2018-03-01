package com.bsalazar.kekomo.ui_dishes.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.data.entities.Product;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bsalazar on 25/05/2017.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private ArrayList<Product> products;

    public IngredientsAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final IngredientViewHolder holder, final int position) {
        holder.ingredient_name.setText(products.get(holder.getAdapterPosition()).getName());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_name)
        TextView ingredient_name;
        @BindView(R.id.delete_ingredient)
        ImageView delete_ingredient;

        IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            delete_ingredient.setVisibility(View.GONE);
        }
    }
}
