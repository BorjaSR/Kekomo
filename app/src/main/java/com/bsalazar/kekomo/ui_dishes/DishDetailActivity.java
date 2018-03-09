package com.bsalazar.kekomo.ui_dishes;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.data.LocalDataSource;
import com.bsalazar.kekomo.data.entities.Dish;
import com.bsalazar.kekomo.general.FileSystem;
import com.bsalazar.kekomo.ui_dishes.adapters.IngredientsAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bsalazar on 20/07/2017.
 */

public class DishDetailActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private ImageView dish_image;
    private TextView dish_name, dish_description, dish_preparation;
    @BindView(R.id.rv_ingredients)
    RecyclerView rv_ingredients;

    private int dishID;
    private Dish dish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.END);
            getWindow().setEnterTransition(slide);
            getWindow().setAllowEnterTransitionOverlap(false);
            getWindow().setAllowReturnTransitionOverlap(false);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.activity_dish_detail);
        }

        dishID = -1;
        if (getIntent().getExtras() != null)
            dishID = (int) getIntent().getExtras().get("DishID");

        dish_image = findViewById(R.id.dish_image);
        dish_name = findViewById(R.id.dish_name);
        dish_description = findViewById(R.id.dish_description);
        dish_preparation = findViewById(R.id.dish_preparation);
    }

    @Override
    protected void onResume() {
        super.onResume();

        dish = null;
        if(dishID != -1)
            dish = LocalDataSource.getInstance(this).getDishByID(dishID);

        if(dish != null){
            dish_name.setText(dish.getName());
            dish_description.setText(dish.getDescription());
            dish_preparation.setText(dish.getPreparation());

            rv_ingredients.setHasFixedSize(true);
            IngredientsAdapter adapter = new IngredientsAdapter(dish.getProducts());
            rv_ingredients.setAdapter(adapter);

            Glide.with(this)
                    .load(FileSystem.getInstance(this).IMAGES_PATH + dish.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(dish_image);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dish_detail, menu);
        return true;
    }

    public boolean edit = false;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(edit){
                    Intent intent = getIntent();
                    intent.putExtra(MyDishesActivity.RESULT_EDIT_ID, dishID);
                    setResult(Activity.RESULT_OK, intent);
                } else
                    setResult(Activity.RESULT_CANCELED, null);

                onBackPressed();
                return true;
            case R.id.action_edit:
                edit = true;
                Intent intent = new Intent(this, NewDishActivity.class);
                intent.putExtra("DISH_TO_EDIT", dishID);
                startActivity(intent);
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(edit){
            Intent intent = getIntent();
            intent.putExtra(MyDishesActivity.RESULT_EDIT_ID, dishID);
            setResult(Activity.RESULT_OK, intent);
        } else
            setResult(Activity.RESULT_CANCELED, null);

    }
}
