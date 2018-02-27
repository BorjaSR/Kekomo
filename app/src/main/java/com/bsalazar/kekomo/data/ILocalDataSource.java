package com.bsalazar.kekomo.data;

import com.bsalazar.kekomo.data.entities.Dish;
import com.bsalazar.kekomo.data.entities.Event;
import com.bsalazar.kekomo.data.entities.Product;

import java.util.List;

/**
 * Created by borja.salazar on 21/02/2018.
 */

public interface ILocalDataSource {

    long saveDish(Dish dish);

    long saveEvent(Event event);

    long saveProduct(Product product);

    void updateDish(Dish dish);

    void updateEvent(Event event);

    void updateProduct(Product product);

    void deleteDish(Dish dish);

    void deleteEvent(Event event);

    void deleteProduct(Product product);

    Dish getDishByID(int id);

    Event getEventByID(int id);

    Product getProductByID(int id);

    List<Dish> getAllDishes();

    List<Event> getAllEvents();

    List<Product> getAllProducts();

    List<Product> getAllProductsSavedByUser();

    void clearDishes();

    void clearProducts();

    void clearEvents();

    List<Event> getEventsByDate(String date);

    Event getEventsByDateAndType(String date, int type);

    List<Event> getEventsByDish(int dishID);
}
