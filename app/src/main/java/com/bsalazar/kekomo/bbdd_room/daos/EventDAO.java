package com.bsalazar.kekomo.bbdd_room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bsalazar.kekomo.bbdd_room.entities.Dish;
import com.bsalazar.kekomo.bbdd_room.entities.Event;

import java.util.List;

/**
 * Created by borja.salazar on 20/02/2018.
 */

@Dao
public interface EventDAO {

    @Query("SELECT * FROM Events")
    List<Event> getAll();

    @Query("SELECT * FROM Events WHERE eventID = :eventID")
    List<Event> getByID(int eventID);

    @Update
    void updateUsers(Event event);

    @Insert
    void insertBothUsers(Event event);

    @Delete
    void deleteUsers(Event event);

    @Query("DELETE FROM Events")
    void clear();

}
