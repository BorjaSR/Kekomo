package com.bsalazar.kekomo.bbdd.controllers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bsalazar.kekomo.bbdd.entities.Dish;
import com.bsalazar.kekomo.bbdd.entities.Event;
import com.bsalazar.kekomo.bbdd.entities.Product;
import com.bsalazar.kekomo.bbdd.tables.DishesTable;
import com.bsalazar.kekomo.bbdd.tables.EventTable;
import com.bsalazar.kekomo.bbdd.tables.ProductTable;
import com.bsalazar.kekomo.general.Constants;
import com.bsalazar.kekomo.general.Tools;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bsalazar on 18/10/17.
 */

public class ProductController {

    public Product add(Product product, SQLiteDatabase database) {

        try {
            SQLiteDatabase tempDatabase = database;
            if (tempDatabase == null)
                tempDatabase = Constants.database;

            if (tempDatabase != null && tempDatabase.isOpen()) {

                ContentValues values = setObject(product);

                if (values != null) {
                    int id = getAll().size();
                    values.put(ProductTable.ID, id);
                    values.put(ProductTable.CREATED, Tools.parseDateToSQL(new Date(System.currentTimeMillis())));
                    values.put(ProductTable.UPDATED, Tools.parseDateToSQL(new Date(System.currentTimeMillis())));
                    values.put(ProductTable.DELETE, 0);

                    //INSERT
                    tempDatabase.insert(ProductTable.TABLE_NAME, null, values);

                    return getByID(id);
                }
            }
        } catch (Exception e) {
            Log.e("CheckIn Error: addUpd", e.getMessage());
        }
        return null;
    }

    public void update(Product product, SQLiteDatabase database) {

        try {
            SQLiteDatabase tempDatabase = database;
            if (tempDatabase == null)
                tempDatabase = Constants.database;

            if (tempDatabase != null && tempDatabase.isOpen()) {

                ContentValues values = setObject(product);

                if (values != null) {
                    values.put(ProductTable.UPDATED, Tools.parseDateToSQL(new Date(System.currentTimeMillis())));

                    //UPDATE
                    Constants.database.update(ProductTable.TABLE_NAME, values, ProductTable.ID + " = " + product.getId(), null);
                }
            }
        } catch (Exception e) {
            Log.e("CheckIn Error: addUpd", e.getMessage());
        }
    }



    public Product getByID(int id) {
        String selectQuery = "SELECT * FROM " + ProductTable.TABLE_NAME + " WHERE " + ProductTable.ID + " = " + id;
        Cursor cursor = Constants.database.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst())
                return fillObject(cursor);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Product> getAll() throws ParseException {
        String selectQuery = "SELECT * FROM " + ProductTable.TABLE_NAME + " ORDER BY " + ProductTable.NAME + " DESC";
        return fillList(selectQuery);
    }

    public void deleteByID(int id){
        Constants.database.delete(ProductTable.TABLE_NAME, ProductTable.ID + " = " + id, null);
    }

    private ArrayList<Product> fillList(String selectQuery) {
        ArrayList<Product> list = new ArrayList<>();

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

    private ContentValues setObject(Product obj) {
        if (obj == null)
            return null;

        ContentValues values = new ContentValues();

        values.put(ProductTable.ID, obj.getId());
        values.put(ProductTable.CREATED, String.valueOf(obj.getCreated()));
        values.put(ProductTable.UPDATED, String.valueOf(obj.getUpdated()));
        values.put(ProductTable.DELETE, obj.isDeleted());

        values.put(ProductTable.NAME, obj.getName());
        values.put(ProductTable.STOCK, obj.getStock());
        values.put(ProductTable.TYPE, obj.getType());
        values.put(ProductTable.FROZEN, obj.isFrozen());

        return values;
    }

    @NonNull
    private Product fillObject(Cursor cursor) throws ParseException {
        Product product = new Product();

        product.setId(cursor.getInt(cursor.getColumnIndex(ProductTable.ID)));
        product.setCreated(Tools.parseDateFromSQL(cursor.getString(cursor.getColumnIndex(ProductTable.CREATED))));
        product.setUpdated(Tools.parseDateFromSQL(cursor.getString(cursor.getColumnIndex(ProductTable.UPDATED))));
        product.setDeleted(cursor.getInt(cursor.getColumnIndex(ProductTable.DELETE)) > 0);

        product.setName(cursor.getString(cursor.getColumnIndex(ProductTable.NAME)));
        product.setStock(cursor.getInt(cursor.getColumnIndex(ProductTable.STOCK)));
        product.setType(cursor.getInt(cursor.getColumnIndex(ProductTable.TYPE)));
        product.setFrozen(cursor.getInt(cursor.getColumnIndex(ProductTable.FROZEN)) > 0);

        return product;
    }

}
