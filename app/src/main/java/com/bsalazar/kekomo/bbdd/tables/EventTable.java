package com.bsalazar.kekomo.bbdd.tables;

/**
 * Created by bsalazar on 26/06/2017.
 */

public class EventTable {


    public static final String TABLE_NAME = "BSR_Events";


    public static final String ID = "ID";
    public static final String DISH_ID = "Dish_ID";
    public static final String DATE = "Date";
    public static final String TYPE = "Type";

    public static final String CREATED = "Created";
    public static final String UPDATED = "LastUpdate";
    public static final String DELETE = "Deleted";


    public static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DISH_ID + " INTEGER, " +
            DATE + " TEXT, " +
            TYPE + " INTEGER, " +
            CREATED + " TEXT, " +
            UPDATED + " TEXT, " +
            DELETE + " INT " +
            ")";

    public static final String DROP_QUERY = "DROP TABLE " + TABLE_NAME;
}
