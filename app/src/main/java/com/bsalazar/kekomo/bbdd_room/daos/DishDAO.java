package com.bsalazar.kekomo.bbdd_room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bsalazar.kekomo.bbdd_room.entities.Dish;

import java.util.List;

/**
 * Created by borja.salazar on 20/02/2018.
 */

@Dao
public interface DishDAO {

    @Query("SELECT * FROM Dishes")
    List<Dish> getAll();

    @Query("SELECT * FROM Dishes WHERE dishID = :dishID")
    List<Dish> getByID(int dishID);

    @Update
    public void updateUsers(Dish... dish);

    @Insert
    public void insertBothUsers(Dish dish);

    @Delete
    public void deleteUsers(Dish dish);

}
