package com.bsalazar.kekomo.general;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.widget.Toast;

import com.bsalazar.kekomo.MainActivity;
import com.bsalazar.kekomo.bbdd.controllers.DishesController;
import com.bsalazar.kekomo.bbdd.controllers.EventsController;
import com.bsalazar.kekomo.bbdd.entities.Dish;
import com.bsalazar.kekomo.bbdd.entities.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by bsalazar on 19/07/2017.
 */

public class ElectionAlgorithm {

    //FUNCTION: MAX_PUNCTUATION/(x^RATIO)
    private static final double MAX_PUNCTUATION = 100;
    private static final double RATIO = 0.5;


    public ArrayList<Integer> calculateDishesList() {
        EventsController eventsController = new EventsController();
        DishesController dishesController = new DishesController();


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        HashMap<Double, Double> dishes_punctuation = new HashMap<>();

        //Check today dishes
        ArrayList<Event> eventsOfToday= eventsController.getForDate(dateFormat.format(calendar.getTime()));
        if (eventsOfToday.size() > 0) {
            for (Event event : eventsOfToday) {
                double punctuation = MAX_PUNCTUATION / (Math.pow(0.25, RATIO));

                if (!dishes_punctuation.containsKey((double) event.getDishId())) {
                    dishes_punctuation.put((double) event.getDishId(), punctuation);
                } else {
                    double old_punct = dishes_punctuation.get((double) event.getDishId());
                    dishes_punctuation.put((double) event.getDishId(), old_punct + punctuation);
                }
            }
        }


        //Evaluate the previous events (1 year)
        for (int i = 1; i <= 365; i++) {
            calendar.add(Calendar.DATE, -1);
            ArrayList<Event> eventsOfDate = eventsController.getForDate(dateFormat.format(calendar.getTime()));

            if (eventsOfDate.size() > 0) {
                for (Event event : eventsOfDate) {
                    Log.d("[HAY PLATO]", dateFormat.format(calendar.getTime()) + " " + dishesController.getByID(event.getDishId()).getId() + " " + dishesController.getByID(event.getDishId()).getName());

                    double variable = i;
                    if (event.getType() == Constants.DISH_TYPE_DINNER)
                        variable -= 0.5d;

                    double punctuation = MAX_PUNCTUATION / (Math.pow(variable, RATIO));

                    if (!dishes_punctuation.containsKey((double) event.getDishId())) {
                        dishes_punctuation.put((double) event.getDishId(), punctuation);
                    } else {
                        double old_punct = dishes_punctuation.get((double) event.getDishId());
                        dishes_punctuation.put((double) event.getDishId(), old_punct + punctuation);
                    }
                }
            }
        }


        //Evaluate the next events (1 year)
        calendar.setTime(today);
        for (int i = 1; i <= 365; i++) {
            calendar.add(Calendar.DATE, 1);
            ArrayList<Event> eventsOfDate = eventsController.getForDate(dateFormat.format(calendar.getTime()));

            if (eventsOfDate.size() > 0) {
                for (Event event : eventsOfDate) {
//                    Log.d("[HAY PLATO]", dateFormat.format(calendar.getTime()) + " " + dishesController.getByID(event.getDishId()).getName());

                    double variable = i;
                    if (event.getType() == Constants.DISH_TYPE_DINNER)
                        variable += 0.5d;

                    double punctuation = MAX_PUNCTUATION / (Math.pow(variable, RATIO));

                    if (!dishes_punctuation.containsKey((double) event.getDishId())) {
                        dishes_punctuation.put((double) event.getDishId(), punctuation);
                    } else {
                        double old_punct = dishes_punctuation.get((double) event.getDishId());
                        dishes_punctuation.put((double) event.getDishId(), old_punct + punctuation);
                    }
                }
            }
        }

        //Evaluate the non appear dishes
        ArrayList<Dish> dishes = dishesController.getAll();
        for (Dish dish : dishes) {
            if (!dishes_punctuation.containsKey((double) dish.getId()))
                dishes_punctuation.put((double) dish.getId(), 0d);
        }

        // Add Random Factor
        for (Dish dish : dishes) {
            double random = Math.random() * ((MAX_PUNCTUATION / 100) * 10);
            double old_punct = dishes_punctuation.get((double) dish.getId());
            dishes_punctuation.put((double) dish.getId(), old_punct + random);
        }

        ElectionAlgorithm.ValueComparatorD vc = new ElectionAlgorithm.ValueComparatorD(dishes_punctuation);
        TreeMap<Double, Double> dish_appearances_sorted = new TreeMap<>(vc);

        dish_appearances_sorted.putAll(dishes_punctuation);


        ArrayList<Integer> dishes_sorted = new ArrayList<>();
        for (Map.Entry<Double, Double> entry : dish_appearances_sorted.entrySet()) {
            dishes_sorted.add(entry.getKey().intValue());
            Log.d("LIST DISHES SHORTED", entry.getKey().intValue() + " " + entry.getValue());// PRINT RESULT
        }
        return dishes_sorted;
    }

    private class ValueComparatorD implements Comparator<Double> {
        Map<Double, Double> base;

        ValueComparatorD(Map<Double, Double> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        public int compare(Double a, Double b) {
            if (base.get(a) < base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}
