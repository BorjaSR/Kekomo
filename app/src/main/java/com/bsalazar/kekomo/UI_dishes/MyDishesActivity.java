package com.bsalazar.kekomo.UI_dishes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd.controllers.DishesController;
import com.bsalazar.kekomo.bbdd.entities.Dish;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by bsalazar on 22/06/2017.
 */

public class MyDishesActivity extends AppCompatActivity {


    private RecyclerView my_dishes_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dishes);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Dish> dishes = new ArrayList<>();
        try {
            dishes = new DishesController().getAll();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        my_dishes_recycler = (RecyclerView) findViewById(R.id.my_dishes_recycler);
        my_dishes_recycler.setHasFixedSize(false);
        my_dishes_recycler.setLayoutManager(new LinearLayoutManager(this));
        DishesAdapter adapter = new DishesAdapter(this, dishes);
        my_dishes_recycler.setAdapter(adapter);
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
