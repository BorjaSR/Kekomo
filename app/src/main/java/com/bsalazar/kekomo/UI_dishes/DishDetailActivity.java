package com.bsalazar.kekomo.ui_dishes;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd.controllers.DishesController;
import com.bsalazar.kekomo.bbdd.entities.Dish;
import com.bsalazar.kekomo.general.FileSystem;
import com.bumptech.glide.Glide;

import java.io.IOException;

/**
 * Created by bsalazar on 20/07/2017.
 */

public class DishDetailActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private ImageView dish_image;
    private TextView dish_name, dish_description, dish_preparation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.END);
            getWindow().setEnterTransition(slide);
            getWindow().setAllowEnterTransitionOverlap(false);
            getWindow().setAllowReturnTransitionOverlap(false);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.activity_dish_detail);
        }
        int dishID = -1;
        if (getIntent().getExtras() != null)
            dishID = (int) getIntent().getExtras().get("DishID");

        Dish dish = null;
        if(dishID != -1)
            dish = new DishesController().getByID(dishID);


        dish_image = (ImageView) findViewById(R.id.dish_image);
        dish_name = (TextView) findViewById(R.id.dish_name);
        dish_description = (TextView) findViewById(R.id.dish_description);
        dish_preparation = (TextView) findViewById(R.id.dish_preparation);

        if(dish != null){
            dish_name.setText(dish.getName());
            dish_description.setText(dish.getDescription());
            dish_preparation.setText(dish.getPreparation());
        }

        Glide.with(this)
                .load(FileSystem.getInstance(this).IMAGES_PATH + dish.getImage())
                .asBitmap()
                .into(dish_image);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return true;
    }
}
