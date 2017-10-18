package com.bsalazar.kekomo.bbdd.tables;

/**
 * Created by bsalazar on 18/10/17.
 */

public class ProductTable {

    public static final String TABLE_NAME = "BSR_Products";

    public static final String ID = "ID";
    public static final String NAME = "Name";
    public static final String TYPE = "Type";
    public static final String STOCK = "Stock";
    public static final String FROZEN = "Frozen";

    public static final String CREATED = "Created";
    public static final String UPDATED = "LastUpdate";
    public static final String DELETE = "Deleted";


    public static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " TEXT, " +
            TYPE + " INT, " +
            STOCK + " INT, " +
            FROZEN + " INT, " +
            CREATED + " TEXT, " +
            UPDATED + " TEXT, " +
            DELETE + " INT " +
            ")";

    public static final String DROP_QUERY = "DROP TABLE " + TABLE_NAME;
}
