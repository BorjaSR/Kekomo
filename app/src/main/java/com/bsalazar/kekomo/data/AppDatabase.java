package com.bsalazar.kekomo.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.bsalazar.kekomo.data.daos.DishDAO;
import com.bsalazar.kekomo.data.daos.DishProductDAO;
import com.bsalazar.kekomo.data.daos.EventDAO;
import com.bsalazar.kekomo.data.daos.ProductDAO;
import com.bsalazar.kekomo.data.entities.Dish;
import com.bsalazar.kekomo.data.entities.Event;
import com.bsalazar.kekomo.data.entities.Product;
import com.bsalazar.kekomo.data.entities.RelaionDishProducts;

@Database (entities = {
        Dish.class,
        Event.class,
        Product.class,
        RelaionDishProducts.class},
        version = 1)
abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "kekomo";
    private static final Object LOCK = new Object();

    private static volatile AppDatabase sInstance;

    static AppDatabase getInstance(Context context) {
//        if(sInstance == null) {
//            synchronized(LOCK) {
                if(sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
                }
//            }
//        }
        return sInstance;
    }

    abstract DishDAO getDishesDAO();

    abstract EventDAO getEventsDAO();

    abstract ProductDAO getProductsDAO();

    abstract DishProductDAO getDishProductDAO();
}