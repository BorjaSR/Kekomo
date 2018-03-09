package com.bsalazar.kekomo.data;

import android.content.Context;

import com.bsalazar.kekomo.data.daos.DishDAO;
import com.bsalazar.kekomo.data.daos.DishProductDAO;
import com.bsalazar.kekomo.data.daos.EventDAO;
import com.bsalazar.kekomo.data.daos.ProductDAO;
import com.bsalazar.kekomo.data.entities.Dish;
import com.bsalazar.kekomo.data.entities.DishProducts;
import com.bsalazar.kekomo.data.entities.Event;
import com.bsalazar.kekomo.data.entities.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by borja.salazar on 21/02/2018.
 */

public class LocalDataSource implements ILocalDataSource {

    private static DishDAO dishDAO;
    private EventDAO eventDAO;
    private ProductDAO productDAO;
    private DishProductDAO dishProductDAO;

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
        dishProductDAO = database.getDishProductDAO();
    }

    //endregion


    @Override
    public long saveDish(Dish dish) {

        dish.setCreated(System.currentTimeMillis());
        dish.setUpdated(System.currentTimeMillis());

        long dishID = dishDAO.insertDish(dish);
        addProductsToDish((int)dishID, dish.getProducts());

        return dishID;
    }

    private void addProductsToDish(int dishID, ArrayList<Product> porducts){
        long productID;

        for (Product product : porducts) {
            if (product.getId() <= 0)
                productID = saveProduct(product);
            else
                productID = product.getId();

            dishProductDAO.insertDishProduct(new DishProducts(dishID, (int) productID));
        }
    }

    @Override
    public long saveEvent(Event event) {
        event.setCreated(System.currentTimeMillis());
        event.setUpdated(System.currentTimeMillis());
        return eventDAO.insertEvent(event);
    }

    @Override
    public long saveProduct(Product product) {
        product.setCreated(System.currentTimeMillis());
        product.setUpdated(System.currentTimeMillis());
        return productDAO.insertProduct(product);
    }

    @Override
    public void updateDish(Dish dish) {
        dish.setUpdated(System.currentTimeMillis());

        dishProductDAO.deleteProductsFromDish(dish.getId());
        addProductsToDish(dish.getId(), dish.getProducts());

        dishDAO.updateDish(dish);
    }

    @Override
    public void updateEvent(Event event) {
        event.setUpdated(System.currentTimeMillis());
        eventDAO.updateEvent(event);
    }

    @Override
    public void updateProduct(Product product) {
        product.setUpdated(System.currentTimeMillis());
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

        Dish dish = dishDAO.getByID(id);
        dish.setProducts((ArrayList<Product>) dishProductDAO.getProductsByDish(id));

        return dish;
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
    public List<Product> getAllProductsSavedByUser() {
        return productDAO.getAllSaved();
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
