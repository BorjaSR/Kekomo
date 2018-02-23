package com.bsalazar.kekomo.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bsalazar.kekomo.data.entities.Dish;
import com.bsalazar.kekomo.data.entities.Product;
import com.bsalazar.kekomo.data.entities.RelaionDishProducts;

import java.util.List;

/**
 * Created by borja.salazar on 20/02/2018.
 */

@Dao
public interface DishProductDAO {

    @Query("SELECT * FROM DishProducts")
    List<RelaionDishProducts> getAll();

    @Update
    void updateDishProduct(RelaionDishProducts product);

    @Insert
    void insertDishProduct(RelaionDishProducts product);

    @Delete
    void deleteDishProduct(RelaionDishProducts product);

    @Query("DELETE FROM DishProducts")
    void clear();
}