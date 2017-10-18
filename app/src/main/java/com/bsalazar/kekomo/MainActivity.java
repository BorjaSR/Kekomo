package com.bsalazar.kekomo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bsalazar.kekomo.bbdd.SQLiteHelper;
import com.bsalazar.kekomo.bbdd.controllers.DishesController;
import com.bsalazar.kekomo.bbdd.controllers.EventsController;
import com.bsalazar.kekomo.bbdd.entities.Dish;
import com.bsalazar.kekomo.bbdd.entities.Event;
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
import java.util.Date;

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

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        Constants.database = sqLiteHelper.getWritableDatabase();

        next_event_layout = (RelativeLayout) findViewById(R.id.nect_event_layout);
        button_comer = (LinearLayout) findViewById(R.id.button_comer);
        text_comer = (TextView) findViewById(R.id.text_comer);
        calendar_button = (LinearLayout) findViewById(R.id.calendar_button);
        planificar_button = (LinearLayout) findViewById(R.id.planificar_button);
        añadir_plato_button = (LinearLayout) findViewById(R.id.añadir_plato_button);
        platos_button = (LinearLayout) findViewById(R.id.platos_button);
        next_dish_image = (ImageView) findViewById(R.id.next_dish_image);
        next_dish_name = (TextView) findViewById(R.id.next_dish_name);

        button_comer.setOnClickListener(this);
        calendar_button.setOnClickListener(this);
        planificar_button.setOnClickListener(this);
        añadir_plato_button.setOnClickListener(this);
        platos_button.setOnClickListener(this);


//        try {
//            new EventsController().deleteAll();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

////        //SET RANDOM EVENTS FOR A MONTH
//        Date today = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(today);
//        for(int i = 1; i <= 30; i++){
//            calendar.add(Calendar.DATE, -1);
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            DishesController dishesController = new DishesController();
//
//            ArrayList<Dish> dishes = dishesController.getAll();
//            int random = (int) (Math.random() * dishes.size());
//
//            Dish dish = new DishesController().getByID(random);
//            Event event = new Event();
//            event.setDishId(dish.getId());
//            event.setDate(dateFormat.format(calendar.getTime()));
//            event.setType(Constants.DISH_TYPE_LUNCH);
//
//            new EventsController().add(event, Constants.database);
//
//
//            random = (int) (Math.random() * dishes.size());
//
//            dish = new DishesController().getByID(random);
//            event = new Event();
//            event.setDishId(dish.getId());
//            event.setDate(dateFormat.format(calendar.getTime()));
//            event.setType(Constants.DISH_TYPE_DINNER);
//
//            new EventsController().add(event, Constants.database);
//        }

        configUI();
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

    private boolean electionsShoed = false;
    private ElectionFragment electionFragment;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_comer:
                ArrayList<Integer> dishList = new ElectionAlgorithm().calculateDishesListNEW();
                if(dishList != null){
                    showDishList(dishList);

                    electionFragment = new ElectionFragment();
                    Bundle args = new Bundle();
                    args.putIntegerArrayList("dishes", dishList);
                    electionFragment.setArguments(args);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, electionFragment)
                            .addToBackStack(null)
                            .commit();
                    electionsShoed = true;
                } else
                    Snackbar.make(button_comer, "Todavia no tienes platos guardados", Snackbar.LENGTH_SHORT).show();
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
    public void onBackPressed() {
//        super.onBackPressed();
//        getFragmentManager().beginTransaction().isEmpty();
        if(electionsShoed){
            electionsShoed = false;
            getFragmentManager().beginTransaction().remove(electionFragment).commit();
        } else{
            setResult(RESULT_OK, null);
            finish();
        }
    }


    public void configUI(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Event next_event = null;
        try {
            if (new Date().getHours() <= Constants.LUNCH_TIME)
                next_event = new EventsController().getForDateAndType(simpleDateFormat.format(new Date()), Constants.DISH_TYPE_LUNCH);
            else
                next_event = new EventsController().getForDateAndType(simpleDateFormat.format(new Date()), Constants.DISH_TYPE_DINNER);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(next_event == null) {
            if (new Date().getHours() <= Constants.LUNCH_TIME)
                text_comer.setText(getString(R.string.que_como));
            else
                text_comer.setText(getString(R.string.que_ceno));
        } else {
            next_event_layout.setVisibility(View.VISIBLE);
            Dish dish = new DishesController().getByID(next_event.getDishId());
            next_dish_name.setText(dish.getName());

            Glide.with(this)
                    .load(FileSystem.getInstance(this).IMAGES_PATH + dish.getImage())
                    .into(next_dish_image);
        }
    }

    private void showDishList(ArrayList<Integer> dishes) {
        StringBuilder builder = new StringBuilder();
        for (Integer id : dishes)
            builder.append(id).append(", ");

        Toast.makeText(getApplicationContext(), builder.toString(), Toast.LENGTH_SHORT).show();
        showOption(new DishesController().getByID(dishes.get(0)));
    }

    public void showOption(final Dish dish) {
        Snackbar.make(button_comer, dish.getName(), Snackbar.LENGTH_SHORT)
                .setAction("Vale!", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                        Event event = new Event();
                        event.setDishId(dish.getId());
                        event.setDate(dateFormat.format(new Date()));

                        if (new Date().getHours() <= Constants.LUNCH_TIME)
                            event.setType(Constants.DISH_TYPE_LUNCH);
                        else
                            event.setType(Constants.DISH_TYPE_DINNER);

                        new EventsController().add(event, Constants.database);
                    }
                })
                .show();
    }
}
