package com.bsalazar.kekomo.ui_dishes.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd.entities.Dish;
import com.bsalazar.kekomo.general.FileSystem;
import com.bsalazar.kekomo.ui_dishes.DishDetailActivity;
import com.bsalazar.kekomo.ui_dishes.MyDishesActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by bsalazar on 25/05/2017.
 */

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishViewHolder> {

    private Activity mContext;
    private ArrayList<Dish> dishes;

    public DishesAdapter(Activity context, ArrayList<Dish> dishes) {
        this.mContext = context;
        this.dishes = dishes;
    }
    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_item, parent, false);
        return new DishViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DishViewHolder holder, int position) {
        final Dish dish = dishes.get(position);

        holder.dish_name.setText(dish.getName());
        holder.dish_description.setText(dish.getDescription());

        Glide.with(mContext)
                .load(FileSystem.getInstance(mContext).IMAGES_PATH + dish.getImage())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.dish_image);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent intent = new Intent(mContext, DishDetailActivity.class);
                    intent.putExtra("DishID", dish.getId());

                    ActivityOptions optionMultiple = ActivityOptions.makeSceneTransitionAnimation(mContext,
                            Pair.create((View) holder.dish_image, holder.dish_image.getTransitionName()));

                    mContext.startActivityForResult(intent, MyDishesActivity.RESULT_EDIT,  optionMultiple.toBundle());
                } else {
                    Intent intent = new Intent(mContext, DishDetailActivity.class);
                    intent.putExtra("DishID", dish.getId());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class DishViewHolder extends RecyclerView.ViewHolder {

        LinearLayout item;
        ImageView dish_image;
        TextView dish_name, dish_description;

        public DishViewHolder(View itemView) {
            super(itemView);

            item = (LinearLayout) itemView.findViewById(R.id.item);
            dish_image = (ImageView) itemView.findViewById(R.id.dish_image);
            dish_name = (TextView) itemView.findViewById(R.id.dish_name);
            dish_description = (TextView) itemView.findViewById(R.id.dish_description);
        }
    }
}
