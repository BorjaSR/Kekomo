package com.bsalazar.kekomo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bsalazar.kekomo.UI_dishes.MyDishesActivity;
import com.bsalazar.kekomo.UI_dishes.NewDishActivity;
import com.bsalazar.kekomo.bbdd.SQLiteHelper;
import com.bsalazar.kekomo.bbdd.controllers.DishesController;
import com.bsalazar.kekomo.bbdd.controllers.EventsController;
import com.bsalazar.kekomo.bbdd.entities.Dish;
import com.bsalazar.kekomo.UI_calendar.CalendarActivity;
import com.bsalazar.kekomo.bbdd.entities.Event;
import com.bsalazar.kekomo.general.Constants;
import com.bsalazar.kekomo.general.FileSystem;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setEnterTransition(new Fade());
//        }

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

        configUI();
    }

    private boolean electionsShoed = false;
    private ElectionFragment electionFragment;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_comer:
                ArrayList<Integer> dishList = calculateDishesList();
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

    private ArrayList<Integer> calculateDishesList() {
        EventsController eventsController = new EventsController();
        DishesController dishesController = new DishesController();

        try {
            ArrayList<Event> events = eventsController.getAll();
            if (events.size() == 0) {

                ArrayList<Dish> dishes = dishesController.getAll();
                if (dishes.size() == 0)
                    Snackbar.make(button_comer, "Todavia no tienes platos guardados", Snackbar.LENGTH_SHORT).show();
                else {
                    ArrayList<Integer> dishes_list = new ArrayList<>();
                    while (dishes.size() > 0) {
                        int random = (int) (Math.random() * dishes.size());
                        dishes_list.add(dishes.get(random).getId());
                        dishes.remove(dishes.get(random));
                    }

                    return dishes_list;
                }

            } else {
                ArrayList<Dish> dishes = dishesController.getAll();

                HashMap<Integer, Integer> dish_appearances = new HashMap<>();
                ValueComparator vc = new ValueComparator(dish_appearances);
                TreeMap<Integer, Integer> dish_appearances_sorted = new TreeMap<>(vc);

                for (Dish dish : dishes)
                    dish_appearances.put(dish.getId(), eventsController.getEventsForDishID(dish.getId()).size());

                dish_appearances_sorted.putAll(dish_appearances);

                ArrayList<Integer> dishes_sorted = new ArrayList<>();
                for (Map.Entry<Integer, Integer> entry : dish_appearances_sorted.entrySet())
                    dishes_sorted.add(entry.getKey());

                return dishes_sorted;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    private class ValueComparator implements Comparator<Integer> {
        Map<Integer, Integer> base;

        ValueComparator(Map<Integer, Integer> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        public int compare(Integer a, Integer b) {
            if (base.get(a) < base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }

    private void showDishList(ArrayList<Integer> dishes) {
        StringBuilder builder = new StringBuilder();
        for (Integer id : dishes)
            builder.append(id + ", ");

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
