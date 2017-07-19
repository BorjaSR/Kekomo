package com.bsalazar.kekomo.UI_dishes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd.entities.Dish;
import com.bsalazar.kekomo.general.FileSystem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bsalazar on 25/05/2017.
 */

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishViewHolder> {

    private Context mContext;
    private ArrayList<Dish> dishes;

    public DishesAdapter(Context context, ArrayList<Dish> dishes) {
        this.mContext = context;
        this.dishes = dishes;
    }
    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_item, parent, false);
        return new DishViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DishViewHolder holder, int position) {
        final Dish dish = dishes.get(position);

        holder.dish_name.setText(dish.getName());
        holder.dish_description.setText(dish.getDescription());

        Glide.with(mContext)
                .load(FileSystem.getInstance(mContext).IMAGES_PATH + dish.getImage())
                .into(holder.dish_image);
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class DishViewHolder extends RecyclerView.ViewHolder {

        ImageView dish_image;
        TextView dish_name, dish_description;

        public DishViewHolder(View itemView) {
            super(itemView);

            dish_image = (ImageView) itemView.findViewById(R.id.dish_image);
            dish_name = (TextView) itemView.findViewById(R.id.dish_name);
            dish_description = (TextView) itemView.findViewById(R.id.dish_description);
        }
    }
}
