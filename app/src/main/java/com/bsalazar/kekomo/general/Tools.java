package com.bsalazar.kekomo.general;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bsalazar on 22/06/2017.
 */

public class Tools {

    public static String parseDateToSQL(Date date) {

        if (date == null)
            return "";

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
    }

    public static Date parseDateFromSQL(String date) {
        try {
            if (date == null)
                return new Date(System.currentTimeMillis());

            if (date.length() <= 0)
                return null;

            if (date.contains("T"))
                date = date.replace("T", " ");

            if (date.length() > 10) {
                if (date.length() < 20)
                    date = date.concat(".000");

                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(date);
            } else {
                return new SimpleDateFormat("yyyy-MM-dd").parse(date);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
}
