package com.bsalazar.kekomo.bbdd.controllers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bsalazar.kekomo.bbdd.entities.Dish;
import com.bsalazar.kekomo.bbdd.tables.DishesTable;
import com.bsalazar.kekomo.general.Constants;
import com.bsalazar.kekomo.general.Tools;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bsalazar on 22/06/2017.
 */

public class DishesController {

    public Dish add(Dish dish, SQLiteDatabase database) {

        Dish dishR = null;
        try {
            SQLiteDatabase tempDatabase = database;
            if (tempDatabase == null)
                tempDatabase = Constants.database;

            if (tempDatabase != null && tempDatabase.isOpen()) {

                ContentValues values = setObject(dish);

                if (values != null) {
                    int id = getAll().size();
                    values.put(DishesTable.ID, id);
                    values.put(DishesTable.CREATED, Tools.parseDateToSQL(new Date(System.currentTimeMillis())));
                    values.put(DishesTable.UPDATED, Tools.parseDateToSQL(new Date(System.currentTimeMillis())));

                    //INSERT
                    tempDatabase.insert(DishesTable.TABLE_NAME, null, values);

                    dishR = getByID(id);
                }
            }
        } catch (Exception e) {
            Log.e("CheckIn Error: addUpd", e.getMessage());
        }

        return dishR;
    }

    public Dish getByID(int id) {
        String selectQuery = "SELECT * FROM " + DishesTable.TABLE_NAME + " WHERE " + DishesTable.ID + " = " + id;
        Cursor cursor = Constants.database.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst())
                return fillObject(cursor);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Dish dish, SQLiteDatabase database) {

        try {
            SQLiteDatabase tempDatabase = database;
            if (tempDatabase == null)
                tempDatabase = Constants.database;

            boolean flagCreated = false; //false = insert
            if (tempDatabase != null && tempDatabase.isOpen()) {

                ContentValues values = setObject(dish);

                if (values != null) {
                    values.put(DishesTable.UPDATED, Tools.parseDateToSQL(new Date(System.currentTimeMillis())));

                    //UPDATE
                    tempDatabase.update(DishesTable.TABLE_NAME, values, DishesTable.ID + " = " + dish.getId(), null);
                }
            }
        } catch (Exception e) {
            Log.e("CheckIn Error: addUpd", e.getMessage());
        }
    }

    public ArrayList<Dish> getAll() {
        String selectQuery = "SELECT * FROM " + DishesTable.TABLE_NAME + " ORDER BY " + DishesTable.NAME + " ASC";
        return fillList(selectQuery);
    }

    private ArrayList<Dish> fillList(String selectQuery) {
        ArrayList<Dish> list = new ArrayList<>();

        if (Constants.database != null && Constants.database.isOpen()) {
            Cursor cursor = Constants.database.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        list.add(fillObject(cursor));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.getMessage();
            } finally {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
            }
        }

        return list;
    }

    private ContentValues setObject(Dish obj) {
        if (obj == null)
            return null;

        ContentValues values = new ContentValues();

        values.put(DishesTable.ID, obj.getId());
        values.put(DishesTable.CREATED, String.valueOf(obj.getCreated()));
        values.put(DishesTable.UPDATED, String.valueOf(obj.getUpdated()));
        values.put(DishesTable.DELETE, obj.isDeleted());

        values.put(DishesTable.NAME, obj.getName());
        values.put(DishesTable.DESCRIPTION, obj.getDescription());
        values.put(DishesTable.TAGS, obj.getTags());
        values.put(DishesTable.IMAGE, obj.getImage());

        return values;
    }

    @NonNull
    private Dish fillObject(Cursor cursor) throws ParseException {
        Dish dish = new Dish();

        dish.setId(cursor.getInt(cursor.getColumnIndex(DishesTable.ID)));
        dish.setCreated(Tools.parseDateFromSQL(cursor.getString(cursor.getColumnIndex(DishesTable.CREATED))));
        dish.setUpdated(Tools.parseDateFromSQL(cursor.getString(cursor.getColumnIndex(DishesTable.UPDATED))));
        dish.setDeleted(cursor.getInt(cursor.getColumnIndex(DishesTable.DELETE)) > 0);

        dish.setName(cursor.getString(cursor.getColumnIndex(DishesTable.NAME)));
        dish.setDescription(cursor.getString(cursor.getColumnIndex(DishesTable.DESCRIPTION)));
        dish.setTags(cursor.getString(cursor.getColumnIndex(DishesTable.TAGS)));
        dish.setImage(cursor.getString(cursor.getColumnIndex(DishesTable.IMAGE)));

        return dish;
    }

}
