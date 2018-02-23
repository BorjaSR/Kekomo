package com.bsalazar.kekomo.data;

import android.content.Context;

import com.bsalazar.kekomo.data.daos.DishDAO;
import com.bsalazar.kekomo.data.daos.EventDAO;
import com.bsalazar.kekomo.data.daos.ProductDAO;
import com.bsalazar.kekomo.data.entities.Dish;
import com.bsalazar.kekomo.data.entities.Event;
import com.bsalazar.kekomo.data.entities.Product;

import java.util.List;

/**
 * Created by borja.salazar on 21/02/2018.
 */

public class LocalDataSource implements ILocalDataSource {

    private static DishDAO dishDAO;
    private EventDAO eventDAO;
    private ProductDAO productDAO;

    private static LocalDataSource instance;

    public static LocalDataSource newInstance(Context context) {
        instance = new LocalDataSource(context);
        return instance;
    }

    public static LocalDataSource getInstance(Context context) {
        if (instance == null)
            instance = new LocalDataSource(context);
        return instance;
    }

    //region Constructor

    private LocalDataSource(Context context) {
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        dishDAO = database.getDishesDAO();
        eventDAO = database.getEventsDAO();
        productDAO = database.getProductsDAO();
    }

    //endregion


    @Override
    public long saveDish(Dish dish) {
        return dishDAO.insertDish(dish);
    }

    @Override
    public long saveEvent(Event event) {
        return eventDAO.insertEvent(event);
    }

    @Override
    public long saveProduct(Product product) {
        return productDAO.insertProduct(product);
    }

    @Override
    public void updateDish(Dish dish) {
        dishDAO.updateDish(dish);
    }

    @Override
    public void updateEvent(Event event) {
        eventDAO.updateEvent(event);
    }

    @Override
    public void updateProduct(Product product) {
        productDAO.updateProduct(product);
    }

    @Override
    public void deleteDish(Dish dish) {
        dishDAO.deleteDish(dish);
    }

    @Override
    public void deleteEvent(Event event) {
        eventDAO.deleteEvent(event);
    }

    @Override
    public void deleteProduct(Product product) {
        productDAO.deleteProduct(product);
    }

    @Override
    public Dish getDishByID(int id) {
        return dishDAO.getByID(id);
    }

    @Override
    public Event getEventByID(int id) {
        return eventDAO.getByID(id);
    }

    @Override
    public Product getProductByID(int id) {
        return productDAO.getByID(id);
    }

    @Override
    public List<Dish> getAllDishes() {
        return dishDAO.getAll();
    }

    @Override
    public List<Event> getAllEvents() {
        return eventDAO.getAll();
    }

    @Override
    public List<Product> getAllProducts() {
        return productDAO.getAll();
    }

    @Override
    public void clearDishes() {
        dishDAO.clear();
    }

    @Override
    public void clearProducts() {
        eventDAO.clear();
    }

    @Override
    public void clearEvents() {
        productDAO.clear();
    }

    @Override
    public List<Event> getEventsByDate(String date) {
        return eventDAO.getByDate(date);
    }

    @Override
    public Event getEventsByDateAndType(String date, int type) {
        return eventDAO.getByDateAndType(date, type);
    }

    @Override
    public List<Event> getEventsByDish(int dishID) {
        return eventDAO.getByDishID(dishID);
    }
}