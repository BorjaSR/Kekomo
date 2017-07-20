package com.bsalazar.kekomo.ui_dishes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd.controllers.DishesController;
import com.bsalazar.kekomo.bbdd.entities.Dish;

import java.util.ArrayList;

/**
 * Created by bsalazar on 22/06/2017.
 */

public class MyDishesActivity extends AppCompatActivity {

    private TextView add_dish;
    private RecyclerView my_dishes_recycler;
    private DishesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dishes);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add_dish = (TextView) findViewById(R.id.add_dish);

        my_dishes_recycler = (RecyclerView) findViewById(R.id.my_dishes_recycler);
        my_dishes_recycler.setHasFixedSize(false);
        my_dishes_recycler.setLayoutManager(new LinearLayoutManager(this));

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

        ArrayList<Dish> dishes = new DishesController().getAll();

        adapter = new DishesAdapter(this, dishes);
        my_dishes_recycler.setAdapter(adapter);

        if(dishes.size() == 0)
            findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.empty_list).setVisibility(View.GONE);
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

}
