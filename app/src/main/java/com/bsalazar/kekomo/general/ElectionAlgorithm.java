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
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by bsalazar on 19/07/2017.
 */

public class ElectionAlgorithm {

    private static final double MAX_PUNCTUATION = 100;
    private static final double RATIO = 0.5;


    public ArrayList<Integer> calculateDishesList() {

        calculateDishesListNEW();

        EventsController eventsController = new EventsController();
        DishesController dishesController = new DishesController();

        try {
            ArrayList<Event> events = eventsController.getAll();
            if (events.size() == 0) {

                ArrayList<Dish> dishes = dishesController.getAll();
                if (dishes.size() == 0)
//                    Snackbar.make(button_comer, "Todavia no tienes platos guardados", Snackbar.LENGTH_SHORT).show();
                    return null;
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
                ElectionAlgorithm.ValueComparator vc = new ElectionAlgorithm.ValueComparator(dish_appearances);
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


    //FUNCTION: 100/(x^0,5)
    public ArrayList<Integer> calculateDishesListNEW() {
        EventsController eventsController = new EventsController();
        DishesController dishesController = new DishesController();


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        HashMap<Integer, Double> dishes_punctuation = new HashMap<>();

        //Evaluate the previous events (1 year)
        for (int i = 1; i <= 365; i++) {
            calendar.add(Calendar.DATE, -1);
            ArrayList<Event> eventsOfDate = eventsController.getForDate(dateFormat.format(calendar.getTime()));

            if(eventsOfDate.size() > 0){
                for(Event event : eventsOfDate){
                    Log.d("[HAY PLATO]", dateFormat.format(calendar.getTime()) + " " + dishesController.getByID(event.getDishId()).getName());

                    double variable = i;
                    if(event.getType() == Constants.DISH_TYPE_DINNER)
                        variable -= 0.5d;

                    double punctuation = MAX_PUNCTUATION/(Math.pow(variable, RATIO));

                    if (!dishes_punctuation.containsKey(event.getDishId())){
                        dishes_punctuation.put(event.getDishId(), punctuation);
                    } else {
                        double old_punct = dishes_punctuation.get(event.getDishId());
                        dishes_punctuation.put(event.getDishId(), old_punct + punctuation);
                    }
                }
            }
        }


        //Evaluate the next events (1 year)
        calendar.setTime(today);
        for (int i = 1; i <= 365; i++) {
            calendar.add(Calendar.DATE, 1);
            ArrayList<Event> eventsOfDate = eventsController.getForDate(dateFormat.format(calendar.getTime()));

            if(eventsOfDate.size() > 0){
                for(Event event : eventsOfDate){
                    Log.d("[HAY PLATO]", dateFormat.format(calendar.getTime()) + " " + dishesController.getByID(event.getDishId()).getName());

                    double variable = i;
                    if(event.getType() == Constants.DISH_TYPE_DINNER)
                        variable += 0.5d;

                    double punctuation = MAX_PUNCTUATION/(Math.pow(variable, RATIO));

                    if (!dishes_punctuation.containsKey(event.getDishId())){
                        dishes_punctuation.put(event.getDishId(), punctuation);
                    } else {
                        double old_punct = dishes_punctuation.get(event.getDishId());
                        dishes_punctuation.put(event.getDishId(), old_punct + punctuation);
                    }
                }
            }
        }

        //Evaluate the non appear dishes
        ArrayList<Dish> dishes = dishesController.getAll();
        for(Dish dish : dishes){
            if(!dishes_punctuation.containsKey(dish.getId()))
                dishes_punctuation.put(dish.getId(), 0d);
        }

        // Add Random Factor
        for(Dish dish : dishes){
            double random = Math.random() * ((MAX_PUNCTUATION/100)*10);
            double old_punct = dishes_punctuation.get(dish.getId());
            dishes_punctuation.put(dish.getId(), old_punct + random);
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
}
