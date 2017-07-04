package com.bsalazar.kekomo.bbdd.controllers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bsalazar.kekomo.bbdd.entities.Event;
import com.bsalazar.kekomo.bbdd.tables.EventTable;
import com.bsalazar.kekomo.general.Constants;
import com.bsalazar.kekomo.general.Tools;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bsalazar on 26/06/2017.
 */

public class EventsController {


    public void add(Event event, SQLiteDatabase database) {

        try {
            SQLiteDatabase tempDatabase = database;
            if (tempDatabase == null)
                tempDatabase = Constants.database;

            if (tempDatabase != null && tempDatabase.isOpen()) {

                ContentValues values = setObject(event);

                if (values != null) {
                    int id = getAll().size();
                    values.put(EventTable.ID, id);
                    values.put(EventTable.CREATED, Tools.parseDateToSQL(new Date(System.currentTimeMillis())));
                    values.put(EventTable.UPDATED, Tools.parseDateToSQL(new Date(System.currentTimeMillis())));

                    //INSERT
                    tempDatabase.insert(EventTable.TABLE_NAME, null, values);
                }
            }
        } catch (Exception e) {
            Log.e("CheckIn Error: addUpd", e.getMessage());
        }
    }


    public ArrayList<Event> getAll() throws ParseException {
        String selectQuery = "SELECT * FROM " + EventTable.TABLE_NAME + " ORDER BY " + EventTable.DATE + " DESC";
        return fillList(selectQuery);
    }

    public void deleteAll() throws ParseException {
        Constants.database.delete(EventTable.TABLE_NAME, null, null);
    }

    public ArrayList<Event> getForDate(String date) throws ParseException {
        String selectQuery = "SELECT * FROM " + EventTable.TABLE_NAME + " WHERE " + EventTable.DATE + " = '" + date + "' ORDER BY " + EventTable.DATE + " DESC";
        return fillList(selectQuery);
    }

    public Event getForDateAndType(String date, int type) throws ParseException {
        String selectQuery = "SELECT * FROM " + EventTable.TABLE_NAME + " WHERE " + EventTable.DATE + " = '" + date + "' AND " + EventTable.TYPE + " = " + type +
                " ORDER BY " + EventTable.DATE + " DESC";

        Cursor cursor = Constants.database.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst())
                return fillObject(cursor);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Event> getEventsForDishID(int dishID) throws ParseException {
        String selectQuery = "SELECT * FROM " + EventTable.TABLE_NAME + " WHERE " + EventTable.DISH_ID + " = " + dishID + " ORDER BY " + EventTable.DATE + " DESC";
        return fillList(selectQuery);
    }

    private ArrayList<Event> fillList(String selectQuery) {
        ArrayList<Event> list = new ArrayList<>();

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

    private ContentValues setObject(Event obj) {
        if (obj == null)
            return null;

        ContentValues values = new ContentValues();

        values.put(EventTable.ID, obj.getId());
        values.put(EventTable.CREATED, String.valueOf(obj.getCreated()));
        values.put(EventTable.UPDATED, String.valueOf(obj.getUpdated()));
        values.put(EventTable.DELETE, obj.isDeleted());

        values.put(EventTable.DISH_ID, obj.getDishId());
        values.put(EventTable.DATE, obj.getDate());
        values.put(EventTable.TYPE, obj.getType());

        return values;
    }

    @NonNull
    private Event fillObject(Cursor cursor) throws ParseException {
        Event event = new Event();

        event.setId(cursor.getInt(cursor.getColumnIndex(EventTable.ID)));
        event.setCreated(Tools.parseDateFromSQL(cursor.getString(cursor.getColumnIndex(EventTable.CREATED))));
        event.setUpdated(Tools.parseDateFromSQL(cursor.getString(cursor.getColumnIndex(EventTable.UPDATED))));
        event.setDeleted(cursor.getInt(cursor.getColumnIndex(EventTable.DELETE)) > 0);

        event.setDishId(cursor.getInt(cursor.getColumnIndex(EventTable.DISH_ID)));
        event.setDate(cursor.getString(cursor.getColumnIndex(EventTable.DATE)));
        event.setType(cursor.getInt(cursor.getColumnIndex(EventTable.TYPE)));

        return event;
    }

}
