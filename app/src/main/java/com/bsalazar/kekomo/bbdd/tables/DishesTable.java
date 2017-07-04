package com.bsalazar.kekomo.bbdd.tables;

/**
 * Created by bsalazar on 22/06/2017.
 */

public class DishesTable {

    public static final String TABLE_NAME = "BSR_Dishes";


    public static final String ID = "ID";
    public static final String NAME = "Name";
    public static final String DESCRIPTION = "Description";
    public static final String TAGS = "Tags";
    public static final String IMAGE = "Image";

    public static final String CREATED = "Created";
    public static final String UPDATED = "LastUpdate";
    public static final String DELETE = "Deleted";


    public static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " TEXT, " +
            DESCRIPTION + " TEXT, " +
            TAGS + " TEXT, " +
            IMAGE + " TEXT, " +
            CREATED + " TEXT, " +
            UPDATED + " TEXT, " +
            DELETE + " INT " +
            ")";

    public static final String DROP_QUERY = "DROP TABLE " + TABLE_NAME;
}
