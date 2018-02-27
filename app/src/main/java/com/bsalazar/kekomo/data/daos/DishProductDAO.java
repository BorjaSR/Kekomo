package com.bsalazar.kekomo.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bsalazar.kekomo.data.entities.DishProducts;

import java.util.List;

/**
 * Created by borja.salazar on 20/02/2018.
 */

@Dao
public interface DishProductDAO {

    @Query("SELECT * FROM DishProducts")
    List<DishProducts> getAll();

    @Query("SELECT * FROM DishProducts WHERE productId = :productID")
    List<DishProducts> getByProduct(int productID);

    @Query("SELECT * FROM DishProducts WHERE dishId = :dishID")
    List<DishProducts> getByDish(int dishID);

    @Update
    void updateDishProduct(DishProducts product);

    @Insert
    void insertDishProduct(DishProducts product);

    @Delete
    void deleteDishProduct(DishProducts product);

    @Query("DELETE FROM DishProducts")
    void clear();
}