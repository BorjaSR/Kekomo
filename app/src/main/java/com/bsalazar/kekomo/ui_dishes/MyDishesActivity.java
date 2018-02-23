package com.bsalazar.kekomo.ui_dishes;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.data.LocalDataSource;
import com.bsalazar.kekomo.data.entities.Dish;
import com.bsalazar.kekomo.ui_dishes.adapters.DishesAdapter;

import java.util.ArrayList;

/**
 * Created by bsalazar on 22/06/2017.
 */

public class MyDishesActivity extends AppCompatActivity {

    private TextView add_dish;
    private RecyclerView my_dishes_recycler;
    private DishesAdapter adapter;
    private ArrayList<Dish> dishes;

    public static final int RESULT_EDIT = 0;
    public static final String RESULT_EDIT_ID = "DishID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dishes);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Fade());
            getWindow().setAllowEnterTransitionOverlap(false);
            getWindow().setAllowReturnTransitionOverlap(false);
        }

        add_dish = findViewById(R.id.add_dish);
        my_dishes_recycler = findViewById(R.id.my_dishes_recycler);
        my_dishes_recycler.setHasFixedSize(false);
        my_dishes_recycler.setLayoutManager(new LinearLayoutManager(this));

        dishes = (ArrayList<Dish>) LocalDataSource.getInstance(this).getAllDishes();

        adapter = new DishesAdapter(this, dishes);
        my_dishes_recycler.setAdapter(adapter);

        if (dishes.size() == 0)
            findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.empty_list).setVisibility(View.GONE);

        add_dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewDishActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_EDIT) {
            if (resultCode == Activity.RESULT_OK) {
                int dishId = data.getExtras().getInt(RESULT_EDIT_ID, -1);
                if (dishId != -1){
                    for (int i = 0; i < dishes.size(); i++)
                        if(dishes.get(i).getId() == dishId)
                            adapter.notifyItemChanged(i);
                }
            }
        }
    }
}
