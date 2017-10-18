package com.bsalazar.kekomo.bbdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bsalazar.kekomo.bbdd.tables.DishesTable;
import com.bsalazar.kekomo.bbdd.tables.EventTable;
import com.bsalazar.kekomo.bbdd.tables.ProductTable;

/**
 * Created by mgonzalez on 21/7/16.
 */
public class SQLiteHelper extends SQLiteOpenHelper{

//    String sqlCreate = "CREATE TABLE mesas (idM TEXT, state TEXT)";

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context contexto) {
        super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DishesTable.CREATE_QUERY);
        database.execSQL(EventTable.CREATE_QUERY);
        database.execSQL(ProductTable.CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int versionAnterior, int versionNueva) {
        try {
            database.execSQL(DishesTable.DROP_QUERY);
            database.execSQL(EventTable.DROP_QUERY);
            database.execSQL(ProductTable.DROP_QUERY);
        }catch (Exception ignored){}

        onCreate(database);
    }

    public void resetBBDD(SQLiteDatabase database){
        try {
            database.execSQL(DishesTable.DROP_QUERY);
            database.execSQL(EventTable.DROP_QUERY);
            database.execSQL(ProductTable.DROP_QUERY);
        }catch (Exception ignored){}

        onCreate(database);
    }
}