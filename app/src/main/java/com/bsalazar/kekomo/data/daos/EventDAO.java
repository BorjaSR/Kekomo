package com.bsalazar.kekomo.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bsalazar.kekomo.data.entities.Event;

import java.util.List;

/**
 * Created by borja.salazar on 20/02/2018.
 */

@Dao
public interface EventDAO {

    @Query("SELECT * FROM Events")
    List<Event> getAll();

    @Query("SELECT * FROM Events WHERE id = :eventID")
    Event getByID(int eventID);

    @Query("SELECT * FROM Events WHERE Date = :date ORDER BY Date DESC")
    List<Event> getByDate(String date);

    @Query("SELECT * FROM Events WHERE Date = :date AND type = :type ORDER BY Date DESC")
    Event getByDateAndType(String date, int type);

    @Query("SELECT * FROM Events WHERE dishID = :dishID ORDER BY Date DESC")
    List<Event> getByDishID(int dishID);

    @Update
    void updateEvent(Event event);

    @Insert
    long insertEvent(Event event);

    @Delete
    void deleteEvent(Event event);

    @Query("DELETE FROM Events")
    void clear();

}
