package com.bsalazar.kekomo.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bsalazar.kekomo.data.entities.Dish;

import java.util.List;

/**
 * Created by borja.salazar on 20/02/2018.
 */

@Dao
public interface DishDAO {

    @Query("SELECT * FROM Dishes")
    List<Dish> getAll();

    @Query("SELECT * FROM Dishes WHERE id = :dishID")
    Dish getByID(int dishID);

    @Update
    void updateDish(Dish dish);

    @Insert
    long insertDish(Dish dish);

    @Delete
    void deleteDish(Dish dish);

    @Query("DELETE FROM Dishes")
    void clear();
}
