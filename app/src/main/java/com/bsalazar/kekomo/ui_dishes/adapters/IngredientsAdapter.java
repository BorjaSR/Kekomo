package com.bsalazar.kekomo.ui_dishes.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd_room.entities.Product;

import java.util.ArrayList;

/**
 * Created by bsalazar on 25/05/2017.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private Activity mContext;
    private ArrayList<Product> products;

    private IngredientsAdapter.OnClickDelete onClickDelete;

    public IngredientsAdapter(Activity context, ArrayList<Product> products) {
        this.mContext = context;
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

        holder.delete_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickDelete != null)
                    onClickDelete.onDeleteItem(products.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView ingredient_name;
        ImageView delete_ingredient;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredient_name = (TextView) itemView.findViewById(R.id.ingredient_name);
            delete_ingredient = (ImageView) itemView.findViewById(R.id.delete_ingredient);
        }
    }

    public void setOnClickDelete(IngredientsAdapter.OnClickDelete onClickDelete) {
        this.onClickDelete = onClickDelete;
    }

    public interface OnClickDelete {
        void onDeleteItem(Product product, int position);
    }
}
