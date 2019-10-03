package com.bsalazar.kekomo;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bsalazar.kekomo.data.LocalDataSource;
import com.bsalazar.kekomo.data.entities.Dish;
import com.bsalazar.kekomo.data.entities.Event;
import com.bsalazar.kekomo.data.entities.Product;
import com.bsalazar.kekomo.general.Constants;
import com.bsalazar.kekomo.general.ElectionAlgorithm;
import com.bsalazar.kekomo.general.FileSystem;
import com.bsalazar.kekomo.ui_calendar.CalendarActivity;
import com.bsalazar.kekomo.ui_dishes.MyDishesActivity;
import com.bsalazar.kekomo.ui_dishes.NewDishActivity;
import com.bsalazar.kekomo.ui_pantry.PantryActivity;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private TextView text_comer;
  private RelativeLayout next_event_layout;
  private LinearLayout calendar_button, button_comer, planificar_button, añadir_plato_button, platos_button;
  private ImageView next_dish_image;
  private TextView next_dish_name;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    next_event_layout = findViewById(R.id.nect_event_layout);
    button_comer = findViewById(R.id.button_comer);
    text_comer = findViewById(R.id.text_comer);
    calendar_button = findViewById(R.id.calendar_button);
    planificar_button = findViewById(R.id.planificar_button);
    añadir_plato_button = findViewById(R.id.añadir_plato_button);
    platos_button = findViewById(R.id.platos_button);
    next_dish_image = findViewById(R.id.next_dish_image);
    next_dish_name = findViewById(R.id.next_dish_name);

    button_comer.setOnClickListener(this);
    calendar_button.setOnClickListener(this);
    planificar_button.setOnClickListener(this);
    añadir_plato_button.setOnClickListener(this);
    platos_button.setOnClickListener(this);

//    deleteEvents();
//    generateDishesAndSaveEvents();
    configUI();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  private void deleteEvents() {
    LocalDataSource.getInstance(this).clearEvents();
  }

  private void generateDishes() {

    //SAVE 25 DEFAULT DISHES
    for (int i = 0; i < 25; i++) {
      Dish dish = new Dish();
      dish.setName("Dish " + i);
      dish.setDescription("Descripcion " + i);
      dish.setPreparation("Preparacion " + i);
      dish.setProducts(new ArrayList<>());
      dish.setTags("x");
      dish.setImage("x");

      LocalDataSource.getInstance(this).saveDish(dish);
    }
  }

  private void generateDishesAndSaveEvents() {

    generateDishes();

////        //SET RANDOM EVENTS FOR A MONTH
    Date today = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(today);
    for (int i = 1; i <= 30; i++) {
      calendar.add(Calendar.DATE, -1);

      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
      LocalDataSource localDataSource = LocalDataSource.getInstance(this);

      ArrayList<Dish> dishes = (ArrayList<Dish>) localDataSource.getAllDishes();
      int random = (int) (Math.random() * dishes.size()) + 1;

      Dish dish = localDataSource.getDishByID(random);
      if (dish == null)
        Log.e("DISH NULL WITH ID ", String.valueOf(random));

      Event event = new Event();
      event.setDishID(dish.getId());
      event.setDate(dateFormat.format(calendar.getTime()));
      event.setType(Constants.DISH_TYPE_LUNCH);

      localDataSource.saveEvent(event);
//            new EventsController().add(event, Constants.database);


      random = (int) (Math.random() * dishes.size());

      dish = localDataSource.getDishByID(random);
      if (dish == null)
        Log.e("DISH NULL WITH ID: ", String.valueOf(random));
      event = new Event();
      event.setDishID(dish.getId());
      event.setDate(dateFormat.format(calendar.getTime()));
      event.setType(Constants.DISH_TYPE_DINNER);

      localDataSource.saveEvent(event);
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_pantry:
        startActivity(new Intent(getApplicationContext(), PantryActivity.class));
        return true;
    }
    return true;
  }

  @SuppressLint("RestrictedApi")
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button_comer:
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

          Intent i = new Intent(MainActivity.this, ElectionActivity.class);

          if (new Date().getHours() <= Constants.LUNCH_TIME)
            i.putExtra("dishType", Constants.DISH_TYPE_LUNCH);
          else
            i.putExtra("dishType", Constants.DISH_TYPE_DINNER);

          View sharedView = button_comer;
          String transitionName = getString(R.string.transitionName);

          ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
//          startActivity(i, transitionActivityOptions.toBundle());
          startActivityForResult(i, 20, transitionActivityOptions.toBundle());
        }

        break;
      case R.id.calendar_button:
        startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
        break;
      case R.id.planificar_button:
        break;
      case R.id.añadir_plato_button:
        startActivity(new Intent(getApplicationContext(), NewDishActivity.class));
        break;
      case R.id.platos_button:
        startActivity(new Intent(getApplicationContext(), MyDishesActivity.class));
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 20 && resultCode == RESULT_OK) {
      configUI();
    }
  }

  @Override
  public void onBackPressed() {
    setResult(RESULT_OK, null);
    finish();
  }


  public void configUI() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    Event next_event;
    if (new Date().getHours() <= Constants.LUNCH_TIME)
      next_event = LocalDataSource.getInstance(getApplicationContext()).getEventsByDateAndType(simpleDateFormat.format(new Date()), Constants.DISH_TYPE_LUNCH);
    else
      next_event = LocalDataSource.getInstance(getApplicationContext()).getEventsByDateAndType(simpleDateFormat.format(new Date()), Constants.DISH_TYPE_DINNER);


    if (next_event == null) {
      if (new Date().getHours() <= Constants.LUNCH_TIME)
        text_comer.setText(getString(R.string.que_como));
      else
        text_comer.setText(getString(R.string.que_ceno));
    } else {
      next_event_layout.setVisibility(View.VISIBLE);
      Dish dish = LocalDataSource.getInstance(getApplicationContext()).getDishByID(next_event.getDishID());
      next_dish_name.setText(dish.getName());

      Glide.with(this)
          .load(FileSystem.getInstance(this).IMAGES_PATH + dish.getImage())
          .into(next_dish_image);
    }
  }
}
