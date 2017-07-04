package com.bsalazar.kekomo.general;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by bsalazar on 22/06/2017.
 */

public class Constants {

    public final static int DISH_TYPE_BREAKFAST = 0;
    public final static int DISH_TYPE_LUNCH = 1;
    public final static int DISH_TYPE_DINNER = 2;

    public static int LUNCH_TIME = 15;

    public static SQLiteDatabase database;

    public static final int PERMISSION_RESULT_WRITE_EXTERNAL_STORAGE = 1;
}
